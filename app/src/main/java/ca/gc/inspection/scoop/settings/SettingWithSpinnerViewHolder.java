package ca.gc.inspection.scoop.settings;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import java.text.BreakIterator;
import java.util.ArrayList;

import ca.gc.inspection.scoop.Config;
import ca.gc.inspection.scoop.R;

/**
 * View holder for use with SettingWithSpinner objects
 */
class SettingWithSpinnerViewHolder extends RecyclerView.ViewHolder {
    TextView settingTextView; // Label
    Spinner spinner;
    SettingsAdapter.SpinnerListener mSpinnerListener; // listener used by this view holder to communicate with the SettingsActivity
    String mSettingType;
    Context mContext;


    public SettingWithSpinnerViewHolder(@NonNull View itemView, SettingsAdapter.SpinnerListener spinnerListener, Context context) {
        super(itemView);
        mContext = context;
        settingTextView = itemView.findViewById(R.id.SettingsTextView);
        mSpinnerListener = spinnerListener;

        // set up spinner
        spinner = itemView.findViewById(R.id.languageSpinner);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mSpinnerListener.onSpinnerStateChange(itemView, getAdapterPosition(), mSettingType, position); // call back to the SettingsActivity upon item selected in the spinner
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    /**
     * Populates the spinner with items
     * @param spinnerItems list of spinner items
     */
    public void setSpinnerItems(ArrayList<String> spinnerItems){
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(mContext, android.R.layout.simple_spinner_item, spinnerItems);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
    }

    public void setType(String type){
        mSettingType = type;
    }

    public void setValue(int value) { spinner.setSelection(value); }
}
