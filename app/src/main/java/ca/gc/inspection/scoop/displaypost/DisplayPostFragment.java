package ca.gc.inspection.scoop.displaypost;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import ca.gc.inspection.scoop.R;
import ca.gc.inspection.scoop.postoptionsdialog.PostOptionsDialogReceiver;

import static ca.gc.inspection.scoop.Config.SWIPE_REFRESH_COLOUR_1;
import static ca.gc.inspection.scoop.Config.SWIPE_REFRESH_COLOUR_2;
import static ca.gc.inspection.scoop.Config.SWIPE_REFRESH_COLOUR_3;
import static com.google.gson.internal.$Gson$Preconditions.checkNotNull;


/**
 * Fragment which acts as the main view for the viewing community feed action.
 * Responsible for creating the Presenter and Adapter
 * We implement DeleteCommentReceiver so that that the PostOptionsDialog can refresh the detailed post view when a post is deleted
 */
public class DisplayPostFragment extends Fragment implements
        DisplayPostContract.View.Fragment,
        SwipeRefreshLayout.OnRefreshListener,
        PostOptionsDialogReceiver.DeleteCommentReceiver {

    // recycler view widgets
    private RecyclerView mRecyclerView;
    private DisplayPostAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private View view;
    private DisplayPostContract.Presenter.FragmentAPI mDisplayPostPresenter;
    private DisplayPostActivity mDisplayPostActivity;
    private SwipeRefreshLayout mSwipeRefreshLayout;

    public void setPresenter(@NonNull DisplayPostContract.Presenter.FragmentAPI presenter) {
        mDisplayPostPresenter = checkNotNull(presenter);
    }

    /**
     * Empty Constructor for fragments
     */
    public DisplayPostFragment() {
    }

    public static DisplayPostFragment newInstance() {
        return new DisplayPostFragment();
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
        mDisplayPostActivity = (DisplayPostActivity) getActivity();
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_display_post, container, false);
        setPresenter(mDisplayPostActivity.getPresenter());
        mDisplayPostPresenter.setFragmentView(this);
        checkNotNull(mDisplayPostActivity.getActivityId());
        setSwipeRefreshLayout(view);
        return view;
    }

    /**
     * See - Fragment Lifecycle
     * @param view: the view
     * @param savedInstanceState: ??
     */
    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);
        setRecyclerView();
    }

    private void setSwipeRefreshLayout(View view) {
        mSwipeRefreshLayout = view.findViewById(R.id.fragment_display_post_swipe);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary,
                SWIPE_REFRESH_COLOUR_1,
                SWIPE_REFRESH_COLOUR_2,
                SWIPE_REFRESH_COLOUR_3);

        // Used to show Swipe Refresh animation on activity create
        mSwipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                loadDataFromDatabase();
            }
        });
    }

    /**
     * To implement SwipeRefreshLayout.OnRefreshListener
     */
    @Override
    public void onRefresh() {
        loadDataFromDatabase();
    }

    @Override
    public void onLoadedDataFromDatabase() {
        mSwipeRefreshLayout.setRefreshing(false);
    }

    /**
     * Helper method to load the post/comments from the database and update the SwipeRefreshLayout to
     * show a loading circle
     */
    private void loadDataFromDatabase() {
        mSwipeRefreshLayout.setRefreshing(true);
        mDisplayPostPresenter.loadDataFromDatabase(mDisplayPostActivity.getActivityId());
    }

    /**
     * Reload the post/comments data when returning to the Fragment
     */
    @Override
    public void onResume() {
        super.onResume();
        if (mAdapter != null)
            mAdapter.refreshAdapter();
        if (!mSwipeRefreshLayout.isRefreshing()) {
            loadDataFromDatabase();
        }
    }

    /**
     * Sets the recycler view
     */
    public void setRecyclerView() {
        // setting up the recycler view
        mRecyclerView = view.findViewById(R.id.fragment_display_post_rv);
        mRecyclerView.setHasFixedSize(true);

        // setting the layout manager to the recycler view
        mLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(mLayoutManager);

        // using the custom adapter for the recycler view
        mAdapter = new DisplayPostAdapter(this, (DisplayPostContract.Presenter.FragmentAPI.AdapterAPI) mDisplayPostPresenter);
        mRecyclerView.setAdapter(mAdapter);
    }

    /**
     * Method called by PostOptionsDialog class when a comment is deleted
     */
    @Override
    public void onDeletePostComment(boolean isPost) {
        if (isPost == true){
            mDisplayPostActivity.goBack(view);
        }
        else{
            loadDataFromDatabase();
        }
    }

}
