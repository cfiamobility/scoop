package ca.gc.inspection.scoop.splashscreen;

import android.app.Activity;
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
 * - The SplashScreen is the first screen the user enters upon opening the app
 * - This is the view for the splash screen activity
 */
public class SplashScreenActivity extends AppCompatActivity implements SplashScreenContract.View {
    // text fields
    TextInputEditText email, password;
    // text field layouts - needed to set the error messages if the user input is invalid
    TextInputLayout emailLayout, passwordLayout;
    // reference to the presenter
    private SplashScreenContract.Presenter mSplashScreenPresenter;

    /**
     * stores a reference to the presenter to be accessed later
     * @param presenter
     */
    @Override
    public void setPresenter(SplashScreenContract.Presenter presenter) {
        mSplashScreenPresenter = presenter;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mSplashScreenPresenter = new SplashScreenPresenter(this);

        setContentView(R.layout.activity_splash_screen);

        email = findViewById(R.id.activity_splash_screen_et_email); // instantiating email
        password = findViewById(R.id.activity_splash_screen_et_password); // instantiating password

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

        // to check if a current user is logged in
        goToMainScreen(getApplicationContext(), this);
    }

    private void goToMainScreen(Context context, Activity activity){
        SharedPreferences sharedPreferences = context.getSharedPreferences("ca.gc.inspection.scoop", Context.MODE_PRIVATE);
        if(!sharedPreferences.getString("userid", "nothing").equals("nothing")){
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
     * Invoked when the user clicks the "create account" button
     * @param view
     */
    public void createAccount (View view) {
        startActivity(new Intent(getApplicationContext(), SignUpActivity.class));
        finish();
    }

    /**
     * Invoked when the user clicks the "log in" button
     * @param view
     */
    public void logIn (View view) {
        mSplashScreenPresenter.loginUser(NetworkUtils.getInstance(this), email.getText().toString(), password.getText().toString());
    }

    /**
     * invoked by the presenter to set an error message when the users inputs an incorrect password
     * @param error string containing the error message
     */
    @Override
    public void setPasswordLayoutError(String error) {
        passwordLayout.setError(error);
    }

    /**
     * invoked by the presenter to set and error message when the user inputs an invalid/incorrect email
     * @param error string containing the error message
     */
    @Override
    public void setEmailLayoutError(String error) {
        emailLayout.setError(error);
    }

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

    private void loginSuccess(){
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        getApplicationContext().startActivity(intent);
        finish();
    }
}
