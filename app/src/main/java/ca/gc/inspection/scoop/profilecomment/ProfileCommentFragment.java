package ca.gc.inspection.scoop.profilecomment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ca.gc.inspection.scoop.postoptionsdialog.PostOptionsDialogReceiver;
import ca.gc.inspection.scoop.util.NetworkUtils;
import ca.gc.inspection.scoop.R;

import static ca.gc.inspection.scoop.Config.SWIPE_REFRESH_COLOUR_1;
import static ca.gc.inspection.scoop.Config.SWIPE_REFRESH_COLOUR_2;
import static ca.gc.inspection.scoop.Config.SWIPE_REFRESH_COLOUR_3;
import static com.google.gson.internal.$Gson$Preconditions.checkNotNull;


/**
 * Fragment which acts as the main view for the viewing profile comments action.
 * Responsible for creating the Presenter and Adapter
 */
public class ProfileCommentFragment extends Fragment implements
        ProfileCommentContract.View,
        SwipeRefreshLayout.OnRefreshListener,
        PostOptionsDialogReceiver.DeleteCommentReceiver {

    // recycler view widgets
    private RecyclerView commentsRecyclerView;
    private ProfileCommentAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private View view;
    private String userid;
    private ProfileCommentContract.Presenter mProfileCommentPresenter;
    private SwipeRefreshLayout mSwipeRefreshLayout;

    @Override
    public void setPresenter(@NonNull ProfileCommentContract.Presenter presenter) {
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
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_profile_comments, container, false);
        Bundle bundle = getArguments();
        userid = bundle.getString("userid");
        setPresenter(new ProfileCommentPresenter(this, NetworkUtils.getInstance(getContext())));
        setSwipeRefreshLayout(view);
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
     * Helper method to load the comments from the database and update the SwipeRefreshLayout to
     * show a loading circle
     */
    private void setSwipeRefreshLayout(View view) {
        mSwipeRefreshLayout = view.findViewById(R.id.fragment_profile_comments_swipe);
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

    private void loadDataFromDatabase() {
        mSwipeRefreshLayout.setRefreshing(true);
        mProfileCommentPresenter.loadDataFromDatabase(userid);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!mSwipeRefreshLayout.isRefreshing())
            loadDataFromDatabase();
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
        mAdapter = new ProfileCommentAdapter(this,
                (ProfileCommentContract.Presenter.AdapterAPI) mProfileCommentPresenter);
        commentsRecyclerView.setAdapter(mAdapter);
    }


    /**
     * Method called by PostOptionsDialog class when a comment is deleted
     */
    @Override
    public void onDeletePostComment(boolean isPost) {
        loadDataFromDatabase();
    }
}