package ca.gc.inspection.scoop.searchprofile.view;

import ca.gc.inspection.scoop.R;
import ca.gc.inspection.scoop.search.SearchActivity;
import ca.gc.inspection.scoop.search.SearchContract;
import ca.gc.inspection.scoop.searchprofile.SearchProfileContract;
import ca.gc.inspection.scoop.searchprofile.presenter.SearchProfilePresenter;
import ca.gc.inspection.scoop.util.NetworkUtils;

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
import android.widget.TextView;

import static ca.gc.inspection.scoop.Config.SWIPE_REFRESH_COLOUR_1;
import static ca.gc.inspection.scoop.Config.SWIPE_REFRESH_COLOUR_2;
import static ca.gc.inspection.scoop.Config.SWIPE_REFRESH_COLOUR_3;
import static ca.gc.inspection.scoop.searchpost.view.SearchPostFragment.SEARCH_RESULTS_INFO_SUFFIX;
import static com.google.gson.internal.$Gson$Preconditions.checkNotNull;


/**
 * A simple {@link Fragment} subclass.
 */
public class SearchProfileFragment extends Fragment implements
        SearchProfileContract.View,
        SearchContract.View.Fragment,
        SwipeRefreshLayout.OnRefreshListener {

    // recycler view widgets
    private RecyclerView peopleRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private  RecyclerView.LayoutManager mLayoutManager;
    private View view;
    private SearchActivity mSearchActivity;
    private SearchProfileContract.Presenter mSearchProfilePresenter;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private TextView mResultsInfo;

    @Override
    public void setPresenter(@NonNull SearchProfileContract.Presenter presenter) {
        mSearchProfilePresenter = checkNotNull(presenter);
    }

    /**
     * Empty Constructor for fragments
     */
    public SearchProfileFragment() {
    }

    /**
     * When the fragment initializes
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mSearchActivity = (SearchActivity) getActivity();
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_search_people, container, false);
        setPresenter(new SearchProfilePresenter(this, NetworkUtils.getInstance(getContext())));
        setSwipeRefreshLayout(view);
        mResultsInfo = view.findViewById(R.id.fragment_search_people_results_info);
        return view;
    }

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

    /**
     * Helper method to load the profiles from the database and update the SwipeRefreshLayout to
     * show a loading circle
     */
    private void setSwipeRefreshLayout(View view) {
        mSwipeRefreshLayout = view.findViewById(R.id.fragment_search_people_swipe);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary,
                SWIPE_REFRESH_COLOUR_1,
                SWIPE_REFRESH_COLOUR_2,
                SWIPE_REFRESH_COLOUR_3);
    }

    @Override
    public void onRefresh() {
        String query = mSearchActivity.getCurrentSearchQuery();
        if (query != null && !query.isEmpty())
            loadDataFromDatabase(query);
        else mSwipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void onLoadedDataFromDatabase() {
        mSwipeRefreshLayout.setRefreshing(false);
    }

    private void loadDataFromDatabase(String query) {
        mSwipeRefreshLayout.setRefreshing(true);
        mSearchProfilePresenter.loadDataFromDatabase(query);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!mSwipeRefreshLayout.isRefreshing()) {
            String query = mSearchActivity.getCurrentSearchQuery();
            if (query != null && !query.isEmpty()) {
                loadDataFromDatabase(query);
            }
        }
    }

    /**
     * Sets the recycler view
     */
    public void setRecyclerView() {
        // initializing the recycler view
        peopleRecyclerView = view.findViewById(R.id.fragment_search_people_rv);
        peopleRecyclerView.setHasFixedSize(true);

        // setting up the layout manager for the recycler view
        mLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        peopleRecyclerView.setLayoutManager(mLayoutManager);

        // setting up the mAdapter of the recycler view to the custom people search results mAdapter
        mAdapter = new SearchProfileAdapter(this,
                (SearchProfileContract.Presenter.AdapterAPI) mSearchProfilePresenter);
        peopleRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public void searchQuery(String query) {
        if (mSearchProfilePresenter != null) {
            loadDataFromDatabase(query);
        }
    }
}
