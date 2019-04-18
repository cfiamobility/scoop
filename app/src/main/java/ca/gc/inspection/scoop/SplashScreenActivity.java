package ca.gc.inspection.scoop;

import android.content.Intent;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;

public class SplashScreenActivity extends AppCompatActivity {

    // text fields
    TextInputEditText email, password;

    // text field layouts - needed to set the error messages if the user input is invalid
    static TextInputLayout emailLayout, passwordLayout;

    public void createAccount (View view) {
        startActivity(new Intent(getApplicationContext(), SignUpActivity.class));
        finish();
    }

    public void logIn (View view) {
        LoginController.loginUser(email.getText().toString(), password.getText().toString(), getApplicationContext(), this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        email = findViewById(R.id.email_edit_text); // instantiating email
        password = findViewById(R.id.password_edit_text); // instantiating password

        emailLayout = findViewById(R.id.email_edit_text_layout); // instantiating email text field layout
        passwordLayout = findViewById(R.id.password_edit_text_layout); // instantiating password text field layout

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
        SplashScreenActivityController.goToMainScreen(getApplicationContext(), this);

    }
}
