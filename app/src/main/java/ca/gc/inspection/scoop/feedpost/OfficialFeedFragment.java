package ca.gc.inspection.scoop.feedpost;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import ca.gc.inspection.scoop.profilecomment.ProfileCommentContract;
import ca.gc.inspection.scoop.profilepost.ProfilePostFragment;
import ca.gc.inspection.scoop.R;

import static com.google.gson.internal.$Gson$Preconditions.checkNotNull;


/**
 * Fragment which acts as the main view for the viewing official feed action.
 * Responsible for creating the Presenter and Adapter
 */
public class OfficialFeedFragment extends Fragment implements FeedPostContract.View {

    // recycler view widget
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
    public OfficialFeedFragment() {
    }

    /**
     * When the fragment initializes
     * @param inflater: inflates the layout
     * @param container: contains the layout
     * @param savedInstanceState: ??
     * @return
     */
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_official_feed, container, false);
        // setPresenter(new FeedPostPresenter(this));
        // TODO call different loaddatafromdatabase method for official feed data
//        mFeedPostPresenter.loadDataFromDatabase(NetworkUtils.getInstance(getContext()), Config.currentUser);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);
        // TODO add setRecyclerView
    }

    public void setRecyclerView(){
//
//        // initializing the recycler view
//        mRecyclerView = view.findViewById(R.id.fragment_official_feed_rv);
//        mRecyclerView.setHasFixedSize(true);
//
//        // setting up the layout manager for the recycler view
//        mLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
//        mRecyclerView.setLayoutManager(mLayoutManager);
//
//        // setting up the custom adapter for the recycler view
//        mAdapter = new FeedPostAdapter(this, (FeedPostContract.Presenter.AdapterAPI) mFeedPostPresenter);
//        mRecyclerView.setAdapter(mAdapter);
    }

    public String getFeedType(){
        return "official";
    }


//    /**
//     * FROM ADAPTER
//     */
//    @Override
//    public void setPostText(String postText, PostCommentViewHolder holder) {
//        holder.postText.setText(postText);
//    }
//
//    /**
//     * Description: sets title of post
//     * @param postTitle: title of post
//     * @param holder: viewholder of item
//     */
//    @Override
//    public void setPostTitle(String postTitle, PostCommentViewHolder holder) {
//        holder.postTitle.setText(postTitle);
//    }
//
//    /**
//     * Description: sets image of post
//     * @param image: image of post
//     * @param holder: viewholder of item
//     */
//    @Override
//    public void setPostImage(Bitmap image, FeedPostViewHolder holder) {
//        holder.postImage.setImageBitmap(image);
//    }
//
//    /**
//     * Description: sets image of user
//     * @param image: image of user
//     * @param holder: viewholder of item
//     */
//    @Override
//    public void setUserImage(Bitmap image, PostCommentViewHolder holder) {
//        holder.profileImage.setImageBitmap(image);
//    }
//
//    /**
//     * Description: sets name of user
//     * @param userName: name of user
//     * @param holder: viewholder of item
//     */
//    @Override
//    public void setUserName(String userName, PostCommentViewHolder holder) {
//        holder.username.setText(userName);
//    }
//
//    /**
//     * Description: sets like count on post
//     * @param likeCount: like count on post
//     * @param holder: viewholder of item
//     */
//    @Override
//    public void setLikeCount(String likeCount, PostCommentViewHolder holder) {
//        holder.likeCount.setText(likeCount);
//    }
//
//    /**
//     * Description: sets date of post
//     * @param date: date of post
//     * @param holder: viewholder of item
//     */
//    @Override
//    public void setDate(String date, PostCommentViewHolder holder) {
//        holder.date.setText(date);
//    }
//
//    /**
//     * Description: sets neutral state of likes
//     * @param holder: viewholder of item
//     */
//    @Override
//    public void setLikeNeutralState(PostCommentViewHolder holder) {
//        holder.upvote.setColorFilter(Color.BLACK, PorterDuff.Mode.SRC_ATOP); //sets upvote color to black
//        holder.downvote.setColorFilter(Color.BLACK, PorterDuff.Mode.SRC_ATOP); //sets downvote color to black
//    }
//
//    /**
//     * Description: sets upvote state of likes
//     * @param holder: viewholder of item
//     */
//    @Override
//    public void setLikeUpvoteState(PostCommentViewHolder holder) {
//        holder.upvote.setColorFilter(Color.RED, PorterDuff.Mode.SRC_ATOP); //sets upvote color to red
//        holder.downvote.setColorFilter(Color.BLACK, PorterDuff.Mode.SRC_ATOP); //sets downvote color to black
//    }
//
//    /**
//     * Description: sets downvote state of likes
//     * @param holder: viewholder of item
//     */
//    @Override
//    public void setLikeDownvoteState(PostCommentViewHolder holder) {
//        holder.upvote.setColorFilter(Color.BLACK, PorterDuff.Mode.SRC_ATOP); //sets upvote color to black
//        holder.downvote.setColorFilter(Color.BLUE, PorterDuff.Mode.SRC_ATOP); //sets downvote color to blue
//    }
//
//    /**
//     * Description: sets comment count of post
//     * @param commentCount: comment count of post
//     * @param holder: viewholder of item
//     */
//    @Override
//    public void setCommentCount(String commentCount, ProfilePostViewHolder holder) {
//        holder.commentCount.setText(commentCount);
//    }
//
//    /**
//     * Description: hides post image if there is none
//     * @param holder: viewholder of item
//     */
//    @Override
//    public void hidePostImage(FeedPostViewHolder holder) {
//        holder.postImage.setVisibility(View.GONE);
//    }
//
//    /**
//     * Description: hides date if there is an error
//     * @param holder: viewholder of item
//     */
//    @Override
//    public void hideDate(PostCommentViewHolder holder) {
//        holder.date.setVisibility(View.GONE);
//    }
}
