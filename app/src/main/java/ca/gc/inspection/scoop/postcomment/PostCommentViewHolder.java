package ca.gc.inspection.scoop.postcomment;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.SpannableStringBuilder;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import ca.gc.inspection.scoop.editleavedialog.EditLeaveDialog;
import ca.gc.inspection.scoop.editleavedialog.EditLeaveEventListener;
import ca.gc.inspection.scoop.postoptionsdialog.PostOptionsDialogReceiver;
import ca.gc.inspection.scoop.searchprofile.UserProfileListener;
import ca.gc.inspection.scoop.util.ActivityUtils;
import ca.gc.inspection.scoop.util.CameraUtils;
import ca.gc.inspection.scoop.R;
import ca.gc.inspection.scoop.util.TextFormat;

import static android.view.View.GONE;
import static ca.gc.inspection.scoop.searchprofile.view.SearchProfileViewHolder.getSpannableStringBuilderWithFormat;

/**
 * ViewHolder for replying to a post action; it is the most generic View Holder
 * and contains the minimum views (no comment count, options menus, or images)
 * related to "posting" actions. Parent View Holder for ProfilePostViewHolder.
 */

public class PostCommentViewHolder extends RecyclerView.ViewHolder implements
        PostCommentContract.View.ViewHolder,
        UserProfileListener,
        PostOptionsDialogReceiver.EditCommentReceiver,
        EditLeaveEventListener {

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

    /**
     * Check if there are unsaved changes for the post comment. If so, prompt the user if they want
     * to leave the editing UI and lose their unsaved changes.
     *
     * @param fragmentManager   Pass in only the Android object we need instead of extracting the
     *                          fragment manager from the View. (Principle of least privilege)
     * @param i                 adapter position.
     * @param activityId        unique identifier of post comment.
     */
    private void confirmLoseEdits(FragmentManager fragmentManager, int i, String activityId) {
        if (mPresenter.unsavedEditsExistForViewHolder(i, activityId)) {
            EditLeaveDialog editLeaveDialog = EditLeaveDialog.newInstance(activityId);
            editLeaveDialog.setEditLeaveEventListener(this);
            editLeaveDialog.show(fragmentManager, EditLeaveDialog.TAG);
        } else {
            confirmLeaveEvent(activityId);
        }
    }

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

    /**
     * Helper method to setup the edit comment layout objects.
     * Overridden by ProfileLikesViewHolder since Posts do not contain these view objects.
     *
     * @param v     Used to call findViewById
     */
    protected void setupEditComment(View v) {
        mCoordinatorLayout = v.findViewById(R.id.edit_comment_coordinator);
        counter = v.findViewById(R.id.edit_post_text_counter);

        editText = v.findViewById(R.id.edit_post_text);
        editButton = v.findViewById(R.id.edit_post_text_btn);
        cancelButton = v.findViewById(R.id.edit_post_cancel_btn);
        hideEditText();
    }

    /**
     * Updates current word count when editing comment. Caches data so that the edits persist through
     * scrolling and refreshes.
     *
     * @param activityId    Unique identifier of post comment
     * @return              TextWatcher
     */
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

    /**
     * Hides the UI for editing the comment text.
     */
    @Override
    public void hideEditText() {
        if (editText != null && editButton != null && counter != null) {
            postText.setVisibility(View.VISIBLE);
            editText.setVisibility(View.GONE);
            editButton.setVisibility(View.GONE);
            counter.setVisibility(View.GONE);
        }
    }

    /**
     * Shows the UI for editing the comment text.
     */
    public void showEditText() {
        if (editText != null && editButton != null && counter != null) {
            postText.setVisibility(View.GONE);
            editText.setVisibility(View.VISIBLE);
            editButton.setVisibility(View.VISIBLE);
            counter.setVisibility(View.VISIBLE);
        }
    }

    /**
     * Called during onBind if there are unsaved edits in the EditCommentCache which is relevant to
     * this ViewHolder.
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
     * Shows formatted text which can include bold words and an italicized footer message.
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

    /**
     * Check if the comment can be edited. If so, setup the UI for editing.
     * Otherwise, show a SnackBar message.
     *
     * @param i             Current adapter position - can change when comments are added/removed or
     *                      when the page is refreshed
     * @param activityId    Unique identifier for post comment
     */
    @Override
    public void onEditComment(int i, String activityId) {
        if (!waitingForResponse) {
            mTextEditorWatcher = getEditCommentTextWatcher(activityId);
            editText.addTextChangedListener(mTextEditorWatcher);
            editText.setText(mPresenter.getPostTextById(i));

            setEditButtonOnClickListener(i, activityId);
            setCancelButtonOnClickListener(i, activityId);
            showEditText();
        }
        else Log.d("PostCommentViewHolder", "waitingForResponse for onEditComment");
    }

    /**
     * Helper method for onEditComment to setup the editButton listener. Checks if the new text is
     * not empty and different from the current text before saving the changes to the database.
     * Otherwise, show the relevant SnackBar message. Since these messages are very short,
     * we do not need to cache them in the ViewHolderStateCache.
     *
     * @param i             Current adapter position - can change when comments are added/removed or
     *                      when the page is refreshed
     * @param activityId    Unique identifier for post comment
     */
    private void setEditButtonOnClickListener(int i, String activityId) {
        editButton.setOnClickListener(
                (View view) -> {
                    if (validEditsExist(i, activityId)) {
                        sendCommentToDatabase(i, activityId);
                    }
                });
    }

    /**
     * Helper method to validate that the edited text is not empty and that it differs from the original.
     *
     * @param i             Adapter position.
     * @param activityId    Unique identifier for post comment.
     * @return              True if the edit can be saved to the database.
     */
    private boolean validEditsExist(int i, String activityId) {
        if (editText.getText().toString().isEmpty()) {
            if (mSnackbar == null || !mSnackbar.isShownOrQueued()) {
                mSnackbar = Snackbar.make(mCoordinatorLayout, R.string.edit_comment_empty_text_error, SNACKBAR_LENGTH_VERY_SHORT);
                mSnackbar.show();
            }
            return false;
        } else if (!mPresenter.unsavedEditsExistForViewHolder(i, activityId)) {
            if (mSnackbar == null || !mSnackbar.isShownOrQueued()) {
                mSnackbar = Snackbar.make(mCoordinatorLayout, R.string.edit_post_no_changes, SNACKBAR_LENGTH_VERY_SHORT);
                mSnackbar.show();
            }
            return false;
        }
        return true;
    }

    /**
     * Helper method for onEditComment to setup the cancelButton listener. Need to drop our unsaved
     * changes from the Presenter's Cache otherwise onBind will re-attach the stale data to the
     * ViewHolder.
     *
     * @param i             Current adapter position - can change when comments are added/removed or
     *                      when the page is refreshed
     * @param activityId    Unique identifier for post comment
     */
    private void setCancelButtonOnClickListener(int i, String activityId) {
        cancelButton.setOnClickListener(view ->
                confirmLoseEdits(((FragmentActivity) view.getContext()).getSupportFragmentManager(), i, activityId));
    }

    /**
     * Hides editing UI and goes back to showing the post comment text.
     * Updates the Presenter's cache to prevent onBind from setting the ViewHolder back to an edit
     * state when scrolling.
     *
     * @param activityId    Unique identifier for post comment.
     */
    private void cancelEdit(String activityId) {
        hideEditText();
        setWaitingForResponse(false);
        dismissSnackBar();
        /* must remove text watcher otherwise the edit comment cache will be repopulated and scrolling
        will bind the stale edit text state to the viewholder */
        editText.removeTextChangedListener(mTextEditorWatcher);
        mPresenter.onCancelEditComment(activityId);
    }

    /**
     * Saves the edits made to the post comment to the database.
     *
     * @param i             Current adapter position - can change when comments are added/removed or
     *                      when the page is refreshed
     * @param activityId    Unique identifier for post comment
     */
    private void sendCommentToDatabase(int i, String activityId) {
        if (!waitingForResponse) {
            ActivityUtils.hideKeyboardFrom(mView.getContext(), mView);
            waitingForResponse = true;

            String newText = editText.getText().toString();
            mPresenter.sendCommentToDatabase(this, i, activityId, newText);
        }
    }

    /**
     * Getter for call back identifier.
     *
     * When editing a post comment there is a brief period between sending the comment to the database
     * and receiving the database response. Upon the database response, the presenter needs to know
     * if the ViewHolder is still relevant or if it is displaying a different post comment.
     *
     * If the ViewHolder was scrolled and recycled, we don't want it to update UI as it may be
     * showing a completely different post comment. Instead the correct view holder will eventually
     * have its UI updated using the onBind methods and Presenter-scoped Cache objects.
     *
     * @return String of unique identifier for post comment (ie. activityId)
     */
    @NonNull
    @Override
    public String getCallBackIdentifier() {
        if (mCallBackIdentifier == null)
            return "";
        else
            return mCallBackIdentifier;
    }

    /**
     * Setter for call back identifier. This is set by the Presenter prior to editing the comment.
     *
     * See documentation for getCallBackIdentifier.
     *
     * @param callBackIdentifier    String of unique identifier for post comment (ie. activityId)
     */
    @Override
    public void setCallBackIdentifier(String callBackIdentifier) {
        mCallBackIdentifier = callBackIdentifier;
    }

    /**
     * Clears the call back identifier every time we bind new data to the ViewHolder. This occurs
     * when scrolling or refreshing.
     *
     * See documentation for getCallBackIdentifier.
     */
    @Override
    public void clearCallBackIdentifier() {
        mCallBackIdentifier = null;
    }

    /**
     * Used by the Presenter's onBind methods to persist whether the post comment is waiting
     * for a database response.
     *
     * @param waitingForResponse    This specific post comment cannot send another database request
     *                              until a response is received.
     */
    @Override
    public void setWaitingForResponse(boolean waitingForResponse) {
        Log.d("PostCommentViewHolder", "set waiting for response: " + waitingForResponse);
        this.waitingForResponse = waitingForResponse;
    }

    /**
     * Used by the Presenter's onBind methods to persist Snackbar for comment in progress.
     *
     * @param activityId        Unique indentifier of post comment.
     */
    @Override
    public void setSnackBarForCommentInProgress(String activityId) {
        Log.d("PostCommentViewHolder", "snackbar edit comment in progress");
        mSnackbar = Snackbar.make(mCoordinatorLayout, R.string.edit_comment_in_progress, Snackbar.LENGTH_INDEFINITE);
        addSnackBarOnDismissCallback(activityId);
        mSnackbar.show();
    }

    /**
     * Used by the Presenter's onBind methods to persist Snackbar for comment success.
     *
     * @param activityId        Unique indentifier of post comment.
     */
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

    /**
     * Used by the Presenter's onBind methods to persist Snackbar for comment retry.
     *
     * @param i                 adapter position
     * @param activityId        Unique indentifier of post comment.
     */
    @Override
    public void setSnackBarEditCommentRetry(int i, String activityId) {
        Log.d("PostCommentViewHolder", "snackbar edit comment retry");
        mSnackbar = Snackbar.make(mCoordinatorLayout, R.string.edit_comment_failed, Snackbar.LENGTH_INDEFINITE);
        mSnackbar.setAction(R.string.retry_action, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validEditsExist(i, activityId)) {
                    sendCommentToDatabase(i, activityId);
                }
            }
        });
        addSnackBarOnDismissCallback(activityId);
        mSnackbar.show();
    }

    /**
     * When the SnackBar is dismissed, we need to update the Presenter's ViewHolderStateCache otherwise
     * onBind will set the ViewHolder's SnackBar to a stale state. (ie. every time you scroll back
     * to the ViewHolder it may reshow the last SnackBar which was already dismissed)
     *
     * @param activityId
     */
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

    /**
     * Called by Presenter to dismiss the current SnackBar during onBind when new data is attached
     * to the recycled ViewHolders.
     */
    @Override
    public void dismissSnackBar() {
        Log.d("PostCommentViewHolder", "snackbar dismiss");
        if (mSnackbar != null) {
            mSnackbar.dismiss();
        }
    }

    /**
     * Callback for EditLeaveDialog confirm option.
     * User has confirmed they want to stop editing the post comment lose their unsaved edits.
     *
     * @param params    contains the activityId.
     */
    @Override
    public void confirmLeaveEvent(String... params) {
        cancelEdit(params[0]);
    }

    /**
     * Callback for EditLeaveDialog's option to cancel leave event.
     * Not necessary but included to be consistent with attaching a callback to a Dialog button.
     */
    @Override
    public void cancelLeaveEvent() {

    }
}
