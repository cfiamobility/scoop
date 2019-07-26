package ca.gc.inspection.scoop.createpost;

import ca.gc.inspection.scoop.R;
import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;

import ca.gc.inspection.scoop.Config;
import ca.gc.inspection.scoop.ImageFilePath;
import ca.gc.inspection.scoop.util.CameraUtils;
import ca.gc.inspection.scoop.util.NetworkUtils;

import static com.google.gson.internal.$Gson$Preconditions.checkNotNull;

public class CreatePostActivity extends AppCompatActivity implements CreatePostContract.View {
    /**
     * Implements the View in the CreatePostContract interface to follow MVP architecture.
     * Allows the user to create a new post by adding a title, text, and using the camera or camera roll
     * to add an image.
     */

    protected static final int TEXT_CHAR_LIMIT = 255;
    private CreatePostContract.Presenter mPresenter;
    protected EditText postTitle;
    protected EditText postText;
    protected ImageView postImage;
    protected CoordinatorLayout mCoordinatorLayout;

    protected TextView counter;
    protected final TextWatcher mTextEditorWatcher = new TextWatcher() {
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        public void onTextChanged(CharSequence s, int start, int before, int count) {
            //This sets a textview to the current length
            counter.setText(String.valueOf(s.length()) + "/255");
        }

        public void afterTextChanged(Editable s) {
        }
    };
    protected boolean waitingForResponse = false;

    public void returnToPrevious (View view) {
        finish();
    }

    @Override
    public void setPresenter(@NonNull CreatePostContract.Presenter presenter) {
        mPresenter = checkNotNull(presenter);
    }

