package ca.gc.inspection.scoop.feedpost;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.json.JSONArray;
import org.json.JSONException;

import ca.gc.inspection.scoop.R;

public class FeedPostAdapter extends RecyclerView.Adapter<FeedPostViewHolder>  {
    private JSONArray posts, images;
    private FeedPostPresenter mFeedPostPresenter;
    private FeedPostContract.View mFeedPostView;


    public FeedPostAdapter(JSONArray posts, JSONArray images) {
        this.posts = posts;
        this.images = images;
    }

    @NonNull
    @Override
    public FeedPostViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_post, viewGroup, false);
        FeedPostViewHolder vh = new FeedPostViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull FeedPostViewHolder holder, int i) {

        mFeedPostPresenter = new FeedPostPresenter(mFeedPostView, posts, images, i, holder);
//        mFeedPostPresenter.getPosts();
        try {
            mFeedPostPresenter.displayPost();
            mFeedPostPresenter.displayImages();
            mFeedPostPresenter.formPostTitle();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return posts.length();
    }


    public void setView (FeedPostContract.View feedPostView){
        mFeedPostView = feedPostView;
    }
//    @Override
//    public void setPostText(String postText, ProfileCommentViewHolder holder) {
//        holder.postText.setText(postText);
//    }
//
//    /**
//     * Description: sets title of post
//     * @param postTitle: title of post
//     * @param holder: viewholder of item
//     */
//    @Override
//    public void setPostTitle(String postTitle, ProfileCommentViewHolder holder) {
//        holder.postTitle.setText(postTitle);
//    }
//
//    /**
//     * Description: sets image of post
//     * @param image: image of post
//     * @param holder: viewholder of item
//     */
//    @Override
//    public void setPostImage(Bitmap image, FeedPostViewHolder holder) {
//        holder.postImage.setImageBitmap(image);
//    }
//
//    /**
//     * Description: sets image of user
//     * @param image: image of user
//     * @param holder: viewholder of item
//     */
//    @Override
//    public void setUserImage(Bitmap image, ProfileCommentViewHolder holder) {
//        holder.profileImage.setImageBitmap(image);
//    }
//
//    /**
//     * Description: sets name of user
//     * @param userName: name of user
//     * @param holder: viewholder of item
//     */
//    @Override
//    public void setUserName(String userName, ProfileCommentViewHolder holder) {
//        holder.username.setText(userName);
//    }
//
//    /**
//     * Description: sets like count on post
//     * @param likeCount: like count on post
//     * @param holder: viewholder of item
//     */
//    @Override
//    public void setLikeCount(String likeCount, ProfileCommentViewHolder holder) {
//        holder.likeCount.setText(likeCount);
//    }
//
//    /**
//     * Description: sets date of post
//     * @param date: date of post
//     * @param holder: viewholder of item
//     */
//    @Override
//    public void setDate(String date, ProfileCommentViewHolder holder) {
//        holder.date.setText(date);
//    }
//
//    /**
//     * Description: sets neutral state of likes
//     * @param holder: viewholder of item
//     */
//    @Override
//    public void setLikeNeutralState(ProfileCommentViewHolder holder) {
//        holder.upvote.setColorFilter(Color.BLACK, PorterDuff.Mode.SRC_ATOP); //sets upvote color to black
//        holder.downvote.setColorFilter(Color.BLACK, PorterDuff.Mode.SRC_ATOP); //sets downvote color to black
//    }
//
//    /**
//     * Description: sets upvote state of likes
//     * @param holder: viewholder of item
//     */
//    @Override
//    public void setLikeUpvoteState(ProfileCommentViewHolder holder) {
//       holder.upvote.setColorFilter(Color.RED, PorterDuff.Mode.SRC_ATOP); //sets upvote color to red
//       holder.downvote.setColorFilter(Color.BLACK, PorterDuff.Mode.SRC_ATOP); //sets downvote color to black
//    }
//
//    /**
//     * Description: sets downvote state of likes
//     * @param holder: viewholder of item
//     */
//    @Override
//    public void setLikeDownvoteState(ProfileCommentViewHolder holder) {
//        holder.upvote.setColorFilter(Color.BLACK, PorterDuff.Mode.SRC_ATOP); //sets upvote color to black
//        holder.downvote.setColorFilter(Color.BLUE, PorterDuff.Mode.SRC_ATOP); //sets downvote color to blue
//    }
//
//    /**
//     * Description: sets comment count of post
//     * @param commentCount: comment count of post
//     * @param holder: viewholder of item
//     */
//    @Override
//    public void setCommentCount(String commentCount, ProfilePostViewHolder holder) {
//        holder.commentCount.setText(commentCount);
//    }
//
//    /**
//     * Description: hides post image if there is none
//     * @param holder: viewholder of item
//     */
//    @Override
//    public void hidePostImage(FeedPostViewHolder holder) {
//        holder.postImage.setVisibility(View.GONE);
//    }
//
//    /**
//     * Description: hides date if there is an error
//     * @param holder: viewholder of item
//     */
//    @Override
//    public void hideDate(ProfileCommentViewHolder holder) {
//        holder.date.setVisibility(View.GONE);
//    }
}
