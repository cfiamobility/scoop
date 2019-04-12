package ca.gc.inspection.scoop;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class officialFeedScreen extends Fragment {

    Button button;

    // recycler view widget
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdpater;
    private RecyclerView.LayoutManager mLayoutManager;

    // arryalist test
    private List<String> test;


    public officialFeedScreen() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_official_feed_screen, container, false);

        // initializing test array list
        test = new ArrayList<>();

        test.add("OFFICIAL 1");
        test.add("OFFICIAL 2");
        test.add("OFFICIAL 3");

        // initializing the recycler view
        mRecyclerView = view.findViewById(R.id.recycler_view);
        mRecyclerView.setHasFixedSize(true);

        // setting up the layout manager for the recycler view
        mLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(mLayoutManager);

        // setting up the custom adapter for the recycler view
        mAdpater = new feedAdapter(test);
        mRecyclerView.setAdapter(mAdpater);

        button = view.findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainScreen.otherUserClicked(getFragmentManager(), "e577a74a-0da0-47c8-981a-2b93a7747d4e");
            }
        });


        return view;
    }
}
