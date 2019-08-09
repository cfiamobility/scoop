package ca.gc.inspection.scoop.settings;

import java.util.HashMap;

import ca.gc.inspection.scoop.base.BasePresenter;
import ca.gc.inspection.scoop.base.BaseView;

/**
 * Contract between SettingsActivity & SettingsPresenter
 */
public interface SettingsContract {
    interface View extends BaseView<SettingsContract.Presenter> {

        void setSetting(String settingKey, String string);

        void setUpRecyclerView();

        void publishLocalPreferences(HashMap<String, String> prefsMap);
    }

    interface Presenter extends BasePresenter {

        void updateSetting(String settingType, String value);

        void updateSettings();

        void loadSettings();

        void updateLocalPreferences();
    }
}
