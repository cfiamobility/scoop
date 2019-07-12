package ca.gc.inspection.scoop.searchpost;

import ca.gc.inspection.scoop.Config;
import ca.gc.inspection.scoop.R;
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

import ca.gc.inspection.scoop.TabFragment;
import ca.gc.inspection.scoop.search.SearchContract;
import ca.gc.inspection.scoop.util.NetworkUtils;

import static com.google.gson.internal.$Gson$Preconditions.checkNotNull;


/**
 * A simple {@link Fragment} subclass.
 */
public class SearchPostFragment extends Fragment implements
        SearchPostContract.View,
        SearchContract.View.Fragment {

    // recycler view widgets
    private RecyclerView postRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private View view;
    private SearchPostContract.Presenter mSearchPostPresenter;

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
        view = inflater.inflate(R.layout.fragment_search_post, container, false);
//        mSearchPostPresenter.loadDataFromDatabase(Config.currentUser, "*");
        setPresenter(new SearchPostPresenter(this, NetworkUtils.getInstance(getContext())));
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
        mSearchPostPresenter.loadDataFromDatabase(Config.currentUser, query);
    }
}
