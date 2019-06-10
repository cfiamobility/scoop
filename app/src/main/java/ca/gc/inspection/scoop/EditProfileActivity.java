package ca.gc.inspection.scoop;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.Toast;

import java.io.File;

import ca.gc.inspection.scoop.util.ActivityUtils;
import de.hdodenhof.circleimageview.CircleImageView;

import static ca.gc.inspection.scoop.EditProfileFragment.bitmap;
import static ca.gc.inspection.scoop.MyCamera.CHOOSE_PIC_REQUEST_CODE;
import static ca.gc.inspection.scoop.MyCamera.MY_CAMERA_PERMISSION_CODE;
import static ca.gc.inspection.scoop.MyCamera.currentPhotoPath;
import static ca.gc.inspection.scoop.MyCamera.imageOrientationValidator;

public class EditProfileActivity extends AppCompatActivity {

    private EditProfileContract.Presenter mPresenter;
    private EditProfileFragment mView;

	// method for the back button
	public void finishActivity(View view) {
		finish();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_edit_profile);

        mView = (EditProfileFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_edit_profile);
        if (mView == null) {
            // Create the fragment
            mView = EditProfileFragment.newInstance();
            ActivityUtils.addFragmentToActivity(
                    getSupportFragmentManager(), mView, R.id.activity_edit_profile);
        }

        mPresenter = new EditProfilePresenter();

		// set the system status bar color
		getWindow().setStatusBarColor(ContextCompat.getColor(getApplicationContext(), R.color.primary_dark));

		//Getting the userID
		SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("ca.gc.inspection.scoop", Context.MODE_PRIVATE);

		// DEFAULT TO BE CHANGED TO ""
        String userID = sharedPreferences.getString("userid", "");

		Log.i("userid", userID + "_");

		// If userID is unavailable.
		if (userID.equals("")) {
			finish();
		}
		else {
		    mView.setUserId(userID);
        }

		// when the soft keyboard is open tapping anywhere else will close the keyboard
		findViewById(R.id.activity_edit_profile).setOnTouchListener(new View.OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
				inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
				return true;
			}
		});
	}

	// when the change profile picture text is tapped
	public void changeProfilePicture(View view) {

		// custom dialog box
		final Dialog dialog = new Dialog(EditProfileActivity.this);
		dialog.setContentView(R.layout.dialog_change_profile_picture);

		// Imageview inside the dialog box
		CircleImageView previewImage = dialog.findViewById(R.id.dialog_change_profile_picture_img_profile);
		previewImage.setImageBitmap(bitmap);    // TODO: remove dependency on static fragment variable

		// Button to take a picture
		Button takePhotoButton = dialog.findViewById(R.id.dialog_change_profile_picture_btn_photo);
		takePhotoButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// closing the dialog box
				dialog.dismiss();

				// Checks for permission to write (implicit read) externnal data and camera permissions
				if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED || checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
					requestPermissions(new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, MY_CAMERA_PERMISSION_CODE);
				} else {
					// Goes to this method to open camera
					takePicture();
				}
			}
		});

		// Button to open gallery and choose a picture
		Button photoGalleryButton = dialog.findViewById(R.id.dialog_change_profile_picture_btn_gallery);
		photoGalleryButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// Closing the dialog box
				dialog.dismiss();

				// Intent to open gallery
				Intent choosePictureIntent = new Intent(Intent.ACTION_GET_CONTENT);
				choosePictureIntent.setType("image/*");
				startActivityForResult(Intent.createChooser(choosePictureIntent, "Select Picture"), CHOOSE_PIC_REQUEST_CODE);
			}
		});

		// so the buttons are shown
		takePhotoButton.setEnabled(true);
		photoGalleryButton.setEnabled(true);

		// show the custom dialog
		dialog.show();
	}

	private void takePicture() {
		MyCamera.takePicture(this);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK) {
			// This runs if the user has successfully chosen a picture from their gallery
			if (requestCode == CHOOSE_PIC_REQUEST_CODE) {
				// If the image is 0kb, etc, error
				if (data == null) {
					Toast.makeText(getApplicationContext(), "Image cannot be null!", Toast.LENGTH_LONG).show();
				} else {
					try {
						// Get the data reference
						Uri uri = data.getData();

						// Gets the absolute path of the image to be stored into the db
						String realPath = ImageFilePath.getPath(EditProfileActivity.this, data.getData());

						// Gets the bitmap of the image to be editted to be rightside up
						Bitmap oldBitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);

						// sends the old bitmap through the orientation validator because android sucks and it takes pictures in landscape mode
						// android camera is a J O K E
						Bitmap bitmap = imageOrientationValidator(oldBitmap, realPath);

						// Sets the imageview to the corrected bitmap
						mView.setProfilePicBitmap(bitmap);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			} else {
				// This runs when the user has taken a picture

				// Intent to scan for the photo that was just taken
				Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);

				// saves the file here
				File f = new File(currentPhotoPath);
				Uri contentUri = Uri.fromFile(f);
				mediaScanIntent.setData(contentUri);
				// Literally don't know what this does but itll break if you touch any of this. Copied most of this method from the documentation, goodluck.
				this.sendBroadcast(mediaScanIntent);

				// Scales the image - google : "android camera basics" and look for the documentation entitled: 'take a picture'. It's pretty bad but yea.
				int targetW = mView.getProfilePicWidth();
				int targetH = mView.getProfilePicHeight();
				BitmapFactory.Options bmOptions = new BitmapFactory.Options();
				bmOptions.inJustDecodeBounds = true;
				BitmapFactory.decodeFile(currentPhotoPath, bmOptions);
				int photoW = bmOptions.outWidth;
				int photoH = bmOptions.outHeight;
				int scaleFactor = Math.min(photoW / targetW, photoH / targetH);
				bmOptions.inJustDecodeBounds = false;
				bmOptions.inSampleSize = scaleFactor;
				bmOptions.inPurgeable = true;

				// getting the bitmap to be corrected
				Bitmap oldBitmap = BitmapFactory.decodeFile(currentPhotoPath, bmOptions);

				// correcting the bitmap
				Bitmap bitmap = imageOrientationValidator(oldBitmap, currentPhotoPath);

				// Setting the image
				mView.setProfilePicBitmap(bitmap);
			}

		} else if (resultCode != RESULT_CANCELED) {
			Toast.makeText(getApplicationContext(), "Cancelled!", Toast.LENGTH_LONG).show();
		}
	}

	// General permissions request
	@Override
	public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
		super.onRequestPermissionsResult(requestCode, permissions, grantResults);
		if (requestCode == MY_CAMERA_PERMISSION_CODE) {
			if (grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
				Toast.makeText(this, "Permission Granted", Toast.LENGTH_LONG).show();
				takePicture();
			} else {
				Toast.makeText(this, "Permission Denied", Toast.LENGTH_LONG).show();
			}
		}
	}

    public void makeToast(String text, final int length) {
        Toast.makeText(this, text, length).show();
    }
}
