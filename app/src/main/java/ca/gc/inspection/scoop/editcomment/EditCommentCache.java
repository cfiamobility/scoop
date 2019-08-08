package ca.gc.inspection.scoop.editcomment;

import android.util.Log;

import java.util.HashMap;

/**
 * Datacache for storing the current edits for post comments. We index the EditCommentData objects by
 * the unique activityId of a post comment. This is because the adapter position i of a post comment
 * can change if comments are added, deleted or if the order is refreshed.
 */
public class EditCommentCache {
    protected HashMap<String, EditCommentData> mMap;

    public EditCommentCache() {
        mMap = new HashMap<>();
    }

    /**
     * Add an EditCommentData containing the current edits for a post comment if it does not exist in
     * the cache. Otherwise, update the existing values.
     *
     * @param activityId        unique identifier of post comment.
     * @param postText          to be updated to.
     */
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

    public int size() {
        return mMap.size();
    }

    @Override
    public String toString() {
        return mMap.toString();
    }
}
