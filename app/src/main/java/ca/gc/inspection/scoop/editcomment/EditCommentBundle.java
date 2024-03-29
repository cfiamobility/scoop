package ca.gc.inspection.scoop.editcomment;


import ca.gc.inspection.scoop.createpost.InteractorBundle;
import ca.gc.inspection.scoop.editpost.EditPostData;
import ca.gc.inspection.scoop.postcomment.PostCommentContract;

/**
 * See InteractorBundle documentation
 *
 * Bundle used to store data for PostCommentPresenter which is needed in the callback
 */
public class EditCommentBundle extends InteractorBundle {

    private String mActivityId;
    private PostCommentContract.View.ViewHolder mViewHolderInterface;

    public EditCommentBundle() {
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
