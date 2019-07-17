package ca.gc.inspection.scoop.searchpeople;

import ca.gc.inspection.scoop.R;
import ca.gc.inspection.scoop.search.SearchContract;
import ca.gc.inspection.scoop.searchpost.view.SearchPostContract;
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

import java.util.ArrayList;
import java.util.List;

import static ca.gc.inspection.scoop.searchpost.view.SearchPostFragment.SEARCH_RESULTS_INFO_SUFFIX;
import static com.google.gson.internal.$Gson$Preconditions.checkNotNull;


/**
 * A simple {@link Fragment} subclass.
 */
public class SearchPeopleFragment extends Fragment implements
        SearchPeopleContract.View,
        SearchContract.View.Fragment,
        SwipeRefreshLayout.OnRefreshListener {

    // recycler view widgets
    private RecyclerView peopleRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private  RecyclerView.LayoutManager mLayoutManager;
    private View view;
    private SearchPeopleContract.Presenter mSearchPeoplePresenter;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private TextView mResultsInfo;
    private String mLastSearchQuery;

    @Override
    public void setPresenter(@NonNull SearchPeopleContract.Presenter presenter) {
        mSearchPeoplePresenter = checkNotNull(presenter);
    }

    /**
     * Empty Constructor for fragments
     */
    public SearchPeopleFragment() {
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
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_search_people, container, false);
        setPresenter(new SearchPeoplePresenter(this, NetworkUtils.getInstance(getContext())));
        setSwipeRefreshLayout(view);

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



    @Override
    public void onRefresh() {

    }

    @Override
    public void onLoadedDataFromDatabase() {

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
        mAdapter = new SearchPeopleAdapter(this,
                (SearchPeopleContract.Presenter.AdapterAPI) mSearchPeoplePresenter);
        peopleRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public void searchQuery(String query) {

    }

}
