package ca.gc.inspection.scoop.settings;

import ca.gc.inspection.scoop.*;
import ca.gc.inspection.scoop.util.NetworkUtils;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static com.google.gson.internal.$Gson$Preconditions.checkNotNull;

/**
 * View class for the settings
 * To add a setting: add new settingItem object to the settingsList
 */
public class SettingsActivity extends AppCompatActivity implements SettingsAdapter.ItemClickListener, SettingsAdapter.SwitchToggleListener, SettingsAdapter.SpinnerListener ,SettingsContract.View {
    void generateSettingsList(){
        //////////////////////////////////////////////////////////////
        //  LIST CONTAINING ALL SETTING OBJECTS                     //
        //                                                          //
        //    ;;;;;                                                 //
        //    ;;;;;                                                 //
        //    ;;;;;      add to this list to create new settings    //
        //  ..;;;;;..                                               //
        //   ':::::'                                                //
        //     ':`                                                  //
        //////////////////////////////////////////////////////////////

        settingsList = new ArrayList<>(Arrays.asList( // add to this list to create new settings
                new SettingWithSwitchAndSubLabel(getString(R.string.push_notifications), getString(R.string.push_notifications_description), Settings.PUSH_NOTIFICATION_SETTING), // PUSH NOTIFICATION SETTING
                new SettingWithSpinner(getString(R.string.Languages), Settings.LANGUAGE_SETTING, new ArrayList<String>(Arrays.asList(getResources().getStringArray(R.array.languages_array))))  // LANGUAGE SETTING
        ));
    }

    ArrayList<SettingsItem> settingsList;

    private SettingsContract.Presenter mPresenter;
    RecyclerView recyclerView;
    ProgressBar progressBar;
    SettingsAdapter adapter;
    HashMap<String, String> fetchedSettingValues = new HashMap<>(); // contains settings and values initially fetched from the server upon opening the settings view

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        generateSettingsList();

        setPresenter(new SettingsPresenter(this, NetworkUtils.getInstance(this), getApplicationContext()));
        mPresenter.loadSettings(); // fetch initial setting values from server upon opening the view

        // set the system status bar color
        getWindow().setStatusBarColor(ContextCompat.getColor(getApplicationContext(), R.color.primary_dark));

        progressBar = findViewById(R.id.progressBar);

    }

    public void finishActivity(View view) {
        finish();
    }

    /**
     * Listener for clicks used by any settings items which implement clicks
     * @param view
     * @param position
     */
    @Override
    public void onItemClick(View view, int position) {
        // click listener not used yet
    }

    /**
     * Listener for toggling of switches used by any settings items which use switches
     * @param view
     * @param position
     * @param settingType
     * @param value
     */
    @Override
    public void onSwitchToggle(View view, int position, String settingType, boolean value) {
        mPresenter.updateSetting(settingType, String.valueOf(value));
    }

    /**
     * Listener for spinners used by any setting which uses spinners
     * @param view
     * @param position
     * @param settingType
     * @param value
     */
    @Override
    public void onSpinnerStateChange(View view, int position, String settingType, int value) {
        mPresenter.updateSetting(settingType, String.valueOf(value));
    }

    /**
     * Sets the presenter associated with this view
     * @param presenter
     */
    @Override
    public void setPresenter(@NonNull SettingsContract.Presenter presenter) {
        mPresenter = checkNotNull(presenter);
    }

    /**
     * Sets settings to values fetched from the server (used only during the pre-setup of the view)
     * @param settingKey setting key
     * @param value setting value
     */
    @Override
    public void setSetting(String settingKey, String value) {
        fetchedSettingValues.put(settingKey, value);
    }

    /**
     * Sets up the recycler view of settings after their values have already been fetched from the server
     */
    @Override
    public void setUpRecyclerView() {
        progressBar.setVisibility(View.GONE);

        // set initial values of settings
        for (int i = 0; i < settingsList.size(); i++){
            SettingsItem settingItem = settingsList.get(i);
            String type = settingItem.getType();
            settingItem.setValue(fetchedSettingValues.get(type));
        }

        // set up recycler view
        recyclerView = findViewById(R.id.settingsRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new SettingsAdapter(this, settingsList);

        // SET ALL LISTENERS HERE
        adapter.setClickListener(this);
        adapter.setSwitchToggleListener(this);
        adapter.setSpinnerListener(this);

        recyclerView.setAdapter(adapter);
    }

    /**
     * Before closing the settings view, publish the updated settings to the users's local preferences to be accesses later
     * @param prefsMap
     */
    @Override
    public void publishLocalPreferences(HashMap<String, String> prefsMap) {
        SharedPreferences prefs =  getApplicationContext().getSharedPreferences("ca.gc.inspection.scoop", Context.MODE_PRIVATE);

        for (Map.Entry<String, String> entry : prefsMap.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            prefs.edit().putString(key, value).apply();
        }
    }

    /**
     * upon destroying the settings activity, push new settings to the server database and update the local preferences
     */
    @Override
    public void onDestroy() {
        progressBar.setVisibility(View.VISIBLE);
        mPresenter.updateSettings();
        mPresenter.updateLocalPreferences();
        super.onDestroy();
    }

    /**
     * Super class which all settings object's MUST inherit from
     */
    public static class SettingsItem{
        private String mType;   // unique identifier of the setting. Value taken from Settings.java
        private String mValue;  // initial value of the setting
        public String getType() { return mType; }
        public void setValue(String value) {mValue = value;}
    }

    /**
     * Class for all setting objects which have switches
     */
    public static class SettingWithSwitch extends SettingsItem{
        private String mLabel; // Label used for the setting in the UI
        SettingWithSwitch(String label, String type){
            mLabel = label;
            super.mType = type;
        }
        public String getText(){ // Returns the label for this setting
            return mLabel;
        }
        public boolean getValue() { return Boolean.valueOf(super.mValue); }
    }

    /**
     * Class for all setting objects which have switches and a sub label
     */
    public static class SettingWithSwitchAndSubLabel extends SettingsItem{
        private String mLabel; // Label used for the setting in the UI
        private String mSubLabel; // Sub label used for the settings in the UI
        SettingWithSwitchAndSubLabel(String label, String sublabel, String type){
            mLabel = label;
            mSubLabel = sublabel;
            super.mType = type;
        }
        public String getText(){ // Returns the label for this setting
            return mLabel;
        }
        public String getSubText() {return mSubLabel;}
        public boolean getValue() { return Boolean.valueOf(super.mValue); }
    }

    /**
     * Class for all setting objects which have spinners
     */
    public static class SettingWithSpinner extends SettingsItem{
        private String mLabel; // Label used for the setting in the UI
        private ArrayList<String> mOptions; // List of drop down items for the spinner of this item
        SettingWithSpinner(String label, String type, ArrayList<String> options){
            mLabel = label;
            super.mType = type;
            mOptions = options;
        }
        public String getText(){ // Returns the label for this setting
            return mLabel;
        }
        public int getValue() { return Integer.valueOf(super.mValue); }
        public ArrayList<String> getOptions() {return mOptions;} // returns all drop down items for the spinner
    }


}
