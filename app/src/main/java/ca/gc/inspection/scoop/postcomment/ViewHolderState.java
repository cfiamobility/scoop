package ca.gc.inspection.scoop.postcomment;

import android.support.annotation.NonNull;

/**
 * Stores the state of a ViewHolder such as the current SnackBar messages being shown, and whether
 * it is waiting for a database response. Used in a Presenter's onBind methods to restore the
 * state of the ViewHolder when they are recycled during scrolling.
 */
public class ViewHolderState {

    public enum SnackBarState {
        NONE, EDIT_COMMENT_IN_PROGRESS, EDIT_COMMENT_SUCCESS, EDIT_COMMENT_RETRY
    }

    private String mActivityId;
    private int mPosition;
    private SnackBarState mSnackBarState = SnackBarState.NONE;
    private boolean mWaitingForResponse = false;

    ViewHolderState(String activityId) {
        mActivityId = activityId;
    }

    public int getPosition() {
        return mPosition;
    }

    public ViewHolderState setPosition(int i) {
        mPosition = i;
        return this;
    }

    /**
     * The estimated adapter position may get out of sync with the Presenter's DataCache
     * when items are added, removed, or reordered. Despite this, we provide a method to update
     * the estimated position when a post comment is added as a best effort to keep this value
     * in sync.
     */
    public ViewHolderState incrementPosition() {
        mPosition += 1;
        return this;
    }

    public ViewHolderState setSnackBarState(SnackBarState snackBarState) {
        mSnackBarState = snackBarState;
        return this;
    }

    @NonNull
    public SnackBarState getSnackBarState() {
        return mSnackBarState;
    }

    public boolean isWaitingForResponse() {
        return mWaitingForResponse;
    }

    public ViewHolderState setWaitingForResponse(boolean waitingForResponse) {
        mWaitingForResponse = waitingForResponse;
        // allows method chaining
        return this;
    }
}
