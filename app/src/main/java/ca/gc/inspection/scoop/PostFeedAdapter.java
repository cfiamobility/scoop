package ca.gc.inspection.scoop;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.json.JSONArray;
import org.json.JSONException;

public class PostFeedAdapter extends RecyclerView.Adapter<FeedPostViewHolder> implements ImageController.ImageInterface{
    private JSONArray posts, images;

    public PostFeedAdapter(JSONArray posts, JSONArray images) {
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

       ImageController imageController = new ImageController(this, posts, images, i, holder);
        try {
            imageController.displayPost();
            imageController.displayImages();
            imageController.formPostTitle();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return posts.length();
    }

    /**
     * Description: sets text of Post
     * @param postText: text of Post
     * @param holder: viewholder of item
     */
    @Override
    public void setPostText(String postText, MostGenericViewHolder holder) {
        holder.postText.setText(postText);
    }

    /**
     * Description: sets title of Post
     * @param postTitle: title of Post
     * @param holder: viewholder of item
     */
    @Override
    public void setPostTitle(String postTitle, MostGenericViewHolder holder) {
        holder.postTitle.setText(postTitle);
    }

    /**
     * Description: sets image of Post
     * @param image: image of Post
     * @param holder: viewholder of item
     */
    @Override
    public void setPostImage(Bitmap image, FeedPostViewHolder holder) {
        holder.postImage.setImageBitmap(image);
    }

    /**
     * Description: sets image of user
     * @param image: image of user
     * @param holder: viewholder of item
     */
    @Override
    public void setUserImage(Bitmap image, MostGenericViewHolder  holder) {
        holder.profileImage.setImageBitmap(image);
    }

    /**
     * Description: sets name of user
     * @param userName: name of user
     * @param holder: viewholder of item
     */
    @Override
    public void setUserName(String userName, MostGenericViewHolder holder) {
        holder.username.setText(userName);
    }

    /**
     * Description: sets like count on Post
     * @param likeCount: like count on Post
     * @param holder: viewholder of item
     */
    @Override
    public void setLikeCount(String likeCount, MostGenericViewHolder holder) {
        holder.likeCount.setText(likeCount);
    }

    /**
     * Description: sets date of Post
     * @param date: date of Post
     * @param holder: viewholder of item
     */
    @Override
    public void setDate(String date, MostGenericViewHolder holder) {
        holder.date.setText(date);
    }

    /**
     * Description: sets neutral state of likes
     * @param holder: viewholder of item
     */
    @Override
    public void setLikeNeutralState(MostGenericViewHolder holder) {
        holder.upvote.setColorFilter(Color.BLACK, PorterDuff.Mode.SRC_ATOP); //sets upvote color to black
        holder.downvote.setColorFilter(Color.BLACK, PorterDuff.Mode.SRC_ATOP); //sets downvote color to black
    }

    /**
     * Description: sets upvote state of likes
     * @param holder: viewholder of item
     */
    @Override
    public void setLikeUpvoteState(MostGenericViewHolder holder) {
       holder.upvote.setColorFilter(Color.RED, PorterDuff.Mode.SRC_ATOP); //sets upvote color to red
       holder.downvote.setColorFilter(Color.BLACK, PorterDuff.Mode.SRC_ATOP); //sets downvote color to black
    }

    /**
     * Description: sets downvote state of likes
     * @param holder: viewholder of item
     */
    @Override
    public void setLikeDownvoteState(MostGenericViewHolder holder) {
        holder.upvote.setColorFilter(Color.BLACK, PorterDuff.Mode.SRC_ATOP); //sets upvote color to black
        holder.downvote.setColorFilter(Color.BLUE, PorterDuff.Mode.SRC_ATOP); //sets downvote color to blue
    }

    /**
     * Description: sets comment count of Post
     * @param commentCount: comment count of Post
     * @param holder: viewholder of item
     */
    @Override
    public void setCommentCount(String commentCount, ProfilePostsFeedViewHolder holder) {
        holder.commentCount.setText(commentCount);
    }

    /**
     * Description: hides Post image if there is none
     * @param holder: viewholder of item
     */
    @Override
    public void hidePostImage(FeedPostViewHolder holder) {
        holder.postImage.setVisibility(View.GONE);
    }

    /**
     * Description: hides date if there is an error
     * @param holder: viewholder of item
     */
    @Override
    public void hideDate(MostGenericViewHolder holder) {
        holder.date.setVisibility(View.GONE);
    }
}
