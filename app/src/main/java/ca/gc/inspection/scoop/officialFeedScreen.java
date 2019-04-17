package ca.gc.inspection.scoop;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.json.JSONArray;

import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class officialFeedScreen extends Fragment implements FeedController.FeedInterface {


    // recycler view widget
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_official_feed_screen, container, false);

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);

        FeedController controller = new FeedController(this);
        controller.getPosts();
    }


    @Override
    public void setRecyclerView(JSONArray posts, JSONArray images){

        // initializing the recycler view
        mRecyclerView = view.findViewById(R.id.recycler_view);
        mRecyclerView.setHasFixedSize(true);

        // setting up the layout manager for the recycler view
        mLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(mLayoutManager);

        // setting up the custom adapter for the recycler view
        mAdapter = new feedAdapter(posts, images);
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public String getFeedType(){
        return "official";
    }
}
