package ca.gc.inspection.scoop.postcomment;

import android.util.Log;

import java.util.HashMap;

public class EditCommentCache {
    protected HashMap<String, EditCommentData> mMap;

    EditCommentCache() {
        mMap = new HashMap<>();
    }

    public void insertOrUpdateExistingEditCommentDataWithPostText(String activityId, String postText) {
        if (mMap.containsKey(activityId)) {
            mMap.get(activityId).setPostText(postText);
        }
        else {
            mMap.put(activityId, new EditCommentData(activityId, postText));
        }
        Log.d("EditCommentCache", "map:" + toString());
    }

    public EditCommentData getEditCommentData(String activityId) {
        return mMap.getOrDefault(activityId, null);
    }

    public void removeEditCommentData(String activityId) {
        mMap.remove(activityId);
    }

    @Override
    public String toString() {
        return mMap.toString();
    }
}
