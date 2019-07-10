package ca.gc.inspection.scoop.feedpost;

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
import ca.gc.inspection.scoop.util.NetworkUtils;

import static ca.gc.inspection.scoop.Config.SWIPE_REFRESH_COLOUR_1;
import static ca.gc.inspection.scoop.Config.SWIPE_REFRESH_COLOUR_2;
import static ca.gc.inspection.scoop.Config.SWIPE_REFRESH_COLOUR_3;
import static com.google.gson.internal.$Gson$Preconditions.checkNotNull;

public class SavedPostFragment extends Fragment implements
        FeedPostContract.View,
        SwipeRefreshLayout.OnRefreshListener {

    // recycler view widgets
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private View view;
    private FeedPostContract.Presenter mFeedPostPresenter;
    private SwipeRefreshLayout mSwipeRefreshLayout;

    @Override
    public void setPresenter(@NonNull FeedPostContract.Presenter presenter) {
        mFeedPostPresenter = checkNotNull(presenter);
    }

    /**
     * Empty Constructor for fragments
     */
    public SavedPostFragment() {
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
        // Fragment nested inside Activity
        view = inflater.inflate(R.layout.fragment_saved_post, container, false);
        setPresenter(new FeedPostPresenter(this, NetworkUtils.getInstance(getContext())));
        setSwipeRefreshLayout(view);
        loadDataFromDatabase();
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
        mSwipeRefreshLayout = view.findViewById(R.id.fragment_saved_post_swipe);
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
        mFeedPostPresenter.loadDataFromDatabase(getFeedType());
    }

    /**
     * Sets the recycler view
     */
    //TODO: what order do we want to display saved posts?
    public void setRecyclerView() {
        // setting up the recycler view
        mRecyclerView = view.findViewById(R.id.fragment_saved_post_rv);
        mRecyclerView.setHasFixedSize(true);

        // setting the layout manager to the recycler view
        mLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(mLayoutManager);

        // using the custom adapter for the recycler view
        mAdapter = new FeedPostAdapter(this,
                (FeedPostContract.Presenter.AdapterAPI) mFeedPostPresenter);
        mRecyclerView.setAdapter(mAdapter);

    }

    public String getFeedType(){
        return "saved";
    }

}
