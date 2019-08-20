package ca.gc.inspection.scoop.editprofile;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Map;

import ca.gc.inspection.scoop.base.BasePresenter;
import ca.gc.inspection.scoop.base.BaseView;

/**
 * This interface is the contract that communicates the methods between the Edit
 * Profile View (EditProfileActivity) and the Edit Profile Presenter (EditProfilePresenter)
 */
public interface EditProfileContract {

    /**
     * View interface implemented by EditProfileFragment
     */
    interface View extends BaseView<Presenter> {

        void setPositionETAdapter(ArrayList<String> positionAutoComplete);

        void setDivisionETAdapter(ArrayList<String> divisionsAutoComplete);

        void setInitialFill(JSONObject response);

        void onProfileUpdated(boolean success);
    }

    /**
     * Presenter interface implemented by EditProfilePresenter
     */
    interface Presenter extends BasePresenter {

        void initialFill();

        void getPositionAutoCompleteFromDB(String positionChangedCapped);

        void getDivisionAutoCompleteFromDB(String divisionChangedCapitalized);

        void updateUserInfo(Map<String, String> params);
    }
}
