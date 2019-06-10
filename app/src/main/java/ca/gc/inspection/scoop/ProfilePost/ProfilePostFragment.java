package ca.gc.inspection.scoop.ProfilePost;


import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.json.JSONArray;

import ca.gc.inspection.scoop.R;
import ca.gc.inspection.scoop.ReplyPost.ReplyPostContract;
import ca.gc.inspection.scoop.ReplyPost.ReplyPostPresenter;
import ca.gc.inspection.scoop.ReplyPost.ReplyPostViewHolder;


/**
 * A simple {@link Fragment} subclass.
 */
public class ProfilePostsFragment extends Fragment implements ProfilePostContract.View {

    // recycler view widgets
    private RecyclerView postRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private String userid;
    private View view;
    private ReplyPostContract.Presenter mProfilePostPresenter;

    public void setPresenter (ReplyPostContract.Presenter presenter){
        mProfilePostPresenter = presenter;
    }

    public String getUserId() {
        return userid;
    }

    /**
     *
     * @param inflater: inflates the layout
     * @param container: contains the layout
     * @param savedInstanceState: ??
     * @return - returns the view created
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_profile_posts, container, false);
        Bundle bundle = getArguments();
        userid = bundle.getString("userid");
        return view;
    }

    /**
     * See - Fragment Lifecycle
     * @param view: the view
     * @param savedInstanceState: ??
     */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
//        ProfilePostContract.Presenter mProfilePostPresenter = new ProfilePostPresenter(this);
//        mProfilePostPresenter.getUserPosts(userid);
    }

    /**
     * Sets the recycler view with the adapter created
     * @param posts: JSONArray of posts from db
     * @param images: JSONArray of profile pictures from db
     */

    public void setPostRecyclerView(JSONArray posts, JSONArray images) {
        // initializing the recycler view
        postRecyclerView = view.findViewById(R.id.fragment_profile_posts_rv);
        postRecyclerView.setHasFixedSize(true);

        // setting the layout manager for the recycler view
        mLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        postRecyclerView.setLayoutManager(mLayoutManager);

        // setting the custom adapter for the recycler view
        mAdapter = new ProfilePostAdapter(posts, images, userid);
        ((ProfilePostAdapter) mAdapter).setView(this);
        postRecyclerView.setAdapter(mAdapter);
    }


    /**
     * FROM ADAPTER
     */

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
    public void setCommentCount(String commentCount, ProfilePostViewHolder holder) {
        holder.commentCount.setText(commentCount);
    }

    @Override
    public void hideDate(ReplyPostViewHolder holder) {
        holder.date.setVisibility(View.GONE);
    }

}