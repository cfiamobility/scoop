package ca.gc.inspection.scoop.createofficialpost;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Objects;

import ca.gc.inspection.scoop.R;
import ca.gc.inspection.scoop.createpost.CreatePostActivity;
import ca.gc.inspection.scoop.editleavedialog.EditLeaveDialog;
import ca.gc.inspection.scoop.editleavedialog.EditLeaveEventListener;
import ca.gc.inspection.scoop.util.CameraUtils;
import ca.gc.inspection.scoop.util.NetworkUtils;
import de.hdodenhof.circleimageview.CircleImageView;

import static ca.gc.inspection.scoop.Config.INTENT_ACTIVITY_ID_KEY;
import static ca.gc.inspection.scoop.feedpost.FeedPost.FEED_POST_IMAGE_PATH_KEY;
import static ca.gc.inspection.scoop.postcomment.PostComment.PROFILE_COMMENT_POST_TEXT_KEY;
import static ca.gc.inspection.scoop.profilepost.ProfilePost.PROFILE_POST_TITLE_KEY;
import static com.google.gson.internal.$Gson$Preconditions.checkNotNull;

public class CreateOfficialPostActivity extends AppCompatActivity implements
        CreateOfficialPostContract.View {
    private static final String TAG = "CreateOfficialPostActivity";

    protected EditText postTitle;
    protected AutoCompleteTextView mBuildingName, mReasonForClosure, mActionRequired;
    protected CoordinatorLayout mCoordinatorLayout;
    private CircleImageView profileImage;
    /**
     * Implements the View in the CreateOfficialPostContract interface to follow MVP architecture.
     * Allows a privileged user to create a new official BCP post by adding the title, building,
     * reason for closure and action required.
     */

    private CreateOfficialPostContract.Presenter mPresenter;
    private Button mBackButton;

    private Snackbar mSnackbar;
    private boolean waitingForResponse = false;

    public void returnToPrevious (View view) {
        finish();
    }

    public void setPresenter(@NonNull CreateOfficialPostContract.Presenter presenter) {
        mPresenter = checkNotNull(presenter);
    }

    public CreateOfficialPostContract.Presenter newPresenter() {
        return new CreateOfficialPostPresenter(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_official_post);
        Bundle bundle = getIntent().getExtras();

        setPresenter(newPresenter());

        /* Initialize edit texts, image view, and buttons for create official Post xml
         *  postTitle: title of the Post
         */
        mCoordinatorLayout = findViewById(R.id.activity_create_official_post_coordinator);
        postTitle = findViewById(R.id.activity_create_official_post_et_title);
        profileImage = findViewById(R.id.activity_create_official_post_img_profile);
        mPresenter.getUserProfileImage(NetworkUtils.getInstance(this));

        mBuildingName = findViewById(R.id.create_official_bcp_post_building);
        mReasonForClosure = findViewById(R.id.create_official_bcp_post_reason_for_closure);
        mActionRequired = findViewById(R.id.create_official_bcp_post_action_required);

        mBackButton = findViewById(R.id.activity_create_official_post_btn_back);

        mBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    public void sendPostToDatabase(String postTitle, int buildingId, int reasonForClosure, int actionRequired) {
        if (postTitle.isEmpty()) {
            Snackbar.make(mCoordinatorLayout, R.string.create_post_title_empty_error, Snackbar.LENGTH_SHORT).show();
        } else if (buildingId == -1) {
            Snackbar.make(mCoordinatorLayout, R.string.create_post_text_empty_error, Snackbar.LENGTH_SHORT).show();
        } else if (reasonForClosure == -1) {
            Snackbar.make(mCoordinatorLayout, R.string.create_post_text_empty_error, Snackbar.LENGTH_SHORT).show();
        } else if (actionRequired == -1) {
            Snackbar.make(mCoordinatorLayout, R.string.create_post_text_empty_error, Snackbar.LENGTH_SHORT).show();
        } else if (!waitingForResponse) {
            mPresenter.sendPostToDatabase(NetworkUtils.getInstance(this), postTitle, buildingId, reasonForClosure, actionRequired);
        }
    }

    /**
     * Callback method to update the UI based on the database response.
     * If the post was created successfully, we can finish the activity. Otherwise, show a SnackBar
     * for the user to retry saving their changes to the database.
     *
     * @param success True if a post was edited
     */
    @Override
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
                            -1,
                            -1,
                            -1);
                }
            });
            mSnackbar.show();
        }
    }

    /**
     * Sets the profileImageCircle with the users profileimage taken from the database.
     * @param profileImageBitmap
     */
    @Override
    public void setUserProfileImage(Bitmap profileImageBitmap) {
        profileImage.setImageBitmap(profileImageBitmap);
    }
}
