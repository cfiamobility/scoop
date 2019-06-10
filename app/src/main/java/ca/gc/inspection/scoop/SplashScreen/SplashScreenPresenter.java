package ca.gc.inspection.scoop;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.util.Log;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.auth0.android.jwt.Claim;
import com.auth0.android.jwt.JWT;

import java.util.HashMap;
import java.util.Map;

import ca.gc.inspection.scoop.SplashScreen.SplashScreenActivity;
import ca.gc.inspection.scoop.SplashScreen.SplashScreenContract;

public class SplashScreenActivityController {

    @NonNull
    private final SplashScreenContract.View mSplashScreenView;

    public SplashScreenPresenter(SplashScreenContract.View splashScreenView){
        mSplashScreenView = splashScreenView;
        mSplashScreenView.setPresenter(this);
    }

    public static void goToMainScreen(Context context, Activity activity) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("ca.gc.inspection.scoop", Context.MODE_PRIVATE);
        if (!sharedPreferences.getString("userid", "nothing").equals("nothing")) {
            String userId = sharedPreferences.getString("userid", "nothing");
            String token = sharedPreferences.getString("token", "nothing");
            Intent intent = new Intent(context, MainActivity.class);
            Config.currentUser = userId;
            Config.token = token;
            context.startActivity(intent);
            activity.finish();
        }
    }



    /**
     *
     * @param email: email of the person logging in
     * @param password: password of the person logging in
     * @param context: context of activity
     * @param activity: activity
     */
    public static void loginUser(final String email, final String password, final Context context, final Activity activity){
        String URL = Config.baseIP + "signup/login"; //url for which the http request will be made, corresponding to Node.js code
        RequestQueue requestQueue = Volley.newRequestQueue(context); //setting up the request queue for volley
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() { //setting up the request as a post request
            @Override
            public void onResponse(String response) {

                Log.i("RESPONSE", response); // the response string that is received from the user


                // checking to see if the server responded with error messages
                if (response.contains("Incorrect Password")) { // if the user entered an incorrect password
                    SplashScreenActivity.passwordLayout.setError("Incorrect Password");
                } else if (response.contains("Invalid Email")) { // if the user entered an invalid email
                    SplashScreenActivity.emailLayout.setError("Invalid Email");
                } else {

                    // to grab the user id from the jwt token
                    Log.i("Response", response);
                    JWT parsedJWT = new JWT(response); // convert the response string into a JWT token
                    Claim userIdMetaData = parsedJWT.getClaim("userid"); // to get the user id claim from the token
                    String userid = userIdMetaData.asString(); // converting the claim into a string

                    Log.i("TOKEN", String.valueOf(parsedJWT)); // token
                    Log.i("USER ID", userid); // user id

                    // storing the token into shared preferences
                    SharedPreferences sharedPreferences = context.getSharedPreferences("ca.gc.inspection.scoop", Context.MODE_PRIVATE);
                    sharedPreferences.edit().putString("token", response).apply();
                    Config.token = response;

                    // storing the user id into shared preferences
                    sharedPreferences.edit().putString("userid", userid).apply();
                    Config.currentUser = userid;

                    // changes activities once login is successful
                    Intent intent = new Intent(context, MainActivity.class);
                    context.startActivity(intent);
                    activity.finish();
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

        stringRequest.setRetryPolicy(new DefaultRetryPolicy( //refrains Volley from sending information twice
                0,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(stringRequest); //adds the request to the request queue

    }
}
