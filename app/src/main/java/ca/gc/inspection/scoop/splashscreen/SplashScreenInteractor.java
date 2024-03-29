package ca.gc.inspection.scoop.splashscreen;

import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.auth0.android.jwt.Claim;
import com.auth0.android.jwt.JWT;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.HashMap;
import java.util.Map;

import ca.gc.inspection.scoop.Config;
import ca.gc.inspection.scoop.util.NetworkUtils;

/**
 * SplashScreenInteractor used by the Presenter to create POST request and store data into the Model(database/server)
 */
public class SplashScreenInteractor {

    private SplashScreenPresenter mSplashScreenPresenter;

    SplashScreenInteractor(SplashScreenPresenter presenter){
        mSplashScreenPresenter = presenter;
    }


    /**
     * Verifies user inputted email and password.
     * - Response contains error message if login failed
     * - Response contains token/userid if login successful
     * @param network Singleton utility to add string request to global application request queue
     * @param email User inputted email
     * @param password User inputted password
     */
    void loginUser(NetworkUtils network, final String email, final String password){
        //URL for which the http request will be made, corresponding to Node.js code
        String URL = Config.baseIP + "signup/login";
        //Setting up the request as a Post request
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                // the response string that is received from the user
                Log.i("RESPONSE", response);

                // Helper method to check if the server responded with error messages
                if (mSplashScreenPresenter.validLogin(response)) {
                    // to grab the user id from the jwt token
                    JWT parsedJWT = new JWT(response); // convert the response string into a JWT token
                    Claim userIdMetaData = parsedJWT.getClaim("userid"); // to get the user id claim from the token
                    String userid = userIdMetaData.asString(); // converting the claim into a string

                    Log.i("TOKEN", String.valueOf(parsedJWT)); // token
                    Log.i("USER ID", userid); // user id

                    // Helper method to store token and user id into shared preferences
                    getSettings(network, userid, response);
                }

            }
        }, new Response.ErrorListener() {


            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("error info", error.toString()); //if there is an error, it will log the error

            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("email", email); //puts the required parameters into the Hashmap which is sent to Node.js code
                params.put("password", password);
                return params;
            }
        };

        // refrains Volley from sending information twice
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                0,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        // adds the request to the request queue
        network.addToRequestQueue(stringRequest);

    }

    /**
     * Gets all setting values for the user
     * @param network
     * @param userid
     * @param token
     */
    private void getSettings(NetworkUtils network, String userid, String token) {
        String URL = Config.baseIP + "settings/getUserSettings";

        StringRequest postRequest = new StringRequest(Request.Method.POST, URL,
                response -> {
                    // response
                    Log.d("Response", response);

                    try {
                        mSplashScreenPresenter.storePreferences(userid, token , new JSONArray(response));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                error -> {
                    // error
                    Log.d("Error.Response", String.valueOf(error));
                }
        ) {
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String> params = new HashMap<>(); // shallow copy settingsMap
                params.put("userid", userid);
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
        network.addToRequestQueue(postRequest);
    }
}
