package ca.gc.inspection.scoop.profilelikes;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ca.gc.inspection.scoop.Config;
import ca.gc.inspection.scoop.profilelikes.ProfileLikesAdapter;
import ca.gc.inspection.scoop.profilelikes.ProfileLikesContract;
import ca.gc.inspection.scoop.profilelikes.ProfileLikesPresenter;
import ca.gc.inspection.scoop.profilelikes.ProfileLikesViewHolder;
import ca.gc.inspection.scoop.util.NetworkUtils;
import ca.gc.inspection.scoop.postoptionsdialog.PostOptionsDialogFragment;
import ca.gc.inspection.scoop.profilecomment.ProfileCommentContract;
import ca.gc.inspection.scoop.R;

import static com.google.gson.internal.$Gson$Preconditions.checkNotNull;


/**
 * Fragment which acts as the main view for the viewing profile post action.
 * Responsible for creating the Presenter and Adapter
 */
public class ProfileLikesFragment extends Fragment implements ProfileLikesContract.View {

    // recycler view widgets
    private RecyclerView postRecyclerView;
    private ProfileLikesAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private String userid;
    private View view;
    private ProfileLikesContract.Presenter mProfileLikesPresenter;

    @Override
    public void setPresenter(@NonNull ProfileLikesContract.Presenter presenter) {
        mProfileLikesPresenter = checkNotNull(presenter);
    }

    /**
     * Empty Constructor for fragments
     */
    public ProfileLikesFragment() {
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
        view = inflater.inflate(R.layout.fragment_profile_posts, container, false);
        Bundle bundle = getArguments();
        userid = bundle.getString("userid");
        setPresenter(new ProfileLikesPresenter(this, NetworkUtils.getInstance(getContext())));
        mProfileLikesPresenter.loadDataFromDatabase(userid);
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
        postRecyclerView = view.findViewById(R.id.fragment_profile_posts_rv);
        postRecyclerView.setHasFixedSize(true);

        // setting the layout manager for the recycler view
        mLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        postRecyclerView.setLayoutManager(mLayoutManager);

        // setting the custom adapter for the recycler view
        mAdapter = new ProfileLikesAdapter(this,
                (ProfileLikesContract.Presenter.AdapterAPI) mProfileLikesPresenter);
        postRecyclerView.setAdapter(mAdapter);
    }

    public static void setPostOptionsListener(ProfileLikesViewHolder viewHolder, String activityid){
        // to get the options menu to appear
        viewHolder.optionsMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // bundle
                Bundle bundle = new Bundle();
                PostOptionsDialogFragment bottomSheetDialog = new PostOptionsDialogFragment();

                //gets the activity id and stores in bundle to be fetched in PostOptionsDialogFragment
                Log.i("post I am clicking: ", activityid);
                bundle.putString("ACTIVITY_ID", activityid);
                bottomSheetDialog.setArguments(bundle);

                final Context context = v.getContext();
                FragmentManager fragmentManager = ((AppCompatActivity)context).getSupportFragmentManager();
                bottomSheetDialog.show(fragmentManager, "bottomSheet");



            }
        });
    }


}