package ca.gc.inspection.scoop.settings;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import ca.gc.inspection.scoop.R;

import static ca.gc.inspection.scoop.settings.SettingsActivity.*;

/**
 * Adapter for the setting activity's recycler view
 */
class SettingsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    // Unique id's for each settings item class
    // IF A NEW SETTING OBJECT CLASS IS MADE, REMEMBER TO ADD THEM HERE!
    private final int settingWithSwitch = 0, settingWithSwitchAndSubLabel = 1, settingWithSpinner = 2;

    private static List<SettingsItem> mData; // Contains list of all setting objects
    private LayoutInflater mInflator;
    private ItemClickListener mClickListener;
    private SwitchToggleListener mSwitchToggleListener;
    private SpinnerListener mSpinnerListener;
    private Context context;

    SettingsAdapter(Context context, List<SettingsItem> data){
        this.mInflator = LayoutInflater.from(context);
        this.mData = data;
        this.context = context;
    }

    /**
     * This sets up the view for each view holder
     * @param parent
     * @param i the identifier of the view type of the settings object
     * @return
     */
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        RecyclerView.ViewHolder viewHolder;
        View view;
        switch (i){
            case settingWithSwitch:
                view = mInflator.inflate(R.layout.settings_row_with_switch, parent, false);
                viewHolder = new SettingWithSwitchViewHolder(view, mSwitchToggleListener);
                break;
            case settingWithSwitchAndSubLabel:
                view = mInflator.inflate(R.layout.settings_row_with_switch_and_extra_text, parent, false);
                viewHolder = new SettingWithSwitchAndSubLabelViewHolder(view, mSwitchToggleListener);
                break;
            case settingWithSpinner:
                view = mInflator.inflate(R.layout.settings_row_with_spinner, parent, false);
                viewHolder = new SettingWithSpinnerViewHolder(view, mSpinnerListener, context);
                break;
            default:
                viewHolder = null;
                break;
        }
        return viewHolder;
    }

    /**
     * This binds all needed data to each setting object's view holder (i.e. label, value, etc)
     * - must cast each settingsItem object to their respective sub class
     * @param settingsViewHolder
     * @param i index of the settings item in the recycler view
     */
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder settingsViewHolder, int i) {
        Object setting = mData.get(i);
        switch (settingsViewHolder.getItemViewType()){
            case settingWithSwitch:
                SettingWithSwitch settingWithSwitchObj = (SettingWithSwitch) setting;
                SettingWithSwitchViewHolder settingWithSwitchViewHolder = (SettingWithSwitchViewHolder) settingsViewHolder;
                settingWithSwitchViewHolder.settingTextView.setText(settingWithSwitchObj.getText());
                settingWithSwitchViewHolder.setType(settingWithSwitchObj.getType());
                settingWithSwitchViewHolder.setValue(settingWithSwitchObj.getValue());
                break;
            case settingWithSwitchAndSubLabel:
                SettingWithSwitchAndSubLabel settingWithSwitchAndSubLabelObj = (SettingWithSwitchAndSubLabel) setting;
                SettingWithSwitchAndSubLabelViewHolder settingWithSwitchAndSubLabelViewHolder = (SettingWithSwitchAndSubLabelViewHolder) settingsViewHolder;
                settingWithSwitchAndSubLabelViewHolder.settingTextView.setText(settingWithSwitchAndSubLabelObj.getText());
                settingWithSwitchAndSubLabelViewHolder.settingSubTextView.setText(settingWithSwitchAndSubLabelObj.getSubText());
                settingWithSwitchAndSubLabelViewHolder.setType(settingWithSwitchAndSubLabelObj.getType());
                settingWithSwitchAndSubLabelViewHolder.setValue(settingWithSwitchAndSubLabelObj.getValue());

                break;
            case settingWithSpinner:
                SettingWithSpinner settingWithSpinnerObj = (SettingWithSpinner) setting;
                SettingWithSpinnerViewHolder settingWithSpinnerViewHolder = (SettingWithSpinnerViewHolder) settingsViewHolder;
                settingWithSpinnerViewHolder.settingTextView.setText(settingWithSpinnerObj.getText());
                settingWithSpinnerViewHolder.setType(settingWithSpinnerObj.getType());
                settingWithSpinnerViewHolder.setSpinnerItems(settingWithSpinnerObj.getOptions());
                settingWithSpinnerViewHolder.setValue(settingWithSpinnerObj.getValue());
                break;
        }
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    /**
     * This method associates each setting's object class with a unique ID
     * @param position
     * @return
     */
    @Override
    public int getItemViewType(int position) {
        if (getItem(position) instanceof SettingWithSwitch){
            return settingWithSwitch;
        }
        else if (getItem(position) instanceof SettingWithSpinner){
            return settingWithSpinner;
        }
        else if (getItem(position) instanceof SettingWithSwitchAndSubLabel){
            return settingWithSwitchAndSubLabel;
        }
        return -1; // error
    }

    Object getItem(int id){
        return mData.get(id);
    }

    //////////////////////////////////////////
    // Set Listener Methods
    //////////////////////////////////////////
    /**
     * sets listener for all setting view holders that use clicks
     * @param itemClickListener
     */
    void setClickListener(ItemClickListener itemClickListener){
        this.mClickListener = itemClickListener;
    }
    /**
     * sets listener for all setting view holders that use switches
     * @param switchToggleListener
     */
    void setSwitchToggleListener(SwitchToggleListener switchToggleListener){
        this.mSwitchToggleListener = switchToggleListener;
    }
    /**
     * sets listener for all setting view holders that use spinner
     * @param spinnerListener
     */
    void setSpinnerListener(SpinnerListener spinnerListener){
        this.mSpinnerListener = spinnerListener;
    }

    //////////////////////////////////////////////////////////
    // Listener Interfaces
    // - these are implemented by the SettingsActivity
    //////////////////////////////////////////////////////////
    /**
     * Listener interface for clicks
     */
    public interface ItemClickListener{
        void onItemClick(View view, int position);
    }
    /**
     * Listener interface for toggling of switches
     */
    public interface SwitchToggleListener{
        void onSwitchToggle(View view, int position, String settingType, boolean value);
    }
    /**
     * Listener interface for spinners
     */
    public interface SpinnerListener{
        void onSpinnerStateChange(View view, int position, String settingType, int value );
    }
}
