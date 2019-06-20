package ca.gc.inspection.scoop.splashscreen;

import android.app.Activity;
import android.content.Context;

import ca.gc.inspection.scoop.base.BasePresenter;
import ca.gc.inspection.scoop.base.BaseView;
import ca.gc.inspection.scoop.util.NetworkUtils;

/**
 * This interface is the contract between the splash screen view (SplashScreenActivity) and the splash screen presenter (SplashScreenPresenter)
 */
public interface SplashScreenContract {

    /**
     * Interaface implemented by SplashScreenActivity
     */
    interface View extends BaseView<Presenter> {
        void setPasswordLayoutError(String error);

        void setEmailLayoutError(String error);

        void storePreferences(String userid, String response);
    }

    /**
     * Interface implemented by SplashScreenPresenter
     */
    interface Presenter extends BasePresenter {

        void loginUser(NetworkUtils network, final String email, final String password);

        boolean validLogin(String response);

        void storePreferences(String userid, String response);
    }
}
