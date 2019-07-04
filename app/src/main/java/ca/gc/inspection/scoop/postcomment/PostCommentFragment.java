package ca.gc.inspection.scoop.postcomment;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ca.gc.inspection.scoop.Config;
import ca.gc.inspection.scoop.MainActivity;
import ca.gc.inspection.scoop.R;
import ca.gc.inspection.scoop.displaypost.DisplayPostActivity;
import ca.gc.inspection.scoop.util.NetworkUtils;

import static ca.gc.inspection.scoop.Config.INTENT_ACTIVITY_ID_KEY;
import static com.google.gson.internal.$Gson$Preconditions.checkNotNull;


/**
 * Fragment which acts as the main view for the viewing profile comments action.
 * Responsible for creating the Presenter and Adapter
 */
public class PostCommentFragment extends Fragment implements PostCommentContract.View {

    // recycler view widgets
    private RecyclerView commentsRecyclerView;
    private PostCommentAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private View view;
    private PostCommentContract.Presenter mPostCommentPresenter;

    @Override
    public void setPresenter (@NonNull PostCommentContract.Presenter presenter){
        mPostCommentPresenter = checkNotNull(presenter);
    }

    /**
     * When the fragment initializes
     * @param inflater: inflates the view
     * @param container: contains the view
     * @param savedInstanceState: ??
     * @return: the view
     */
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_profile_comments, container, false);
        setPresenter(new PostCommentPresenter(this, NetworkUtils.getInstance(getContext())));
        mPostCommentPresenter.loadDataFromDatabase(Config.currentUser);
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
        mAdapter = new PostCommentAdapter(this,
                (PostCommentContract.Presenter.AdapterAPI) mPostCommentPresenter);
        commentsRecyclerView.setAdapter(mAdapter);
    }

    public static void setLikesListener(PostCommentViewHolder viewHolder, int i) {

        viewHolder.upvote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewHolder.changeUpvoteLikeState(viewHolder, i); //changes upvote state on click
            }
        });

        viewHolder.downvote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewHolder.changeDownvoteLikeState(viewHolder, i); //changes downvote state on click
            }
        });
    }

    public static void setUserInfoListener(PostCommentViewHolder viewHolder, String posterId) {
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

    public static void setDisplayPostListener(PostCommentViewHolder viewHolder, String activityId){
        // tapping on any item from the view holder will go to the display post activity
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), DisplayPostActivity.class);
                intent.putExtra(INTENT_ACTIVITY_ID_KEY, activityId);
                v.getContext().startActivity(intent);
            }
        });
    }
}