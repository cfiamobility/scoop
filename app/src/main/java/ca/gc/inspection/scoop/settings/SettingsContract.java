package ca.gc.inspection.scoop.settings;

import java.util.HashMap;

import ca.gc.inspection.scoop.base.BasePresenter;
import ca.gc.inspection.scoop.base.BaseView;

public interface SettingsContract {
    interface View extends BaseView<SettingsContract.Presenter> {

        void setSetting(String settingKey, String string);

        void setUpRecyclerView();
    }

    interface Presenter extends BasePresenter {

        void updateSetting(String settingType, boolean value);

        void updateSettings();

        void loadSettings();
    }
}
