package ca.gc.inspection.scoop.settings;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import ca.gc.inspection.scoop.R;

/**
 * View holder for use with SettingWithSwitch objects
 */
class SettingWithSwitchViewHolder extends RecyclerView.ViewHolder {
    TextView settingTextView; // Label
    Switch toggle;
    SettingsAdapter.SwitchToggleListener mSwitchToggleListener;
    String mSettingType;


    public SettingWithSwitchViewHolder(@NonNull View itemView, SettingsAdapter.SwitchToggleListener switchToggleListener) {
        super(itemView);

        settingTextView = itemView.findViewById(R.id.SettingsTextView);

        // set up the switch
        mSwitchToggleListener = switchToggleListener;
        toggle = itemView.findViewById(R.id.setting_switch);
        toggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mSwitchToggleListener.onSwitchToggle(itemView, getAdapterPosition(), mSettingType, isChecked); // call back to SettingsActivity upon switch toggle
            }
        });


    }

    public void setType(String type){
        mSettingType = type;
    }

    public void setValue(boolean value) { toggle.setChecked(value); }
}
