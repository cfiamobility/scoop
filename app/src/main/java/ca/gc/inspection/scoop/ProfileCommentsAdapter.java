package ca.gc.inspection.scoop;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.json.JSONArray;

import java.util.List;

public class ProfileCommentsAdapter extends RecyclerView.Adapter<MostGenericViewHolder> implements MostGenericController.MostGenericInterface {

    private JSONArray comments, images;

    /**
     * Constructor for the adapter
     * @param comments: JSONArray of comments
     * @param images: JSONArray of profile images
     */
    public ProfileCommentsAdapter(JSONArray comments, JSONArray images) {
        this.comments = comments;
        this.images = images;
    }

    /**
     * Creates the viewholder that contains all the parts of the adapter
     * @param viewGroup: ??
     * @param i: item iterator for each row of the recycler view
     * @returns a new view holder
     */
    @NonNull
    @Override
    public MostGenericViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.custom_row_comments, viewGroup, false);
        return new MostGenericViewHolder(v);
    }

    /**
     * Runs after the viewholder is created
     * @param mostGenericViewHolder:  newly created viewholder
     * @param i: iterator for each row
     */
    @Override
    public void onBindViewHolder(@NonNull MostGenericViewHolder mostGenericViewHolder, int i) {
        MostGenericController controller = new MostGenericController(this, comments, images, i, mostGenericViewHolder);
        try {
            controller.displayPost();
            controller.displayImages();
            controller.formPostTitle();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Gets the item Count of the comments JSONArray
     * @return the length
     */
    @Override
    public int getItemCount() {
        return comments.length();
    }

    /**
     * Sets the post title ("Replying to ..." )
     * @param postTitle: post title
     * @param holder: the view holder created
     */
    @Override
    public void setPostTitle(String postTitle, MostGenericViewHolder holder) {
        holder.postTitle.setText(postTitle);
    }

    /**
     *
     * @param postText
     * @param holder
     */
    @Override
    public void setPostText(String postText, MostGenericViewHolder holder) {
        holder.postText.setText(postText);
    }

    /**
     *
     * @param image
     * @param holder
     */
    @Override
    public void setUserImage(Bitmap image, MostGenericViewHolder holder) {
        Log.i("image", image.toString());
        holder.profileImage.setImageBitmap(image);
    }

    /**
     *
     * @param userName
     * @param holder
     */
    @Override
    public void setUserName(String userName, MostGenericViewHolder holder) {
        holder.username.setText(userName);
    }

    /**
     *
     * @param likeCount
     * @param holder
     */
    @Override
    public void setLikeCount(String likeCount, MostGenericViewHolder holder) {
        holder.likeCount.setText(likeCount);
    }

    /**
     *
     * @param date
     * @param holder
     */
    @Override
    public void setDate(String date, MostGenericViewHolder holder) {
        holder.date.setText(date);
    }

    /**
     *
     * @param holder
     */
    @Override
    public void setLikeDownvoteState(MostGenericViewHolder holder) {
        holder.upvote.setColorFilter(Color.BLACK, PorterDuff.Mode.SRC_ATOP); //sets upvote color to black
        holder.downvote.setColorFilter(Color.BLUE, PorterDuff.Mode.SRC_ATOP); //sets downvote color to blue
    }

    /**
     *
     * @param holder
     */
    @Override
    public void setLikeNeutralState(MostGenericViewHolder holder) {
        holder.upvote.setColorFilter(Color.BLACK, PorterDuff.Mode.SRC_ATOP); //sets upvote color to black
        holder.downvote.setColorFilter(Color.BLACK, PorterDuff.Mode.SRC_ATOP); //sets downvote color to black
    }

    /**
     *
     * @param holder
     */
    @Override
    public void setLikeUpvoteState(MostGenericViewHolder holder) {
        holder.upvote.setColorFilter(Color.RED, PorterDuff.Mode.SRC_ATOP); //sets upvote color to red
        holder.downvote.setColorFilter(Color.BLACK, PorterDuff.Mode.SRC_ATOP); //sets downvote color to black
    }

    /**
     *
     * @param holder
     */
    @Override
    public void hideDate(MostGenericViewHolder holder) {
        holder.date.setVisibility(View.GONE);
    }

}
