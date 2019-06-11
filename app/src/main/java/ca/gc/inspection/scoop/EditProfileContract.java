package ca.gc.inspection.scoop;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import ca.gc.inspection.scoop.base.BasePresenter;
import ca.gc.inspection.scoop.base.BaseView;

public interface EditProfileContract {
    interface View extends BaseView<Presenter> {

        void setPositionETAdapter(ArrayList<String> positionAutoComplete);

        void setBuildingETAdapter(ArrayList<String> buildingsAutoComplete);

        void setDivisionETAdapter(ArrayList<String> divisionsAutoComplete);

        void setInitialFill(JSONObject response);

        void setAddressSuggestionList(ArrayList<String> cityAL, ArrayList<String> provinceAL);
    }

    interface Presenter extends BasePresenter {

        void initialFill(MySingleton instance);

        void getPositionAutoCompleteFromDB(MySingleton instance, String positionChangedCapped);

        void getAddressAutoCompleteFromDB(MySingleton instance, String addressChangedCapitalized);

        void getDivisionAutoCompleteFromDB(MySingleton instance, String divisionChangedCapitalized);
    }
}
