package ca.gc.inspection.scoop;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.json.JSONArray;


/**
 * A simple {@link Fragment} subclass.
 */
public class ProfilePostsFragment extends Fragment implements ProfilePostsController.ProfilePostsInterface {

    // recycler view widgets
    private RecyclerView postRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private ProfilePostsController profilePostsController;
    private String userid;
    private View view;

    public ProfilePostsFragment() {
        // Required empty public constructor
    }

    /**
     *
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
        profilePostsController = new ProfilePostsController(this);
        profilePostsController.getUserPosts(userid);
    }

    /**
     * Sets the recycler view with the adapter created
     * @param posts: JSONArray of posts from db
     * @param images: JSONArray of profile pictures from db
     */
    @Override
    public void setPostRecyclerView(JSONArray posts, JSONArray images) {
        // initializing the recycler view
        postRecyclerView = view.findViewById(R.id.recycler_view);
        postRecyclerView.setHasFixedSize(true);

        // setting the layout manager for the recycler view
        mLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        postRecyclerView.setLayoutManager(mLayoutManager);

        // setting the custom adapter for the recycler view
        mAdapter = new ProfilePostsAdapter(posts, images);
        postRecyclerView.setAdapter(mAdapter);
    }
}
