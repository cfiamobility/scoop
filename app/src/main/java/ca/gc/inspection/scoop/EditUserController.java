package ca.gc.inspection.scoop;

import android.content.Context;
import android.util.Log;

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

import java.util.HashMap;
import java.util.Map;

class EditUserController {

	// Runs when the edit profile is pressed
	static void initialFill(Context context) {

		// URL TO BE CHANGED - userID passed as a parameter to NodeJS
		String URL = Config.baseIP + "edituser/getinitial/" + editProfileScreen.userID;

		// Request to set up Volley.Method.GET
		final RequestQueue requestQueue = Volley.newRequestQueue(context);

		// Requesting response be sent back as a JSON Object
		JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.GET, URL, null,  new Response.Listener<JSONObject>() {
			@Override
			public void onResponse(JSONObject response) {
				editProfileScreen.initialFill(response);
			}
		}, new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				Log.i("error", error.toString());
			}
		}) {
			@Override
			public Map<String, String> getHeaders() throws AuthFailureError {
				// inserting the token into the response header that will be sent to the server
				Map<String, String> header = new HashMap<>();
				header.put("authorization", Config.token);
				return header;
			}
		};
		// Request submitted
		requestQueue.add(getRequest);
	}

	// takes care of the requests when the text is changed in the positions edittext
	static void positionAutoComplete(final Context context, String positionChangedCapped) {
		// URL TO BE CHANGED - position entered passed to NodeJS as a parameter
		String URL = Config.baseIP + "edituser/positionchanged/" + positionChangedCapped;

		// Request to set up the get method
		RequestQueue requestQueue = Volley.newRequestQueue(context);

		// Asking for an array from response (will send back 3 objects in an array)
		JsonArrayRequest getRequest = new JsonArrayRequest(Request.Method.GET, URL, null, new Response.Listener<JSONArray>() {
			@Override
			public void onResponse(JSONArray response) {
				editProfileScreen.positionAutoSetup(response, context);
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
		requestQueue.add(getRequest);
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
				editProfileScreen.addressAutoSetup(response, context);
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
				editProfileScreen.divisionAutoSetup(response, context);
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
	static void updateUserInfo(Context context, final Map<String, String> params) {

		// Request url
		String URL = Config.baseIP + "edituser/updatedatabase";

		// Request for the put method
		RequestQueue requestQueue = Volley.newRequestQueue(context);

		// Asking for a string back
		StringRequest stringRequest = new StringRequest(Request.Method.PUT, URL, new Response.Listener<String>() {
			@Override
			public void onResponse(String response) {}
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
