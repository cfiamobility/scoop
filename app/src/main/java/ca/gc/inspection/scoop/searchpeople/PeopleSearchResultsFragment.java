package ca.gc.inspection.scoop.searchpeople;

import ca.gc.inspection.scoop.R;
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
public class PeopleSearchResultsFragment extends Fragment {

    // recycler view widgets
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private  RecyclerView.LayoutManager layoutManager;

    // arraylist to test the recycler view
    private List<String> test = new ArrayList<>();

    public PeopleSearchResultsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_search_people, container, false);

        // initializing the test array
        test.add("PERSON 1");
        test.add("PERSON 2");
        test.add("PERSON 3");
        test.add("PERSON 4");

        // initializing the recycler view
        recyclerView = view.findViewById(R.id.fragment_search_people_rv);
        recyclerView.setHasFixedSize(true);

        // setting up the layout manager for the recycler view
        layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);

        // setting up the adapter of the recycler view to the custom people search results adapter
        adapter = new PeopleSearchResultsAdapter(test);
        recyclerView.setAdapter(adapter);

        return view;
    }

}
