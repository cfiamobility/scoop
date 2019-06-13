package ca.gc.inspection.scoop.ProfilePost;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.json.JSONArray;


import ca.gc.inspection.scoop.Config;
import ca.gc.inspection.scoop.R;

public class ProfilePostAdapter extends RecyclerView.Adapter<ProfilePostViewHolder>{
	private JSONArray posts, images;
	private ProfilePostPresenter mProfilePostPresenter;
    private ProfilePostContract.View mProfilePostView;

	public ProfilePostAdapter(JSONArray posts, JSONArray images) {
		this.posts = posts;
		this.images = images;
	}

	@NonNull
	@Override
	public ProfilePostViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
		View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.custom_profile_layout, viewGroup, false);
		return new ProfilePostViewHolder(v);
	}

	@Override
	public void onBindViewHolder(@NonNull ProfilePostViewHolder profilePostViewHolder, int i) {
		mProfilePostPresenter = new ProfilePostPresenter(mProfilePostView, posts, images, i, profilePostViewHolder);
//        mProfilePostPresenter.getUserPosts(Config.currentUser);
		try {
			mProfilePostPresenter.displayPost();
			mProfilePostPresenter.displayImages();
			mProfilePostPresenter.formPostTitle();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public int getItemCount() {
		return posts.length();
	}


    public void setView (ProfilePostContract.View profilePostView){
        mProfilePostView = profilePostView;
    }
//
//
//	@Override
//	public void setPostText(String postText, ProfileCommentViewHolder holder) {
//		holder.postText.setText(postText);
//	}
//
//	@Override
//	public void setPostTitle(String postTitle, ProfileCommentViewHolder holder) {
//		holder.postTitle.setText(postTitle);
//	}
//
//	@Override
//	public void setUserImage(Bitmap image, ProfileCommentViewHolder holder) {
//		Log.i("image", image.toString());
//		holder.profileImage.setImageBitmap(image);
//	}
//
//	@Override
//	public void setUserName(String userName, ProfileCommentViewHolder holder) {
//		holder.username.setText(userName);
//	}
//
//	@Override
//	public void setLikeCount(String likeCount, ProfileCommentViewHolder holder) {
//		holder.likeCount.setText(likeCount);
//	}
//
//	@Override
//	public void setDate(String date, ProfileCommentViewHolder holder) {
//		holder.date.setText(date);
//	}
//
//	@Override
//	public void setLikeDownvoteState(ProfileCommentViewHolder holder) {
//		holder.upvote.setColorFilter(Color.BLACK, PorterDuff.Mode.SRC_ATOP); //sets upvote color to black
//		holder.downvote.setColorFilter(Color.BLUE, PorterDuff.Mode.SRC_ATOP); //sets downvote color to blue
//	}
//
//	@Override
//	public void setLikeNeutralState(ProfileCommentViewHolder holder) {
//		holder.upvote.setColorFilter(Color.BLACK, PorterDuff.Mode.SRC_ATOP); //sets upvote color to black
//		holder.downvote.setColorFilter(Color.BLACK, PorterDuff.Mode.SRC_ATOP); //sets downvote color to black
//	}
//
//	public void setLikeUpvoteState(ProfilePostViewHolder holder) {
//		holder.upvote.setColorFilter(Color.RED, PorterDuff.Mode.SRC_ATOP); //sets upvote color to red
//		holder.downvote.setColorFilter(Color.BLACK, PorterDuff.Mode.SRC_ATOP); //sets downvote color to black
//	}
//
//	public void setCommentCount(String commentCount, ProfilePostViewHolder holder) {
//		holder.commentCount.setText(commentCount);
//	}
//
//	@Override
//	public void hideDate(ProfileCommentViewHolder holder) {
//		holder.date.setVisibility(View.GONE);
//	}

}
