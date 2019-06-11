package ca.gc.inspection.scoop;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.ArrayAdapter;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.google.gson.internal.$Gson$Preconditions.checkNotNull;

class EditProfilePresenter implements EditProfileContract.Presenter {

    private EditProfileInteractor mInteractor;
    private EditProfileContract.View mView;

    EditProfilePresenter(@NonNull EditProfileContract.View view) {
        mInteractor = new EditProfileInteractor(this);
        mView = checkNotNull(view);
    }

	@Override
	public void start() {

	}

	// Runs when the edit profile is pressed
    @Override
	public void initialFill(MySingleton singleton) {
		// Request submitted
        mInteractor.initialFill(singleton);
	}

	public void setInitialFill(JSONObject response) {
        mView.setInitialFill(response);
    }

	// takes care of the requests when the text is changed in the positions edittext
	public void getPositionAutoCompleteFromDB(MySingleton singleton, String positionChangedCapitalized) {
		mInteractor.getPositionAutoCompleteFromDB(singleton, positionChangedCapitalized);
	}

	public void getAddressAutoCompleteFromDB(MySingleton singleton, String addressChangedCapitalized) {
        mInteractor.getAddressAutoCompleteFromDB(singleton, addressChangedCapitalized);
    }

    public void getDivisionAutoCompleteFromDB(MySingleton singleton, String divisionChangedCapitalized) {
        mInteractor.getDivisionAutoCompleteFromDB(singleton, divisionChangedCapitalized);
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
	static void updateUserInfo(final Context context, final Map<String, String> params) {

		// Request url
		String URL = Config.baseIP + "edituser/updatedatabase";

		// Request for the put method
		RequestQueue requestQueue = Volley.newRequestQueue(context);

		// Asking for a string back
		StringRequest stringRequest = new StringRequest(Request.Method.PUT, URL, new Response.Listener<String>() {
			@Override
			public void onResponse(String response) {
				if (response.equals("success")) {
					TabFragment.refresh();
					((Activity) context).finish();
				}
			}
		}, new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {}
		}) {
			// Inputting the hashmap into the get params method
			@Override
			protected Map<String, String> getParams() {
				return params;
			}

			@Override
			public Map<String, String> getHeaders() throws AuthFailureError {
				// inserting the token into the response header that will be sent to the server
				Map<String, String> header = new HashMap<>();
				header.put("authorization", Config.token);
				return header;
			}
		};

		// submitting the request
		requestQueue.add(stringRequest);
	}
}
