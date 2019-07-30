package ca.gc.inspection.scoop.postcomment;


import ca.gc.inspection.scoop.createpost.InteractorBundle;
import ca.gc.inspection.scoop.editpost.EditPostData;

public class EditPostCommentBundle extends InteractorBundle {
    private PostCommentContract.View.ViewHolder mViewHolderInterface;
    private int mPosition;
    private EditPostData mEditPostData;

    EditPostCommentBundle() {
        super();
    }

    public void setViewHolder(PostCommentContract.View.ViewHolder viewHolderInterface) {
        mViewHolderInterface = viewHolderInterface;
    }

    public PostCommentContract.View.ViewHolder getViewHolder() {
        return mViewHolderInterface;
    }

    public void setPosition(int position) {
        mPosition = position;
    }

    public int getPosition() {
        return mPosition;
    }

    public void setEditPostData(EditPostData editPostData) {
        mEditPostData = editPostData;
    }

    public EditPostData getEditPostData() {
        return mEditPostData;
    }
}
