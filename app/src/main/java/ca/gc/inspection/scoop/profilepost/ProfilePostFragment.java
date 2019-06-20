package ca.gc.inspection.scoop.profilepost;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PorterDuff;
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

import org.json.JSONArray;

import ca.gc.inspection.scoop.Config;
import ca.gc.inspection.scoop.MySingleton;
import ca.gc.inspection.scoop.PostOptionsDialog;
import ca.gc.inspection.scoop.profilecomment.ProfileCommentContract;
import ca.gc.inspection.scoop.profilecomment.ProfileCommentFragment;
import ca.gc.inspection.scoop.profilecomment.ProfileCommentPresenter;
import ca.gc.inspection.scoop.profilecomment.ProfileCommentViewHolder;
import ca.gc.inspection.scoop.R;

import static com.google.gson.internal.$Gson$Preconditions.checkNotNull;


/**
 * A simple {@link Fragment} subclass that holds a recycler view of profile posts on the
 * profile feed
 */
public class ProfilePostFragment extends ProfileCommentFragment implements ProfilePostContract.View {

    // recycler view widgets
    private RecyclerView postRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private String userid;
    private View view;
    private ProfilePostContract.Presenter mProfilePostPresenter;

    @Override
    public void setPresenter(@NonNull ProfileCommentContract.Presenter presenter) {
        mProfilePostPresenter = (ProfilePostContract.Presenter) checkNotNull(presenter);
    }

    /**
     * Empty Constructor for fragments
     */
    public ProfilePostFragment() {
    }

    /**
     * When the fragment initializes
     * @param inflater: inflates the layout
     * @param container: contains the layout
     * @param savedInstanceState: ??
     * @return - returns the view created
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_profile_posts, container, false);
        Bundle bundle = getArguments();
        userid = bundle.getString("userid");
        setPresenter(new ProfilePostPresenter(this));
        mProfilePostPresenter.loadDataFromDatabase(MySingleton.getInstance(getContext()), Config.currentUser);
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
    @Override
    public void setRecyclerView() {
        // initializing the recycler view
        postRecyclerView = view.findViewById(R.id.fragment_profile_posts_rv);
        postRecyclerView.setHasFixedSize(true);

        // setting the layout manager for the recycler view
        mLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        postRecyclerView.setLayoutManager(mLayoutManager);

        // setting the custom adapter for the recycler view
        mAdapter = new ProfilePostAdapter(this, (ProfilePostContract.Presenter.AdapterAPI) mProfilePostPresenter);
        postRecyclerView.setAdapter(mAdapter);
    }

    public void setPostOptionsListener(ProfilePostViewHolder viewHolder){
        // to get the options menu to appear
        viewHolder.optionsMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PostOptionsDialog bottomSheetDialog = new PostOptionsDialog();
                final Context context = v.getContext();
                FragmentManager fragmentManager = ((AppCompatActivity)context).getSupportFragmentManager();
                bottomSheetDialog.show(fragmentManager, "bottomSheet");
            }
        });
    }

}