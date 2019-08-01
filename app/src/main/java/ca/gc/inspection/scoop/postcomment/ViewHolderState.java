package ca.gc.inspection.scoop.postcomment;

import android.support.annotation.NonNull;

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

    public void setPosition(int i) {
        mPosition = i;
    }

    public void incrementPosition() {
        mPosition += 1;
    }

    public void setSnackBarState(SnackBarState snackBarState) {
        mSnackBarState = snackBarState;
    }

    @NonNull
    public SnackBarState getSnackBarState() {
        return mSnackBarState;
    }

    public boolean isWaitingForResponse() {
        return mWaitingForResponse;
    }

    public void setWaitingForResponse(boolean waitingForResponse) {
        mWaitingForResponse = waitingForResponse;
    }
}
