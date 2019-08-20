package ca.gc.inspection.scoop.searchpost.view;

import ca.gc.inspection.scoop.Config;
import ca.gc.inspection.scoop.R;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import ca.gc.inspection.scoop.postoptionsdialog.PostOptionsDialogReceiver;
import ca.gc.inspection.scoop.search.SearchActivity;
import ca.gc.inspection.scoop.search.SearchContract;
import ca.gc.inspection.scoop.searchpost.SearchPostContract;
import ca.gc.inspection.scoop.searchpost.presenter.SearchPostPresenter;
import ca.gc.inspection.scoop.util.NetworkUtils;

import static ca.gc.inspection.scoop.Config.SWIPE_REFRESH_COLOUR_1;
import static ca.gc.inspection.scoop.Config.SWIPE_REFRESH_COLOUR_2;
import static ca.gc.inspection.scoop.Config.SWIPE_REFRESH_COLOUR_3;
import static com.google.gson.internal.$Gson$Preconditions.checkNotNull;


/**
 * A simple {@link Fragment} subclass.
 */
public class SearchPostFragment extends Fragment implements
        SearchPostContract.View,
        SearchContract.View.Fragment,
        SwipeRefreshLayout.OnRefreshListener,
        PostOptionsDialogReceiver.DeleteCommentReceiver {

    // recycler view widgets
    private RecyclerView postRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private View view;
    private SearchActivity mSearchActivity;
    private SearchPostContract.Presenter mSearchPostPresenter;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private TextView mResultsInfo;

    public static final String SEARCH_RESULTS_INFO_SUFFIX = " results";

    @Override
    public void setPresenter(@NonNull SearchPostContract.Presenter presenter) {
        mSearchPostPresenter = checkNotNull(presenter);
    }

    /**
     * Empty Constructor for fragments
     */
    public SearchPostFragment() {
        Log.d("Search Post Fragment", "constructor");
    }

    /**
     * When the fragment initializes
     * @param inflater: inflates the layout
     * @param container: contains the layout
     * @param savedInstanceState: ??
     * @return - returns the view created
     */
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mSearchActivity = (SearchActivity) getActivity();
        view = inflater.inflate(R.layout.fragment_search_post, container, false);
        setPresenter(new SearchPostPresenter(this, NetworkUtils.getInstance(getContext())));
        setSwipeRefreshLayout(view);
        mResultsInfo = view.findViewById(R.id.fragment_search_post_results_info);
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
        setRecyclerView();
    }

    @Override
    public void setResultsInfo(int resultsCount) {
        if (resultsCount >= 0) {
            String resultsInfoText = resultsCount + SEARCH_RESULTS_INFO_SUFFIX;
            mResultsInfo.setText(resultsInfoText);
        }
    }

    private void setSwipeRefreshLayout(View view) {
        mSwipeRefreshLayout = view.findViewById(R.id.fragment_search_post_swipe);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary,
                SWIPE_REFRESH_COLOUR_1,
                SWIPE_REFRESH_COLOUR_2,
                SWIPE_REFRESH_COLOUR_3);
    }

    /**
     * To implement SwipeRefreshLayout.OnRefreshListener
     */
    @Override
    public void onRefresh() {
        String query = mSearchActivity.getCurrentSearchQuery();
        if (query != null && !query.isEmpty())
            loadDataFromDatabase(Config.currentUser, query);
        else mSwipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!mSwipeRefreshLayout.isRefreshing()) {
            String query = mSearchActivity.getCurrentSearchQuery();
            if (query != null && !query.isEmpty()) {
                loadDataFromDatabase(Config.currentUser, query);
            }
        }
    }

    @Override
    public void onLoadedDataFromDatabase() {
        mSwipeRefreshLayout.setRefreshing(false);
    }

    private void loadDataFromDatabase(String userId, String query) {
        mSwipeRefreshLayout.setRefreshing(true);
        mSearchPostPresenter.loadDataFromDatabase(userId, query);
    }

    /**
     * Sets the recycler view
     */
    public void setRecyclerView() {
        // initializing the recycler view
        postRecyclerView = view.findViewById(R.id.fragment_search_post_rv);
        postRecyclerView.setHasFixedSize(true);

        // setting the layout manager for the recycler view
        mLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        postRecyclerView.setLayoutManager(mLayoutManager);

        // setting the custom adapter for the recycler view
        mAdapter = new SearchPostAdapter(this,
                (SearchPostContract.Presenter.AdapterAPI) mSearchPostPresenter);
        postRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public void searchQuery(String query) {
        if (mSearchPostPresenter != null) {
            loadDataFromDatabase(Config.currentUser, query);
        }
    }

    /**
     * Called by the PostOptionsDialog when a post is deleted
     * @param isPost
     */
    @Override
    public void onDeletePostComment(boolean isPost) {
        loadDataFromDatabase(Config.currentUser, mSearchActivity.getCurrentSearchQuery());
    }
}
