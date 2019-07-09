package ca.gc.inspection.scoop.profile;

import java.util.HashMap;

import ca.gc.inspection.scoop.base.BasePresenter;
import ca.gc.inspection.scoop.base.BaseView;

interface ProfileContract {

    interface View extends BaseView<Presenter>{
        void setProfileInfoFields(HashMap<String, String> profileInfoFields);

    }

    interface Presenter extends BasePresenter{
        void getUserInfo();
        void getOtherUserInfo(String userid);
    }

}
