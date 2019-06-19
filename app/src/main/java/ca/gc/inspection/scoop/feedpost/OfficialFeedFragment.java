package ca.gc.inspection.scoop.feedpost;


import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.json.JSONArray;

import ca.gc.inspection.scoop.MySingleton;
import ca.gc.inspection.scoop.profilecomment.ProfileCommentViewHolder;
import ca.gc.inspection.scoop.profilepost.ProfilePostFragment;
import ca.gc.inspection.scoop.profilepost.ProfilePostViewHolder;
import ca.gc.inspection.scoop.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class OfficialFeedFragment extends ProfilePostFragment implements FeedPostContract.View {


    // recycler view widget
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private View view;
    private FeedPostContract.Presenter mFeedPostPresenter;
    private FeedPostInteractor mFeedPostInteractor;


    public void setPresenter (FeedPostContract.Presenter presenter){
        mFeedPostPresenter = presenter;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_official_feed, container, false);

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);

//        FeedController controller = new FeedController(this);
//        controller.getPosts();
//        mFeedPostProfileCommentInteractor = new ProfileCommentInteractor(this);
//        mFeedPostProfileCommentInteractor.getUserComments(Config.currentUser);
//        mFeedPostPresenter.getPosts(MySingleton.getInstance(getActivity()));
        mFeedPostInteractor = new FeedPostInteractor(this);
        mFeedPostInteractor.getFeedPosts(MySingleton.getInstance(getContext()));
    }


    @Override
    public void setRecyclerView(JSONArray posts, JSONArray images){

        // initializing the recycler view
        mRecyclerView = view.findViewById(R.id.fragment_official_feed_rv);
        mRecyclerView.setHasFixedSize(true);

        // setting up the layout manager for the recycler view
        mLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(mLayoutManager);

        // setting up the custom adapter for the recycler view
        mAdapter = new FeedPostAdapter(posts, images);
        ((FeedPostAdapter) mAdapter).setView(this);
        mRecyclerView.setAdapter(mAdapter);

//        mFeedPostPresenter.getPosts(MySingleton.getInstance(getContext()));

    }

    @Override
    public String getFeedType(){
        return "official";
    }


    /**
     * FROM ADAPTER
     */
    @Override
    public void setPostText(String postText, ProfileCommentViewHolder holder) {
        holder.postText.setText(postText);
    }

    /**
     * Description: sets title of post
     * @param postTitle: title of post
     * @param holder: viewholder of item
     */
    @Override
    public void setPostTitle(String postTitle, ProfileCommentViewHolder holder) {
        holder.postTitle.setText(postTitle);
    }

    /**
     * Description: sets image of post
     * @param image: image of post
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
    public void setUserImage(Bitmap image, ProfileCommentViewHolder holder) {
        holder.profileImage.setImageBitmap(image);
    }

    /**
     * Description: sets name of user
     * @param userName: name of user
     * @param holder: viewholder of item
     */
    @Override
    public void setUserName(String userName, ProfileCommentViewHolder holder) {
        holder.username.setText(userName);
    }

    /**
     * Description: sets like count on post
     * @param likeCount: like count on post
     * @param holder: viewholder of item
     */
    @Override
    public void setLikeCount(String likeCount, ProfileCommentViewHolder holder) {
        holder.likeCount.setText(likeCount);
    }

    /**
     * Description: sets date of post
     * @param date: date of post
     * @param holder: viewholder of item
     */
    @Override
    public void setDate(String date, ProfileCommentViewHolder holder) {
        holder.date.setText(date);
    }

    /**
     * Description: sets neutral state of likes
     * @param holder: viewholder of item
     */
    @Override
    public void setLikeNeutralState(ProfileCommentViewHolder holder) {
        holder.upvote.setColorFilter(Color.BLACK, PorterDuff.Mode.SRC_ATOP); //sets upvote color to black
        holder.downvote.setColorFilter(Color.BLACK, PorterDuff.Mode.SRC_ATOP); //sets downvote color to black
    }

    /**
     * Description: sets upvote state of likes
     * @param holder: viewholder of item
     */
    @Override
    public void setLikeUpvoteState(ProfileCommentViewHolder holder) {
        holder.upvote.setColorFilter(Color.RED, PorterDuff.Mode.SRC_ATOP); //sets upvote color to red
        holder.downvote.setColorFilter(Color.BLACK, PorterDuff.Mode.SRC_ATOP); //sets downvote color to black
    }

    /**
     * Description: sets downvote state of likes
     * @param holder: viewholder of item
     */
    @Override
    public void setLikeDownvoteState(ProfileCommentViewHolder holder) {
        holder.upvote.setColorFilter(Color.BLACK, PorterDuff.Mode.SRC_ATOP); //sets upvote color to black
        holder.downvote.setColorFilter(Color.BLUE, PorterDuff.Mode.SRC_ATOP); //sets downvote color to blue
    }

    /**
     * Description: sets comment count of post
     * @param commentCount: comment count of post
     * @param holder: viewholder of item
     */
    @Override
    public void setCommentCount(String commentCount, ProfilePostViewHolder holder) {
        holder.commentCount.setText(commentCount);
    }

    /**
     * Description: hides post image if there is none
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
    public void hideDate(ProfileCommentViewHolder holder) {
        holder.date.setVisibility(View.GONE);
    }
}
