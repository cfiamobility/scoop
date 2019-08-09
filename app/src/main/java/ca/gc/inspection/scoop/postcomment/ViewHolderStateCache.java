package ca.gc.inspection.scoop.postcomment;

import android.support.annotation.NonNull;
import android.util.Log;

import java.util.HashMap;

/**
 * Datacache for storing the current view holder states. We index the ViewHolderState objects by
 * the unique activityId of a post comment. This is because the adapter position i of a post comment
 * can change if comments are added, deleted or if the order is refreshed.
 */
public class ViewHolderStateCache {
    private HashMap<String, ViewHolderState> mMap;

    ViewHolderStateCache() {
        mMap = new HashMap<>();
    }

    /**
     * Add a ViewHolderState object containing the current state for a  post comment if it does not exist in
     * the cache. Otherwise, update the existing values.
     *
     * @param activityId        unique identifier of post comment.
     * @param waitingForState   whether the post comment is waiting for a database response
     * @param position          estimated adapter position - may change when comments are added/removed/reordered
     * @param snackBarState     enum defined in ViewHolderState
     */
    public void createIfMissingViewHolderState(
            String activityId, boolean waitingForState, int position, ViewHolderState.SnackBarState snackBarState) {
        if (!mMap.containsKey(activityId)) {
            ViewHolderState viewHolderState = new ViewHolderState(activityId);
            mMap.put(activityId, viewHolderState);
        }

        getViewHolderState(activityId).setPosition(position)
                .setWaitingForResponse(waitingForState)
                .setSnackBarState(snackBarState);
        Log.d("ViewHolderState",
                "waitingForResponse" + getViewHolderState(activityId).isWaitingForResponse() +
                ",\tsnackBarState:" + getViewHolderState(activityId).getSnackBarState().toString());
    }

    public ViewHolderState getViewHolderState(String activityId) {
        return mMap.getOrDefault(activityId, null);
    }

    public void removeViewHolderState(String activityId) {
        mMap.remove(activityId);
    }

    /**
     * The estimated adapter position may get out of sync with the Presenter's DataCache
     * when items are added, removed, or reordered. Despite this, we provide a method to update
     * the estimated positions when a post comment is added as a best effort to keep these values
     * in sync.
     */
    public void incrementPositionForAll() {
        for (ViewHolderState viewHolderState: mMap.values()) {
            viewHolderState.incrementPosition();
        }
    }

    @Override
    public String toString() {
        return mMap.toString();
    }

}
