package ca.gc.inspection.scoop.signup;

import android.app.Activity;
import android.content.Context;

import ca.gc.inspection.scoop.base.BasePresenter;
import ca.gc.inspection.scoop.base.BaseView;
import ca.gc.inspection.scoop.util.NetworkUtils;

public interface SignUpContract {

    /**
     * Interface implemented by SignUpActivity
     */
    interface View extends BaseView<SignUpContract.Presenter> {
        void storePreferences(String userid, String response);
    }

    /**
     * Interface implemented by SignUpPresenter
     */
    interface Presenter extends BasePresenter {
        void registerUser(final NetworkUtils network, final String email, final String password, final String firstName, final String lastName);
        // called in interactor
        void storePreferences(String userid, String response);
    }

}
