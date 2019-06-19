package ca.gc.inspection.scoop.feedpost;


import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.json.JSONArray;

import ca.gc.inspection.scoop.Config;
import ca.gc.inspection.scoop.MySingleton;
import ca.gc.inspection.scoop.profilecomment.ProfileCommentViewHolder;
import ca.gc.inspection.scoop.profilepost.ProfilePostFragment;
import ca.gc.inspection.scoop.profilepost.ProfilePostViewHolder;
import ca.gc.inspection.scoop.R;

import static com.google.gson.internal.$Gson$Preconditions.checkNotNull;


/**
 * A simple {@link Fragment} subclass.
 */
public class CommunityFeedFragment extends ProfilePostFragment implements FeedPostContract.View  {

    // recycler view widgets
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private View view;
    private FeedPostContract.Presenter mFeedPostPresenter;

    public void setPresenter(@NonNull FeedPostContract.Presenter presenter) {
        mFeedPostPresenter = checkNotNull(presenter);
    }

    /**
     * Empty Constructor for fragments
     */
    public CommunityFeedFragment() {
    }


    /**
     * When the fragment initializes
     * @param inflater: inflates the layout
     * @param container: contains the layout
     * @param savedInstanceState: ??
     * @return
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_community_feed, container, false);
        setPresenter(new FeedPostPresenter(this));
        mFeedPostPresenter.loadDataFromDatabase(MySingleton.getInstance(getContext()), Config.currentUser);
        return view;
    }

    /**
     * See - Fragment Lifecycle
     * @param view: the view
     * @param savedInstanceState: ??
     */
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);
        setRecyclerView();
    }

    /**
     * Sets the recycler view
     */
    @Override
    public void setRecyclerView() {
        // setting up the recycler view
        mRecyclerView = view.findViewById(R.id.fragment_community_feed_rv);
        mRecyclerView.setHasFixedSize(true);

        // setting the layout manager to the recycler view
        mLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(mLayoutManager);

        // using the custom adapter for the recycler view
        mAdapter = new FeedPostAdapter(this, (FeedPostContract.Presenter.AdapterAPI) mFeedPostPresenter);
        mRecyclerView.setAdapter(mAdapter);

    }

    @Override
    public String getFeedType(){
        return "community";
    }


    //TODO remove set methods
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
