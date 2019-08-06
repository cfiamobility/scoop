package ca.gc.inspection.scoop.postcomment;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.SpannableStringBuilder;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import ca.gc.inspection.scoop.postoptionsdialog.PostOptionsDialogReceiver;
import ca.gc.inspection.scoop.profilelikes.ProfileLikesContract;
import ca.gc.inspection.scoop.searchprofile.UserProfileListener;
import ca.gc.inspection.scoop.util.ActivityUtils;
import ca.gc.inspection.scoop.util.CameraUtils;
import ca.gc.inspection.scoop.R;
import ca.gc.inspection.scoop.util.TextFormat;

import static android.view.View.GONE;
import static ca.gc.inspection.scoop.searchprofile.view.SearchProfileViewHolder.getSpannableStringBuilderWithFormat;
import static java.lang.Boolean.TRUE;

/**
 * ViewHolder for replying to a post action; it is the most generic View Holder
 * and contains the minimum views (no comment count, options menus, or images)
 * related to "posting" actions. Parent View Holder for ProfilePostViewHolder.
 */

public class PostCommentViewHolder extends RecyclerView.ViewHolder implements
        PostCommentContract.View.ViewHolder,
        UserProfileListener,
        PostOptionsDialogReceiver.EditCommentReceiver {

    private static final int SNACKBAR_LENGTH_VERY_SHORT = 1000;
    PostCommentContract.Presenter.ViewHolderAPI mPresenter;

    public TextView username, date, postText, likeCount, editText, counter;
    public ImageView profileImage, upvote, downvote, editButton, cancelButton;
    public ImageView optionsMenu;
    public ImageView saved, unsaved;
//    public Boolean savedStatus;
    protected boolean waitingForResponse = false;

    protected TextWatcher mTextEditorWatcher;
    protected CoordinatorLayout mCoordinatorLayout;
    private Snackbar mSnackbar;
    private View mView;
    @Nullable
    protected String mCallBackIdentifier;
    private boolean isEditing;

    public PostCommentViewHolder(View v, PostCommentContract.Presenter.ViewHolderAPI presenter) {
        super(v);
        mView = v;

        username = v.findViewById(R.id.name);
        profileImage = v.findViewById(R.id.profile_image);
        date = v.findViewById(R.id.date);
        postText = v.findViewById(R.id.post_text);
        likeCount = v.findViewById(R.id.vote_count);
        upvote = v.findViewById(R.id.up_vote);
        downvote = v.findViewById(R.id.down_vote);
        optionsMenu = v.findViewById(R.id.options_menu);
        saved = v.findViewById(R.id.item_post_img_saved);
        unsaved = v.findViewById(R.id.item_post_img_unsaved);

        mPresenter = presenter;

        // edit comment
        setupEditComment(v);
    }

    protected void setupEditComment(View v) {
        mCoordinatorLayout = v.findViewById(R.id.edit_comment_coordinator);
        counter = v.findViewById(R.id.edit_post_text_counter);

        editText = v.findViewById(R.id.edit_post_text);
        editButton = v.findViewById(R.id.edit_post_text_btn);
        cancelButton = v.findViewById(R.id.edit_post_cancel_btn);
        hideEditText();
    }

    private TextWatcher getEditCommentTextWatcher(String activityId) {
        return new TextWatcher() {
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //This sets a textview to the current length
                counter.setText(String.valueOf(s.length()) + "/255");
                mPresenter.cacheEditCommentData(activityId, s.toString());
            }

            public void afterTextChanged(Editable s) {
            }
        };
    }

    @Override
    public void hideEditText() {
        if (editText != null && editButton != null && counter != null) {
            isEditing = false;
            postText.setVisibility(View.VISIBLE);
            editText.setVisibility(View.GONE);
            editButton.setVisibility(View.GONE);
            counter.setVisibility(View.GONE);
        }
    }

    public void showEditText() {
        if (editText != null && editButton != null && counter != null) {
            isEditing = true;
            postText.setVisibility(View.GONE);
            editText.setVisibility(View.VISIBLE);
            editButton.setVisibility(View.VISIBLE);
            counter.setVisibility(View.VISIBLE);
        }
    }

    /**
     *
     * @param postText
     */
    @Override
    public PostCommentContract.View.ViewHolder setEditPostText(String postText) {
        this.editText.setText(postText);
        return this;
    }

    /**
     *
     * @param postText
     */
    @Override
    public PostCommentContract.View.ViewHolder setPostText(String postText) {
        this.postText.setText(postText);
        return this;
    }

    /**
     *
     * @param postText
     */
    @Override
    public PostCommentContract.View.ViewHolder setPostTextWithFormat(String postText, TextFormat textFormat) {
        SpannableStringBuilder spannableStringBuilder = getSpannableStringBuilderWithFormat(postText, textFormat);
        this.postText.setText(spannableStringBuilder);
        return this;
    }

    /**
     *
     * @param userName
     */
    @Override
    public PostCommentContract.View.ViewHolder setUserName(String userName) {
        username.setText(userName);
        return this;
    }

    /**
     *
     * @param likeCount
     */
    @Override
    public PostCommentContract.View.ViewHolder setLikeCount(String likeCount) {
        if(likeCount.equals("null"))
            likeCount = "0";
        this.likeCount.setText(likeCount);
        return this;
    }

    /**
     *
     * @param date
     */
    @Override
    public PostCommentContract.View.ViewHolder setDate(String date) {
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"); //formats the date accordingly
            Date parsedDate = dateFormat.parse(date); //parses the created date to be in specified date format
            DateFormat properDateFormat = new SimpleDateFormat("MM-dd-yy"); //formats the date to be how we want it to output
            this.date.setText(properDateFormat.format(parsedDate));
            return this;
        } catch(Exception e){
            e.printStackTrace();
            return hideDate();
        }
    }

    @Override
    public PostCommentContract.View.ViewHolder setLikeState(LikeState likeState) {
        if (likeState == null)
            return this;

        Log.i("likestate: ", likeState.getDatabaseValue());

        switch (likeState) {
            case UPVOTE:
                setLikeUpvoteState();
                break;
            case NEUTRAL:
                setLikeNeutralState();
                break;
            case DOWNVOTE:
                setLikeDownvoteState();
                break;
            default:
                setLikeNeutralState();
                break;
        }
        return this;
    }

    private void setLikeDownvoteState() {
        upvote.setColorFilter(Color.BLACK, PorterDuff.Mode.SRC_ATOP); //sets upvote color to black
        downvote.setColorFilter(Color.BLUE, PorterDuff.Mode.SRC_ATOP); //sets downvote color to blue
    }

    private void setLikeNeutralState() {
        upvote.setColorFilter(Color.BLACK, PorterDuff.Mode.SRC_ATOP); //sets upvote color to black
        downvote.setColorFilter(Color.BLACK, PorterDuff.Mode.SRC_ATOP); //sets downvote color to black
    }

    private void setLikeUpvoteState() {
        upvote.setColorFilter(Color.RED, PorterDuff.Mode.SRC_ATOP); //sets upvote color to red
        downvote.setColorFilter(Color.BLACK, PorterDuff.Mode.SRC_ATOP); //sets downvote color to black
    }

    @Override
    public PostCommentContract.View.ViewHolder hideDate() {
        date.setVisibility(GONE);
        return this;
    }

    /**
     * Description: changes image from a string to a bitmap, then setting image
     * @param image: image to convert
     */
    @Override
    public PostCommentContract.View.ViewHolder setUserImageFromString(String image){
        if (image != null && !image.isEmpty()) {
            Bitmap bitmap = CameraUtils.stringToBitmap(image); //converts image string to bitmap
            Log.i("image", image);
            profileImage.setImageBitmap(bitmap);
        }
        return this;
    }

    public PostCommentContract.View.ViewHolder setSavedState(Boolean savedState) {
        if(saved!=null && unsaved!=null){
            if(savedState){
                this.unsaved.setVisibility(GONE);
                this.saved.setVisibility(View.VISIBLE);
            } else {
                this.saved.setVisibility(GONE);
                this.unsaved.setVisibility(View.VISIBLE);
            }
        }
        return this;
    }

    public void changeUpvoteLikeState(int i) {
        try {
            mPresenter.changeUpvoteLikeState(this, i);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void changeDownvoteLikeState(int i) {
        try {
            mPresenter.changeDownvoteLikeState(this, i);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Invokes a method in the Presenter to update the given viewHolder's saved state
     * @param i the position of the viewHolder, used to identify the view
     */
    public void updateSavedState(int i){
        try {
            mPresenter.updateSavedState(this, i);
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public TextView getUserName() {
        return username;
    }

    @Override
    public ImageView getProfileImage() {
        return profileImage;
    }

    @Override
    public void onEditComment(int i, String activityId) {
        if (!waitingForResponse) {
            if (!isEditing) {
                mTextEditorWatcher = getEditCommentTextWatcher(activityId);
                editText.addTextChangedListener(mTextEditorWatcher);
                editText.setText(postText.getText());

                setEditButtonOnClickListener(i, activityId);
                setCancelButtonOnClickListener(activityId);
                showEditText();
            }
            else {
                Snackbar.make(mCoordinatorLayout, "Already editing comment", SNACKBAR_LENGTH_VERY_SHORT).show();
            }
        }
    }

    private void setEditButtonOnClickListener(int i, String activityId) {
        editButton.setOnClickListener(
                (View view) -> {
                    if (editText.getText().toString().isEmpty()) {
                        if (mSnackbar == null || !mSnackbar.isShownOrQueued()) {
                            mSnackbar = Snackbar.make(mCoordinatorLayout, R.string.edit_comment_empty_text_error, SNACKBAR_LENGTH_VERY_SHORT);
                            mSnackbar.show();
                        }
                    } else if (!mPresenter.unsavedEditsExist(i, activityId)) {
                        if (mSnackbar == null || !mSnackbar.isShownOrQueued()) {
                            mSnackbar = Snackbar.make(mCoordinatorLayout, R.string.edit_post_no_changes, SNACKBAR_LENGTH_VERY_SHORT);
                            mSnackbar.show();
                        }
                    } else sendCommentToDatabase(i, activityId);
                });
    }

    private void setCancelButtonOnClickListener(String activityId) {
        cancelButton.setOnClickListener(view -> {
            hideEditText();
            setWaitingForResponse(false);
            dismissSnackBar();
            mPresenter.onCancelEditComment(activityId);
        });
    }

    private void sendCommentToDatabase(int i, String activityId) {
        if (!waitingForResponse) {
            ActivityUtils.hideKeyboardFrom(mView.getContext(), mView);

            String newText = editText.getText().toString();
            mPresenter.sendCommentToDatabase(this, i, activityId, newText);
        }
    }

    @NonNull
    @Override
    public String getCallBackIdentifier() {
        if (mCallBackIdentifier == null)
            return "";
        else
            return mCallBackIdentifier;
    }

    @Override
    public void setCallBackIdentifier(String callBackIdentifier) {
        mCallBackIdentifier = callBackIdentifier;
    }

    @Override
    public void clearCallBackIdentifier() {
        mCallBackIdentifier = null;
    }

    @Override
    public void setWaitingForResponse(boolean waitingForResponse) {
        Log.d("PostCommentViewHolder", "set waiting for response");
        this.waitingForResponse = waitingForResponse;
    }

    @Override
    public void setSnackBarForCommentInProgress(String activityId) {
        Log.d("PostCommentViewHolder", "snackbar edit comment in progress");
        mSnackbar = Snackbar.make(mCoordinatorLayout, R.string.edit_comment_in_progress, Snackbar.LENGTH_INDEFINITE);
        addSnackBarOnDismissCallback(activityId);
        mSnackbar.show();
    }

    @Override
    public void setSnackBarEditCommentSuccess(String activityId) {
        Log.d("PostCommentViewHolder", "snackbar edit comment success");
        if (mSnackbar == null) {
            mSnackbar = Snackbar.make(mCoordinatorLayout, R.string.edit_comment_success, SNACKBAR_LENGTH_VERY_SHORT);
        }
        else {
            mSnackbar.setText(R.string.edit_comment_success);
            mSnackbar.setDuration(SNACKBAR_LENGTH_VERY_SHORT);
        }
        addSnackBarOnDismissCallback(activityId);
        mSnackbar.show();
    }

    @Override
    public void setSnackBarEditCommentRetry(int i, String activityId) {
        Log.d("PostCommentViewHolder", "snackbar edit comment retry");
        mSnackbar = Snackbar.make(mCoordinatorLayout, R.string.edit_comment_failed, Snackbar.LENGTH_INDEFINITE);
        mSnackbar.setAction(R.string.retry_action, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendCommentToDatabase(i, activityId);
            }
        });
        addSnackBarOnDismissCallback(activityId);
        mSnackbar.show();
    }

    private void addSnackBarOnDismissCallback(String activityId) {
        if (mSnackbar != null) {
            mSnackbar.addCallback(new Snackbar.Callback() {
                @Override
                public void onDismissed(Snackbar transientBottomBar, int event) {
                    super.onDismissed(transientBottomBar, event);
                    if (event != DISMISS_EVENT_MANUAL && event != DISMISS_EVENT_CONSECUTIVE) {
                        mPresenter.onSnackBarDismissed(activityId);
                    }
                }
            });
        }
    }

    @Override
    public void dismissSnackBar() {
        Log.d("PostCommentViewHolder", "snackbar dismiss");
        if (mSnackbar != null) {
            mSnackbar.dismiss();
        }
    }
}
