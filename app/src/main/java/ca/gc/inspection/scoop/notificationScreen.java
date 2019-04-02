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
public class notificationScreen extends Fragment {

    // recycler view widgets
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;

    // array list for testing
    private List<String> test;

    public notificationScreen() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_notifications_screen, container, false);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Notifications");

        // initializing test array list
        test = new ArrayList<>();
        test.add("PERSON 1");
        test.add("PERSON 2");
        test.add("PERSON 3");

        // initializing recycler view
        recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);

        // setting the layout manager for the recycler view
        layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);

        // setting the custom adapter for the recycler view
        adapter = new notificationAdapter(test);
        recyclerView.setAdapter(adapter);

        return view;
    }
}
