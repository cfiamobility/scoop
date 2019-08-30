package ca.gc.inspection.scoop.splashscreen;

import org.json.JSONArray;
import org.json.JSONException;

import ca.gc.inspection.scoop.base.BasePresenter;
import ca.gc.inspection.scoop.base.BaseView;
import ca.gc.inspection.scoop.util.NetworkUtils;

/**
 * This interface is the contract that communicates the methods between the Splash
 * Screen View (SplashScreenActivity) and the Splash Screen Presenter (SplashScreenPresenter)
 */
public interface SplashScreenContract {

    /**
     * View interface implemented by SplashScreenActivity
     */
    interface View extends BaseView<Presenter> {
        void setPasswordLayoutError(String error);

        void setEmailLayoutError(String error);

        void storePreferences(String userid, String token, JSONArray settings) throws JSONException;
    }

    /**
     * Presenter interface implemented by SplashScreenPresenter
     */
    interface Presenter extends BasePresenter {

        void loginUser(NetworkUtils network, final String email, final String password);

    }
}
