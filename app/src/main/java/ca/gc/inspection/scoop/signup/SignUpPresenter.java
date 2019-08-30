package ca.gc.inspection.scoop.signup;

import org.json.JSONArray;
import org.json.JSONException;

import ca.gc.inspection.scoop.util.NetworkUtils;

/**
 *  - The SignUpPresenter provides methods for validating user inputted registration text
 *    in the View and communicating registration information between the View and Model
 *  - This is the Presenter for the Sign Up action case
 */
class SignUpPresenter implements SignUpContract.Presenter{

	private SignUpContract.View mSignUpView;
	private SignUpInteractor mSignUpInteractor;

    /**
     * Constructor that takes in the respective action case View. Calls the setPresenter
     * method to associate itself to the View and constructs the respective Interactor.
     * @param view View that is linked to the respective Presenter
     */
    SignUpPresenter (SignUpContract.View view){
		mSignUpView = view;
		mSignUpView.setPresenter(this);

		mSignUpInteractor = new SignUpInteractor(this);
	}

	/**
	 * Static method invoked by View to verify that user inputted password meets requirements
	 * @param password User inputted password
	 * @return TRUE if contains at least 1 Uppercase, 1 Lowercase, 1 Number, and 1 Non-Alphanumeric character
     *         otherwise, FALSE
	 */
	 static boolean isValidPassword(String password) {
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

    /**
     * Provides registration information from the View and passes it to the Interactor
     * @param network Allows login info to be added to singleton request queue
     * @param email User inputted email after being validated
     * @param password User inputted password after being validated
     * @param firstName User inputted first name after being capitalized
     * @param lastName User inputted last name after being capitalized
     */
	public void registerUser(final NetworkUtils network, final String email, final String password, final String firstName, final String lastName){
		mSignUpInteractor.registerUser(network, email, password, firstName, lastName);
	}

    /**
     * Provides the userid and response token from the Interactor and passes it to the View
     * @param userid Unique user ID
     * @param token Unique response token associated each session
     */
	public void storePreferences(String userid, String token, JSONArray settings) throws JSONException {
		mSignUpView.storePreferences(userid, token, settings);
	}

	public void setErrorMessage(String response){
		if (response.equals("ERROR_EMAIL_FORMAT")){
			mSignUpView.displayErrorMessage("This email does not appear to be an official government email.");
		} else if (response.equals("ERROR_EMAIL_EXISTS")){
			mSignUpView.displayErrorMessage("An account already exists with this email.");
		}
	}

}
