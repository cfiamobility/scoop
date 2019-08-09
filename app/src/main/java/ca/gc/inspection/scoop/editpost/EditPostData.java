package ca.gc.inspection.scoop.editpost;

import ca.gc.inspection.scoop.editcomment.EditCommentData;

/**
 * Data class used to store the editable fields of a Post. Extends from EditCommentData by
 * adding a post title and post image path field.
 *
 * Unlike EditCommentData, this is not stored in a Cache object and simply provides data encapsulation
 * when passing parameters through the Presenter and View layers. (ie. to start the EditPostActivity)
 */
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
