package ca.gc.inspection.scoop.savedpost;

import ca.gc.inspection.scoop.*;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import ca.gc.inspection.scoop.ProfileAdapter;

import static com.google.gson.internal.$Gson$Preconditions.checkNotNull;

public class SavedPostActivity extends AppCompatActivity implements SavedPostContract.View {

    private SavedPostContract.Presenter mPresenter;

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

        // set the system status bar color
        getWindow().setStatusBarColor(ContextCompat.getColor(getApplicationContext(), R.color.primary_dark));

        setPresenter(new SavedPostPresenter(this));

        // initializing test array
        test = new ArrayList<>();

        test.add("POST 1");
        test.add("POST 2");
        test.add("POST 3");
        test.add("POST 4");

        // initializing the recycler view
        recyclerView = findViewById(R.id.activity_saved_post_rv);
        recyclerView.setHasFixedSize(true);

        // setting up the layout manager for the recycler view
        layoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);

        // setting up the custom adapter for the recycler view
        adapter = new ProfileAdapter();
        recyclerView.setAdapter(adapter);

    }

    @Override
    public void setPresenter(@NonNull SavedPostContract.Presenter presenter) {
        mPresenter = checkNotNull(presenter);
    }
}
