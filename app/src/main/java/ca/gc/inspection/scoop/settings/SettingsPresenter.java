package ca.gc.inspection.scoop.settings;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;

import ca.gc.inspection.scoop.util.NetworkUtils;

import static com.google.gson.internal.$Gson$Preconditions.checkNotNull;

/**
 * Presenter for the settings activity
 */
public class SettingsPresenter implements SettingsContract.Presenter {

    private SettingsInteractor mInteractor;
    private SettingsContract.View mView;
    private Context mActivityContext;
    private HashMap<String, String> mSettingsMap; // Will contain ONLY settings which have been edited. Setting which the user didn't touch will not be in this list

    SettingsPresenter(@NonNull SettingsContract.View view, NetworkUtils networkUtils, Context context) {
        mInteractor = new SettingsInteractor(this, networkUtils);
        mView = checkNotNull(view);
        mActivityContext = context;
        mSettingsMap = new HashMap<String, String>();

    }

    /**
     * Sends all edited setting values to the server database
     */
    @Override
    public void updateSettings() {
        mInteractor.updateSettings(mSettingsMap);
    }

    /**
     * loads settings from the server
     * - called upon initially opening the settings activity
     */
    @Override
    public void loadSettings() {
        mInteractor.loadSettings();
    }

    /**
     * Gets user settings from the database for use in updating the local preferences
     */
    @Override
    public void updateLocalPreferences() {
        mInteractor.getUserSettings();
    }

    /**
     * Update the local preferences using settings fetched from the database
     * @param response // JSONArray containing all user settings fetched from the server
     * @throws JSONException
     */
    public void publishLocalPreferences(JSONArray response) throws JSONException {
        HashMap<String, String> prefsMap = new HashMap<>();
        JSONObject setting = response.getJSONObject(0);
        Iterator<String> keys = setting.keys();
        while(keys.hasNext()){
            String settingKey = keys.next();
            if (settingKey.equals("userid")){ continue;}
            prefsMap.put(settingKey, setting.getString(settingKey));
        }
        mView.publishLocalPreferences(prefsMap);
    }

    /**
     * When a user edits a setting, this method stores the updated value to the setting map to be later sent to the server
     * @param settingType unique setting id taken from Settings.java
     * @param value updated settings value
     */
    @Override
    public void updateSetting(String settingType, String value) {
        mSettingsMap.put(settingType, value);
    }


    /**
     * After fetching initial user setting values from the server, this method sends the values to the view to be used by the recycler view
     * @param response JSONArray containing all of the users settings and values
     * @throws JSONException
     */
    public void setSettings(JSONArray response) throws JSONException {
        JSONObject setting = response.getJSONObject(0);
        Iterator<String> keys = setting.keys();

        while(keys.hasNext()){
            String settingKey = keys.next();
            if (settingKey.equals("userid")){ continue;}
            mView.setSetting(settingKey, setting.getString(settingKey));
            Log.d("settingKey", settingKey);
            Log.d("settingValue", setting.getString(settingKey));
        }

        mView.setUpRecyclerView();
    }
}
