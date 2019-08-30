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

    /**
     *  enum containing all the different types of view holders
     *   - each enum/view holder needs their own unique onCreateViewHolder & onBindViewHolder methods
     *   - IF A NEW SETTING OBJECT CLASS IS MADE, REMEMBER TO ADD THEIR RESPECTIVE ENUM HERE!
     */
    public enum ViewHolderType {
        SettingWithSwitch{ // enum for settings with a switch
            @Override
            public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent) {
                View view = mInflator.inflate(R.layout.settings_row_with_switch, parent, false);
                RecyclerView.ViewHolder viewHolder = new SettingWithSwitchViewHolder(view, mSwitchToggleListener);
                return viewHolder;
            }

            @Override
            public void onBindViewHolder(RecyclerView.ViewHolder settingsViewHolder, int i) {
                SettingsItem setting = mData.get(i);
                SettingWithSwitch settingWithSwitchObj = (SettingWithSwitch) setting;
                SettingWithSwitchViewHolder settingWithSwitchViewHolder = (SettingWithSwitchViewHolder) settingsViewHolder;
                settingWithSwitchViewHolder.settingTextView.setText(settingWithSwitchObj.getText());
                settingWithSwitchViewHolder.setType(settingWithSwitchObj.getType());
                settingWithSwitchViewHolder.setValue(settingWithSwitchObj.getValue());
            }
        },
        SettingWithSwitchAndSubLabel { // enum for settings with a switch and a sub label
            @Override
            public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent) {
                View view = mInflator.inflate(R.layout.settings_row_with_switch_and_extra_text, parent, false);
                RecyclerView.ViewHolder viewHolder = new SettingWithSwitchAndSubLabelViewHolder(view, mSwitchToggleListener);
                return viewHolder;
            }

            @Override
            public void onBindViewHolder(RecyclerView.ViewHolder settingsViewHolder, int i) {
                SettingsItem setting = mData.get(i);
                SettingWithSwitchAndSubLabel settingWithSwitchAndSubLabelObj = (SettingWithSwitchAndSubLabel) setting;
                SettingWithSwitchAndSubLabelViewHolder settingWithSwitchAndSubLabelViewHolder = (SettingWithSwitchAndSubLabelViewHolder) settingsViewHolder;
                settingWithSwitchAndSubLabelViewHolder.settingTextView.setText(settingWithSwitchAndSubLabelObj.getText());
                settingWithSwitchAndSubLabelViewHolder.settingSubTextView.setText(settingWithSwitchAndSubLabelObj.getSubText());
                settingWithSwitchAndSubLabelViewHolder.setType(settingWithSwitchAndSubLabelObj.getType());
                settingWithSwitchAndSubLabelViewHolder.setValue(settingWithSwitchAndSubLabelObj.getValue());
            }
        },
        SettingWithSpinner { // enum for settings with a spinner
            @Override
            public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent) {
                View view = mInflator.inflate(R.layout.settings_row_with_spinner, parent, false);
                RecyclerView.ViewHolder viewHolder = new SettingWithSpinnerViewHolder(view, mSpinnerListener, context);
                return viewHolder;
            }

            @Override
            public void onBindViewHolder(RecyclerView.ViewHolder settingsViewHolder, int i) {
                SettingsItem setting = mData.get(i);
                SettingWithSpinner settingWithSpinnerObj = (SettingWithSpinner) setting;
                SettingWithSpinnerViewHolder settingWithSpinnerViewHolder = (SettingWithSpinnerViewHolder) settingsViewHolder;
                settingWithSpinnerViewHolder.settingTextView.setText(settingWithSpinnerObj.getText());
                settingWithSpinnerViewHolder.setType(settingWithSpinnerObj.getType());
                settingWithSpinnerViewHolder.setSpinnerItems(settingWithSpinnerObj.getOptions());
                settingWithSpinnerViewHolder.setValue(settingWithSpinnerObj.getValue());
            }
        };

        // Functions implemented by each enum
        public abstract RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent);               // unique onCreateViewHolder method required by each enum
        public abstract void onBindViewHolder(RecyclerView.ViewHolder settingsViewHolder, int i);   // unique onBindViewHolder method required by each enum

        /**
         * returns the ViewHolderTypes's position in its enum declaration
         * @return
         */
        public int getEnumIndex(){
            return ordinal();
        }
    }

    private static List<SettingsItem> mData;                        // Contains list of all setting objects
    private static LayoutInflater mInflator;
    private ItemClickListener mClickListener;                       // Listener for clicks (not being used at the moment)
    private static SwitchToggleListener mSwitchToggleListener;      // Listener for switches
    private static SpinnerListener mSpinnerListener;                // Listener for spinners
    private static Context context;                                 // Activity context is stored because it's sometimes needed by different view holders (i.e. spinners required a context)

    /**
     * Constructor for the adapter
     * @param context Activity context
     * @param data List of setting items
     */
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
        return ViewHolderType.values()[i].onCreateViewHolder(parent);
    }

    /**
     * This binds all needed data to each setting object's view holder (i.e. label, value, etc)
     * @param settingsViewHolder
     * @param i index of the settings item in the recycler view
     */
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder settingsViewHolder, int i) {
        mData.get(i).getViewHolderType().onBindViewHolder(settingsViewHolder, i);
    }

    /**
     * returns number of setting items
     * @return
     */
    @Override
    public int getItemCount() {
        return mData.size();
    }

    /**
     * This method associates each setting's object class with a unique ID
     * @param position
     * @return unique ID
     */
    @Override
    public int getItemViewType(int position) {
        return getItem(position).getViewHolderType().getEnumIndex();
    }

    /**
     * returns a specific setting item
     * @param id position of the item in the recycler view
     * @return
     */
    SettingsItem getItem(int id){
        return mData.get(id);
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
}
