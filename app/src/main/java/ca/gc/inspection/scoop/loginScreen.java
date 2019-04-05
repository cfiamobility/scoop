package ca.gc.inspection.scoop;

import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

public class loginScreen extends AppCompatActivity {

    EditText email, password;

    // to return to the splash screen
    public void goBackToSplash (View v) {
        startActivity(new Intent(v.getContext(), splashScreen.class));
        finish();
    }

    // if the login was successful -> go to the main screen
    public void logInSuccessful (View v) {
        LoginController.loginUser(email.getText().toString(), password.getText().toString(), getApplicationContext(), this); //calling the loginUser method
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_screen);

        // set the system status bar color
        getWindow().setStatusBarColor(ContextCompat.getColor(getApplicationContext(), R.color.medium_blue));

        // when the soft keyboard is open tapping anywhere else will close the keyboard
        findViewById(R.id.login_layout).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                return true;
            }
        });

        email = (EditText) findViewById(R.id.email); //instantiating email
        password = (EditText) findViewById(R.id.password); //instantiating password

    }
}

