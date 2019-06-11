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

	// takes care of the requests when the text is changed in the positions edittext
	public void positionAutoComplete(MySingleton singleton, String positionChangedCapped) {
		mInteractor.positionAutoComplete(singleton, positionChangedCapped);
	}

    // Method to setup the front end of autocomplete text view for positions
    public void positionAutoSetup(JSONArray response) {
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

	// takes care of the requests when the text is changed in the building edittext
	static void addressAutoComplete(final Context context, String addressChangedCapped) {
		// URL TO BE CHANGED - address passed as parameter to nodeJS
		String URL = Config.baseIP + "edituser/addresschanged/" + addressChangedCapped;

		// Request for get method
		RequestQueue requestQueue = Volley.newRequestQueue(context);

		// Asking for a JSONArray
		JsonArrayRequest getRequest = new JsonArrayRequest(Request.Method.GET, URL, null, new Response.Listener<JSONArray>() {
			@Override
			public void onResponse(JSONArray response) {
				EditProfileActivity.addressAutoSetup(response, context);
			}
		}, new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {}
		}) {
			@Override
			public Map<String, String> getHeaders() throws AuthFailureError {
				// inserting the token into the response header that will be sent to the server
				Map<String, String> header = new HashMap<>();
				header.put("authorization", Config.token);
				return header;
			}
		};
		// Submitting the request
		requestQueue.add(getRequest);
	}

	// takes care of the requests when the text is changed in the divisions edittext
	static void divisionAutoComplete(final Context context, String divisionChangedCapped) {
		// Inputted division is passed as a parameter to NodeJS
		String URL = Config.baseIP + "edituser/divisionchanged/" + divisionChangedCapped;

		// Request for the get method
		RequestQueue requestQueue = Volley.newRequestQueue(context);

		// Asking for a JSONArray back
		JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, URL, null, new Response.Listener<JSONArray>() {
			@Override
			public void onResponse(JSONArray response) {
				EditProfileActivity.divisionAutoSetup(response, context);
			}
		}, new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {}
		}) {
			@Override
			public Map<String, String> getHeaders() throws AuthFailureError {
				// inserting the token into the response header that will be sent to the server
				Map<String, String> header = new HashMap<>();
				header.put("authorization", Config.token);
				return header;
			}
		};
		// submitting the request
		requestQueue.add(jsonArrayRequest);
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
