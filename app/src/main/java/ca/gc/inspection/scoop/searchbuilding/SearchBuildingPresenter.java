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

/**
 * Presenter for the search building activity
 */
class SearchBuildingPresenter implements
        SearchBuildingContract.Presenter,
        BuildingListReceiver {

    private SearchBuildingContract.View mView;          // stores the view
    private SearchBuildingInteractor mInteractor;       // stores the interactor

    public Map<String, Integer> buildingIDs;            // we use a hashmap to store the buildingid's correlating to each address in the recycler view
                                                        // so that we can pass the correct id back to the edit profile activity once an address is selected


    public SearchBuildingPresenter(SearchBuildingContract.View view, NetworkUtils network) {
        mInteractor = new SearchBuildingInteractor(this, network);
        mView = checkNotNull(view);
        buildingIDs = new HashMap<>();
    }

    public void populateBuildingNamesList() {
        mInteractor.getAllBuildings();
    }

    /**
     * gets building id from our hashmap using the building's address as the key
     * @param building
     * @return
     */
    @Override
    public int getBuildingID(String building) {
        return buildingIDs.get(building);
    }


    /**
     * - builds our data set used by the recycler view
     * - also builds a hashmap containing a <address,buildingid> key-value pair because we need to pass the correct building id back to the edit profile activity after selecting an address
     * @param response json array containing all building addresses fetched from the database
     */
    public void setBuildingsData(JSONArray response){
        for (int i = 0; i < response.length(); i++){
            JSONObject jsonBuilding = null;
            try {
                jsonBuilding = response.getJSONObject(i);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            //mView.addBuilding(jsonBuilding.toString());

            try {
                mView.addBuilding(jsonBuilding.getString("building"));                                            // build dataset of addresses
                buildingIDs.put(jsonBuilding.getString("building"), jsonBuilding.getInt("buildingid"));    // build hashmap of <address,buildingid> key-value pairs

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        mView.updateRecyclerView();
    }


}
