package ca.gc.inspection.scoop.editpost;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import ca.gc.inspection.scoop.R;

import ca.gc.inspection.scoop.Config;
import ca.gc.inspection.scoop.createpost.CreatePostActivity;
import ca.gc.inspection.scoop.util.CameraUtils;
import ca.gc.inspection.scoop.util.NetworkUtils;

import static ca.gc.inspection.scoop.postcomment.PostComment.PROFILE_COMMENT_POST_TEXT_KEY;
import static ca.gc.inspection.scoop.profilepost.ProfilePost.PROFILE_POST_TITLE_KEY;
import static com.google.gson.internal.$Gson$Preconditions.checkNotNull;

public class EditPostActivity extends CreatePostActivity implements EditPostContract.View {
    /**
     * Implements the View in the EditPostContract interface to follow MVP architecture.
     * Allows the user to create a new post by adding a title, text, and using the camera or camera roll
     * to add an image.
     */

    private EditPostContract.Presenter mPresenter;

    public void returnToPrevious (View view) {
        finish();
    }

    public void setPresenter(@NonNull EditPostContract.Presenter presenter) {
        mPresenter = checkNotNull(presenter);
    }

    @Override
    public EditPostContract.Presenter newPresenter() {
        return new EditPostPresenter(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getIntent().getExtras();

        setPresenter(newPresenter());

        /** Initialize edit texts, image view, and buttons for create Post xml
         *  postTitle: title of the Post
         *  postText: message or description of the Post (set to have a character limit of 255)
         *  postImage: (OPTIONAL) user can choose to add a picture to their Post from either the camera, or the camera roll
         *  counter: character counter for postText
         *
         */
        TextView heading = findViewById(R.id.activity_create_post_txt_create_post_title);
        heading.setText("Edit Post");
        Button send = findViewById(R.id.activity_create_post_btn_post);
        send.setText("Save");

        postTitle.setText(bundle.getString(PROFILE_POST_TITLE_KEY));
        postText.setText(bundle.getString(PROFILE_COMMENT_POST_TEXT_KEY));
    }

    public void createPost(String postTitle, String postText, Drawable postImage) {
        if (postTitle.isEmpty()) {
            Snackbar.make(mCoordinatorLayout, R.string.create_post_title_empty_error, Snackbar.LENGTH_SHORT).show();
        } else if (postText.isEmpty()) {
            Snackbar.make(mCoordinatorLayout, R.string.create_post_text_empty_error, Snackbar.LENGTH_SHORT).show();
        } else if (!creatingPost) {
            Snackbar.make(mCoordinatorLayout, R.string.create_post_in_progress, Snackbar.LENGTH_INDEFINITE).show();
            creatingPost = true;
            String imageBitmap = "";
            if (postImage != null) {
                imageBitmap = CameraUtils.bitmapToString(((BitmapDrawable) postImage).getBitmap());
                Log.i("bitmap", imageBitmap);
            }
            mPresenter.sendPostToDatabase(NetworkUtils.getInstance(this), Config.currentUser, postTitle, postText, imageBitmap);
        }
    }

    public void onPostCreated(boolean success) {
        creatingPost = false;
        if (success) {
            finish();
        }
        else {
            Snackbar mSnackbar = Snackbar.make(mCoordinatorLayout, R.string.create_post_failed, Snackbar.LENGTH_INDEFINITE);
            mSnackbar.setAction(R.string.create_post_retry, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    createPost(
                            postTitle.getText().toString(),
                            postText.getText().toString(),
                            postImage.getDrawable());
                }
            });
            mSnackbar.show();
        }
    }
}
