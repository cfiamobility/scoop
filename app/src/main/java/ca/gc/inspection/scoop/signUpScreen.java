package ca.gc.inspection.scoop;

import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

public class signUpScreen extends AppCompatActivity {

    // to go back to the splash screen when the back button is pressed
    public void goBackToSplash(View v) {
        startActivity(new Intent(v.getContext(), splashScreen.class));
        finish();
    }

    // if sign up was successful -> to to the main screen
    public void signUpSuccessful(View v) {
        startActivity(new Intent(v.getContext(), mainScreen.class));
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_up_screen);

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
<<<<<<< Updated upstream
=======
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
        RegisterController.registerUser(getApplicationContext(), email, password, firstName, lastName, this);
    }
>>>>>>> Stashed changes

    }
}
