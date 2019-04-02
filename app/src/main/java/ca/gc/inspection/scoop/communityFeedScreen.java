package ca.gc.inspection.scoop;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class communityFeedScreen extends Fragment {

    // recycler view widgets
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    // array list to test the custom recycler view
    private List<String> test;

    public communityFeedScreen() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_community_feed_screen, container, false);

        // setting the title of the activity for the custom tool bar
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Community");

        // initializing the test array
        test = new ArrayList<>();

        test.add("ITEM 1");
        test.add("ITEM 2");
        test.add("ITEM 3");

        // setting up the recycler view
        mRecyclerView = view.findViewById(R.id.recycler_view);
        mRecyclerView.setHasFixedSize(true);

        // setting the layout manager to the recycler view
        mLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(mLayoutManager);

        // using the custom adapter for the recycler view
        mAdapter = new feedAdapter(test);
        mRecyclerView.setAdapter(mAdapter);

        return view;
    }

}
