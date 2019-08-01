package ca.gc.inspection.scoop.postcomment;


import ca.gc.inspection.scoop.createpost.InteractorBundle;
import ca.gc.inspection.scoop.editpost.EditPostData;
import ca.gc.inspection.scoop.postcomment.PostCommentContract;

public class EditCommentBundle extends InteractorBundle {

    private String mActivityId;
    private PostCommentContract.View.ViewHolder mViewHolderInterface;

    EditCommentBundle() {
        super();
    }

    public String getActivityId() {
        return mActivityId;
    }

    public void setActivityId(String activityId) {
        mActivityId = activityId;
    }

    public void setViewHolder(PostCommentContract.View.ViewHolder viewHolderInterface) {
        mViewHolderInterface = viewHolderInterface;
    }

    public PostCommentContract.View.ViewHolder getViewHolder() {
        return mViewHolderInterface;
    }
}
