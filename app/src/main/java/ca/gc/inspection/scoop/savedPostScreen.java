package ca.gc.inspection.scoop;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

public class savedPostScreen extends AppCompatActivity {

    // recycler view widgets
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;

    // array list for testing
    private List<String> test;

    public void finishActivity(View view) {
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saved_post);

        // initializing test array
        test = new ArrayList<>();

        test.add("POST 1");
        test.add("POST 2");
        test.add("POST 3");
        test.add("POST 4");

        // initializing the recycler view
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);

        // setting up the layout manager for the recycler view
        layoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);

        // setting up the custom adapter for the recycler view
        adapter = new feedAdapter(test);
        recyclerView.setAdapter(adapter);
    }
}
