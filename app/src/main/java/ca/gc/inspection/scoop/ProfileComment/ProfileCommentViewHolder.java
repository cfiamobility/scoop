package ca.gc.inspection.scoop.ProfileComment;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import ca.gc.inspection.scoop.*;

/**
 * ViewHolder for replying to a post action; it is the most generic View Holder
 * and contains the minimum views (no comment count, options menus, or images)
 * related to "posting" actions. Parent View Holder for ProfilePostViewHolder.
 */

public class ProfileCommentViewHolder extends RecyclerView.ViewHolder implements ProfileCommentViewHolderContract {

    public TextView username, date, postText, postTitle, likeCount;
    public ImageView profileImage, upvote, downvote;

    public ProfileCommentViewHolder(View v) {
        super(v);
        username = v.findViewById(R.id.name);
        profileImage = v.findViewById(R.id.profile_image);
        date = v.findViewById(R.id.date);
        postText = v.findViewById(R.id.post_text);
        postTitle = v.findViewById(R.id.post_title);
        likeCount = v.findViewById(R.id.vote_count);
        upvote = v.findViewById(R.id.up_vote);
        downvote = v.findViewById(R.id.down_vote);
    }

    /**
     * Sets the post title ("Replying to ..." )
     * @param postTitle: post title
     */
    @Override
    public void setPostTitle(String postTitle) {
        this.postTitle.setText(postTitle);
    }

    /**
     *
     * @param postText
     */
    @Override
    public void setPostText(String postText) {
        this.postText.setText(postText);
    }

    /**
     *
     * @param image
     */
    @Override
    public void setUserImage(Bitmap image) {
        Log.i("image", image.toString());
        profileImage.setImageBitmap(image);
    }

    /**
     *
     * @param userName
     */
    @Override
    public void setUserName(String userName) {
        username.setText(userName);
    }

    /**
     *
     * @param likeCount
     */
    @Override
    public void setLikeCount(String likeCount) {
        this.likeCount.setText(likeCount);
    }

    /**
     *
     * @param date
     */
    @Override
    public void setDate(String date) {
        this.date.setText(date);
    }


    @Override
    public void setLikeDownvoteState() {
        upvote.setColorFilter(Color.BLACK, PorterDuff.Mode.SRC_ATOP); //sets upvote color to black
        downvote.setColorFilter(Color.BLUE, PorterDuff.Mode.SRC_ATOP); //sets downvote color to blue
    }

    @Override
    public void setLikeNeutralState() {
        upvote.setColorFilter(Color.BLACK, PorterDuff.Mode.SRC_ATOP); //sets upvote color to black
        downvote.setColorFilter(Color.BLACK, PorterDuff.Mode.SRC_ATOP); //sets downvote color to black
    }

    @Override
    public void setLikeUpvoteState() {
        upvote.setColorFilter(Color.RED, PorterDuff.Mode.SRC_ATOP); //sets upvote color to red
        downvote.setColorFilter(Color.BLACK, PorterDuff.Mode.SRC_ATOP); //sets downvote color to black
    }
}
