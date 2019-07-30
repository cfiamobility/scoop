package ca.gc.inspection.scoop.postcomment;

import java.util.HashMap;

public class EditCommentCache {
    private HashMap<String, EditCommentData> mMap;

    EditCommentCache() {
        mMap = new HashMap<>();
    }

    public void addEditCommentData(String activityId, EditCommentData editCommentData) {
        mMap.put(activityId, editCommentData);
    }

    public EditCommentData getEditCommentData(String activityId) {
        return mMap.get(activityId);
    }
}
