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

    }
}
