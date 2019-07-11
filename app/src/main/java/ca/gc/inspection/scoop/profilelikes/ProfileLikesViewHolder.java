package ca.gc.inspection.scoop.profilelikes;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import ca.gc.inspection.scoop.R;
import ca.gc.inspection.scoop.profilecomment.ProfileCommentContract;
import ca.gc.inspection.scoop.profilecomment.ProfileCommentViewHolder;


import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import ca.gc.inspection.scoop.profilecomment.ProfileCommentViewHolder;
import ca.gc.inspection.scoop.R;
import ca.gc.inspection.scoop.profilelikes.ProfileLikesContract;

public class ProfileLikesViewHolder extends ProfileCommentViewHolder
        implements ProfileLikesContract.View.ViewHolder {
    /**
     * ViewHolder for viewing a profile post.
     */

    ProfileLikesContract.Presenter.ViewHolderAPI mPresenter;

    public TextView commentCount;
//    public ImageView optionsMenu;


    public ProfileLikesViewHolder(View v, ProfileLikesContract.Presenter.ViewHolderAPI presenter) {
        super(v, presenter);
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
}
