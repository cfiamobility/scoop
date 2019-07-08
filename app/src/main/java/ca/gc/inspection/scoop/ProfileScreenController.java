package ca.gc.inspection.scoop;

import android.content.Context;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

// TODO refactor Profile into MVP architecture
// these methods can be in an Interactor
class ProfileScreenController {

	// Request to get the current user's profile information
	public static void getUserInfo(Context context) {
		RequestQueue requestQueue = Volley.newRequestQueue(context);

		String url = Config.baseIP + "profile/initialfill/" + Config.currentUser;

		JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
			@Override
			public void onResponse(JSONObject response) {
				// Sending the response back to be decomposed
				ProfileFragment.informationRespone(response);
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
		requestQueue.add(jsonObjectRequest);
	}

	// Request to get the clicked on user's profile information
	public static void getOtherUserInfo(Context context, String userid) {
		RequestQueue requestQueue = Volley.newRequestQueue(context);

		String url = Config.baseIP + "profile/initialfill/" + userid;

		JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
			@Override
			public void onResponse(JSONObject response) {
				// Sending the response back to be decomposed
				OtherUserFragment.otherInformationRespone(response);
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
		requestQueue.add(jsonObjectRequest);
	}
}
