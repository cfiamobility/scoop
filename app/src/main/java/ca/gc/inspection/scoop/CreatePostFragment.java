package ca.gc.inspection.scoop;
import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;

import androidx.annotation.NonNull;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;

public class CreatePostFragment extends Fragment implements CreatePostContract.View {

    private CreatePostContract.Presenter mPresenter;
    private EditText postTitle, postText;
    private ImageView postImage;
    private CreatePostActivity createPostActivity;

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
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        createPostActivity = (CreatePostActivity) getActivity();
        assert (createPostActivity != null);
    }

    @Override
    public void setPresenter(@NonNull CreatePostContract.Presenter presenter) {
        mPresenter = presenter;
    }

    public static CreatePostFragment newInstance() {
        return new CreatePostFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@android.support.annotation.NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_create_post, container, false);

        /** Initialize edit texts, image view, and buttons for create post xml
         *  postTitle: title of the post
         *  postText: message or description of the post (set to have a character limit of 255)
         *  postImage: (OPTIONAL) user can choose to add a picture to their post from either the camera, or the camera roll
         *  counter: character counter for postText
         *
         */
        postTitle = root.findViewById(R.id.activity_create_post_et_title);

        postText = root.findViewById(R.id.activity_create_post_et_post_content);
        postText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(255)});
        postText.addTextChangedListener(mTextEditorWatcher);

        postImage = root.findViewById(R.id.activity_create_post_img_post);

        counter = root.findViewById(R.id.activity_create_post_txt_word_counter);

        Button camera = root.findViewById(R.id.activity_create_post_btn_camera);
        Button cameraRoll = root.findViewById(R.id.activity_create_post_btn_image);
        Button send = root.findViewById(R.id.activity_create_post_btn_post);

        /** OnClickListener for the camera button that launches the native camera app.
         *  Deals with permission checks for Camera
         */
        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (createPostActivity.checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    requestPermissions(new String[]{Manifest.permission.CAMERA}, MyCamera.MY_CAMERA_PERMISSION_CODE);
                } else {
                    createPostActivity.takePicture();
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
                if (createPostActivity.checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    createPostActivity.requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, MyCamera.MY_CAMERA_ROLL_PERMISSION_CODE);
                } else {
                    Intent choosePictureIntent = new Intent(Intent.ACTION_GET_CONTENT);
                    choosePictureIntent.setType("image/*");
                    createPostActivity.startActivityForResult(Intent.createChooser(choosePictureIntent, "Select Picture"), MyCamera.CHOOSE_PIC_REQUEST_CODE);
                }

            }
        });

        /** OnClickListenger for the send button that will grab all the user inputs and send everthing to the CreatePostPresenter
         *
         */
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (postTitle.getText().toString().isEmpty()) {
                    createPostActivity.makeToast("Add a title to continue", Toast.LENGTH_SHORT);
                } else if (postText.getText().toString().isEmpty()) {
                    createPostActivity.makeToast("Add a message to continue", Toast.LENGTH_SHORT);
                } else {
                    String title = postTitle.getText().toString();
                    String text = postText.getText().toString();
                    String imageBitmap = "";
                    if (postImage.getDrawable() != null) {
                        imageBitmap = MyCamera.bitmapToString(((BitmapDrawable) postImage.getDrawable()).getBitmap());
                        Log.i("bitmap", imageBitmap);
                    }
                    CreatePostContract.Presenter.sendPostToDatabase(createPostActivity.getBaseContext(), Config.currentUser, title, text, imageBitmap);

                    createPostActivity.finish();
                }
            }
        });
        return root;
    }

    public void setBitmapWithLayout(ViewGroup.LayoutParams layoutParams, Bitmap newBitmap) {
        postImage.setLayoutParams(layoutParams);
        postImage.setImageBitmap(newBitmap);
    }
}
