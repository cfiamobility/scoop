package ca.gc.inspection.scoop.editpost;

import ca.gc.inspection.scoop.editcomment.EditCommentData;

public class EditPostData extends EditCommentData {
    private String mPostTitle, mPostImagePath;

    public EditPostData(String activityId, String postTitle, String postText, String postImagePath) {
        super(activityId, postText);
        mPostTitle = postTitle;
        mPostImagePath = postImagePath;
    }

    public String getPostTitle() {
        return mPostTitle;
    }

    public void setPostTitle(String postTitle) {
        this.mPostTitle = postTitle;
    }

    public String getPostImagePath() {
        return mPostImagePath;
    }

    public void setPostImagePath(String postImagePath) {
        this.mPostImagePath = postImagePath;
    }
}
