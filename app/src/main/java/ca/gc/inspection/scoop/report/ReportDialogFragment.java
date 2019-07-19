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

public class ReportDialogFragment extends DialogFragment implements ReportContract.View{

    public static String TAG = "Report Dialog Full Screen";

    Button submitButton;
    EditText reportBodyET;

    ReportContract.Presenter mPresenter;

    public void setPresenter(ReportContract.Presenter presenter){
        mPresenter = presenter;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, 0);
        setPresenter(new ReportPresenter(this, NetworkUtils.getInstance(getContext())));
    }

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

        submitButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String activityId = getArguments().getString("ACTIVITY_ID");
                String posterId = getArguments().getString("POSTER_ID");
                String reportReason = spinner.getSelectedItem().toString();
                String reportBody = reportBodyET.getText().toString();

                Log.i("REPORT_REASON", reportReason);
                Log.i("REPORT_BODY", reportBody);

                if (reportReason.equals("Select a reason…")){
                    setReportFailMessage("Please select one of the reasons listed from the dropdown!");
                } else if(reportBody.equals("")){
                    setReportFailMessage("Please give us more details about the reason for your report!");
                } else {
                    mPresenter.submitReport(activityId, posterId, Config.currentUser, reportReason, reportBody);
                }

            }
        });
        return view;
    }


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


    public void reportConfirmation(){
        ReportConfirmationDialogFragment dialog = new ReportConfirmationDialogFragment();
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        dialog.show(ft, ReportConfirmationDialogFragment.TAG);
        Log.i("BUTTON PRESSED", "Thank you report");
        dismiss();
    }

    public void setReportFailMessage(String message){
        Toast.makeText(getContext(),message,Toast.LENGTH_SHORT).show();
    }

}
