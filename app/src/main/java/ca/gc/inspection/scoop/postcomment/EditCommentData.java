package ca.gc.inspection.scoop.postcomment;

public class EditCommentData {
    protected String mActivityId, mPostText;

    public EditCommentData(String activityId, String postText) {
        mActivityId = activityId;
        mPostText = postText;
    }

    public String getActivityId() {
        return mActivityId;
    }

    public void setActivityId(String activityId) {
        this.mActivityId = activityId;
    }

    public String getPostText() {
        return mPostText;
    }

    public void setPostText(String postText) {
        this.mPostText = postText;
    }
}
