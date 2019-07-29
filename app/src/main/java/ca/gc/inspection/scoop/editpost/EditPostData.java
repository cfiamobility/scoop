package ca.gc.inspection.scoop.editpost;

public class EditPostData {
    private String mActivityId, mPostTitle, mPostText, mPostImagePath;

    public EditPostData(String activityId, String postTitle, String postText, String postImagePath) {
        mActivityId = activityId;
        mPostTitle = postTitle;
        mPostText = postText;
        mPostImagePath = postImagePath;
    }

    public String getActivityId() {
        return mActivityId;
    }

    public void setActivityId(String activityId) {
        this.mActivityId = activityId;
    }

    public String getPostTitle() {
        return mPostTitle;
    }

    public void setPostTitle(String postTitle) {
        this.mPostTitle = postTitle;
    }

    public String getPostText() {
        return mPostText;
    }

    public void setPostText(String postText) {
        this.mPostText = postText;
    }

    public String getPostImagePath() {
        return mPostImagePath;
    }

    public void setPostImagePath(String postImagePath) {
        this.mPostImagePath = postImagePath;
    }
}
