package ca.gc.inspection.scoop.signup;

import android.util.Log;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.auth0.android.jwt.Claim;
import com.auth0.android.jwt.JWT;

import java.util.HashMap;
import java.util.Map;

import ca.gc.inspection.scoop.Config;
import ca.gc.inspection.scoop.util.NetworkUtils;

/**
 * SignUpInteractor used by Presenter to create POST request and store data into the Model(database/server)
 */
public class SignUpInteractor {

    SignUpPresenter mSignUpPresenter;

    public SignUpInteractor (SignUpPresenter presenter){
        mSignUpPresenter = presenter;
    }

    /**
     * Creates a POST request and adds to request queue to store validated user registration data
     * @param network Singleton utility to add string request to global application request queue
     * @param email User inputted email after being validated
     * @param password User inputted password after being validated
     * @param firstName User inputted first name after being capitalized
     * @param lastName User inputted last name after being capitalized
     */
    public void registerUser(final NetworkUtils network, final String email, final String password, final String firstName, final String lastName) {
        //URL for which the http request will be made, corresponding to Node.js code
        String url = Config.baseIP + "signup/register";
        //Setting up the request as a Post request
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                // the response string that is received from the user
                Log.i("RESPONSE", response);

                if (response.equals("ERROR_EMAIL_FORMAT")){
                    mSignUpPresenter.setErrorMessage(response);
                } else if (response.equals("ERROR_EMAIL_EXISTS")){
                    mSignUpPresenter.setErrorMessage(response);
                } else {
                    // grabbing the user id from the jwt token
                    JWT parsedJWT = new JWT(response);
                    Claim userIdMetaData = parsedJWT.getClaim("userid");
                    String userid = userIdMetaData.asString();

                    Log.i("TOKEN", String.valueOf(parsedJWT));
                    Log.i("USER ID", userid);

                    // Helper method to store token and user id into shared preferences
                    mSignUpPresenter.storePreferences(userid, response);
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

        // refrains Volley from sending information twice
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                0,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        // adds the request to the request queue
        network.addToRequestQueue(stringRequest);
    }
}
