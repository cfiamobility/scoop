package ca.gc.inspection.scoop.splashscreen;

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

        void storePreferences(String userid, String response);
    }

    /**
     * Presenter interface implemented by SplashScreenPresenter
     */
    interface Presenter extends BasePresenter {

        void loginUser(NetworkUtils network, final String email, final String password);

    }
}
