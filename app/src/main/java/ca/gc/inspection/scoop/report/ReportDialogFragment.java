package ca.gc.inspection.scoop.report;

import android.app.Dialog;
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
import android.widget.Toast;

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

        Toolbar toolbar = view.findViewById(R.id.toolbar);
        toolbar.setTitle("Report");

        reportBodyET = view.findViewById(R.id.report_text);
        submitButton = view.findViewById(R.id.dialog_report_btn_submit);

        Spinner spinner = view.findViewById(R.id.report_spinner);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(),
                R.array.report_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);


        submitButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {


                //TODO reportBodyET != null
                String activityId = getArguments().getString("ACTIVITY_ID");
                String posterId = getArguments().getString("POSTER_ID");
                String reportReason = spinner.getSelectedItem().toString();
                Log.i("REPORT_REASON", reportReason);
                String reportBody = reportBodyET.getText().toString();
                Log.i("REPORT_BODY", reportBody);

                mPresenter.submitReport(activityId, posterId, Config.currentUser, reportReason, reportBody);
            }
        });


//        setSubmitButtonListener(activityId, Config.currentUser,
//                reportReason, reportBody);

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
