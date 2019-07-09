package ca.gc.inspection.scoop.profile;

import android.content.Context;
import android.provider.ContactsContract;

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

import ca.gc.inspection.scoop.Config;
import ca.gc.inspection.scoop.util.NetworkUtils;

public class ProfileInteractor {

    protected ProfilePresenter mPresenter;
    public NetworkUtils mNetwork;

    protected ProfileInteractor(){

    }

    ProfileInteractor(ProfilePresenter presenter, NetworkUtils network){
        mPresenter = presenter;
        mNetwork = network;
    }


    // Request to get the current user's profile information
	public void getUserInfo() {
		String url = Config.baseIP + "profile/initialfill/" + Config.currentUser;

		JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
			@Override
			public void onResponse(JSONObject response) {
				// Sending the response back to be decomposed
				mPresenter.informationResponse(response);
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
		mNetwork.addToRequestQueue(jsonObjectRequest);
	}




	// Request to get the clicked on user's profile information
	public void getOtherUserInfo(String userid) {
		String url = Config.baseIP + "profile/initialfill/" + userid;

		JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
			@Override
			public void onResponse(JSONObject response) {
				// Sending the response back to be decomposed
				mPresenter.informationResponse(response);
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
        mNetwork.addToRequestQueue(jsonObjectRequest);
	}

}
