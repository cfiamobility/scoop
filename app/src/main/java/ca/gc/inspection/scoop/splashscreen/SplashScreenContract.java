package ca.gc.inspection.scoop.splashscreen;

import android.app.Activity;
import android.content.Context;

import ca.gc.inspection.scoop.base.BasePresenter;
import ca.gc.inspection.scoop.base.BaseView;

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
    }

    /**
     * Interface implemented by SplashScreenPresenter
     */
    interface Presenter extends BasePresenter {
        void goToMainScreen(Context context, Activity activity);

        void loginUser(final String email, final String password, final Context context, final Activity activity);
    }
}
