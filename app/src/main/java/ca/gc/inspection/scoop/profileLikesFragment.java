package ca.gc.inspection.scoop;


import android.os.Bundle;
import android.support.v4.app.Fragment;
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
public class profileLikesFragment extends Fragment {

    // recycler view widgets
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    // array list for testing
    private List<String> test;


    public profileLikesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile_likes, container, false);

        // initiailizng test array
        test = new ArrayList<>();

        test.add("LIKE 1");
        test.add("LIKE 2");
        test.add("LIKE 3");

        // initializing recycler view
        mRecyclerView = view.findViewById(R.id.recycler_view);
        mRecyclerView.setHasFixedSize(true);

        // setting up the layout manager for the recycler view
        mLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(mLayoutManager);

        // setting up the custom adapter for the recycler view
        mAdapter = new feedAdapter(test);
        mRecyclerView.setAdapter(mAdapter);

        return view;
    }

}
