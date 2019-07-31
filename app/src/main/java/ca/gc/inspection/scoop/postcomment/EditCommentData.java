package ca.gc.inspection.scoop.postcomment;

public class EditCommentData {
    protected String mActivityId, mPostText;
    private int mPosition;

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

    public int getPosition() {
        return mPosition;
    }

    public void setPosition(int i) {
        mPosition = i;
    }

    public void incrementPosition() {
        mPosition += 1;
    }
}
