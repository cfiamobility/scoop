package ca.gc.inspection.scoop.settings;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;

import ca.gc.inspection.scoop.util.NetworkUtils;

import static com.google.gson.internal.$Gson$Preconditions.checkNotNull;

public class SettingsPresenter implements SettingsContract.Presenter {

    private SettingsInteractor mInteractor;
    private SettingsContract.View mView;
    private Context mActivityContext;
    private HashMap<String, String> mSettingsMap;

    SettingsPresenter(@NonNull SettingsContract.View view, NetworkUtils networkUtils, Context context) {
        mInteractor = new SettingsInteractor(this, networkUtils);
        mView = checkNotNull(view);
        mActivityContext = context;
        mSettingsMap = new HashMap<String, String>();
    }


    @Override
    public void updateSettings() {
        mInteractor.updateSettings(mSettingsMap);
    }

    @Override
    public void loadSettings() {
        mInteractor.loadSettings();
    }

    public void updateLocalPreferences() {
        //TODO: update local preferences
        //SharedPreferences prefs = mActivityContext.getSharedPreferences("ca.gc.inspection.scoop", Context.MODE_PRIVATE);
        //prefs.edit().putString(settingType, String.valueOf(value)).apply();
    }

    @Override
    public void updateSetting(String settingType, boolean value) {
        mSettingsMap.put(settingType, String.valueOf(value));
    }


    public void setSettings(JSONArray response) throws JSONException {
        Log.d("THIS HAD BEEN", String.valueOf(response.getJSONObject(0)));


        JSONObject setting = response.getJSONObject(0);
        Iterator<String> keys = setting.keys();


        while(keys.hasNext()){
            String settingKey = keys.next();
            if (settingKey.equals("userid")){ continue;}
            mView.setSetting(settingKey, setting.getString(settingKey));
            Log.d("settingKey", settingKey);
            Log.d("settingValue", setting.getString(settingKey));
        }


            //mView.addBuilding(jsonBuilding.toString());


        mView.setUpRecyclerView();
    }
}
