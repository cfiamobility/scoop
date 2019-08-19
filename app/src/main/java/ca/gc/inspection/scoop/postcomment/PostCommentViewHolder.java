package ca.gc.inspection.scoop.postcomment;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableStringBuilder;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import ca.gc.inspection.scoop.profilelikes.ProfileLikesContract;
import ca.gc.inspection.scoop.searchprofile.UserProfileListener;
import ca.gc.inspection.scoop.util.CameraUtils;
import ca.gc.inspection.scoop.R;
import ca.gc.inspection.scoop.util.TextFormat;

import static android.view.View.GONE;
import static ca.gc.inspection.scoop.searchprofile.view.SearchProfileViewHolder.getSpannableStringBuilderWithFormat;
import static java.lang.Boolean.TRUE;

/**
 * ViewHolder for replying to a post action; it is the most generic View Holder
 * and contains the minimum views (no comment count, options menus, or images)
 * related to "posting" actions. Parent View Holder for ProfilePostViewHolder.
 */

public class PostCommentViewHolder extends RecyclerView.ViewHolder implements
        PostCommentContract.View.ViewHolder,
        UserProfileListener {

    PostCommentContract.Presenter.ViewHolderAPI mPresenter;

    public TextView username, date, postText, likeCount;
    public ImageView profileImage, upvote, downvote;
    public ImageView optionsMenu;
    public ImageView saved, unsaved;
//    public Boolean savedStatus;

    public PostCommentViewHolder(View v, PostCommentContract.Presenter.ViewHolderAPI presenter) {
        super(v);
        username = v.findViewById(R.id.name);
        profileImage = v.findViewById(R.id.profile_image);
        date = v.findViewById(R.id.date);
        postText = v.findViewById(R.id.post_text);
        likeCount = v.findViewById(R.id.vote_count);
        upvote = v.findViewById(R.id.up_vote);
        downvote = v.findViewById(R.id.down_vote);
        optionsMenu = v.findViewById(R.id.options_menu);
        saved = v.findViewById(R.id.item_post_img_saved);
        unsaved = v.findViewById(R.id.item_post_img_unsaved);

        mPresenter = presenter;
    }

    /**
     *
     * @param postText
     */
    @Override
    public PostCommentContract.View.ViewHolder setPostText(String postText) {
        this.postText.setText(postText);
        return this;
    }

    /**
     *
     * @param postText
     */
    @Override
    public PostCommentContract.View.ViewHolder setPostTextWithFormat(String postText, TextFormat textFormat) {
        SpannableStringBuilder spannableStringBuilder = getSpannableStringBuilderWithFormat(postText, textFormat);
        this.postText.setText(spannableStringBuilder);
        return this;
    }

    /**
     *
     * @param userName
     */
    @Override
    public PostCommentContract.View.ViewHolder setUserName(String userName) {
        username.setText(userName);
        return this;
    }

    /**
     *
     * @param likeCount
     */
    @Override
    public PostCommentContract.View.ViewHolder setLikeCount(String likeCount) {
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
    public PostCommentContract.View.ViewHolder setDate(String date) {
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
    public PostCommentContract.View.ViewHolder setLikeState(LikeState likeState) {
        if (likeState == null)
            return this;

        Log.i("likestate: ", likeState.getDatabaseValue());

        switch (likeState) {
            case UPVOTE:
                setLikeUpvoteState();
                break;
            case NEUTRAL:
                setLikeNeutralState();
                break;
            case DOWNVOTE:
                setLikeDownvoteState();
                break;
            default:
                setLikeNeutralState();
                break;
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
    public PostCommentContract.View.ViewHolder hideDate() {
        date.setVisibility(GONE);
        return this;
    }

    /**
     * Description: changes image from a string to a bitmap, then setting image
     * @param image: image to convert
     */
    @Override
    public PostCommentContract.View.ViewHolder setUserImageFromString(String image){
        if (image != null && !image.isEmpty()) {
            Bitmap bitmap = CameraUtils.stringToBitmap(image); //converts image string to bitmap
            Log.i("image", image);
            profileImage.setImageBitmap(bitmap);
        }
        return this;
    }

    public PostCommentContract.View.ViewHolder setSavedState(Boolean savedState) {
        if(saved!=null && unsaved!=null){
            if(savedState){
                this.unsaved.setVisibility(GONE);
                this.saved.setVisibility(View.VISIBLE);
            } else {
                this.saved.setVisibility(GONE);
                this.unsaved.setVisibility(View.VISIBLE);
            }
        }
        return this;
    }

    public void changeUpvoteLikeState(int i) {
        try {
            mPresenter.changeUpvoteLikeState(this, i);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void changeDownvoteLikeState(int i) {
        try {
            mPresenter.changeDownvoteLikeState(this, i);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Invokes a method in the Presenter to update the given viewHolder's saved state
     * @param i the position of the viewHolder, used to identify the view
     */
    public void updateSavedState(int i){
        try {
            mPresenter.updateSavedState(this, i);
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public TextView getUserName() {
        return username;
    }

    @Override
    public ImageView getProfileImage() {
        return profileImage;
    }
}
