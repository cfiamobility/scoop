package ca.gc.inspection.scoop.signup;

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
import com.android.volley.toolbox.Volley;
import com.auth0.android.jwt.Claim;
import com.auth0.android.jwt.JWT;

import java.util.HashMap;
import java.util.Map;

import ca.gc.inspection.scoop.Config;
import ca.gc.inspection.scoop.MainActivity;

class SignUpPresenter implements SignUpContract.Presenter{

    SignUpContract.View mSignUpView;

    public SignUpPresenter(SignUpContract.View signUpView){
        mSignUpView = signUpView;
        mSignUpView.setPresenter(this);
    }

	static void registerUser(final Context context, final String email, final String password, final String firstName, final String lastName, final Activity activity) {

		// URL Call
		String url = Config.baseIP + "signup/register";

		// Volley Request
		RequestQueue requestQueue = Volley.newRequestQueue(context);
		StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
			@Override
			public void onResponse(String response) {

				// grabbing the user id from the jwt token
				JWT parsedJWT = new JWT(response);
				Claim userIdMetaData = parsedJWT.getClaim("userid");
				String userid = userIdMetaData.asString();

				Log.i("TOKEN", String.valueOf(parsedJWT));
				Log.i("USER ID", userid);

				// storing the token into shared preferences
				SharedPreferences sharedPreferences = context.getSharedPreferences("ca.gc.inspection.scoop", Context.MODE_PRIVATE);
				sharedPreferences.edit().putString("token", response).apply();
				Config.token = response;

				// storing the user id into shared preferences
				sharedPreferences.edit().putString("userid", userid).apply();
				Config.currentUser = userid;

				// change activities once register is successful
				Intent intent = new Intent(context, MainActivity.class);
				context.startActivity(intent);
				activity.finish();

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

    // [INPUT]:         The password string is passed into this function
    // [PROCESSING]:    Checks to see if the password contains at least 1 Uppercase, 1 Lowercase, 1 Number, and 1 Non-Alphanumeric character.
    // [OUTPUT]:        None.
    public boolean isValidPassword(String password) {
        char ch;
        // Checks, all must be true to pass this test
        boolean containsUpp = false,
                containsLow = false,
                containsNum = false,
                containsSym = false;

        // For loop to loop through the password string
        for (int i = 0; i < password.length(); i++) {
            ch = password.charAt(i);

            // Contains number
            if (Character.isDigit(ch)) {
                containsNum = true;
            }
            // Contains uppercase
            else if (Character.isUpperCase(ch)) {
                containsUpp = true;
            }
            // Contains lowercase
            else if (Character.isLowerCase(ch)) {
                containsLow = true;
            }
            // Contains symbol
            else if (!Character.isLetterOrDigit(ch)) {
                containsSym = true;
            }

            // Check if all specifications are satisfied
            if (containsLow && containsNum && containsUpp && containsSym) {
                return true;
            }
        }
        return false;
    }

    public String capitalizeFirstLetter(String word) {
        char ch[] = word.toCharArray();
        for (int i = 0; i < word.length(); i++) {

            // If first character of a word is found
            if ((i == 0 && ch[i] != ' ') || (ch[i] != ' ' && ch[i - 1] == ' ')) {
                // If it is in lower-case
                if (ch[i] >= 'a' && ch[i] <= 'z') {
                    // Convert into Upper-case
                    ch[i] = (char)(ch[i] - 'a' + 'A');
                }
            }
            // If apart from first character
            // Any one is in Upper-case
            else if (ch[i] >= 'A' && ch[i] <= 'Z')
                // Convert into Lower-Case
                ch[i] = (char)(ch[i] + 'a' - 'A');
        }
        // Convert the char array to equivalent String
        return new String(ch);
    }

}
