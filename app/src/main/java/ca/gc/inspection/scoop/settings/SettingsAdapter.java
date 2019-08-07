package ca.gc.inspection.scoop.settings;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ca.gc.inspection.scoop.R;

class SettingsAdapter extends RecyclerView.Adapter<SettingsViewHolder> {

    private final int settingWithSwitch = 0;

    private static List<Object> mData;
    private LayoutInflater mInflator;
    private ItemClickListener mClickListener;
    private SwitchToggleListener mSwitchToggleListener;

    SettingsAdapter(Context context, List<Object> data){
        this.mInflator = LayoutInflater.from(context);
        this.mData = data;
    }

    @NonNull
    @Override
    public SettingsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        SettingsViewHolder viewHolder;

        switch (i){
            case settingWithSwitch:
                View view = mInflator.inflate(R.layout.settings_row_with_switch, parent, false);
                viewHolder = new SettingsViewHolder(view, mClickListener, mSwitchToggleListener);
                break;
            default:
                viewHolder = null;
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull SettingsViewHolder settingsViewHolder, int i) {
        Object setting = mData.get(i);
        switch (settingsViewHolder.getItemViewType()){
            case settingWithSwitch:
                SettingsActivity.SettingWithSwitch settingWithSwitchObj = (SettingsActivity.SettingWithSwitch) setting;
                settingsViewHolder.settingTextView.setText(settingWithSwitchObj.getText());
                settingsViewHolder.setType(settingWithSwitchObj.getType());
                settingsViewHolder.setValue(settingWithSwitchObj.getValue());
        }
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (getItem(position) instanceof SettingsActivity.SettingWithSwitch){
            return settingWithSwitch;
        }
        return -1; // error
    }

    Object getItem(int id){
        return mData.get(id);
    }

    void setClickListener(ItemClickListener itemClickListener){
        this.mClickListener = itemClickListener;
    }

    void setSwitchToggleListener(SwitchToggleListener switchToggleListener){
        this.mSwitchToggleListener = switchToggleListener;
    }

    public interface ItemClickListener{
        void onItemClick(View view, int position);
    }

    public interface SwitchToggleListener{
        void onSwitchToggle(View view, int position, String settingType, boolean value);
    }
}
