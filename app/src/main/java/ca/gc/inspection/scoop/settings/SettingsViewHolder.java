package ca.gc.inspection.scoop.settings;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import ca.gc.inspection.scoop.R;

class SettingsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    TextView settingTextView;
    Switch toggle;
    SettingsAdapter.ItemClickListener mClickListener;
    SettingsAdapter.SwitchToggleListener mSwitchToggleListener;
    String mSettingType;


    public SettingsViewHolder(@NonNull View itemView, SettingsAdapter.ItemClickListener itemClickListener, SettingsAdapter.SwitchToggleListener switchToggleListener) {
        super(itemView);

        settingTextView = itemView.findViewById(R.id.SettingsTextView);
        mClickListener = itemClickListener;
        itemView.setOnClickListener(this);

        mSwitchToggleListener = switchToggleListener;
        toggle = itemView.findViewById(R.id.setting_switch);
        toggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mSwitchToggleListener.onSwitchToggle(itemView, getAdapterPosition(), mSettingType, isChecked);
            }
        });


    }

    @Override
    public void onClick(View view) {
        if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
    }

    public void setType(String type){
        mSettingType = type;
    }


    public void setValue(boolean value) { toggle.setChecked(value); }
}
