package ca.gc.inspection.scoop.splashscreen;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.auth0.android.jwt.Claim;
import com.auth0.android.jwt.JWT;

import java.util.HashMap;
import java.util.Map;

import ca.gc.inspection.scoop.Config;
import ca.gc.inspection.scoop.MainActivity;
import ca.gc.inspection.scoop.util.NetworkUtils;

/**
 * Interactor used by the presenter to fetch data from the server
 */
public class SplashScreenInteractor {

    private SplashScreenContract.Presenter mSplashScreenPresenter;

    SplashScreenInteractor(SplashScreenPresenter presenter){
        mSplashScreenPresenter = presenter;
    }


    /**
     * Verifies user inputted email and password.
     *  - Response contains error message if login failed
     *  - Response contains user id if login successful

     */
    void loginUser(NetworkUtils network, final String email, final String password){
        //url for which the http request will be made, corresponding to Node.js code
        String URL = Config.baseIP + "signup/login";
        //setting up the request as a Post request
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                // the response string that is received from the user
                Log.i("RESPONSE", response);
//              presenter.loginUserCallBack(response, context, activity);
                // checking to see if the server responded with error messages

                if (mSplashScreenPresenter.validLogin(response)) {
                    // to grab the user id from the jwt token
                    JWT parsedJWT = new JWT(response); // convert the response string into a JWT token
                    Claim userIdMetaData = parsedJWT.getClaim("userid"); // to get the user id claim from the token
                    String userid = userIdMetaData.asString(); // converting the claim into a string

                    Log.i("TOKEN", String.valueOf(parsedJWT)); // token
                    Log.i("USER ID", userid); // user id


                    mSplashScreenPresenter.storePreferences(userid, response);
                }
//                // storing the token into shared preferences
//                SharedPreferences sharedPreferences = context.getSharedPreferences("ca.gc.inspection.scoop", Context.MODE_PRIVATE);
//                sharedPreferences.edit().putString("token", response).apply();
//                Config.token = response;
//
//                // storing the user id into shared preferences
//                sharedPreferences.edit().putString("userid", userid).apply();
//                Config.currentUser = userid;
//
//                // changes activities once login is successful
//                Intent intent = new Intent(context, MainActivity.class);
//                context.startActivity(intent);
//                activity.finish();

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

        stringRequest.setRetryPolicy(new DefaultRetryPolicy( //refrains Volley from sending information twice
                0,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        network.addToRequestQueue(stringRequest); //adds the request to the request queue

    }
}
