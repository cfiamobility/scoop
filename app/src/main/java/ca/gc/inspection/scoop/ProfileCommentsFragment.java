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
public class ProfileCommentsFragment extends Fragment implements ProfileCommentsController.ProfileCommentsInterface {

    // recycler view widgets
    private RecyclerView commentsRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private String userid;
    private View view;
    private ProfileCommentsController profileCommentsController;

    /**
     * Empty Constructor for fragments
     */
    public ProfileCommentsFragment() {
        // Required empty public constructor
    }

    /**
     * When the fragment initializes
     * @param inflater: inflates the view
     * @param container: contains the view
     * @param savedInstanceState: ??
     * @return: the view
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_profile_comments, container, false);
        Bundle bundle = getArguments();
        userid = bundle.getString("userid");
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

        // Passes in interface into the controller
        profileCommentsController = new ProfileCommentsController(this);

        // After created, the first method is called
        profileCommentsController.getUserComments(userid);
    }

    /**
     * Sets the recycler view, after initializing and setting up the adapter
     * @param comments: JSONArray of the comments of that user
     * @param images: JSONArray of the profile pictures of that user
     */
    @Override
    public void setCommentsRecylerView(JSONArray comments, JSONArray images) {
        // Initializing the recycler view
        commentsRecyclerView = view.findViewById(R.id.fragment_profile_comments_rv);
        commentsRecyclerView.setHasFixedSize(true);

        // Setting the layout manager for the recycler view
        mLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        commentsRecyclerView.setLayoutManager(mLayoutManager);

        // Setting the custom adapter for the recycler view
        mAdapter = new ProfileCommentsAdapter(comments, images);
        commentsRecyclerView.setAdapter(mAdapter);
    }
}