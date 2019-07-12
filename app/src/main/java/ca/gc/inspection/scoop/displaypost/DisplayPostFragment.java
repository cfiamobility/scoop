package ca.gc.inspection.scoop.displaypost;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import ca.gc.inspection.scoop.R;
import ca.gc.inspection.scoop.postcomment.PostCommentViewHolder;
import ca.gc.inspection.scoop.postoptionsdialog.PostOptionsDialogFragment;

import static ca.gc.inspection.scoop.Config.SWIPE_REFRESH_COLOUR_1;
import static ca.gc.inspection.scoop.Config.SWIPE_REFRESH_COLOUR_2;
import static ca.gc.inspection.scoop.Config.SWIPE_REFRESH_COLOUR_3;
import static com.google.gson.internal.$Gson$Preconditions.checkNotNull;


/**
 * Fragment which acts as the main view for the viewing community feed action.
 * Responsible for creating the Presenter and Adapter
 */
public class DisplayPostFragment extends Fragment implements
        DisplayPostContract.View.Fragment,
        SwipeRefreshLayout.OnRefreshListener {

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

    private void loadDataFromDatabase() {
        mSwipeRefreshLayout.setRefreshing(true);
        mDisplayPostPresenter.loadDataFromDatabase(mDisplayPostActivity.getActivityId());
    }

    @Override
    public void onResume() {
        super.onResume();
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

    public static void setPostOptionsListener(PostCommentViewHolder viewHolder, String activityId, String posterId) {
        // to get the options menu to appear
        viewHolder.optionsMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // bundle
                Bundle bundle = new Bundle();
                PostOptionsDialogFragment bottomSheetDialog = new PostOptionsDialogFragment();

                //gets the activity id and posterd id and stores in bundle to be fetched in PostOptionsDialogFragment
                Log.i("post I am clicking: ", activityId);
                bundle.putString("ACTIVITY_ID", activityId);
                Log.i("poster id I am clicking: ", posterId);
                bundle.putString("POSTER_ID", posterId);
                Log.i("viewholder: ", viewHolder.getClass().toString());
                bundle.putString("VIEWHOLDER_TYPE", viewHolder.getClass().toString());
                bottomSheetDialog.setArguments(bundle);

                final Context context = v.getContext();
                FragmentManager fragmentManager = ((AppCompatActivity) context).getSupportFragmentManager();
                bottomSheetDialog.show(fragmentManager, "bottomSheet");

            }
        });
    }

}
