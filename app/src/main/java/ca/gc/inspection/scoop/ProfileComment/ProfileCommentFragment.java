package ca.gc.inspection.scoop.ProfileComment;


import android.content.Context;
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
import ca.gc.inspection.scoop.ProfilePost.ProfilePostInteractor;
import ca.gc.inspection.scoop.R;

import static com.google.gson.internal.$Gson$Preconditions.checkNotNull;


/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileCommentFragment extends Fragment implements ProfileCommentContract.View {

    // recycler view widgets
    private RecyclerView commentsRecyclerView;
    private ProfileCommentAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private View view;
    private ProfileCommentContract.Presenter mProfileCommentPresenter;

    public void setPresenter (@NonNull ProfileCommentContract.Presenter presenter){
        mProfileCommentPresenter = checkNotNull(presenter);
    }

    /**
     * Empty Constructor for fragments
     */
    public ProfileCommentFragment() {
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
        String userid = bundle.getString("userid"); // TODO: can we delete unused userid var?
        setPresenter(new ProfileCommentPresenter(this));
        mProfileCommentPresenter.loadUserCommentsAndImages(MySingleton.getInstance(getContext()), Config.currentUser);
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
        setRecyclerView();
    }

    /**
     * Sets the recycler view
     */
    public void setRecyclerView() {
        // Initializing the recycler view
        commentsRecyclerView = view.findViewById(R.id.fragment_profile_comments_rv);
        commentsRecyclerView.setHasFixedSize(true);

        // Setting the layout manager for the recycler view
        mLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        commentsRecyclerView.setLayoutManager(mLayoutManager);

        // Setting the custom adapter for the recycler view
        mAdapter = new ProfileCommentAdapter(this, (ProfileCommentContract.Presenter.AdapterAPI) mProfileCommentPresenter);
        mAdapter.setView(this);
        commentsRecyclerView.setAdapter(mAdapter);
    }

    public void setProfileCommentLikesListener(ProfileCommentViewHolder viewHolder, int i) {

        viewHolder.upvote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    mProfileCommentPresenter.changeUpvoteLikeState(
                            MySingleton.getInstance(getContext()), viewHolder, i); //changes upvote state on click
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        viewHolder.downvote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    mProfileCommentPresenter.changeDownvoteLikeState(MySingleton.getInstance(getContext()), viewHolder, i); //changes downvote state on click
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void setProfileCommentUserInfoListener(ProfileCommentViewHolder viewHolder, String posterId) {
        // tapping on profile picture will bring user to poster's profile page
        viewHolder.profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.otherUserClicked(posterId);
            }
        });

        viewHolder.username.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.otherUserClicked(posterId);
            }
        });
    }

    public void setProfileCommentImageListener(ProfileCommentViewHolder viewHolder){
        // tapping on any item from the view holder will go to the display post activity
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.getContext().startActivity(new Intent(v.getContext(), DisplayPostActivity.class));
            }
        });
    }
}