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

    public void setActivityId(String mActivityId) {
        this.mActivityId = mActivityId;
    }

    public String getPostTitle() {
        return mPostTitle;
    }

    public void setPostTitle(String mPostTitle) {
        this.mPostTitle = mPostTitle;
    }

    public String getPostText() {
        return mPostText;
    }

    public String getPostImagePath() {
        return mPostImagePath;
    }

    public void setPostImagePath(String mPostImagePath) {
        this.mPostImagePath = mPostImagePath;
    }
}
