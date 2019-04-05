package ca.gc.inspection.scoop;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;

public class CreatePostScreen extends AppCompatActivity {

    public void returnToPrevious (View view) {
        finish();
    }

    private EditText postTitle, postText;
    private ImageView postImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_post);

        // grab input
        postTitle = findViewById(R.id.postTitle);
        postText = findViewById(R.id.postText);
        postImage = findViewById(R.id.postImage);

        Button camera = findViewById(R.id.cameraButton);
        Button cameraRoll = findViewById(R.id.imageButton);
        Button send = findViewById(R.id.postSend);

        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED || checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    requestPermissions(new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, MyCamera.MY_CAMERA_PERMISSION_CODE);
                } else {
                    takePicture();
                }
            }
        });

        cameraRoll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent choosePictureIntent = new Intent(Intent.ACTION_GET_CONTENT);
                choosePictureIntent.setType("image/*");
                startActivityForResult(Intent.createChooser(choosePictureIntent, "Select Picture"), MyCamera.CHOOSE_PIC_REQUEST_CODE);
            }
        });

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (postTitle.getText().toString().isEmpty()){
                    Toast.makeText(CreatePostScreen.this, "Add a title to continue", Toast.LENGTH_SHORT).show();
                } else if (postText.getText().toString().isEmpty()) {
                    Toast.makeText(CreatePostScreen.this, "Add a message to continue", Toast.LENGTH_SHORT).show();
                } else {
                    String userId = "";
                    String title = postTitle.getText().toString();
                    String text = postText.getText().toString();
                    CreatePostController.sendPostToDatabase(CreatePostScreen.this, userId, title, text, MyCamera.currentPhotoPath);
                    finish();
                }
            }
        });
    }
    public static Uri mMediaUri;
    public void takePicture() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            File photoFile = null;
            try {
                photoFile = MyCamera.createImageFile(this);
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (photoFile != null) {
                mMediaUri = FileProvider.getUriForFile(getApplicationContext(), "com.example.android.fileprovider", photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, mMediaUri);
                startActivityForResult(takePictureIntent, MyCamera.TAKE_PIC_REQUEST_CODE);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MyCamera.MY_CAMERA_PERMISSION_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Permission Granted", Toast.LENGTH_LONG).show();
                takePicture();
            } else {
                Toast.makeText(this, "Permission Denied", Toast.LENGTH_LONG).show();
            }
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            String path = null;
            Uri uri = null;
            if (requestCode == MyCamera.CHOOSE_PIC_REQUEST_CODE) {
                if (data == null) {
                    Toast.makeText(getApplicationContext(), "Image cannot be null!", Toast.LENGTH_LONG).show();
                } else {
                    try {
                        uri = data.getData();
                        path = ImageFilePath.getPath(CreatePostScreen.this, data.getData());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            } else {
                Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                File f = new File(MyCamera.currentPhotoPath);
                uri = Uri.fromFile(f);
                mediaScanIntent.setData(uri);
                this.sendBroadcast(mediaScanIntent);
                path = MyCamera.currentPhotoPath;

            }

            if (!path.isEmpty() && uri != null) {
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
                Bitmap newBitmap = MyCamera.imageOrientationValidator(bitmap, path);

                RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                layoutParams.addRule(RelativeLayout.BELOW, R.id.postText);
                postImage.setLayoutParams(layoutParams);
                postImage.setImageBitmap(newBitmap);
            }

        } else if (resultCode != RESULT_CANCELED) {
            Toast.makeText(getApplicationContext(), "Cancelled!", Toast.LENGTH_LONG).show();
        }
    }
}
