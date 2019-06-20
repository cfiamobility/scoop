package ca.gc.inspection.scoop.signup;

import ca.gc.inspection.scoop.util.NetworkUtils;

class SignUpPresenter implements SignUpContract.Presenter{

	private SignUpContract.View mSignUpView;
	private SignUpInteractor mSignUpInteractor;


	public SignUpPresenter (SignUpContract.View view){
		mSignUpView = view;
		mSignUpView.setPresenter(this);

		mSignUpInteractor = new SignUpInteractor(this);
	}

	protected static String capitalizeFirstLetter(String word) {
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

	/**
	 *
	 * @param password
	 * @return
	 */
	protected static boolean isValidPassword(String password) {
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

	public void registerUser(final NetworkUtils network, final String email, final String password, final String firstName, final String lastName){
		mSignUpInteractor.registerUser(network, email, password, firstName, lastName);
	}

	public void storePreferences(String userid, String response){
		mSignUpView.storePreferences(userid, response);
	}
}
