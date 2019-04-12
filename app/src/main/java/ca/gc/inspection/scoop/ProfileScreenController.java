package ca.gc.inspection.scoop;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

class ProfileScreenController {


	public static void getUserInfo(Context context) {
		RequestQueue requestQueue = Volley.newRequestQueue(context);

		String url = Config.baseIP + "profile/initialfill/" + Config.currentUser;

		JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
			@Override
			public void onResponse(JSONObject response) {
				profileScreen.informationRespone(response);
			}
		}, new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {

			}
		});
		requestQueue.add(jsonObjectRequest);
	}

	public static void getOtherUserInfo(Context context, String userid) {
		RequestQueue requestQueue = Volley.newRequestQueue(context);

		String url = Config.baseIP + "profile/initialfill/" + userid;

		JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
			@Override
			public void onResponse(JSONObject response) {
				OtherUserFragment.otherInformationRespone(response);
			}
		}, new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {

			}
		});
		requestQueue.add(jsonObjectRequest);
	}
}
