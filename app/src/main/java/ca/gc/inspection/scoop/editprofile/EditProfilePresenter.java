package ca.gc.inspection.scoop.editprofile;

import android.support.annotation.NonNull;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import ca.gc.inspection.scoop.util.NetworkUtils;

import static com.google.gson.internal.$Gson$Preconditions.checkNotNull;

class EditProfilePresenter implements EditProfileContract.Presenter {

    private EditProfileInteractor mInteractor;
    private EditProfileContract.View mView;

    EditProfilePresenter(@NonNull EditProfileContract.View view) {
        mInteractor = new EditProfileInteractor(this);
        mView = checkNotNull(view);
    }

	// Runs when the edit profile is pressed
    @Override
	public void initialFill(NetworkUtils network) {
		// Request submitted
        mInteractor.initialFill(network);
	}

	public void setInitialFill(JSONObject response) {
        mView.setInitialFill(response);
    }

	// takes care of the requests when the text is changed in the positions edittext
	public void getPositionAutoCompleteFromDB(NetworkUtils network, String positionChangedCapitalized) {
		mInteractor.getPositionAutoCompleteFromDB(network, positionChangedCapitalized);
	}

	public void getAddressAutoCompleteFromDB(NetworkUtils network, String addressChangedCapitalized) {
        mInteractor.getAddressAutoCompleteFromDB(network, addressChangedCapitalized);
    }

    public void getDivisionAutoCompleteFromDB(NetworkUtils network, String divisionChangedCapitalized) {
        mInteractor.getDivisionAutoCompleteFromDB(network, divisionChangedCapitalized);
    }

    public void setPositionAutoCompleteFromDB(JSONArray response) {
        try {
            // Position map/arraylist redefined every time text is changed
            HashMap<String, String> positionObjects = new HashMap<>();
            ArrayList<String> positionAutoComplete = new ArrayList<>(); // Arraylist used for setting in the array adapter

            // loops through every object
            for (int i = 0; i < response.length(); i++) {
                // Gathers info from the object - positionid and positionname
                String positionid = response.getJSONObject(i).getString("positionid");
                String positionname = response.getJSONObject(i).getString("positionname");
                Log.i("position", positionname);
                // places the objects into the map/arraylis
                positionObjects.put(positionid, positionname);
                positionAutoComplete.add(positionname);
            }

            mView.setPositionETAdapter(positionAutoComplete);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setAddressAutoCompleteFromDB(JSONArray response) {
        try {
            // map/arraylists redefined everytime to clear it
            HashMap<String, String> buildingsObjects = new HashMap<>();
            ArrayList<String> buildingsAutoComplete = new ArrayList<>();
            ArrayList<String> cityAL = new ArrayList<>();
            ArrayList<String> provinceAL = new ArrayList<>();

            // loops through the object
            for (int i = 0; i < response.length(); i++) {
                // gathering info from the object
                String buildingid = response.getJSONObject(i).getString("buildingid");
                String buildingaddress = response.getJSONObject(i).getString("address");
                String buildingcity = response.getJSONObject(i).getString("city");
                String buildingprovince = response.getJSONObject(i).getString("province");
                // placing the info into variables
                buildingsObjects.put(buildingid, buildingaddress);
                buildingsAutoComplete.add(buildingaddress);
                cityAL.add(buildingcity);
                provinceAL.add(buildingprovince);
            }
            mView.setAddressSuggestionList(cityAL, provinceAL);
            mView.setBuildingETAdapter(buildingsAutoComplete);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Method for to setup the front end of autocomplete text view for divisions
    public void setDivisionAutoCompleteFromDB(JSONArray response) {
        try {
            // map/arraylists redefined every time to clear it
            HashMap<String, String> divisionsObjects = new HashMap<>();
            ArrayList<String> divisionsAutoComplete = new ArrayList<>();

            // Loops throught the JSON Array
            for (int i = 0; i < response.length(); i++) {
                // getting the info from the array
                String divisionid = response.getJSONObject(i).getString("divisionid");
                String divisionname = response.getJSONObject(i).getString("division_en");
                // setting the info into variables
                divisionsObjects.put(divisionid, divisionname);
                divisionsAutoComplete.add(divisionname);
            }

            mView.setDivisionETAdapter(divisionsAutoComplete);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

	// Takes care of the request when the save button is pressed
	public void updateUserInfo(NetworkUtils network, Map<String, String> params) {
        mInteractor.updateUserInfo(network, params);
	}

    public void finishUpdateUserInfo() {
        mView.finishUpdateUserInfo();
    }
}
