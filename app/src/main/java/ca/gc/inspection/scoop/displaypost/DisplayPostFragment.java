package ca.gc.inspection.scoop.displaypost;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ca.gc.inspection.scoop.R;
import ca.gc.inspection.scoop.feedpost.CommunityFeedFragment;
import ca.gc.inspection.scoop.feedpost.FeedPostAdapter;
import ca.gc.inspection.scoop.feedpost.FeedPostContract;
import ca.gc.inspection.scoop.feedpost.FeedPostPresenter;
import ca.gc.inspection.scoop.profilepost.ProfilePostFragment;
import ca.gc.inspection.scoop.util.NetworkUtils;

import static com.google.gson.internal.$Gson$Preconditions.checkNotNull;


/**
 * Fragment which acts as the main view for the viewing community feed action.
 * Responsible for creating the Presenter and Adapter
 */
public class DisplayPostFragment extends CommunityFeedFragment implements DisplayPostContract.View {

    // recycler view widgets
    private RecyclerView mRecyclerView;
    private FeedPostAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private View view;
    private DisplayPostContract.Presenter mDisplayPostPresenter;

    public void setPresenter(@NonNull DisplayPostContract.Presenter presenter) {
        mDisplayPostPresenter = checkNotNull(presenter);
    }

    /**
     * Empty Constructor for fragments
     */
    public DisplayPostFragment() {
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
        view = inflater.inflate(R.layout.fragment_community_feed, container, false);
        setPresenter(new FeedPostPresenter(this));
        mDisplayPostPresenter.loadDataFromDatabase(NetworkUtils.getInstance(getContext()), getFeedType());
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

    /**
     * Sets the recycler view
     */
    @Override
    public void setRecyclerView() {
        // setting up the recycler view
        mRecyclerView = view.findViewById(R.id.fragment_community_feed_rv);
        mRecyclerView.setHasFixedSize(true);

        // setting the layout manager to the recycler view
        mLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(mLayoutManager);

        // using the custom adapter for the recycler view
        mAdapter = new FeedPostAdapter(this, (FeedPostContract.Presenter.AdapterAPI) mFeedPostPresenter);
        mRecyclerView.setAdapter(mAdapter);

    }

    // TODO remove unnecessary override?
    public String getFeedType(){
        return "community";
    }
}
