package ca.gc.inspection.scoop;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
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

public class signUpScreen extends AppCompatActivity {

    // Initializing the buttons, edit texts, and string variables
    String firstNameText, lastNameText, emailText, passwordText;
    EditText firstNameET, lastNameET, emailET, passwordET;
    Button registerBTN;

    // to go back to the splash screen when the back button is pressed
    public void goBackToSplash(View v) {
        startActivity(new Intent(v.getContext(), splashScreen.class));
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_up_screen);

        // Assigning front end ids to variables
        firstNameET = findViewById(R.id.first_name_edit_text);
        lastNameET = findViewById(R.id.last_name_edit_text);
        emailET = findViewById(R.id.email_edit_text);
        passwordET = findViewById(R.id.password_edit_text);
        registerBTN = findViewById(R.id.sign_up_button);

        // Setting the onclick method for the register button
        registerBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Getting the text from the edit texts
                firstNameText = firstNameET.getText().toString();
                lastNameText = lastNameET.getText().toString();
                emailText = emailET.getText().toString();
                passwordText = passwordET.getText().toString();

                // Using the user-inputted information to register the user via this method
                registerUser(firstNameText, lastNameText, emailText, passwordText);
            }
        });

        // set the system status bar color
        getWindow().setStatusBarColor(ContextCompat.getColor(getApplicationContext(), R.color.medium_blue));

        // when the soft keyboard is open tapping anywhere will close the keyboard
        findViewById(R.id.sign_up_layout).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                return true;
            }
        });

    }

    // [INPUT]:         The first name, last name, email, and password that the user entered is passed into this function
    // [PROCESSING]:    Checks to see if the strings are valid (Ex. of invalid strings: empty, email isn't @canada.ca, password doesn't have uppercase, lowercase, number, or symbol)
    // [PROCESSING]:    Makes request to NodeJS server and sends info to NodeJS as a Map<String, String>
    // [OUTPUT]:        Toasts to notify of error or success.
    private void registerUser(final String firstName, final String lastName, final String email, final String password) {


        // Checks for string validity
        if (TextUtils.isEmpty(email) || !email.contains("@canada.ca")) {
            Toast.makeText(this, "Email is invalid", Toast.LENGTH_SHORT).show();
            return;
        } else if (TextUtils.isEmpty(password) || password.length() < 8 || !isValidPassword(password)) {
            Toast.makeText(this, "Password is invalid. Ensure your password contains at least one of the following: Uppercase Letter, Lowercase Letter, Number, Symbol.", Toast.LENGTH_SHORT).show();
            return;
        } else if (TextUtils.isEmpty(firstName)) {
            Toast.makeText(this, "Please enter a first name.", Toast.LENGTH_SHORT).show();
            return;
        } else if (TextUtils.isEmpty(lastName)) {
            Toast.makeText(this, "Please enter a last name", Toast.LENGTH_SHORT).show();
            return;
        }




        // IP ADDRESS OF NODEJS SERVER, CHANGE ACCORDINGLY
        String URL = "http://10.0.2.2:3000/register";
        // IMPORTANT




        // Volley Request
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response.contains("Success")) {
                    // Split the string to get userID - response in form of "Success <USERID>"
                    String[] c = response.split(" ");

                    // c[1] is then userID which is stored in shared preferences
                    SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("ca.gc.inspection.scoop", Context.MODE_PRIVATE);
                    sharedPreferences.edit().putString("userId", c[1]).apply();

                    // Toast to show that the user has successfully signed in
                    Toast.makeText(signUpScreen.this, "You have successfully signed up!", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(getApplicationContext(), mainScreen.class));
                    finish();
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

    // [INPUT]:         The password string is passed into this function
    // [PROCESSING]:    Checks to see if the password contains at least 1 Uppercase, 1 Lowercase, 1 Number, and 1 Non-Alphanumeric character.
    // [OUTPUT]:        None.
    private boolean isValidPassword(String password) {
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
}
