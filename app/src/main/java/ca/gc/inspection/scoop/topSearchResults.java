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
public class topSearchResults extends Fragment {

    // recycler view widgets
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;

    // array list to test the recycler view
    private List<String> test = new ArrayList<>();


    public topSearchResults() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_top_search_results, container, false);

        // initializing the test array
        test.add("TOP 1");
        test.add("TOP 2");
        test.add("TOP 3");

        // initializing the recycler view
        recyclerView = view.findViewById(R.id.topSearchRecyclerView);
        recyclerView.setHasFixedSize(true);

        // setting up the layout manager for the recycler view
        layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);

        // setting the adapter of the recycler view to the custom feed adapter
        adapter = new feedAdapter(test);
        recyclerView.setAdapter(adapter);

        return view;
    }

}
