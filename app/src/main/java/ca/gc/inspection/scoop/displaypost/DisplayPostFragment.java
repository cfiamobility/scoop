package ca.gc.inspection.scoop.displaypost;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import ca.gc.inspection.scoop.R;
import static com.google.gson.internal.$Gson$Preconditions.checkNotNull;


/**
 * Fragment which acts as the main view for the viewing community feed action.
 * Responsible for creating the Presenter and Adapter
 */
public class DisplayPostFragment extends Fragment implements DisplayPostContract.View.Fragment {

    // recycler view widgets
    private RecyclerView mRecyclerView;
    private DisplayPostAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private View view;
    private DisplayPostContract.Presenter.FragmentAPI mDisplayPostPresenter;
    private DisplayPostActivity mDisplayPostActivity;

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
        mDisplayPostPresenter.loadDataFromDatabase(mDisplayPostActivity.getActivityId());
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

}
