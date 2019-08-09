package ca.gc.inspection.scoop.splashscreen;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;

import ca.gc.inspection.scoop.Config;
import ca.gc.inspection.scoop.MainActivity;
import ca.gc.inspection.scoop.R;
import ca.gc.inspection.scoop.signup.SignUpActivity;
import ca.gc.inspection.scoop.util.NetworkUtils;

/**
 * - The SplashScreenActivity is the first screen the user enters upon opening the app
 * - This is the View for the Splash Screen action case
 */
public class SplashScreenActivity extends AppCompatActivity implements SplashScreenContract.View {
    // Initializing text fields
    TextInputEditText email, password;
    // Needed to set the error messages if the user input is invalid
    TextInputLayout emailLayout, passwordLayout;
    // reference to the presenter
    private SplashScreenContract.Presenter mSplashScreenPresenter;

    /**
     * Invoked by the Presenter and stores a reference to itself (Presenter) after being constructed by the View
     * @param presenter Presenter to be associated with the View and accessed later
     */
    @Override
    public void setPresenter(SplashScreenContract.Presenter presenter) {
        mSplashScreenPresenter = presenter;
    }


    /**
     * Checks if the current user is logged in and reroutes otherwise,
     * Creates and displays text field layouts with text listeners for email and password input
     * @param savedInstanceState State of the application in a bundle to be recreated so that no prior
     *                           information is lost; if no data is supplied, savedInstanceState is null
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Helper method to check if a current user is logged in
        goToMainScreen(getApplicationContext());

        setContentView(R.layout.activity_splash_screen);
        mSplashScreenPresenter = new SplashScreenPresenter(this);

        email = findViewById(R.id.activity_splash_screen_et_email); // instantiating email edit text
        password = findViewById(R.id.activity_splash_screen_et_password); // instantiating password edit text

        emailLayout = findViewById(R.id.activity_splash_screen_etl_email); // instantiating email text field layout
        passwordLayout = findViewById(R.id.activity_splash_screen_etl_password); // instantiating password text field layout

        // text listeners for the edit text fields
        email.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                emailLayout.setError(null); // to clear the error message for the email text field if there is none
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                passwordLayout.setError(null); // to clear the error message for the password text field if there is none
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        // set the system status bar color
        getWindow().setStatusBarColor(ContextCompat.getColor(getApplicationContext(), R.color.secondary));
    }

    /**
     * Helper method that checks Shared Preferences if a user is logged in
     * If logged in, method creates MainActivity (Community Feed) and ends the Splash Screen activity
     * @param context Context of the current application to access Shared Preferences
     */
    private void goToMainScreen(Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences("ca.gc.inspection.scoop", Context.MODE_PRIVATE);
        if(!sharedPreferences.getString("userid", "nothing").equals("nothing")){
            String userId = sharedPreferences.getString("userid", "nothing");
            String token = sharedPreferences.getString("token", "nothing");

            Intent intent = new Intent(context, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            Config.currentUser = userId;
            Config.token = token;
            context.startActivity(intent);
            finish();
        }
    }

    /**
     * Invoked when the user clicks the "create account" button and ends the current
     * Splash Screen Activity to start the Sign Up Activity
     * @param view The Splash Screen View containing the "create account" button
     */
    public void createAccount (View view) {
        startActivity(new Intent(getApplicationContext(), SignUpActivity.class));
        finish();
    }

    /**
     * Invoked when the user clicks the "log in" button and calls the Presenter loginUser method,
     * passing in NetworkUtils with application context, email input as String, and password as String
     * @param view The Splash Screen View containing the "log in" button
     */
    public void logIn (View view) {
        mSplashScreenPresenter.loginUser(NetworkUtils.getInstance(this), email.getText().toString(), password.getText().toString());
    }

    /**
     * Invoked by the presenter to set an error message when the users inputs an incorrect password
     * @param error string containing the error message
     */
    @Override
    public void setPasswordLayoutError(String error) {
        passwordLayout.setError(error);
    }

    /**
     * Invoked by the presenter to set and error message when the user inputs an invalid/incorrect email
     * @param error string containing the error message
     */
    @Override
    public void setEmailLayoutError(String error) {
        emailLayout.setError(error);
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

        // changes activities once login is successful
        if(Config.token != null && Config.currentUser != null) loginSuccess();
    }

    /**
     * Helper method to switch to MainActivity (Community Feed) and ends the Splash Screen activity
     */
    private void loginSuccess(){
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        getApplicationContext().startActivity(intent);
        finish();
    }
}
