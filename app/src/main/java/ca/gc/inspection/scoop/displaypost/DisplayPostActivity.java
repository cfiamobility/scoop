package ca.gc.inspection.scoop.displaypost;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import ca.gc.inspection.scoop.Config;

import static ca.gc.inspection.scoop.Config.INTENT_ACTIVITY_ID_KEY;
import static com.google.gson.internal.$Gson$Preconditions.checkNotNull;
import ca.gc.inspection.scoop.R;
import ca.gc.inspection.scoop.editcomment.EditCommentContract;
import ca.gc.inspection.scoop.editleavedialog.EditLeaveDialog;
import ca.gc.inspection.scoop.editleavedialog.EditLeaveEventListener;
import ca.gc.inspection.scoop.editpost.EditPostContract;
import ca.gc.inspection.scoop.util.ActivityUtils;
import ca.gc.inspection.scoop.util.NetworkUtils;


public class DisplayPostActivity extends AppCompatActivity implements
        DisplayPostContract.View,
        EditLeaveEventListener.View.EditLeaveDialogAPI {

    private DisplayPostContract.Presenter mDisplayPostPresenter;
    private DisplayPostFragment mDisplayPostFragment;
    private DisplayPostAdapter mAdapter;

    private String mActivityId;
    private EditText mAddCommentET;
    private Button mAddCommentButton;
    private boolean canPostComment;
    private Button mBackButton;

    public void goBack (View view) {
        finish();
    }

    public static void confirmLoseEdits(FragmentManager fragmentManager,
                                 EditLeaveEventListener.Presenter presenter,
                                 EditLeaveEventListener.View.EditLeaveDialogAPI editLeaveEventListener) {

        if (presenter.unsavedEditsExist()) {
            EditLeaveDialog editLeaveDialog = new EditLeaveDialog();
            editLeaveDialog.setEditLeaveEventListener(editLeaveEventListener);
            editLeaveDialog.show(fragmentManager, EditLeaveDialog.TAG);
        } else {
            editLeaveEventListener.confirmLeaveEvent();
        }
    }

    @Override
    public void setPresenter(@NonNull DisplayPostContract.Presenter presenter) {
        mDisplayPostPresenter = checkNotNull(presenter);
    }

    public boolean setActivityIdFromIntent() {
        Intent intent = getIntent();
        if (intent.hasExtra(INTENT_ACTIVITY_ID_KEY)) {
            mActivityId = intent.getStringExtra(INTENT_ACTIVITY_ID_KEY);
            return true;
        }
        else return false;
    }

    protected DisplayPostContract.Presenter.FragmentAPI getPresenter() {
        return (DisplayPostContract.Presenter.FragmentAPI) mDisplayPostPresenter;
    }

    protected String getActivityId() {
        return mActivityId;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_post);

        if (!setActivityIdFromIntent()) {
            Toast.makeText(getApplicationContext(), "Error displaying post", Toast.LENGTH_SHORT).show();
            finish();
        }

        setPresenter(new DisplayPostPresenter(this, NetworkUtils.getInstance(this)));

        mDisplayPostFragment = (DisplayPostFragment) getSupportFragmentManager().findFragmentById(R.id.activity_display_post_rv);
        if (mDisplayPostFragment == null) {
            // Create the fragment
            mDisplayPostFragment = DisplayPostFragment.newInstance();
            ActivityUtils.addFragmentToActivity(
                    getSupportFragmentManager(), mDisplayPostFragment, R.id.activity_display_post_rv);
        }

        // to prevent the soft keyboard from opening when the activity starts
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        // set the system status bar color
        getWindow().setStatusBarColor(ContextCompat.getColor(getApplicationContext(), R.color.primary_dark));

        // when the soft keyboard is open tapping anywhere else will close the keyboard
        findViewById(R.id.activity_display_post_layout).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                return true;
            }
        });

        /* Add Post/comment ticket AMD-96
         * This code is part of the add Post/comment ticket.
         * while displaying a Post, a user can add a comment by typing some text on the comment box and pressing the send button
         * this will initiate this code and will add the comment to the database. Upon succession, the comment box will be reset to empty
         * and the comment list will be updated to show the new comment that was just added
         */
        mAddCommentET = findViewById(R.id.activity_display_post_et_add_comment);
        mAddCommentButton = findViewById(R.id.activity_display_post_btn_add_comment);
        mAddCommentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String commentText = mAddCommentET.getText().toString();
                if (canPostComment) {
                    if (commentText.isEmpty()) {
                        Toast.makeText(DisplayPostActivity.this, R.string.edit_comment_empty_text_error, Toast.LENGTH_SHORT).show();
                        Log.i("comment", commentText);
                    } else {
                        canPostComment = false;

                        //clear comment box
                        mAddCommentET.getText().clear();

                        mDisplayPostPresenter.addPostComment(Config.currentUser, commentText, getActivityId());

                        //hide keyboard
                        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                        inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                    }
                }
            }
        });
        canPostComment = true;

        mBackButton = findViewById(R.id.activity_display_post_btn_back);
        mBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmLoseEdits(getSupportFragmentManager(), mDisplayPostPresenter, DisplayPostActivity.this);
            }
        });
    }

    /**
     * Callback to show a success toast message or error toast message when adding a post comment
     * @param success True if a comment was added
     */
    @Override
    public void onAddPostComment(boolean success) {
        canPostComment = true;
        String toastMessage;
        if (success)
            toastMessage = "Comment posted!";
        else
            toastMessage = "Failed to post comment";
        Toast.makeText(this, toastMessage, Toast.LENGTH_SHORT).show();
    }

    /**
     * User has confirmed they want to leave the activity and lose their unsaved edits.
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
