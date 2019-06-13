package ca.gc.inspection.scoop;

import android.content.Intent;
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

import ca.gc.inspection.scoop.SplashScreen.SplashScreenActivity;

public class SignUpActivity extends AppCompatActivity {

    // Initializing the buttons, edit texts, and string variables
    String firstNameText, lastNameText, emailText, passwordText;
    Button registerBTN;
    TextInputEditText firstNameET, lastNameET, emailET, passwordET;
    TextInputLayout firstNameLayout, lastNameLayout, emailLayout, passwordLayout; // needed to set the error messages for the edit text

    // to go back to the splash screen when the back button is pressed
    public void goBackToSplash(View v) {
        startActivity(new Intent(v.getContext(), SplashScreenActivity.class));
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

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

                // Using the user-inputted information to register the user via this method
                registerUser(firstNameText, lastNameText, emailText, passwordText);
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

    // [INPUT]:         The first name, last name, email, and password that the user entered is passed into this function
    // [PROCESSING]:    Checks to see if the strings are valid (Ex. of invalid strings: empty, email isn't @canada.ca, password doesn't have uppercase, lowercase, number, or symbol)
    // [PROCESSING]:    Makes request to NodeJS server and sends info to NodeJS as a Map<String, String>
    // [OUTPUT]:        Toasts to notify of error or success.
    private void registerUser(final String firstName, final String lastName, final String email, final String password) {
        // Checks for string validity
        if (TextUtils.isEmpty(firstName)) { // first name field is empty
            firstNameLayout.setError("Please enter a first name.");
            return;
        } else if (TextUtils.isEmpty(lastName)) { // last name field is empty
            lastNameLayout.setError("Please enter a last name");
            return;
        } else if (TextUtils.isEmpty(email) || !email.contains("@canada.ca")) { // if the email field is empty or if they do not enter a @canada.ca email
            emailLayout.setError("Please enter valid email.");
            return;
        } else if (TextUtils.isEmpty(password) || password.length() < 8 || !isValidPassword(password)) { // is the password field is empty or is less than 8 characters or is not valid
            passwordLayout.setError("Password is invalid. Ensure your password contains at least one of the following: Uppercase Letter, Lowercase Letter, Number, Symbol.");
            return;
        }

        SignUpController.registerUser(getApplicationContext(), email, password, firstName, lastName, this);
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
