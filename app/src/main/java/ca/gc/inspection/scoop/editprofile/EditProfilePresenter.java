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
 *  - The ReportPresenter provides methods for passing the user-generated report data from the View to the Interactor
 *  - It also provides methods for validating and confirming the report
 *  - This is the Presenter for the Report action case
 */
class EditProfilePresenter implements EditProfileContract.Presenter {

    private EditProfileInteractor mInteractor;
    private EditProfileContract.View mView;

    EditProfilePresenter(@NonNull EditProfileContract.View view, NetworkUtils network) {
        mInteractor = new EditProfileInteractor(this, network);
        mView = checkNotNull(view);
    }

	public void initialFill() {
        mInteractor.initialFill();
	}

	void setInitialFill(JSONObject response) {
        try {
//			// If the user id was correct/matching
//			if (response.get("userid").toString().equals(userID)) {
//				// Name gets and sets
//				String firstNameText = response.get("firstname").toString();
//                String lastNameText = response.get("lastname").toString();
//				firstNameET.setText(firstNameText);
//				lastNameET.setText(lastNameText);
//
//				// Profile picture setting and gtting
//                bitmap = CameraUtils.stringToBitmap(response.get("profileimage").toString());
//                previewProfilePic.setImageBitmap(bitmap);
//
//				// If user has already inputted a position
//				if (!response.get("positionid").toString().equals("null")) {
//                    String positionText = response.get("positionname").toString();
//					positionET.setText(positionText);
//				}
//				// If user has already inputted a division
//				if (!response.get("divisionid").toString().equals("null")) {
//                    String divisionText = response.get("division_en").toString();
//					divisionET.setText(divisionText);
//				}// If user has already inputted an office address
//				if (!response.get("buildingid").toString().equals("null")) {
//                    String buildingText = response.get("address").toString();
//					enterBuildingBTN.setText(buildingText);
//
//					mBuildingId = response.get("buildingid").toString();
//					Log.d("setbuildingid", mBuildingId);
//
//					enterBuildingBTN.setTextColor(Color.parseColor("black"));
//					//getBuilding(); // gets building address in the case that the previous activity was "searchBuildingActivity" and the user selected a new building there
//				}
//				// If the user has already inputted a facebook url
//				if (!response.get("facebook").toString().equals("null")) {
//                    String facebookText = response.get("facebook").toString();
//					facebookET.setText(facebookText);
//				}
//				// if the user has already inputted a twitter url
//				if (!response.get("twitter").toString().equals("null")) {
//                    String twitterText = response.get("twitter").toString();
//					twitterET.setText(twitterText);
//				}
//				// If the user has already inputted a linkedin url
//				if (!response.get("linkedin").toString().equals("null")) {
//                    String linkedinText = response.get("linkedin").toString();
//					linkedinET.setText(linkedinText);
//				}
//				// If the user has already inputted an instagram url
//				if (!response.get("instagram").toString().equals("null")) {
//                    String instagramText = response.get("instagram").toString();
//					instagramET.setText(instagramText);
//				}
//			}
		} catch (Exception e) {
			e.printStackTrace();
		}
        mView.setInitialFill();
    }

	// takes care of the requests when the text is changed in the positions edittext
	public void getPositionAutoCompleteFromDB(String positionChangedCapitalized) {
		mInteractor.getPositionAutoCompleteFromDB(positionChangedCapitalized);
	}

    public void getDivisionAutoCompleteFromDB(String divisionChangedCapitalized) {
        mInteractor.getDivisionAutoCompleteFromDB(divisionChangedCapitalized);
    }

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
                // places the objects into the map/arraylis
                positionObjects.put(positionid, positionname);
                positionAutoComplete.add(positionname);
            }

            mView.setPositionETAdapter(positionAutoComplete);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Method for to setup the front end of autocomplete text view for divisions
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

	// Takes care of the request when the save button is pressed
	public void updateUserInfo(Map<String, String> params) {
        mInteractor.updateUserInfo(params);
	}

    void onProfileUpdated(boolean success) {
        mView.onProfileUpdated(success);
    }
}
