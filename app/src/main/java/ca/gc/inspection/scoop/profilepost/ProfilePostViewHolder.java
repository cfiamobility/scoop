package ca.gc.inspection.scoop.profilepost;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import ca.gc.inspection.scoop.profilecomment.ProfileCommentContract;
import ca.gc.inspection.scoop.profilecomment.ProfileCommentViewHolder;
import ca.gc.inspection.scoop.R;

public class ProfilePostViewHolder extends ProfileCommentViewHolder
        implements ProfilePostContract.View.ViewHolder {

    public TextView commentCount;
    public ImageView optionsMenu;


    public ProfilePostViewHolder(View v) {
        super(v);
        commentCount = v.findViewById(R.id.comment_count);
        optionsMenu = v.findViewById(R.id.options_menu);
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
