package ca.gc.inspection.scoop.ProfileComment;


import android.content.Intent;
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
import org.json.JSONException;
import org.json.JSONObject;

import ca.gc.inspection.scoop.Config;
import ca.gc.inspection.scoop.DisplayPostActivity;
import ca.gc.inspection.scoop.MainActivity;
import ca.gc.inspection.scoop.MyCamera;
import ca.gc.inspection.scoop.MySingleton;
import ca.gc.inspection.scoop.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileCommentFragment extends Fragment implements ProfileCommentContract.View {

    // recycler view widgets
    private RecyclerView commentsRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private View view;
    private ProfileCommentContract.Presenter mProfileCommentPresenter;

    public void setPresenter (ProfileCommentContract.Presenter presenter){
        mProfileCommentPresenter = presenter;
    }

    /**
     * Empty Constructor for fragments
     */
    public ProfileCommentFragment() {
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
        String userid = bundle.getString("userid");
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
//        interactor.getUserComments(Config.currentUser);
        mProfileCommentPresenter.getPosts(MySingleton.getInstance(getContext()), Config.currentUser);
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
        mAdapter = new ProfileCommentAdapter(comments, images);
        ((ProfileCommentAdapter) mAdapter).setView(this);
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
    public void setPostTitle(String postTitle, ProfileCommentViewHolder holder) {
        holder.postTitle.setText(postTitle);
    }

    /**
     *
     * @param postText
     * @param holder
     */
    public void setPostText(String postText, ProfileCommentViewHolder holder) {
        holder.postText.setText(postText);
    }

    /**
     *
     * @param image
     * @param holder
     */
    public void setUserImage(Bitmap image, ProfileCommentViewHolder holder) {
        Log.i("image", image.toString());
        holder.profileImage.setImageBitmap(image);
    }

    /**
     *
     * @param userName
     * @param holder
     */
    public void setUserName(String userName, ProfileCommentViewHolder holder) {
        holder.username.setText(userName);
    }

    /**
     *
     * @param likeCount
     * @param holder
     */
    public void setLikeCount(String likeCount, ProfileCommentViewHolder holder) {
        holder.likeCount.setText(likeCount);
    }

    /**
     *
     * @param date
     * @param holder
     */
    public void setDate(String date, ProfileCommentViewHolder holder) {
        holder.date.setText(date);
    }

    /**
     *
     * @param holder
     */
    public void setLikeDownvoteState(ProfileCommentViewHolder holder) {
        holder.upvote.setColorFilter(Color.BLACK, PorterDuff.Mode.SRC_ATOP); //sets upvote color to black
        holder.downvote.setColorFilter(Color.BLUE, PorterDuff.Mode.SRC_ATOP); //sets downvote color to blue
    }

    /**
     *
     * @param holder
     */
    public void setLikeNeutralState(ProfileCommentViewHolder holder) {
        holder.upvote.setColorFilter(Color.BLACK, PorterDuff.Mode.SRC_ATOP); //sets upvote color to black
        holder.downvote.setColorFilter(Color.BLACK, PorterDuff.Mode.SRC_ATOP); //sets downvote color to black
    }

    /**
     *
     * @param holder
     */
    public void setLikeUpvoteState(ProfileCommentViewHolder holder) {
        holder.upvote.setColorFilter(Color.RED, PorterDuff.Mode.SRC_ATOP); //sets upvote color to red
        holder.downvote.setColorFilter(Color.BLACK, PorterDuff.Mode.SRC_ATOP); //sets downvote color to black
    }

    /**
     *
     * @param holder
     */
    public void hideDate(ProfileCommentViewHolder holder) {
        holder.date.setVisibility(View.GONE);
    }


    public void displayPostListener(ProfileCommentViewHolder holder, String activityid, String posterid){

        holder.upvote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    mProfileCommentPresenter.changeUpvoteLikeState(MySingleton.getInstance(getContext()), activityid, posterid); //changes upvote state on click
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        holder.downvote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    mProfileCommentPresenter.changeDownvoteLikeState(MySingleton.getInstance(getContext()), activityid, posterid); //changes downvote state on click
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        // tapping on any item from the view holder will go to the display post activity
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.getContext().startActivity(new Intent(v.getContext(), DisplayPostActivity.class));
            }
        });

        // tapping on profile picture will bring user to poster's profile page
        holder.profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.otherUserClicked(posterid);
            }
        });

        holder.username.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.otherUserClicked(posterid);
            }
        });
    }

    public void displayImagesListener(ProfileCommentViewHolder holder){
        // tapping on any item from the view holder will go to the display post activity
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.getContext().startActivity(new Intent(v.getContext(), DisplayPostActivity.class));
            }
        });
    }

    /**
     //     * Description: changes image from a string to a bitmap, then setting image
     //     * @param image: image to convert
     //     *
     //     */
    public void formatImage(String image, ProfileCommentViewHolder holder){
        Bitmap bitmap = MyCamera.stringToBitmap(image); //converts image string to bitmap
        setUserImage(bitmap, holder);
    }
}