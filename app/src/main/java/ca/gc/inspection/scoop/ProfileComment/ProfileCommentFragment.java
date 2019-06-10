package ca.gc.inspection.scoop.ReplyPost;


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


/**
 * A simple {@link Fragment} subclass.
 */
public class ReplyPostFragment extends Fragment implements ReplyPostContract.View {

    // recycler view widgets
    private RecyclerView commentsRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private String userid;
    private View view;
    private ReplyPostContract.Presenter mReplyPostPresenter;

    public void setPresenter (ReplyPostContract.Presenter presenter){
        mReplyPostPresenter = presenter;
    }

    /**
     * Empty Constructor for fragments
     */
    public ReplyPostFragment() {
        // Required empty public constructor
    }

    /**
     * When the fragment initializes
     * @param inflater: inflates the view
     * @param container: contains the view
     * @param savedInstanceState: ??
     * @return: the view
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_profile_comments, container, false);
        Bundle bundle = getArguments();
        userid = bundle.getString("userid");
        return view;
    }

    /**
     * After the view is created
     * @param view: view
     * @param savedInstanceState: ??
     */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

//        // Passes in interface into the controller
//        profileCommentsController = new ProfileCommentsController(this);
//
//        // After created, the first method is called
//        profileCommentsController.getUserComments(userid);
    }

    /**
     * Sets the recycler view, after initializing and setting up the adapter
     * @param comments: JSONArray of the comments of that user
     * @param images: JSONArray of the profile pictures of that user
     */
    @Override
    public void setRecyclerView(JSONArray comments, JSONArray images) {
        // Initializing the recycler view
        commentsRecyclerView = view.findViewById(R.id.fragment_profile_comments_rv);
        commentsRecyclerView.setHasFixedSize(true);

        // Setting the layout manager for the recycler view
        mLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        commentsRecyclerView.setLayoutManager(mLayoutManager);

        // Setting the custom adapter for the recycler view
        mAdapter = new ReplyPostAdapter(comments, images, userid);
        ((ReplyPostAdapter) mAdapter).setView(this);
        commentsRecyclerView.setAdapter(mAdapter);
    }

    /**
     * FROM ADAPTER
     */


    /**
     * Sets the post title ("Replying to ..." )
     * @param postTitle: post title
     * @param holder: the view holder created
     */
    public void setPostTitle(String postTitle, ReplyPostViewHolder holder) {
        holder.postTitle.setText(postTitle);
    }

    /**
     *
     * @param postText
     * @param holder
     */
    public void setPostText(String postText, ReplyPostViewHolder holder) {
        holder.postText.setText(postText);
    }

    /**
     *
     * @param image
     * @param holder
     */
    public void setUserImage(Bitmap image, ReplyPostViewHolder holder) {
        Log.i("image", image.toString());
        holder.profileImage.setImageBitmap(image);
    }

    /**
     *
     * @param userName
     * @param holder
     */
    public void setUserName(String userName, ReplyPostViewHolder holder) {
        holder.username.setText(userName);
    }

    /**
     *
     * @param likeCount
     * @param holder
     */
    public void setLikeCount(String likeCount, ReplyPostViewHolder holder) {
        holder.likeCount.setText(likeCount);
    }

    /**
     *
     * @param date
     * @param holder
     */
    public void setDate(String date, ReplyPostViewHolder holder) {
        holder.date.setText(date);
    }

    /**
     *
     * @param holder
     */
    public void setLikeDownvoteState(ReplyPostViewHolder holder) {
        holder.upvote.setColorFilter(Color.BLACK, PorterDuff.Mode.SRC_ATOP); //sets upvote color to black
        holder.downvote.setColorFilter(Color.BLUE, PorterDuff.Mode.SRC_ATOP); //sets downvote color to blue
    }

    /**
     *
     * @param holder
     */
    public void setLikeNeutralState(ReplyPostViewHolder holder) {
        holder.upvote.setColorFilter(Color.BLACK, PorterDuff.Mode.SRC_ATOP); //sets upvote color to black
        holder.downvote.setColorFilter(Color.BLACK, PorterDuff.Mode.SRC_ATOP); //sets downvote color to black
    }

    /**
     *
     * @param holder
     */
    public void setLikeUpvoteState(ReplyPostViewHolder holder) {
        holder.upvote.setColorFilter(Color.RED, PorterDuff.Mode.SRC_ATOP); //sets upvote color to red
        holder.downvote.setColorFilter(Color.BLACK, PorterDuff.Mode.SRC_ATOP); //sets downvote color to black
    }

    /**
     *
     * @param holder
     */
    public void hideDate(ReplyPostViewHolder holder) {
        holder.date.setVisibility(View.GONE);
    }
}