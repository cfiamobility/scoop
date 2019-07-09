package ca.gc.inspection.scoop.profile;

import java.util.HashMap;

import ca.gc.inspection.scoop.base.BasePresenter;
import ca.gc.inspection.scoop.base.BaseView;


/**
 * This interface is the contract that communicates the methods between the Profile Views
 * (ProfileFragment and OtherUserFragment) and the Profile Presenter (ProfilePresenter)
 */
interface ProfileContract {

    /**
     * View interface implemented by ProfileFragment and OtherUserFragment
     */
    interface View extends BaseView<Presenter>{
        void setProfileInfoFields(HashMap<String, String> profileInfoFields);

    }

    /**
     * Presenter interface implemented by ProfilePresenter
     */
    interface Presenter extends BasePresenter{
        void getUserInfo(String userid);
    }

}
