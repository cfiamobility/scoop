package ca.gc.inspection.scoop.report;

import android.app.Dialog;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Arrays;
import java.util.List;

import ca.gc.inspection.scoop.Config;
import ca.gc.inspection.scoop.R;
import ca.gc.inspection.scoop.util.NetworkUtils;


/**
 * - The ReportDialogFragment is the dialog that appears when users click "Report" from the PostOptionsDialog
 *  It allows the users to fill out a report form and submit it
 * - This is the View for the Report action case
 */
public class ReportDialogFragment extends DialogFragment implements ReportContract.View{

    public static String TAG = "Report Dialog Full Screen";

    // UI Declarations
    Button submitButton;
    EditText reportBodyET;

    ReportContract.Presenter mPresenter;

    /**
     * Sets the Presenter for the View
     * @param presenter
     */
    public void setPresenter(ReportContract.Presenter presenter){
        mPresenter = presenter;
    }

    /**
     * Sets the style of the dialog fragment and sets the Presenter, while constructing it
     * @param savedInstanceState state of previous instance
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, 0);
        setPresenter(new ReportPresenter(this, NetworkUtils.getInstance(getContext())));
    }

    /**
     * Creates the view for the dialog fragment including the edittext field, spinner, and submit button
     * @param inflater inflates the layout for the dialog
     * @param container container for the dialog
     * @param savedInstanceState saved state of previous instance containing bundle for ACTIVITY_ID and POSTER_ID
     * @return view
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.dialog_report, container, false);

        Toolbar toolbar = view.findViewById(R.id.report_toolbar);
        toolbar.setTitle("Report");
        toolbar.setTitleTextColor(-1);

        reportBodyET = view.findViewById(R.id.report_text);
        submitButton = view.findViewById(R.id.dialog_report_btn_submit);

        Spinner spinner = view.findViewById(R.id.report_spinner);
        final List<String> reportList = Arrays.asList(getResources().getStringArray(R.array.report_array));
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(),
        android.R.layout.simple_spinner_item, reportList){

            @Override
            public boolean isEnabled(int position){
                if (position == 0) {
                    // Disable the first item from Spinner
                    // First item will be use for hint
                    return false;
                } else {
                    return true;
                }
            }

            @Override
            public View getDropDownView(int position, View convertView,
                                        ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;
                if(position == 0){
                    // Set the hint text color gray
                    tv.setTextColor(Color.GRAY);
                }
                else {
                    tv.setTextColor(Color.BLACK);
                }
                return view;
            }

        };
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);

        //listener for the submit button
        submitButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String activityId = getArguments().getString("ACTIVITY_ID");
                String posterId = getArguments().getString("POSTER_ID");
                String reportReason = spinner.getSelectedItem().toString();
                String reportBody = reportBodyET.getText().toString();

                Log.i("REPORT_REASON", reportReason);
                Log.i("REPORT_BODY", reportBody);

                //sets error messages, otherwise Presenter calls submitReport with necessary parameters
                if (reportReason.equals("Select a reason…")){
                    setReportFailMessage("Please select one of the reasons listed from the dropdown");
                } else if(reportBody.equals("")){
                    setReportFailMessage("Please give us more details about the reason for your report");
                } else {
                    mPresenter.submitReport(activityId, posterId, Config.currentUser, reportReason, reportBody);
                }

            }
        });
        return view;
    }


    /**
     * Overrides and sets the ReportFragmentDialog size to match the size of display window
     */
    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null) {
            int width = ViewGroup.LayoutParams.MATCH_PARENT;
            int height = ViewGroup.LayoutParams.WRAP_CONTENT;
            dialog.getWindow().setLayout(width, height);
        }
    }


    /**
     * Invoked by the Presenter to create the ReportConfirmationDialog and dismiss the current ReportDialog
     */
    public void reportConfirmation(){
        ReportConfirmationDialogFragment dialog = new ReportConfirmationDialogFragment();
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        dialog.show(ft, ReportConfirmationDialogFragment.TAG);
        Log.i("BUTTON PRESSED", "Thank you report");
        dismiss();
    }

    /**
     * Method to set a toast message on report form submission error
     * @param message error message
     */
    public void setReportFailMessage(String message){
        Toast.makeText(getContext(),message,Toast.LENGTH_SHORT).show();
    }

}
