package ca.gc.inspection.scoop.signup;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;

import ca.gc.inspection.scoop.Config;
import ca.gc.inspection.scoop.MainActivity;
import ca.gc.inspection.scoop.R;
import ca.gc.inspection.scoop.splashscreen.SplashScreenActivity;
import ca.gc.inspection.scoop.util.NetworkUtils;
import static ca.gc.inspection.scoop.util.StringUtils.capitalizeFirstLetter;

/**
 * - The SignUpActivity is the screen that allows users to create an account
 * - This is the View for the Sign Up action case
 */

public class SignUpActivity extends AppCompatActivity implements SignUpContract.View{
    // Initializing the buttons, edit texts, and string variables
    String firstNameText, lastNameText, emailText, passwordText, reEnterPasswordText;
    Button registerBTN;
    TextInputEditText firstNameET, lastNameET, emailET, passwordET, reEnterPasswordET;
    // Needed to set the error messages if the user input is invalid
    TextInputLayout firstNameLayout, lastNameLayout, emailLayout, passwordLayout;
    // reference to the presenter
    private SignUpContract.Presenter mSignUpPresenter;


    /**
     * Invoked by the Presenter and stores a reference to itself (Presenter) after being constructed by the View
     * @param presenter Presetner to be associated with the View and access later
     */
    @Override
    public void setPresenter (SignUpContract.Presenter presenter){
        mSignUpPresenter = presenter;
    }

    /**
     * Creates and displays text field layouts with text/button listeners for the first name, last name, email,
     * and password layout
     * Registers user when "register" button is clicked with inputted text
     * @param savedInstanceState State of the application in a bundle to be recreated so that no prior
     *                           information is lost; if no data is supplied, savedInstanceState is null
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_sign_up);
        mSignUpPresenter = new SignUpPresenter(this);

        // text layout variables
        firstNameLayout = findViewById(R.id.activity_sign_up_etl_first_name);
        lastNameLayout = findViewById(R.id.activity_sign_up_etl_last_name);
        emailLayout = findViewById(R.id.activity_sign_up_etl_email);
        passwordLayout = findViewById(R.id.activity_sign_up_etl_password);

        // Assigning front end ids to variables
        firstNameET = findViewById(R.id.activity_sign_up_et_first_name);
        lastNameET = findViewById(R.id.activity_sign_up_et_last_name);
        emailET = findViewById(R.id.activity_sign_up_et_email);
        passwordET = findViewById(R.id.activity_sign_up_et_password);
        reEnterPasswordET = findViewById(R.id.activity_sign_up_et_password_re_enter);
        registerBTN = findViewById(R.id.activity_sign_up_btn_sign_up);

        // Setting the onclick method for the register button
        registerBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Getting the text from the edit texts
                firstNameText = capitalizeFirstLetter(firstNameET.getText().toString());
                lastNameText = capitalizeFirstLetter(lastNameET.getText().toString());
                emailText = (emailET.getText().toString()).toLowerCase();
                passwordText = passwordET.getText().toString();
                reEnterPasswordText = reEnterPasswordET.getText().toString();

                // Using the user-inputted information to register the user via this method
                registerUser(firstNameText, lastNameText, emailText, passwordText, reEnterPasswordText);
            }
        });

        // set the system status bar color
        getWindow().setStatusBarColor(ContextCompat.getColor(getApplicationContext(), R.color.primary_dark));

        // text listeners for the edit text fields so that the error message will disappear when the user starts typing into the text field again
        firstNameET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                firstNameLayout.setError(null); // clearing the error message for the first name field if there is one
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        lastNameET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                lastNameLayout.setError(null); // clearing the error message for the last name field if there is one
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        emailET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                emailLayout.setError(null); // clearing the error message for the email field if there is one
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        passwordET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                passwordLayout.setError(null); // clearing the error message for the password field if there is one
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    /**
     * Invoked when the user hits the Android device back button and ends the current
     * Sign Up Activity and returns to the Splash Screen Activity
     * @param view The current Sign Up View
     */
    public void goBackToSplash(View view) {
        startActivity(new Intent(view.getContext(), SplashScreenActivity.class));
        finish();
    }

    /**
     * Helper method that encapsulates registration by calling methods to validate info
     * and pass the information to the Presenter
     * @param firstName User inputted first name
     * @param lastName User inputted last name
     * @param email User inputted email
     * @param password User inputted password
     */
    private void registerUser(final String firstName, final String lastName, final String email, final String password, final String reEnterPassword) {

        if (validRegister(firstName, lastName, email, password, reEnterPassword)){
            mSignUpPresenter.registerUser(NetworkUtils.getInstance(this), email, password, firstName, lastName);
        }

    }

    /**
     * Helper method to validate user inputted fields: empty or meets the email/password requirements
     * @param firstName User inputted first name
     * @param lastName User inputted last name
     * @param email User inputted email
     * @param password User inputted password
     */
    //TODO: Middle tier to check if email is valid and pass error through response
    private boolean validRegister(final String firstName, final String lastName, final String email, final String password, final String reEnterPassword){
        // Checks for string validity
        boolean isValid = true;
        if (TextUtils.isEmpty(firstName)) { // first name field is empty
            firstNameLayout.setError(getString(R.string.enter_a_first_name));
            isValid = false;
        } if (TextUtils.isEmpty(lastName)) { // last name field is empty
            lastNameLayout.setError(getString(R.string.enter_a_last_name));
            isValid = false;
        } if (TextUtils.isEmpty(email)) { // if the email field is empty
            emailLayout.setError(getString(R.string.enter_a_valid_email));
            isValid = false;
        }

        if (TextUtils.isEmpty(password) || password.length() < 8 || !SignUpPresenter.isValidPassword(password)) { // is the password field is empty or is less than 8 characters or is not valid
            passwordLayout.setError(getString(R.string.enter_a_valid_password));
            isValid = false;
        }else if(!TextUtils.equals(password, reEnterPassword)){ // If password field and re-enter password field do not match
            passwordLayout.setError(getString(R.string.passwords_do_not_match));
            isValid = false;
        }
        else{
            passwordLayout.setError("");
        }

        if (isValid){
            return true;
        }else{
            return false;
        }
    }

    /**
     * Invoked by the Presenter and adds the userid and response token to the Shared Preferences and application
     * config file
     * @param userid Unique user ID passed from Interactor/Model
     * @param response Unique reponse token from Interactor/Model
     */
    public void storePreferences(String userid, String response){
        // storing the token into shared preferences
        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("ca.gc.inspection.scoop", Context.MODE_PRIVATE);
        sharedPreferences.edit().putString("token", response).apply();
        Config.token = response;

        // storing the user id into shared preferences
        sharedPreferences.edit().putString("userid", userid).apply();
        Config.currentUser = userid;

        // change activities once register is successful
        if(Config.token != null && Config.currentUser != null) registerSuccess();
    }

    /**
     * Helper method to switch to MainActivity (Community Feed) and ends the Sign Up activity
     */
    private void registerSuccess(){
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        getApplicationContext().startActivity(intent);
        finish();
    }


    public void displayErrorMessage(String message) {
        emailLayout.setError(message);
    }
}
