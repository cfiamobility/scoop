package ca.gc.inspection.scoop.profilecomment;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import ca.gc.inspection.scoop.*;

/**
 * ViewHolder for replying to a post action; it is the most generic View Holder
 * and contains the minimum views (no comment count, options menus, or images)
 * related to "posting" actions. Parent View Holder for ProfilePostViewHolder.
 */

public class ProfileCommentViewHolder extends RecyclerView.ViewHolder
        implements ProfileCommentContract.View.ViewHolder {

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
        if(likeCount.equals("null"))
            likeCount = "0";
        this.likeCount.setText(likeCount);
        return this;
    }

    /**
     *
     * @param date
     */
    @Override
    public ProfileCommentContract.View.ViewHolder setDate(String date) {
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"); //formats the date accordingly
            Date parsedDate = dateFormat.parse(date); //parses the created date to be in specified date format
            DateFormat properDateFormat = new SimpleDateFormat("MM-dd-yy"); //formats the date to be how we want it to output
            this.date.setText(properDateFormat.format(parsedDate));
            return this;
        } catch(Exception e){
            e.printStackTrace();
            return hideDate();
        }
    }

    @Override
    public ProfileCommentContract.View.ViewHolder setLikeState(LikeState likeState) {
        Log.i("likestate: ", likeState.toString());

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

    @Override
    public ProfileCommentContract.View.ViewHolder hideDate() {
        date.setVisibility(View.GONE);
        return this;
    }

    /**
     * Description: changes image from a string to a bitmap, then setting image
     * @param image: image to convert
     */
    @Override
    public ProfileCommentContract.View.ViewHolder setUserImageFromString(String image){
        if (image != null && !image.isEmpty()) {
            Bitmap bitmap = MyCamera.stringToBitmap(image); //converts image string to bitmap
            Log.i("image", image);
            profileImage.setImageBitmap(bitmap);
        }
        return this;
    }
}
