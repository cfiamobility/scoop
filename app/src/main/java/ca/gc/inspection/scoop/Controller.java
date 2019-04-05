package ca.gc.inspection.scoop;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

class Controller {

	static void registerUser(final Context context, final String email, final String password, final String firstName, final String lastName, final Activity activity) {

		// URL Call
		String url = Config.baseIP + "signup/register";

		// Volley Request
		RequestQueue requestQueue = Volley.newRequestQueue(context);
		StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
			@Override
			public void onResponse(String response) {
				if (response.contains("Success")) {
					// Split the string to get userID - response in form of "Success <USERID>"
					String[] c = response.split(" ");

					// c[1] is then userID which is stored in shared preferences
					SharedPreferences sharedPreferences = context.getSharedPreferences("ca.gc.inspection.scoop", Context.MODE_PRIVATE);
					sharedPreferences.edit().putString("userId", c[1]).apply();

					// Toast to show that the user has successfully signed in
					Toast.makeText(context, "You have successfully signed up!", Toast.LENGTH_SHORT).show();
					context.startActivity(new Intent(context, mainScreen.class));
					activity.finish();
				}
			}
		}, new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				// Error response (usually timeout)
				Log.i("error info", error.toString());
			}
		}) {
			@Override
			protected Map<String, String> getParams() {
				// Map of information to send to NODEJS
				Map<String, String> params = new HashMap<>();
				params.put("email", email);
				params.put("password", password);
				params.put("firstname", firstName);
				params.put("lastname", lastName);
				return params;
			}
		};

		// Stops the application from sending the data twice. Don't know why it works, but it does.
		stringRequest.setRetryPolicy(new DefaultRetryPolicy(
				0,
				DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
				DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

		// Sends the data to NodeJS
		requestQueue.add(stringRequest);
	}

}
