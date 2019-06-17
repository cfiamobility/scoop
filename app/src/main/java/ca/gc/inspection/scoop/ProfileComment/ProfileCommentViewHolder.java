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

public class ProfileCommentViewHolder extends RecyclerView.ViewHolder implements ProfileCommentContract.View.ViewHolder {

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
    public ProfileCommentContract.View.ViewHolder setPostTitle(String postTitle) {
        this.postTitle.setText(postTitle);
        return this;
    }

    /**
     *
     * @param postText
     */
    @Override
    public ProfileCommentContract.View.ViewHolder setPostText(String postText) {
        this.postText.setText(postText);
        return this;
    }

    /**
     *
     * @param image
     */
    @Override
    public ProfileCommentContract.View.ViewHolder setUserImage(Bitmap image) {
        Log.i("image", image.toString());
        profileImage.setImageBitmap(image);
        return this;
    }

    /**
     *
     * @param userName
     */
    @Override
    public ProfileCommentContract.View.ViewHolder setUserName(String userName) {
        username.setText(userName);
        return this;
    }

    /**
     *
     * @param likeCount
     */
    @Override
    public ProfileCommentContract.View.ViewHolder setLikeCount(String likeCount) {
        this.likeCount.setText(likeCount);
        return this;
    }

    /**
     *
     * @param date
     */
    @Override
    public ProfileCommentContract.View.ViewHolder setDate(String date) {
        this.date.setText(date);
        return this;
    }

    @Override
    public ProfileCommentContract.View.ViewHolder setLikeState(LikeState likeState) {
        switch (likeState) {
            case UPVOTE:
                setLikeUpvoteState();
                break;
            case NEUTRAL:
                setLikeNeutralState();
                break;
            case DOWNVOTE:
                setLikeDownvoteState();
        }
        return this;
    }

    private void setLikeDownvoteState() {
        upvote.setColorFilter(Color.BLACK, PorterDuff.Mode.SRC_ATOP); //sets upvote color to black
        downvote.setColorFilter(Color.BLUE, PorterDuff.Mode.SRC_ATOP); //sets downvote color to blue
    }

    private void setLikeNeutralState() {
        upvote.setColorFilter(Color.BLACK, PorterDuff.Mode.SRC_ATOP); //sets upvote color to black
        downvote.setColorFilter(Color.BLACK, PorterDuff.Mode.SRC_ATOP); //sets downvote color to black
    }

    private void setLikeUpvoteState() {
        upvote.setColorFilter(Color.RED, PorterDuff.Mode.SRC_ATOP); //sets upvote color to red
        downvote.setColorFilter(Color.BLACK, PorterDuff.Mode.SRC_ATOP); //sets downvote color to black
    }
}
