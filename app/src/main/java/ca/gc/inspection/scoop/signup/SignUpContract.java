package ca.gc.inspection.scoop.signup;

import ca.gc.inspection.scoop.base.BasePresenter;
import ca.gc.inspection.scoop.base.BaseView;

public interface SignUpContract {
    interface View extends BaseView<SignUpContract.Presenter> {


    }

    interface Presenter extends BasePresenter {

        boolean isValidPassword(String password);

        String capitalizeFirstLetter(String word);

    }
}
