package ca.gc.inspection.scoop;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.json.JSONArray;

public class ProfilePostsAdapter extends RecyclerView.Adapter<ProfileFeedViewHolder> implements ProfileFeedController.ProfileFeedInterface {
	private JSONArray posts, images;

	public ProfilePostsAdapter(JSONArray posts, JSONArray images) {
		this.posts = posts;
		this.images = images;
	}

	@NonNull
	@Override
	public ProfileFeedViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
		View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.custom_row_feed, viewGroup, false);
		return new ProfileFeedViewHolder(v);
	}

	@Override
	public void onBindViewHolder(@NonNull ProfileFeedViewHolder profileFeedViewHolder, int i) {
		ProfileFeedController controller = new ProfileFeedController(this, posts, i, profileFeedViewHolder);
	}

	@Override
	public int getItemCount() {
		return posts.length();
	}

	@Override
	public void setPostText(String postText, MostGenericViewHolder holder) {

	}

	@Override
	public void setPostTitle(String postTitle, MostGenericViewHolder holder) {

	}

	@Override
	public void setUserName(String userName, MostGenericViewHolder holder) {

	}

	@Override
	public void setLikeCount(String likeCount, MostGenericViewHolder holder) {

	}

	@Override
	public void setDate(String date, MostGenericViewHolder holder) {

	}

	@Override
	public void setLikeDownvoteState(MostGenericViewHolder holder) {

	}

	@Override
	public void setLikeNeutralState(MostGenericViewHolder holder) {

	}

	@Override
	public void setLikeUpvoteState(MostGenericViewHolder holder) {

	}

	@Override
	public void setCommentCount(String commentCount, ProfileFeedViewHolder holder) {

	}

	@Override
	public void hideDate(MostGenericViewHolder holder) {

	}

}
