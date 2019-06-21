package ca.gc.inspection.scoop.splashscreen;

import ca.gc.inspection.scoop.util.NetworkUtils;

/**
 * - The SplashScreenPresenter provides methods for communicating and validating login information
 *   between the View and the Model
 * - This is the Presenter for the Splash Screen action case
 */
public class SplashScreenPresenter implements SplashScreenContract.Presenter {

    private SplashScreenContract.View mSplashScreenView;
    private SplashScreenInteractor mSplashScreenInteractor;

    /**
     * Constructor that takes in the respective action case View. Calls the setPresenter
     * method to associate itself to the View and constructs the respective Interactor.
     * @param view View that is linked to the respective Presenter
     */
    SplashScreenPresenter(SplashScreenContract.View view){
        mSplashScreenView = view;
        mSplashScreenView.setPresenter(this);

        mSplashScreenInteractor = new SplashScreenInteractor(this);
    }

       /**
     * Invoked by the Interactor to validate the inputted login information with the data
     * in the server
     * @param response Provided by the network/middle tier upon checking the database for
     *                 a matching email and password
     * @return FALSE if no email exists or password does not match given email, otherwise TRUE
     */
     boolean validLogin(String response){
        if (response.contains("Invalid Email")) { // if the user entered an invalid email
            mSplashScreenView.setEmailLayoutError("Invalid Email");
            return false;
        } else if (response.contains("Incorrect Password")) { // if the user entered an incorrect password
            mSplashScreenView.setPasswordLayoutError("Incorrect Password");
            return false;
        } else {
            return true;
        }
    }

    /**
     * Provides login information from the View and passes it to the Interactor
     * @param network Allows login info to be added to singleton request queue
     * @param email User inputted email to be validated
     * @param password User inputted password to be validated
     */
    public void loginUser(NetworkUtils network, final String email, final String password){
        mSplashScreenInteractor.loginUser(network, email, password);
    }

    /**
     * Provides the userid and response token from the Interactor and passes it to the View
     * @param userid Unique user ID
     * @param response Unique response token associated each session
     */
    public void storePreferences (String userid, String response){
        mSplashScreenView.storePreferences(userid, response);
    }

}
