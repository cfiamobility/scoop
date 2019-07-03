package ca.gc.inspection.scoop.profilecomment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ca.gc.inspection.scoop.Config;
import ca.gc.inspection.scoop.util.NetworkUtils;
import ca.gc.inspection.scoop.R;

import static com.google.gson.internal.$Gson$Preconditions.checkNotNull;


/**
 * Fragment which acts as the main view for the viewing profile comments action.
 * Responsible for creating the Presenter and Adapter
 */
public class ProfileCommentFragment extends Fragment implements ProfileCommentContract.View {

    // recycler view widgets
    private RecyclerView commentsRecyclerView;
    private ProfileCommentAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private View view;
    private ProfileCommentContract.Presenter mProfileCommentPresenter;

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
        setPresenter(new ProfileCommentPresenter(this));
        mProfileCommentPresenter.loadDataFromDatabase(NetworkUtils.getInstance(getContext()), Config.currentUser);
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
                (ProfileCommentContract.Presenter.AdapterAPI) mProfileCommentPresenter,
                NetworkUtils.getInstance(getContext()));
        commentsRecyclerView.setAdapter(mAdapter);
    }
}