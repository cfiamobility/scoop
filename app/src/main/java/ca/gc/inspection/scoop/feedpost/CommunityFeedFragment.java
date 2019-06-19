package ca.gc.inspection.scoop.feedpost;


import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.json.JSONArray;

import ca.gc.inspection.scoop.Config;
import ca.gc.inspection.scoop.DisplayPostActivity;
import ca.gc.inspection.scoop.MySingleton;
import ca.gc.inspection.scoop.profilecomment.ProfileCommentViewHolder;
import ca.gc.inspection.scoop.profilepost.ProfilePostFragment;
import ca.gc.inspection.scoop.profilepost.ProfilePostViewHolder;
import ca.gc.inspection.scoop.R;

import static com.google.gson.internal.$Gson$Preconditions.checkNotNull;


/**
 * A simple {@link Fragment} subclass.
 */
public class CommunityFeedFragment extends ProfilePostFragment implements FeedPostContract.View  {

    // recycler view widgets
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private View view;
    private FeedPostContract.Presenter mFeedPostPresenter;

    public void setPresenter(@NonNull FeedPostContract.Presenter presenter) {
        mFeedPostPresenter = checkNotNull(presenter);
    }

    /**
     * Empty Constructor for fragments
     */
    public CommunityFeedFragment() {
    }


    /**
     * When the fragment initializes
     * @param inflater: inflates the layout
     * @param container: contains the layout
     * @param savedInstanceState: ??
     * @return
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_community_feed, container, false);
        setPresenter(new FeedPostPresenter(this));
        mFeedPostPresenter.loadDataFromDatabase(MySingleton.getInstance(getContext()), getFeedType());
        return view;
    }

    /**
     * See - Fragment Lifecycle
     * @param view: the view
     * @param savedInstanceState: ??
     */
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState){
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

    @Override
    public String getFeedType(){
        return "community";
    }
}