    public CreatePostContract.Presenter newPresenter() {
        return new CreatePostPresenter(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_post);

        // set the system status bar color
        getWindow().setStatusBarColor(ContextCompat.getColor(getApplicationContext(), R.color.primary_dark));

        setPresenter(newPresenter());

        /** Initialize edit texts, image view, and buttons for create Post xml
         *  postTitle: title of the Post
         *  postText: message or description of the Post (set to have a character limit of 255)
         *  postImage: (OPTIONAL) user can choose to add a picture to their Post from either the camera, or the camera roll
         *  counter: character counter for postText
         *
         */
        postTitle = findViewById(R.id.activity_create_post_et_title);

        postText = findViewById(R.id.activity_create_post_et_post_content);
        postText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(TEXT_CHAR_LIMIT)});
        postText.addTextChangedListener(mTextEditorWatcher);

        postImage = findViewById(R.id.activity_create_post_img_post);

        counter = findViewById(R.id.activity_create_post_txt_word_counter);

        mCoordinatorLayout = findViewById(R.id.activity_create_post_coordinator);

        Button camera = findViewById(R.id.activity_create_post_btn_camera);
        Button cameraRoll = findViewById(R.id.activity_create_post_btn_image);
        Button send = findViewById(R.id.activity_create_post_btn_post);
        Button removeImage = findViewById(R.id.activity_create_post_btn_remove_image);

        /** OnClickListener for the camera button that launches the native camera app.
         *  Deals with permission checks for Camera
         */
        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    requestPermissions(new String[]{Manifest.permission.CAMERA}, CameraUtils.MY_CAMERA_PERMISSION_CODE);
                } else {
                    takePicture();
                }
            }
        });


        /** OnClickListenger for the camera roll button that launches the native photo app of the phone which
         *  allows users to select an image from the camera roll
         *  deals with permission checks for read external storage
         */
        cameraRoll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImageFromCameraRoll();
            }
        });

        /** OnClickListenger for the send button that will grab all the user inputs and send everthing to the CreatePostPresenter
         *
         */
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendPostToDatabase(
                        postTitle.getText().toString(),
                        postText.getText().toString(),
                        postImage.getDrawable());
            }
        });

        removeImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                postImage.setImageDrawable(null);
            }
        });
    }

    public void takePicture() {
        CameraUtils.takePicture(this);
    }

    /**
     * Receives the permission granted (or not granted) by the user to start an activity to take a
     * picture or get an image from the camera roll.
     * Overrides Android Activity method.
     *
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == CameraUtils.MY_CAMERA_PERMISSION_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Permission Granted", Toast.LENGTH_LONG).show();
                takePicture();
            } else {
                Toast.makeText(this,  "Permission Denied", Toast.LENGTH_LONG).show();
            }
        } else if (requestCode == CameraUtils.MY_CAMERA_ROLL_PERMISSION_CODE){
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Permission Granted", Toast.LENGTH_LONG).show();
                Intent choosePictureIntent = new Intent(Intent.ACTION_GET_CONTENT);
                choosePictureIntent.setType("image/*");
                startActivityForResult(Intent.createChooser(choosePictureIntent, "Select Picture"), CameraUtils.CHOOSE_PIC_REQUEST_CODE);
            } else {
                Toast.makeText(this, "Permission Denied", Toast.LENGTH_LONG).show();
            }
        }
    }

    /**
     * Receives the result from the Intent used to take a picture or get an image from the camera roll.
     * Creates a Bitmap from the image and updates the GUI to display it.
     * Overrides Android Activity method.
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            String path = null;
            Uri uri = null;
            // Select image from camera roll
            if (requestCode == CameraUtils.CHOOSE_PIC_REQUEST_CODE) {
                if (data == null) {
                    Toast.makeText(getApplicationContext(), "Image cannot be null!", Toast.LENGTH_LONG).show();
                } else {
                    try {
                        uri = data.getData();
                        path = ImageFilePath.getPath(getBaseContext(), data.getData());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
            else {
                // Create file for image from camera
                Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                File f = new File(CameraUtils.currentPhotoPath);
                uri = Uri.fromFile(f);
                mediaScanIntent.setData(uri);
                sendBroadcast(mediaScanIntent);
                path = CameraUtils.currentPhotoPath;
            }

            // If image exists, update UI
            if (path != null || uri != null) {
                BitmapFactory.Options bmOptions = new BitmapFactory.Options();
                bmOptions.inJustDecodeBounds = true;
                BitmapFactory.decodeFile(path, bmOptions);
                bmOptions.inJustDecodeBounds = false;


                Bitmap bitmap = null;
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Bitmap newBitmap = CameraUtils.imageOrientationValidator(bitmap, path);

                setPostImageFromBitmap(newBitmap);
            } else {
                Toast.makeText(this, "Something went wrong while uploading, please try again!", Toast.LENGTH_SHORT).show();
            }

        } else if (resultCode != RESULT_CANCELED) {
            Toast.makeText(getApplicationContext(), "Cancelled!", Toast.LENGTH_LONG).show();
        }
    }

    public void selectImageFromCameraRoll() {
        if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, CameraUtils.MY_CAMERA_ROLL_PERMISSION_CODE);
        } else {
            Intent choosePictureIntent = new Intent(Intent.ACTION_GET_CONTENT);
            choosePictureIntent.setType("image/*");
            startActivityForResult(Intent.createChooser(choosePictureIntent, "Select Picture"), CameraUtils.CHOOSE_PIC_REQUEST_CODE);
        }
    }

    public void sendPostToDatabase(String postTitle, String postText, Drawable postImage) {
        if (postTitle.isEmpty()) {
            Snackbar.make(mCoordinatorLayout, R.string.create_post_title_empty_error, Snackbar.LENGTH_SHORT).show();
        } else if (postText.isEmpty()) {
            Snackbar.make(mCoordinatorLayout, R.string.create_post_text_empty_error, Snackbar.LENGTH_SHORT).show();
        } else if (!waitingForResponse) {
            Snackbar.make(mCoordinatorLayout, R.string.create_post_in_progress, Snackbar.LENGTH_INDEFINITE).show();
            waitingForResponse = true;
            String imageBitmap = "";
            if (postImage != null) {
                imageBitmap = CameraUtils.bitmapToString(((BitmapDrawable) postImage).getBitmap());
                Log.i("bitmap", imageBitmap);
            }
            mPresenter.sendPostToDatabase(NetworkUtils.getInstance(this), Config.currentUser, postTitle, postText, imageBitmap);
        }
    }

    public void setPostImageFromBitmap(Bitmap newBitmap) {
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.addRule(RelativeLayout.BELOW, R.id.activity_create_post_et_post_content);

        postImage.setLayoutParams(layoutParams);
        postImage.setImageBitmap(newBitmap);
    }

    public void onDatabaseResponse(boolean success) {
        waitingForResponse = false;
        if (success) {
            finish();
        }
        else {
            Snackbar mSnackbar = Snackbar.make(mCoordinatorLayout, R.string.create_post_failed, Snackbar.LENGTH_INDEFINITE);
            mSnackbar.setAction(R.string.create_post_retry, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    sendPostToDatabase(
                            postTitle.getText().toString(),
                            postText.getText().toString(),
                            postImage.getDrawable());
                }
            });
            mSnackbar.show();
        }
    }
}
