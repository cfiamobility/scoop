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

public class SignUpInteractor {

    SignUpContract.Presenter mSignUpPresenter;

    public SignUpInteractor (SignUpContract.Presenter presenter){
        mSignUpPresenter = presenter;
    }

    public void registerUser(final NetworkUtils network, final String email, final String password, final String firstName, final String lastName) {

        // URL Call
        String url = Config.baseIP + "signup/register";

        // Volley Request
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                // the response string that is received from the user
                Log.i("RESPONSE", response);
                // grabbing the user id from the jwt token
                JWT parsedJWT = new JWT(response);
                Claim userIdMetaData = parsedJWT.getClaim("userid");
                String userid = userIdMetaData.asString();

                Log.i("TOKEN", String.valueOf(parsedJWT));
                Log.i("USER ID", userid);

                // storing token and user id into shared preferences
                mSignUpPresenter.storePreferences(userid, response);

//                // storing the token into shared preferences
//                SharedPreferences sharedPreferences = context.getSharedPreferences("ca.gc.inspection.scoop", Context.MODE_PRIVATE);
//                sharedPreferences.edit().putString("token", response).apply();
//                Config.token = response;
//
//                // storing the user id into shared preferences
//                sharedPreferences.edit().putString("userid", userid).apply();
//                Config.currentUser = userid;
//
//                // change activities once register is successful
//                Intent intent = new Intent(context, MainActivity.class);
//                context.startActivity(intent);
//                activity.finish();


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
        network.addToRequestQueue(stringRequest);
    }
}
