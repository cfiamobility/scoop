package ca.gc.inspection.scoop.ProfilePost;

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

import ca.gc.inspection.scoop.ProfilePost.ProfilePostsFeedController;
import ca.gc.inspection.scoop.ProfilePost.ProfilePostsFeedPostViewHolder;
import ca.gc.inspection.scoop.PostReply.ReplyPostViewHolder;
import ca.gc.inspection.scoop.R;

public class ProfilePostsAdapter extends RecyclerView.Adapter<ProfilePostsFeedPostViewHolder> implements ProfilePostsFeedController.ProfileFeedInterface {
	private JSONArray posts, images;

	public ProfilePostsAdapter(JSONArray posts, JSONArray images) {
		this.posts = posts;
		this.images = images;
	}

	@NonNull
	@Override
	public ProfilePostsFeedPostViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
		View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.custom_profile_layout, viewGroup, false);
		return new ProfilePostsFeedPostViewHolder(v);
	}

	@Override
	public void onBindViewHolder(@NonNull ProfilePostsFeedPostViewHolder profilePostsFeedViewHolder, int i) {
		ProfilePostsFeedController controller = new ProfilePostsFeedController(this, posts, images, i, profilePostsFeedViewHolder);
		try {
			controller.displayPost();
			controller.displayImages();
			controller.formPostTitle();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public int getItemCount() {
		return posts.length();
	}

	@Override
	public void setPostText(String postText, ReplyPostViewHolder holder) {
		holder.postText.setText(postText);
	}

	@Override
	public void setPostTitle(String postTitle, ReplyPostViewHolder holder) {
		holder.postTitle.setText(postTitle);
	}

	@Override
	public void setUserImage(Bitmap image, ReplyPostViewHolder holder) {
		Log.i("image", image.toString());
		holder.profileImage.setImageBitmap(image);
	}

	@Override
	public void setUserName(String userName, ReplyPostViewHolder holder) {
		holder.username.setText(userName);
	}

	@Override
	public void setLikeCount(String likeCount, ReplyPostViewHolder holder) {
		holder.likeCount.setText(likeCount);
	}

	@Override
	public void setDate(String date, ReplyPostViewHolder holder) {
		holder.date.setText(date);
	}

	@Override
	public void setLikeDownvoteState(ReplyPostViewHolder holder) {
		holder.upvote.setColorFilter(Color.BLACK, PorterDuff.Mode.SRC_ATOP); //sets upvote color to black
		holder.downvote.setColorFilter(Color.BLUE, PorterDuff.Mode.SRC_ATOP); //sets downvote color to blue
	}

	@Override
	public void setLikeNeutralState(ReplyPostViewHolder holder) {
		holder.upvote.setColorFilter(Color.BLACK, PorterDuff.Mode.SRC_ATOP); //sets upvote color to black
		holder.downvote.setColorFilter(Color.BLACK, PorterDuff.Mode.SRC_ATOP); //sets downvote color to black
	}

	@Override
	public void setLikeUpvoteState(ReplyPostViewHolder holder) {
		holder.upvote.setColorFilter(Color.RED, PorterDuff.Mode.SRC_ATOP); //sets upvote color to red
		holder.downvote.setColorFilter(Color.BLACK, PorterDuff.Mode.SRC_ATOP); //sets downvote color to black
	}

	@Override
	public void setCommentCount(String commentCount, ProfilePostsFeedPostViewHolder holder) {
		holder.commentCount.setText(commentCount);
	}

	@Override
	public void hideDate(ReplyPostViewHolder holder) {
		holder.date.setVisibility(View.GONE);
	}

}
