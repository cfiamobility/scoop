package ca.gc.inspection.scoop;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Map;

import ca.gc.inspection.scoop.base.BasePresenter;
import ca.gc.inspection.scoop.base.BaseView;

public interface EditProfileContract {
    interface View extends BaseView<Presenter> {

        void setPositionETAdapter(ArrayList<String> positionAutoComplete);

        void setBuildingETAdapter(ArrayList<String> buildingsAutoComplete);

        void setDivisionETAdapter(ArrayList<String> divisionsAutoComplete);

        void setInitialFill(JSONObject response);

        void setAddressSuggestionList(ArrayList<String> cityAL, ArrayList<String> provinceAL);

        void finishUpdateUserInfo();
    }

    interface Presenter extends BasePresenter {

        void initialFill(NetworkUtils instance);

        void getPositionAutoCompleteFromDB(NetworkUtils instance, String positionChangedCapped);

        void getAddressAutoCompleteFromDB(NetworkUtils instance, String addressChangedCapitalized);

        void getDivisionAutoCompleteFromDB(NetworkUtils instance, String divisionChangedCapitalized);

        void updateUserInfo(NetworkUtils network, Map<String, String> params);
    }
}
