package ca.gc.inspection.scoop.postcomment;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
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

import ca.gc.inspection.scoop.Config;
import ca.gc.inspection.scoop.R;
import ca.gc.inspection.scoop.displaypost.DisplayPostActivity;
import ca.gc.inspection.scoop.postoptionsdialog.PostOptionsDialogFragment;
import ca.gc.inspection.scoop.postoptionsdialog.PostOptionsDialogReceiver;
import ca.gc.inspection.scoop.profile.OtherUserActivity;
import ca.gc.inspection.scoop.searchprofile.UserProfileListener;
import ca.gc.inspection.scoop.util.NetworkUtils;

import static ca.gc.inspection.scoop.Config.INTENT_ACTIVITY_ID_KEY;
import static ca.gc.inspection.scoop.Config.INTENT_ACTIVITY_TYPE_KEY;
import static ca.gc.inspection.scoop.Config.INTENT_POSTER_ID_KEY;
import static com.google.gson.internal.$Gson$Preconditions.checkNotNull;


/**
 * Fragment which acts as the main view for the viewing profile comments action.
 * Responsible for creating the Presenter and Adapter
 */
public abstract class PostCommentFragment extends Fragment implements
        PostCommentContract.View,
        PostOptionsDialogReceiver.DeleteCommentReceiver {

    private final static String TAG = "PostCommentFragment";
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
        // No need to load data from database as this fragment is abstract
        return view;
    }

    /**
     * After the view is created
     * @param view: view
     * @param savedInstanceState: state of previous view that invoked this fragment
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

    /**
     * Creates a listener for the upvote and downvote buttons and invokes the respective viewholder method when clicked
     * @param viewHolder viewholder that contains the content, in this case, specifically the upvote and downvote buttons
     * @param i index of the view being upvoted or downvoted in the recyclerview
     */
    public static void setLikesListener(PostCommentViewHolder viewHolder, int i) {

        viewHolder.upvote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewHolder.changeUpvoteLikeState(i); //changes upvote state on click
            }
        });

        viewHolder.downvote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewHolder.changeDownvoteLikeState(i); //changes downvote state on click
            }
        });
    }

    /**
     * Creates a listener for the profile image and profile name of the post and invokes a helper method when clicked
     * @param viewHolder viewholder that contains the content, in this case, specifically the profile image and profile name
     * @param posterId id of the User whose profile/image is being clicked on
     */
    public static void setUserInfoListener(UserProfileListener viewHolder, String posterId) {
        // tapping on profile picture will bring user to poster's profile page
        viewHolder.getProfileImage().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context = v.getContext();
                startFragmentOrActivity(context, posterId);
            }
        });

        viewHolder.getUserName().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context = v.getContext();
                startFragmentOrActivity(context, posterId);
            }
        });
    }

    /**
     * Sets the listener for unsaving a post if the post is already saved (saved != null)
     * @param viewHolder view (post) that is holding the save listener
     * @param i position of the view in the recycler view
     */
    public static void setSaveListener(PostCommentViewHolder viewHolder, int i){
        if (viewHolder.saved != null){
            viewHolder.saved.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    viewHolder.updateSavedState(i);
                }
            });
        }
    }

    /**
     * Sets the listener for saving a post if the post is not saved (unsaved != null)
     * @param viewHolder view (post) that is holding the save listener
     * @param i position of the view in the recycler view
     */
    public static void setUnsaveListener(PostCommentViewHolder viewHolder, int i){
        if(viewHolder.unsaved != null){
            viewHolder.unsaved.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    viewHolder.updateSavedState(i);
                }
            });
        }
    }

    /**
     * If the View is already in the OtherUserActivity then statically start Profile/OtherUserFragment
     * Otherwise start the OtherUserActivity class
     * @param context Context of the current View that holds the listener
     * @param posterId id of the User whose profile/image is being clicked on
     */
    public static void startFragmentOrActivity(Context context, String posterId){
        if (context.getClass() == OtherUserActivity.class)
            OtherUserActivity.startCorrectFragment(posterId);
        else {
            Intent intent = new Intent(context, OtherUserActivity.class);
            intent.putExtra(INTENT_POSTER_ID_KEY, posterId);
            Log.i("INTENT_POSTER_ID_KEY", posterId);
            context.startActivity(intent);
        }
    }

    /**
     * Creates a listener for the whole post view and launches a DisplayPostActivity when clicked
     * The activityid is store in as an intentextra and passed to the DisplayPostActivity
     * @param viewHolder viewholder that displays the current post
     * @param activityId activityid of the post that the viewholder contains
     */
    public static void setDisplayPostListener(PostCommentViewHolder viewHolder, String activityId, String posterId){
        // tapping on any item from the view holder will go to the display post activity
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context = v.getContext();
                if (context.getClass() == DisplayPostActivity.class)
                    Log.d(TAG, "Already displaying post!");
                else {
                    Intent intent = new Intent(context, DisplayPostActivity.class);
                    intent.putExtra(INTENT_ACTIVITY_ID_KEY, activityId);
                    intent.putExtra(INTENT_POSTER_ID_KEY, posterId);
                    context.startActivity(intent);
                }
            }
        });
    }

    /**
     * Creates a listener for the options menu icon of a post and creates a PostOptionsDialogFragment when clicked
     * The parameters are stored in a bundle to be fetched in PostOptionsDialogFragment
     * Invoked mainly in the adapters for comments (posts without ability to save)
     * @param viewHolder viewholder that displays the post content, in this case, specifically the options menu
     * @param activityId activityid of the post that the viewholder contains
     * @param posterId posterid of the post that the viewholder contains
     */
    public static void setPostOptionsListener(
            PostCommentViewHolder viewHolder, int i, String activityId, String posterId,
            PostOptionsDialogReceiver.DeleteCommentReceiver deleteCommentReceiver){
        // to get the options menu to appear
        viewHolder.optionsMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // bundle
                Bundle bundle = new Bundle();
                PostOptionsDialogFragment bottomSheetDialog = new PostOptionsDialogFragment();

                //gets the activity id, posterid, viewHolderType and stores in bundle to be fetched in PostOptionsDialogFragment
                Log.i("post I am clicking: ", activityId);
                bundle.putString("ACTIVITY_ID", activityId);
                Log.i("poster id I am clicking: ", posterId);
                bundle.putString("POSTER_ID", posterId);
                bundle.putInt(INTENT_ACTIVITY_TYPE_KEY, Config.commentType);
                bundle.putInt("POST_POSITION", i);

                bottomSheetDialog.setArguments(bundle);

                // sets the receiver objects to handle callbacks when the user presses a PostOptionsDialog option
                bottomSheetDialog.setDeleteCommentReceiver(deleteCommentReceiver);
                bottomSheetDialog.setEditCommentReceiver(viewHolder);
                bottomSheetDialog.setViewHolder(viewHolder);

                final Context context = v.getContext();
                FragmentManager fragmentManager = ((AppCompatActivity)context).getSupportFragmentManager();
                bottomSheetDialog.show(fragmentManager, "bottomSheet");
            }
        });
    }

    /**
     * Overloading the method above with additional savedStatus parameter
     * Invoked mainly in the adapters for posts (posts with ability to save)
     * @param viewHolder viewholder that displays the post content, in this case the options menu
     * @param activityId activityid of the post that the viewholder contains
     * @param posterId posterid of the post that the viewholder contains
     * @param savedStatus savedstatus of the post for the user clicking on the options menu
     * @param deleteCommentReceiver interface needed to call the DisplayPostFragment's refresh function
     */
    public static void setPostOptionsListener(
            PostCommentViewHolder viewHolder, int i, String activityId, String posterId, Boolean savedStatus,
            String firstPosterId,
            PostOptionsDialogReceiver.DeleteCommentReceiver deleteCommentReceiver){
        // to get the options menu to appear
        viewHolder.optionsMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // bundle
                Bundle bundle = new Bundle();
                PostOptionsDialogFragment bottomSheetDialog = new PostOptionsDialogFragment();

                //gets the activity id, posterid, viewHolderType, and savedStatus and stores in bundle to be fetched in PostOptionsDialogFragment
                Log.i("post I am clicking: ", activityId);
                bundle.putString("ACTIVITY_ID", activityId);
                Log.i("poster id I am clicking: ", posterId);
                bundle.putString("POSTER_ID", posterId);
                Log.i("viewholder: ", viewHolder.getClass().toString());
                bundle.putString("VIEWHOLDER_TYPE", viewHolder.getClass().toString());
                bundle.putInt(INTENT_ACTIVITY_TYPE_KEY, Config.postType);
                Log.i("post position: ", Integer.toString(i));
                bundle.putInt("POST_POSITION", i);
                Log.i("first poster id: ", firstPosterId);
                bundle.putString("FIRST_POSTER_ID", firstPosterId);

                bottomSheetDialog.setArguments(bundle);

                // sets the receiver objects to handle callbacks when the user presses a PostOptionsDialog option
                bottomSheetDialog.setDeleteCommentReceiver(deleteCommentReceiver);
                bottomSheetDialog.setEditPostReceiver((PostOptionsDialogReceiver.EditPostReceiver) viewHolder);
                bottomSheetDialog.setViewHolder(viewHolder);

                final Context context = v.getContext();
                FragmentManager fragmentManager = ((AppCompatActivity)context).getSupportFragmentManager();
                bottomSheetDialog.show(fragmentManager, "bottomSheet");
            }
        });
    }

    /**
     * Method called by PostOptionsDialog class when a comment is deleted
     */
    @Override
    public void onDeletePostComment(boolean isPost) {
        onLoadedDataFromDatabase();
    }


}