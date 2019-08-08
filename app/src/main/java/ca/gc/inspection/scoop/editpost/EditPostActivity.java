package ca.gc.inspection.scoop.editpost;

import ca.gc.inspection.scoop.R;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import java.util.Objects;
import ca.gc.inspection.scoop.createpost.CreatePostActivity;
import ca.gc.inspection.scoop.editleavedialog.EditLeaveEventListener;
import ca.gc.inspection.scoop.util.CameraUtils;
import ca.gc.inspection.scoop.util.NetworkUtils;

import static ca.gc.inspection.scoop.Config.INTENT_ACTIVITY_ID_KEY;
import static ca.gc.inspection.scoop.displaypost.DisplayPostActivity.confirmLoseEdits;
import static ca.gc.inspection.scoop.feedpost.FeedPost.FEED_POST_IMAGE_PATH_KEY;
import static ca.gc.inspection.scoop.postcomment.PostComment.PROFILE_COMMENT_POST_TEXT_KEY;
import static ca.gc.inspection.scoop.profilepost.ProfilePost.PROFILE_POST_TITLE_KEY;
import static com.google.gson.internal.$Gson$Preconditions.checkNotNull;

public class EditPostActivity extends CreatePostActivity implements
        EditPostContract.View,
        EditLeaveEventListener.View.EditLeaveDialogAPI {
    private static final String TAG = "EditPostActivity";
    /**
     * Implements the View in the EditPostContract interface to follow MVP architecture.
     * Allows the user to create a new post by adding a title, text, and using the camera or camera roll
     * to add an image.
     */

    private EditPostContract.Presenter mPresenter;
    private Bitmap mInitialBitmap;
    private String mActivityId;
    private Button mBackButton;

    private String mInitialPostTitle, mInitialPostText;
    private boolean mImageModified = false;
    private Snackbar mSnackbar;

    /**
     * Used by post related action cases to start the edit post activity.
     * @param context
     * @param editPostData
     */
    public static void startEditPostActivity(
            Context context, EditPostData editPostData) {
        Intent intent = new Intent(context, EditPostActivity.class);
        intent.putExtra(INTENT_ACTIVITY_ID_KEY, editPostData.getActivityId());
        intent.putExtra(PROFILE_POST_TITLE_KEY, editPostData.getPostTitle());
        intent.putExtra(PROFILE_COMMENT_POST_TEXT_KEY, editPostData.getPostText());
        intent.putExtra(FEED_POST_IMAGE_PATH_KEY, editPostData.getPostImagePath());
        context.startActivity(intent);
    }

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
        heading.setText(getString(R.string.edit_post_title));
        Button send = findViewById(R.id.activity_create_post_btn_post);
        send.setText(getString(R.string.save));
        mBackButton = findViewById(R.id.activity_create_post_btn_back);

        mActivityId = Objects.requireNonNull(bundle).getString(INTENT_ACTIVITY_ID_KEY);
        mInitialPostTitle = bundle.getString(PROFILE_POST_TITLE_KEY);
        postTitle.setText(mInitialPostTitle);
        mInitialPostText = bundle.getString(PROFILE_COMMENT_POST_TEXT_KEY);
        postText.setText(mInitialPostText);
        String feedPostImage = bundle.getString(FEED_POST_IMAGE_PATH_KEY, "");

        /*
        When starting the edit post activity, we need to retrieve and display the current post image
        if it exists. Note that some post types (ie. ProfilePost, ProfileLikes, SearchPost) don't show
        the post image, even if the actual post contains one. This means that we will have to fetch the
        image for a single post from the database since the DataCache doesn't contain the post image.
         */
        if (feedPostImage != null && !feedPostImage.isEmpty()) {
            // Set post image from the bundle
            Bitmap bitmap = CameraUtils.stringToBitmap(feedPostImage);
            super.setPostImageFromBitmap(bitmap);
        }
        else {
            // Bundle doesn't contain post image, fetch post image from database
            waitingForResponse = true;
            mPresenter.getPostImage(NetworkUtils.getInstance(this), mActivityId);
        }

        mBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmLoseEdits(getSupportFragmentManager(), mPresenter, EditPostActivity.this);
            }
        });
    }

    /**
     * Overridden to set the flag that the post image was modified so that the user will be prompted
     * when leaving their unsaved changes.
     *
     */
    @Override
    protected void removeImageOnClick() {
        super.removeImageOnClick();
        mImageModified = true;
    }

    /**
     * Overridden to set the flag that the post image was modified so that the user will be prompted
     * when leaving their unsaved changes.
     *
     * @param newBitmap to set post image to
     */
    @Override
    public void setPostImageFromBitmap(Bitmap newBitmap) {
        super.setPostImageFromBitmap(newBitmap);
        mImageModified = true;
    }

    /**
     * Callback to set post image from database response.
     * The bundle did not contain a post image, so we fetch it from the database and handle
     * the response here.
     *
     * @param image post image from the database
     */
    @Override
    public void onDatabaseImageResponse(String image) {
        waitingForResponse = false;
        mInitialBitmap = CameraUtils.stringToBitmap(image);
        setPostImageFromBitmap(mInitialBitmap);
    }

    /**
     * Called by the Presenter to know if their are unsaved edits.
     *
     * @return whether the image, post title, and/or post text was modified.
     */
    @Override
    public boolean unsavedEditsExist() {
        return (mImageModified ||
                !mInitialPostTitle.equals(postTitle.getText().toString()) ||
                !mInitialPostText.equals(postText.getText().toString()));
    }

    public void sendPostToDatabase(String postTitle, String postText, Drawable postImage) {
        if (postTitle.isEmpty()) {
            Snackbar.make(mCoordinatorLayout, R.string.create_post_title_empty_error, Snackbar.LENGTH_SHORT).show();
        } else if (postText.isEmpty()) {
            Snackbar.make(mCoordinatorLayout, R.string.create_post_text_empty_error, Snackbar.LENGTH_SHORT).show();
        } else if (!waitingForResponse) {
            if (unsavedEditsExist()) {
                Snackbar.make(mCoordinatorLayout, R.string.edit_post_in_progress, Snackbar.LENGTH_INDEFINITE).show();
                waitingForResponse = true;
                String imageBitmap = "";
                if (postImage != null) {
                    imageBitmap = CameraUtils.bitmapToString(((BitmapDrawable) postImage).getBitmap());
                    Log.i(TAG, "bitmap: " + imageBitmap);
                }
                if (!mImageModified) {
                    Log.i(TAG, "bitmap equals the initial bitmap");
                    imageBitmap = null;
                }
                mPresenter.sendPostToDatabase(NetworkUtils.getInstance(this), mActivityId, postTitle, postText, imageBitmap);
            }
            else if (mSnackbar == null || !mSnackbar.isShownOrQueued()) {
                mSnackbar = Snackbar.make(mCoordinatorLayout, R.string.edit_post_no_changes, Snackbar.LENGTH_SHORT);
                mSnackbar.show();
            }
        }
    }

    /**
     * Callback method to update the UI based on the database response.
     * If the post was edited successfully, we can finish the activity. Otherwise, show a SnackBar
     * for the user to retry saving their changes to the database.
     *
     * @param success True if a post was edited
     */
    public void onDatabaseResponse(boolean success) {
        waitingForResponse = false;
        if (success) {
            finish();
        }
        else {
            Snackbar mSnackbar = Snackbar.make(mCoordinatorLayout, R.string.edit_post_failed, Snackbar.LENGTH_INDEFINITE);
            mSnackbar.setAction(R.string.retry_action, new View.OnClickListener() {
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

    /**
     * User has confirmed they want to leave the activity and lose their unsaved edits.
     * Edit post does not have any caches to clear.
     */
    @Override
    public void confirmLeaveEvent() {
        finish();
    }

    /**
     * Callback for EditLeaveDialog's option to cancel leave event.
     * Not necessary but included to be consistent with attaching a callback to a Dialog button.
     */
    @Override
    public void cancelLeaveEvent() {

    }
}
