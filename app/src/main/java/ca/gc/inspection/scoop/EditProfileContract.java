package ca.gc.inspection.scoop;

import org.json.JSONArray;

import java.util.ArrayList;

import ca.gc.inspection.scoop.base.BasePresenter;
import ca.gc.inspection.scoop.base.BaseView;

public interface EditProfileContract {
    interface View extends BaseView<Presenter> {

        void setPositionETAdapter(ArrayList<String> positionAutoComplete);
    }

    interface Presenter extends BasePresenter {

        void initialFill(MySingleton instance);

        void positionAutoComplete(MySingleton instance, String positionChangedCapped);
    }
}
