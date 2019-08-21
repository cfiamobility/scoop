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

/**
 *  - The EditProfilePresenter provides methods for passing current user profile information to be displayed in the View
 *  and then sends any updated information back to the Interactor
 *  - Also contains methods for setting up autocompletion suggestions in the Views
 *  - This is the Presenter for the Edit Profile action case
 */
class EditProfilePresenter implements EditProfileContract.Presenter {

    private EditProfileInteractor mInteractor;
    private EditProfileContract.View mView;

    /**
     * Constructor that instantiates the View and Interactor, while constructing an Interactor
     * @param view
     * @param network
     */
    EditProfilePresenter(@NonNull EditProfileContract.View view, NetworkUtils network) {
        mInteractor = new EditProfileInteractor(this, network);
        mView = checkNotNull(view);
    }

    /**
     * Invoked by View to get data from the database
     */
	public void initialFill() {
        mInteractor.initialFill();
	}

    /**
     * Invoked by Interactor to send data from the database to fill in EditText views with any current profile information
     * @param response JSONObject containing current profile data
     */
	void setInitialFill(JSONObject response) {
        mView.setInitialFill(response);
    }

    /**
     * Invoked by View to get list of positions in the database for autocomplete suggestions
     * @param positionChangedCapitalized
     */
	public void getPositionAutoCompleteFromDB(String positionChangedCapitalized) {
		mInteractor.getPositionAutoCompleteFromDB(positionChangedCapitalized);
	}

    /**
     * Invoked by View to get list of divisions in the database for autocomplete suggestions
     * @param divisionChangedCapitalized
     */
    public void getDivisionAutoCompleteFromDB(String divisionChangedCapitalized) {
        mInteractor.getDivisionAutoCompleteFromDB(divisionChangedCapitalized);
    }

    /**
     * Invoked by Interactor to set list of autocomplete suggestions in the EditText View for the Position field
     * @param response JSONArray containing a list of positions
     */
    void setPositionAutoCompleteFromDB(JSONArray response) {
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
                // places the objects into the map/arraylist
                positionObjects.put(positionid, positionname);
                positionAutoComplete.add(positionname);
            }
            mView.setPositionETAdapter(positionAutoComplete);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Invoked by Interactor to set list of autocomplete suggestions in the EditText View for the Division field
     * @param response JSONArray containing a list of divisions
     */
    void setDivisionAutoCompleteFromDB(JSONArray response) {
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

    /**
     * Invoked by the View to send newly edited profile information to the Interactor
     * @param params HashMap of the profile information
     */
	public void updateUserInfo(Map<String, String> params) {
        mInteractor.updateUserInfo(params);
	}

    /**
     * Invoked by the Interactor to display a confirmation message to the View
     * @param success confirmation status
     */
    void onProfileUpdated(boolean success) {
        mView.onProfileUpdated(success);
    }
}
