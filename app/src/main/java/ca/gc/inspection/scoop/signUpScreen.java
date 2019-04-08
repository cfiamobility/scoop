package ca.gc.inspection.scoop;

import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

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

    // if sign up was successful -> to to the main screen
    public void signUpSuccessful(View v) {
        startActivity(new Intent(v.getContext(), mainScreen.class));
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
                firstNameText = capitalizeFirstLetter(firstNameET.getText().toString());
                lastNameText = capitalizeFirstLetter(lastNameET.getText().toString());
                emailText = (emailET.getText().toString()).toLowerCase();
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
        Controller.registerUser(getApplicationContext(), email, password, firstName, lastName, this);
    }

    private String capitalizeFirstLetter(String word) {
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
