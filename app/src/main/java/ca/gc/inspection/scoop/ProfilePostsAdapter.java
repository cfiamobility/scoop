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

import org.json.JSONArray;

public class ProfilePostsAdapter extends RecyclerView.Adapter<ProfilePostsFeedViewHolder> implements ProfilePostsFeedController.ProfileFeedInterface {
	private JSONArray posts, images;

	public ProfilePostsAdapter(JSONArray posts, JSONArray images) {
		this.posts = posts;
		this.images = images;
	}

	@NonNull
	@Override
	public ProfilePostsFeedViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
		View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.custom_profile_layout, viewGroup, false);
		return new ProfilePostsFeedViewHolder(v);
	}

	@Override
	public void onBindViewHolder(@NonNull ProfilePostsFeedViewHolder profilePostsFeedViewHolder, int i) {
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
	public void setPostText(String postText, MostGenericViewHolder holder) {
		holder.postText.setText(postText);
	}

	@Override
	public void setPostTitle(String postTitle, MostGenericViewHolder holder) {
		holder.postTitle.setText(postTitle);
	}

	@Override
	public void setUserImage(Bitmap image, MostGenericViewHolder holder) {
		Log.i("image", image.toString());
		holder.profileImage.setImageBitmap(image);
	}

	@Override
	public void setUserName(String userName, MostGenericViewHolder holder) {
		holder.username.setText(userName);
	}

	@Override
	public void setLikeCount(String likeCount, MostGenericViewHolder holder) {
		holder.likeCount.setText(likeCount);
	}

	@Override
	public void setDate(String date, MostGenericViewHolder holder) {
		holder.date.setText(date);
	}

	@Override
	public void setLikeDownvoteState(MostGenericViewHolder holder) {
		holder.upvote.setColorFilter(Color.BLACK, PorterDuff.Mode.SRC_ATOP); //sets upvote color to black
		holder.downvote.setColorFilter(Color.BLUE, PorterDuff.Mode.SRC_ATOP); //sets downvote color to blue
	}

	@Override
	public void setLikeNeutralState(MostGenericViewHolder holder) {
		holder.upvote.setColorFilter(Color.BLACK, PorterDuff.Mode.SRC_ATOP); //sets upvote color to black
		holder.downvote.setColorFilter(Color.BLACK, PorterDuff.Mode.SRC_ATOP); //sets downvote color to black
	}

	@Override
	public void setLikeUpvoteState(MostGenericViewHolder holder) {
		holder.upvote.setColorFilter(Color.RED, PorterDuff.Mode.SRC_ATOP); //sets upvote color to red
		holder.downvote.setColorFilter(Color.BLACK, PorterDuff.Mode.SRC_ATOP); //sets downvote color to black
	}

	@Override
	public void setCommentCount(String commentCount, ProfilePostsFeedViewHolder holder) {
		holder.commentCount.setText(commentCount);
	}

	@Override
	public void hideDate(MostGenericViewHolder holder) {
		holder.date.setVisibility(View.GONE);
	}

}
