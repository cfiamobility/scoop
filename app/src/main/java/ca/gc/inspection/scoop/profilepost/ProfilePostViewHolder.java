package ca.gc.inspection.scoop.profilepost;

import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import ca.gc.inspection.scoop.postcomment.PostCommentContract;
import ca.gc.inspection.scoop.profilecomment.ProfileCommentViewHolder;
import ca.gc.inspection.scoop.R;
import ca.gc.inspection.scoop.profilelikes.ProfileLikesViewHolder;

import static android.view.View.GONE;

public class ProfilePostViewHolder extends ProfileLikesViewHolder
        implements ProfilePostContract.View.ViewHolder {
    /**
     * ViewHolder for viewing a profile post.
     */

    ProfilePostContract.Presenter.ViewHolderAPI mPresenter;

    public TextView commentCount;
//    public ImageView optionsMenu;


    public ProfilePostViewHolder(View v, ProfilePostContract.Presenter.ViewHolderAPI presenter) {
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
    public ProfilePostContract.View.ViewHolder setCommentCount(String commentCount) {
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
    public ProfilePostContract.View.ViewHolder setPostTitle(String postTitle) {
        super.setPostTitle(postTitle);
        return this;
    }



}
