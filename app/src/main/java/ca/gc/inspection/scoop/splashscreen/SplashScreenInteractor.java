package ca.gc.inspection.scoop.splashscreen;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

import ca.gc.inspection.scoop.Config;
import ca.gc.inspection.scoop.util.NetworkUtils;

/**
 * Interactor used by the presenter to fetch data from the server
 */
public class SplashScreenInteractor {

    private SplashScreenActivityPresenter presenter;
    private RequestQueue requestQueue;

    SplashScreenInteractor(SplashScreenActivityPresenter presenter, NetworkUtils networkUtils){
        this.presenter = presenter;
        this.requestQueue = networkUtils.getRequestQueue();
    }


    /**
     * Verifies user inputted email and password.
     *  - Response contains error message if login failed
     *  - Response contains user id if login successful
     * @param email
     * @param password
     * @param context
     * @param activity
     */
    public void loginUser(final String email, final String password, final Context context, final Activity activity){
        String URL = Config.baseIP + "signup/login"; //url for which the http request will be made, corresponding to Node.js code
        //RequestQueue requestQueue = Volley.newRequestQueue(context); //setting up the request queue for volley
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() { //setting up the request as a Post request
            @Override
            public void onResponse(String response) {

                Log.i("RESPONSE", response); // the response string that is received from the user

                presenter.loginUserCallBack(response, context, activity);

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
