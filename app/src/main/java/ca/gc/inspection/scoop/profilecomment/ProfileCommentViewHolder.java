package ca.gc.inspection.scoop.profilecomment;

import android.view.View;
import android.widget.TextView;
import ca.gc.inspection.scoop.postcomment.PostCommentViewHolder;
import ca.gc.inspection.scoop.R;

public class ProfileCommentViewHolder extends PostCommentViewHolder
        implements ProfileCommentContract.View.ViewHolder {
    /**
     * ViewHolder for replying to a post action; it is the most generic View Holder
     * and contains the minimum views (no comment count, options menus, or images)
     * related to "posting" actions. Parent View Holder for ProfilePostViewHolder.
     */

    ProfileCommentContract.Presenter.ViewHolderAPI mPresenter;

    public TextView postTitle;

    public ProfileCommentViewHolder(View v, ProfileCommentContract.Presenter.ViewHolderAPI presenter) {
        super(v, presenter);
        postTitle = v.findViewById(R.id.post_title);

        mPresenter = presenter;
    }

    /**
     * Sets the post title ("Replying to ..." )
     * @param postTitle: post title
     */
    @Override
    public ProfileCommentContract.View.ViewHolder setPostTitle(String postTitle) {
        this.postTitle.setText(postTitle);
        return this;
    }

}
