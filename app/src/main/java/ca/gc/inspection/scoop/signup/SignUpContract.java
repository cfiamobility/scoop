package ca.gc.inspection.scoop.signup;

import ca.gc.inspection.scoop.base.BasePresenter;
import ca.gc.inspection.scoop.base.BaseView;
import ca.gc.inspection.scoop.util.NetworkUtils;

/**
 * This interface is the contract that communicates the methods between the Sign Up View
 * (SignUpActivity) and the Sign Up Presenter (SignUpPresenter)
 */
public interface SignUpContract {

    /**
     * View interface implemented by SignUpActivity
     */
    interface View extends BaseView<SignUpContract.Presenter> {
        void storePreferences(String userid, String response);
    }

    /**
     * Presenter interface implemented by SignUpPresenter
     */
    interface Presenter extends BasePresenter {

        void registerUser(final NetworkUtils network, final String email, final String password, final String firstName, final String lastName);
    }

}
