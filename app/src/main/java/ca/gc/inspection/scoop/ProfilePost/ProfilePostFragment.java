package ca.gc.inspection.scoop.ProfilePost;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.json.JSONArray;

import ca.gc.inspection.scoop.Config;
import ca.gc.inspection.scoop.MyApplication;
import ca.gc.inspection.scoop.MyCamera;
import ca.gc.inspection.scoop.MySingleton;
import ca.gc.inspection.scoop.PostOptionsDialog;
import ca.gc.inspection.scoop.ProfileComment.ProfileCommentFragment;
import ca.gc.inspection.scoop.ProfileComment.ProfileCommentViewHolder;
import ca.gc.inspection.scoop.R;
import ca.gc.inspection.scoop.ProfileComment.ProfileCommentContract;

import static com.google.gson.internal.$Gson$Preconditions.checkNotNull;


/**
 * A simple {@link Fragment} subclass that holds a recycler view of profile posts on the
 * profile feed
 */
public class ProfilePostFragment extends ProfileCommentFragment implements ProfilePostContract.View {

    // recycler view widgets
    private RecyclerView postRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private String userid;
    private View view;
    private ProfilePostContract.Presenter mProfilePostPresenter;
    private ProfilePostInteractor mProfilePostInteractor;

    public void setPresenter (ProfilePostContract.Presenter presenter){
        mProfilePostPresenter = checkNotNull(presenter);
    }
//    public String getUserId() {
//        return userid;
//    }

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
//        mProfilePostPresenter.getPosts(MySingleton.getInstance(MyApplication.getContext()), Config.currentUser);
        mProfilePostInteractor = new ProfilePostInteractor(this);
        mProfilePostInteractor.getUserPosts(MySingleton.getInstance(getContext()), Config.currentUser);

    }

    /**
     * Sets the recycler view with the adapter created
     * @param posts: JSONArray of posts from db
     * @param images: JSONArray of profile pictures from db
     */

    public void setRecyclerView(JSONArray posts, JSONArray images) {
        // initializing the recycler view
        postRecyclerView = view.findViewById(R.id.fragment_profile_posts_rv);
        postRecyclerView.setHasFixedSize(true);

        // setting the layout manager for the recycler view
        mLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        postRecyclerView.setLayoutManager(mLayoutManager);

        // setting the custom adapter for the recycler view
        mAdapter = new ProfilePostAdapter(posts, images);
        ((ProfilePostAdapter) mAdapter).setView(this);
        postRecyclerView.setAdapter(mAdapter);

//        mProfilePostPresenter.getPosts(MySingleton.getInstance(getActivity()), Config.currentUser);

    }

    public void setCommentCount(String commentCount, ProfilePostViewHolder holder) {
        holder.commentCount.setText(commentCount);
    }

    public void displayPostListener(ProfilePostViewHolder holder){
        // to get the options menu to appear
        holder.optionsMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PostOptionsDialog bottomSheetDialog = new PostOptionsDialog();
                final Context context = v.getContext();
                FragmentManager fragmentManager = ((AppCompatActivity)context).getSupportFragmentManager();
                bottomSheetDialog.show(fragmentManager, "bottomSheet");
            }
        });
    }
    /**
     * FROM ADAPTER
     */

    public void setPostText(String postText, ProfileCommentViewHolder holder) {
        holder.postText.setText(postText);
    }

    public void setPostTitle(String postTitle, ProfileCommentViewHolder holder) {
        holder.postTitle.setText(postTitle);
    }

    public void setUserImage(Bitmap image, ProfileCommentViewHolder holder) {
        Log.i("image", image.toString());
        holder.profileImage.setImageBitmap(image);
    }

    public void setUserName(String userName, ProfileCommentViewHolder holder) {
        holder.username.setText(userName);
    }

    public void setLikeCount(String likeCount, ProfileCommentViewHolder holder) {
        holder.likeCount.setText(likeCount);
    }

    public void setDate(String date, ProfileCommentViewHolder holder) {
        holder.date.setText(date);
    }

    public void setLikeDownvoteState(ProfileCommentViewHolder holder) {
        holder.upvote.setColorFilter(Color.BLACK, PorterDuff.Mode.SRC_ATOP); //sets upvote color to black
        holder.downvote.setColorFilter(Color.BLUE, PorterDuff.Mode.SRC_ATOP); //sets downvote color to blue
    }

    public void setLikeNeutralState(ProfileCommentViewHolder holder) {
        holder.upvote.setColorFilter(Color.BLACK, PorterDuff.Mode.SRC_ATOP); //sets upvote color to black
        holder.downvote.setColorFilter(Color.BLACK, PorterDuff.Mode.SRC_ATOP); //sets downvote color to black
    }

    public void setLikeUpvoteState(ProfileCommentViewHolder holder) {
        holder.upvote.setColorFilter(Color.RED, PorterDuff.Mode.SRC_ATOP); //sets upvote color to red
        holder.downvote.setColorFilter(Color.BLACK, PorterDuff.Mode.SRC_ATOP); //sets downvote color to black
    }

    public void hideDate(ProfileCommentViewHolder holder) {
        holder.date.setVisibility(View.GONE);
    }

//    @Override
//    public void setDisplayPostListener(ProfilePostViewHolder holder){
//        // to get the options menu to appear
//        holder.optionsMenu.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                PostOptionsDialog bottomSheetDialog = new PostOptionsDialog();
//                final Context context = v.getContext();
//                FragmentManager fragmentManager = ((AppCompatActivity)context).getSupportFragmentManager();
//                bottomSheetDialog.show(fragmentManager, "bottomSheet");
//            }
//        });
//    }


}