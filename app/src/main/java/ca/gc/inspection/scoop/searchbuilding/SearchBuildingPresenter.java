package ca.gc.inspection.scoop.searchbuilding;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import ca.gc.inspection.scoop.util.NetworkUtils;

import static com.google.gson.internal.$Gson$Preconditions.checkNotNull;

class SearchBuildingPresenter implements SearchBuildingContract.Presenter {

    private SearchBuildingContract.View mView;
    private SearchBuildingInteractor mInteractor;
    public Map<String, Integer> buildingIDs;


    public SearchBuildingPresenter(SearchBuildingContract.View view, NetworkUtils network) {
        mInteractor = new SearchBuildingInteractor(this, network);
        mView = checkNotNull(view);
        buildingIDs = new HashMap<>();
    }

    public void populateBuildingNamesList() {
        mInteractor.getAllBuildings();
    }

    @Override
    public int getBuildingID(String building) {
        return buildingIDs.get(building);
    }


    public void updateData(JSONArray response){
        for (int i = 0; i < response.length(); i++){
            JSONObject jsonBuilding = null;
            try {
                jsonBuilding = response.getJSONObject(i);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            //mView.addBuilding(jsonBuilding.toString());

            try {
                mView.addBuilding(jsonBuilding.getString("building"));
                buildingIDs.put(jsonBuilding.getString("building"), jsonBuilding.getInt("buildingid"));

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        mView.updateRecyclerView();
    }


}
