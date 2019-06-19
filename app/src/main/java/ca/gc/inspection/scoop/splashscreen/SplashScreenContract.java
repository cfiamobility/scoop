package ca.gc.inspection.scoop.splashscreen;

import android.app.Activity;
import android.content.Context;

import ca.gc.inspection.scoop.base.BasePresenter;
import ca.gc.inspection.scoop.base.BaseView;

public interface SplashScreenContract {

    interface View extends BaseView<SplashScreenContract.Presenter> {

    }

    interface Presenter extends BasePresenter {

        void loginUser(final String email, final String password, final Context context, final Activity activity);

    }
}
