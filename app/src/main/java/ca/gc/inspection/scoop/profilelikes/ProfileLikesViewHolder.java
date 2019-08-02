package ca.gc.inspection.scoop.profilelikes;

import ca.gc.inspection.scoop.R;
import android.view.View;
import android.widget.TextView;
import ca.gc.inspection.scoop.editpost.EditPostData;
import ca.gc.inspection.scoop.postoptionsdialog.PostOptionsDialogReceiver;
import ca.gc.inspection.scoop.profilecomment.ProfileCommentViewHolder;
import static ca.gc.inspection.scoop.editpost.EditPostActivity.startEditPostActivity;

public class ProfileLikesViewHolder extends ProfileCommentViewHolder implements
        ProfileLikesContract.View.ViewHolder,
        PostOptionsDialogReceiver.EditPostReceiver {
    /**
     * ViewHolder for viewing a profile post.
     */

    ProfileLikesContract.Presenter.ViewHolderAPI mPresenter;

    public TextView commentCount;
    private View mView;
//    public ImageView optionsMenu;


    public ProfileLikesViewHolder(View v, ProfileLikesContract.Presenter.ViewHolderAPI presenter) {
        super(v, presenter);
        mView = v;
        commentCount = v.findViewById(R.id.comment_count);
//        optionsMenu = v.findViewById(R.id.options_menu);

        mPresenter = presenter;
    }

    /**
     *
     * @param commentCount
     * @return
     */
    @Override
    public ProfileLikesContract.View.ViewHolder setCommentCount(String commentCount) {
        if(commentCount.equals("null"))
            commentCount = "0";
        this.commentCount.setText(commentCount);
        return this;
    }

    /**
     *
     * @param postTitle: post title
     * @return
     */
    @Override
    public ProfileLikesContract.View.ViewHolder setPostTitle(String postTitle) {
        super.setPostTitle(postTitle);
        return this;
    }


    @Override
    protected void setupEditComment(View v) {
    }

    @Override
    public void onEditPost(int i) {
        EditPostData editPostData = mPresenter.getEditPostData(i);
        startEditPostActivity(mView.getContext(), editPostData);
    }
}
