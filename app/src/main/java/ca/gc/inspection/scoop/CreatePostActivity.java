package ca.gc.inspection.scoop;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
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

import de.hdodenhof.circleimageview.CircleImageView;

public class CreatePostActivity extends AppCompatActivity {

    public void returnToPrevious (View view) {
        finish();
    }

    private EditText postTitle, postText;
    private ImageView postImage;

    private TextView counter;
    private final TextWatcher mTextEditorWatcher = new TextWatcher() {
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        public void onTextChanged(CharSequence s, int start, int before, int count) {
            //This sets a textview to the current length
            counter.setText(String.valueOf(s.length()) + "/255");
        }

        public void afterTextChanged(Editable s) {
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_post);


        /** Initialize edit texts, image view, and buttons for create post xml
         *  postTitle: title of the post
         *  postText: message or description of the post (set to have a character limit of 255)
         *  postImage: (OPTIONAL) user can choose to add a picture to their post from either the camera, or the camera roll
         *  counter: character counter for postText
         *
         */
        postTitle = findViewById(R.id.activity_create_post_et_title);

        postText = findViewById(R.id.activity_create_post_et_post_content);
        postText.setFilters(new InputFilter[]{ new InputFilter.LengthFilter(255) });
        postText.addTextChangedListener(mTextEditorWatcher);

        postImage = findViewById(R.id.activity_create_post_img_post);

        counter = findViewById(R.id.activity_create_post_txt_word_counter);

        Button camera = findViewById(R.id.activity_create_post_btn_camera);
        Button cameraRoll = findViewById(R.id.activity_create_post_btn_image);
        Button send = findViewById(R.id.activity_create_post_btn_post);

        // set the system status bar color
        getWindow().setStatusBarColor(ContextCompat.getColor(getApplicationContext(), R.color.primary_dark));


        /** OnClickListener for the camera button that launches the native camera app.
         *  Deals with permission checks for Camera
         */
        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED ) {
                    requestPermissions(new String[]{Manifest.permission.CAMERA}, MyCamera.MY_CAMERA_PERMISSION_CODE);
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
                if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ) {
                    requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, MyCamera.MY_CAMERA_ROLL_PERMISSION_CODE);
                } else {
                    Intent choosePictureIntent = new Intent(Intent.ACTION_GET_CONTENT);
                    choosePictureIntent.setType("image/*");
                    startActivityForResult(Intent.createChooser(choosePictureIntent, "Select Picture"), MyCamera.CHOOSE_PIC_REQUEST_CODE);
                }

            }
        });

        /** OnClickListenger for the send button that will grab all the user inputs and send everthing to the CreatePostController
         *
         */
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (postTitle.getText().toString().isEmpty()){
                    Toast.makeText(CreatePostActivity.this, "Add a title to continue", Toast.LENGTH_SHORT).show();
                } else if (postText.getText().toString().isEmpty()) {
                    Toast.makeText(CreatePostActivity.this, "Add a message to continue", Toast.LENGTH_SHORT).show();
                } else {
                    String title = postTitle.getText().toString();
                    String text = postText.getText().toString();
                    String imageBitmap = "";
                    if (postImage.getDrawable() != null){
                        imageBitmap = MyCamera.bitmapToString(((BitmapDrawable)postImage.getDrawable()).getBitmap());
                        Log.i("bitmap", imageBitmap);
                    }
                    CreatePostController.sendPostToDatabase(CreatePostActivity.this, Config.currentUser, title, text, imageBitmap);

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
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Permission Granted", Toast.LENGTH_LONG).show();
                takePicture();
            } else {
                Toast.makeText(this, "Permission Denied", Toast.LENGTH_LONG).show();
            }
        } else if (requestCode == MyCamera.MY_CAMERA_ROLL_PERMISSION_CODE){
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Permission Granted", Toast.LENGTH_LONG).show();
                Intent choosePictureIntent = new Intent(Intent.ACTION_GET_CONTENT);
                choosePictureIntent.setType("image/*");
                startActivityForResult(Intent.createChooser(choosePictureIntent, "Select Picture"), MyCamera.CHOOSE_PIC_REQUEST_CODE);
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
                        path = ImageFilePath.getPath(CreatePostActivity.this, data.getData());
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
                Bitmap newBitmap = MyCamera.imageOrientationValidator(bitmap, path);

                RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                layoutParams.addRule(RelativeLayout.BELOW, R.id.activity_create_post_et_post_content);
                postImage.setLayoutParams(layoutParams);
                postImage.setImageBitmap(newBitmap);
            } else {
                Toast.makeText(this, "Something went wrong while uploading, please try again!", Toast.LENGTH_SHORT).show();
            }

        } else if (resultCode != RESULT_CANCELED) {
            Toast.makeText(getApplicationContext(), "Cancelled!", Toast.LENGTH_LONG).show();
        }
    }
}
