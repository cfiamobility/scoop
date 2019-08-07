package ca.gc.inspection.scoop.settings;

import ca.gc.inspection.scoop.*;
import ca.gc.inspection.scoop.util.NetworkUtils;

import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;

import static com.google.gson.internal.$Gson$Preconditions.checkNotNull;

public class SettingsActivity extends AppCompatActivity implements SettingsAdapter.ItemClickListener, SettingsAdapter.SwitchToggleListener, SettingsContract.View {

    private SettingsContract.Presenter mPresenter;
    RecyclerView recyclerView;
    ProgressBar progressBar;
    SettingsAdapter adapter;
    ArrayList<Object> settings;
    HashMap<String, String> fetchedSettingValues = new HashMap<>();

    public void finishActivity(View view) {
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        setPresenter(new SettingsPresenter(this, NetworkUtils.getInstance(this), getApplicationContext()));
        mPresenter.loadSettings();

        // set the system status bar color
        getWindow().setStatusBarColor(ContextCompat.getColor(getApplicationContext(), R.color.primary_dark));

        progressBar = findViewById(R.id.progressBar);

        /**
        settings = new ArrayList<>();
        settings.add(new SettingWithSwitch("Push Notifications", Settings.PUSH_NOTIFICATION_SETTING, Boolean.valueOf(fetchedSettingValues.get(Settings.PUSH_NOTIFICATION_SETTING))));
        //settings.add(new SettingWithSwitch("Push Notificationsz"));
        // TODO: populate this list

        // set up recycler view
        recyclerView = findViewById(R.id.settingsRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new SettingsAdapter(this, settings);
        adapter.setClickListener(this);
        adapter.setSwitchToggleListener(this);
        recyclerView.setAdapter(adapter);
         **/

    }

    /**
     * Listener that is called when the user clicks a building address
     * Passes the building address and it's respective ID back to the edit profile activity
     * @param view
     * @param position
     */
    @Override
    public void onItemClick(View view, int position) {
        // TODO: do stuff
        finish();
    }


    @Override
    public void onSwitchToggle(View view, int position, String settingType, boolean value) {
        //Toast.makeText(this, String.valueOf(value), Toast.LENGTH_SHORT).show();
        mPresenter.updateSetting(settingType, value);
    }

    @Override
    public void setPresenter(@NonNull SettingsContract.Presenter presenter) {
        mPresenter = checkNotNull(presenter);
    }

    @Override
    public void setSetting(String settingKey, String value) {
        fetchedSettingValues.put(settingKey, value);
    }

    @Override
    public void setUpRecyclerView() {
        progressBar.setVisibility(View.GONE);
        settings = new ArrayList<>();
        settings.add(new SettingWithSwitch("Push Notifications", Settings.PUSH_NOTIFICATION_SETTING, Boolean.valueOf(fetchedSettingValues.get(Settings.PUSH_NOTIFICATION_SETTING))));
        //settings.add(new SettingWithSwitch("Push Notificationsz"));
        // TODO: populate this list

        // set up recycler view
        recyclerView = findViewById(R.id.settingsRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new SettingsAdapter(this, settings);
        adapter.setClickListener(this);
        adapter.setSwitchToggleListener(this);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onDestroy() {
        mPresenter.updateSettings();
        super.onDestroy();
    }


    public static class SettingWithSwitch{
        String mLabel;
        String mType;
        Boolean mValue;
        SettingWithSwitch(String label, String type, boolean value){
            mLabel = label;
            mType = type;
            mValue = value;
        }
        public String getText(){
            return mLabel;
        }
        public String getType() { return mType; }
        public boolean getValue() { return mValue; }
    }


}
