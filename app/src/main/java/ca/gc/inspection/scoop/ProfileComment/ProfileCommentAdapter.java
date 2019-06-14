package ca.gc.inspection.scoop.ProfileComment;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.json.JSONArray;

import ca.gc.inspection.scoop.Config;
import ca.gc.inspection.scoop.MySingleton;
import ca.gc.inspection.scoop.*;

public class ProfileCommentAdapter extends RecyclerView.Adapter<ProfileCommentViewHolder> {

    private JSONArray comments, images;
    private ProfileCommentContract.Presenter mProfileCommentPresenter;
    private ProfileCommentContract.View mProfileCommentView;

    /**
     * Constructor for the adapter
     * @param comments: JSONArray of comments
     * @param images: JSONArray of profile images
     */
    public ProfileCommentAdapter(ProfileCommentContract.Presenter presenter, JSONArray comments, JSONArray images) {
        mProfileCommentPresenter = presenter;
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
    public ProfileCommentViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_profile_comments, viewGroup, false);
        return new ProfileCommentViewHolder(v);
    }

    /**
     * Runs after the viewholder is created
     * @param profileCommentViewHolder:  newly created viewholder
     * @param i: iterator for each row
     */
    @Override
    public void onBindViewHolder(@NonNull ProfileCommentViewHolder profileCommentViewHolder, int i) {
        mProfileCommentPresenter = new ProfileCommentPresenter(mProfileCommentView, comments, images, i, profileCommentViewHolder);
//        mProfileCommentPresenter.getUserComments(Config.currentUser);
        try {
            mProfileCommentPresenter.displayPost();
            mProfileCommentPresenter.displayImages();
            mProfileCommentPresenter.formPostTitle();
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



    public void setView (ProfileCommentContract.View profileCommentView){
        mProfileCommentView = profileCommentView;
    }


//    /**
//     * Sets the post title ("Replying to ..." )
//     * @param postTitle: post title
//     * @param holder: the view holder created
//     */
//    public void setPostTitle(String postTitle, ProfileCommentViewHolder holder) {
//        holder.postTitle.setText(postTitle);
//    }
//
//    /**
//     *
//     * @param postText
//     * @param holder
//     */
//    public void setPostText(String postText, ProfileCommentViewHolder holder) {
//        holder.postText.setText(postText);
//    }
//
//    /**
//     *
//     * @param image
//     * @param holder
//     */
//    public void setUserImage(Bitmap image, ProfileCommentViewHolder holder) {
//        Log.i("image", image.toString());
//        holder.profileImage.setImageBitmap(image);
//    }
//
//    /**
//     *
//     * @param userName
//     * @param holder
//     */
//    public void setUserName(String userName, ProfileCommentViewHolder holder) {
//        holder.username.setText(userName);
//    }
//
//    /**
//     *
//     * @param likeCount
//     * @param holder
//     */
//    public void setLikeCount(String likeCount, ProfileCommentViewHolder holder) {
//        holder.likeCount.setText(likeCount);
//    }
//
//    /**
//     *
//     * @param date
//     * @param holder
//     */
//    public void setDate(String date, ProfileCommentViewHolder holder) {
//        holder.date.setText(date);
//    }
//
//    /**
//     *
//     * @param holder
//     */
//    public void setLikeDownvoteState(ProfileCommentViewHolder holder) {
//        holder.upvote.setColorFilter(Color.BLACK, PorterDuff.Mode.SRC_ATOP); //sets upvote color to black
//        holder.downvote.setColorFilter(Color.BLUE, PorterDuff.Mode.SRC_ATOP); //sets downvote color to blue
//    }
//
//    /**
//     *
//     * @param holder
//     */
//    public void setLikeNeutralState(ProfileCommentViewHolder holder) {
//        holder.upvote.setColorFilter(Color.BLACK, PorterDuff.Mode.SRC_ATOP); //sets upvote color to black
//        holder.downvote.setColorFilter(Color.BLACK, PorterDuff.Mode.SRC_ATOP); //sets downvote color to black
//    }
//
//    /**
//     *
//     * @param holder
//     */
//    public void setLikeUpvoteState(ProfileCommentViewHolder holder) {
//        holder.upvote.setColorFilter(Color.RED, PorterDuff.Mode.SRC_ATOP); //sets upvote color to red
//        holder.downvote.setColorFilter(Color.BLACK, PorterDuff.Mode.SRC_ATOP); //sets downvote color to black
//    }
//
//    /**
//     *
//     * @param holder
//     */
//    public void hideDate(ProfileCommentViewHolder holder) {
//        holder.date.setVisibility(View.GONE);
//    }

}
