package ca.gc.inspection.scoop.ReplyPost;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.json.JSONArray;

import ca.gc.inspection.scoop.ProfilePost.ProfilePostContract;
import ca.gc.inspection.scoop.R;

public class ReplyPostAdapter extends RecyclerView.Adapter<ReplyPostViewHolder> {

    private JSONArray comments, images;
    private ReplyPostPresenter mReplyPostPresenter;
    private ReplyPostContract.View mReplyPostView;
    private String mUserId;

    /**
     * Constructor for the adapter
     * @param comments: JSONArray of comments
     * @param images: JSONArray of profile images
     */
    public ReplyPostAdapter(JSONArray comments, JSONArray images, String userId) {
        this.comments = comments;
        this.images = images;
        mUserId = userId;
    }

    /**
     * Creates the viewholder that contains all the parts of the adapter
     * @param viewGroup: ??
     * @param i: item iterator for each row of the recycler view
     * @returns a new view holder
     */
    @NonNull
    @Override
    public ReplyPostViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_profile_comments, viewGroup, false);
        return new ReplyPostViewHolder(v);
    }

    /**
     * Runs after the viewholder is created
     * @param replyPostViewHolder:  newly created viewholder
     * @param i: iterator for each row
     */
    @Override
    public void onBindViewHolder(@NonNull ReplyPostViewHolder replyPostViewHolder, int i) {
        mReplyPostPresenter = new ReplyPostPresenter(mReplyPostView, comments, images, i, replyPostViewHolder);
        mReplyPostPresenter.getUserComments(mUserId);
        try {
            mReplyPostPresenter.displayPost();
            mReplyPostPresenter.displayImages();
            mReplyPostPresenter.formPostTitle();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Gets the item Count of the comments JSONArray
     * @return the length
     */
    public int getItemCount() {
        return comments.length();
    }



    public void setView (ReplyPostContract.View profilePostView){
        mReplyPostView = profilePostView;
    }


//    /**
//     * Sets the post title ("Replying to ..." )
//     * @param postTitle: post title
//     * @param holder: the view holder created
//     */
//    public void setPostTitle(String postTitle, ReplyPostViewHolder holder) {
//        holder.postTitle.setText(postTitle);
//    }
//
//    /**
//     *
//     * @param postText
//     * @param holder
//     */
//    public void setPostText(String postText, ReplyPostViewHolder holder) {
//        holder.postText.setText(postText);
//    }
//
//    /**
//     *
//     * @param image
//     * @param holder
//     */
//    public void setUserImage(Bitmap image, ReplyPostViewHolder holder) {
//        Log.i("image", image.toString());
//        holder.profileImage.setImageBitmap(image);
//    }
//
//    /**
//     *
//     * @param userName
//     * @param holder
//     */
//    public void setUserName(String userName, ReplyPostViewHolder holder) {
//        holder.username.setText(userName);
//    }
//
//    /**
//     *
//     * @param likeCount
//     * @param holder
//     */
//    public void setLikeCount(String likeCount, ReplyPostViewHolder holder) {
//        holder.likeCount.setText(likeCount);
//    }
//
//    /**
//     *
//     * @param date
//     * @param holder
//     */
//    public void setDate(String date, ReplyPostViewHolder holder) {
//        holder.date.setText(date);
//    }
//
//    /**
//     *
//     * @param holder
//     */
//    public void setLikeDownvoteState(ReplyPostViewHolder holder) {
//        holder.upvote.setColorFilter(Color.BLACK, PorterDuff.Mode.SRC_ATOP); //sets upvote color to black
//        holder.downvote.setColorFilter(Color.BLUE, PorterDuff.Mode.SRC_ATOP); //sets downvote color to blue
//    }
//
//    /**
//     *
//     * @param holder
//     */
//    public void setLikeNeutralState(ReplyPostViewHolder holder) {
//        holder.upvote.setColorFilter(Color.BLACK, PorterDuff.Mode.SRC_ATOP); //sets upvote color to black
//        holder.downvote.setColorFilter(Color.BLACK, PorterDuff.Mode.SRC_ATOP); //sets downvote color to black
//    }
//
//    /**
//     *
//     * @param holder
//     */
//    public void setLikeUpvoteState(ReplyPostViewHolder holder) {
//        holder.upvote.setColorFilter(Color.RED, PorterDuff.Mode.SRC_ATOP); //sets upvote color to red
//        holder.downvote.setColorFilter(Color.BLACK, PorterDuff.Mode.SRC_ATOP); //sets downvote color to black
//    }
//
//    /**
//     *
//     * @param holder
//     */
//    public void hideDate(ReplyPostViewHolder holder) {
//        holder.date.setVisibility(View.GONE);
//    }

}
