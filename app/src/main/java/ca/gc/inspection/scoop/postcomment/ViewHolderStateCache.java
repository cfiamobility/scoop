package ca.gc.inspection.scoop.postcomment;

import android.util.Log;

import java.util.HashMap;

public class ViewHolderStateCache {
    private HashMap<String, ViewHolderState> mMap;

    ViewHolderStateCache() {
        mMap = new HashMap<>();
    }

    public void createIfMissingViewHolderState(
            String activityId, boolean waitingForState, int position, ViewHolderState.SnackBarState snackBarState) {
        if (!mMap.containsKey(activityId)) {
            ViewHolderState viewHolderState = new ViewHolderState(activityId);
            mMap.put(activityId, viewHolderState);
        }

        getViewHolderState(activityId).setPosition(position);
        getViewHolderState(activityId).setSnackBarState(snackBarState);
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
