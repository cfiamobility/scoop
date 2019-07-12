package ca.gc.inspection.scoop.searchbuilding;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.SearchView;
import android.widget.Toast;

import java.util.ArrayList;

import ca.gc.inspection.scoop.R;
import ca.gc.inspection.scoop.editprofile.EditProfileActivity;
import ca.gc.inspection.scoop.util.NetworkUtils;

import static ca.gc.inspection.scoop.MyApplication.getContext;

public class SearchBuildingActivity extends AppCompatActivity implements BuildingAdapter.ItemClickListener, SearchBuildingContract.View {

    BuildingAdapter adapter;
    ArrayList<String> buildingNames;
    private SearchBuildingContract.Presenter mPresenter;
    RecyclerView recyclerView;

    SearchView searchBar;


    @Override
    public void setPresenter(SearchBuildingContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_building);
        setPresenter(new SearchBuildingPresenter(this, NetworkUtils.getInstance(getContext()) ));

        searchBar = findViewById(R.id.buildingSearchBar);
        searchBar.setQueryHint("Search Building");
        searchBar.setIconifiedByDefault(false);
        searchBar.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.getFilter().filter(newText);
                return false;
            }
        });

        buildingNames = new ArrayList<>();

        mPresenter.populateBuildingNamesList();

        //buildingNames.add("49 camelot");
        //buildingNames.add("59 camelot");


        // set up recycler view
        recyclerView = findViewById(R.id.buildingNamesRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new BuildingAdapter(this,buildingNames);
        adapter.setClickListener(this);
        recyclerView.setAdapter(adapter);

    }




    @Override
    public void onItemClick(View view, int position) {
        //Toast.makeText(this, "You clicked " + adapter.getItem(position) + " on row number " + position, Toast.LENGTH_SHORT).show();
        //Intent intent = new Intent(this, EditProfileActivity.class);

        Intent intent = new Intent();
        intent.putExtra("building", adapter.getItem(position));
        intent.putExtra("buildingid", mPresenter.getBuildingID(adapter.getItem(position)));

        setResult(Activity.RESULT_OK, intent);

        //startActivity(intent);
        finish();
    }


    @Override
    public void addBuilding(String building) {
        buildingNames.add(building);
    }

    public void updateRecyclerView(){
        adapter.notifyDataSetChanged();
    }
}
