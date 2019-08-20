package ca.gc.inspection.scoop.report;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import ca.gc.inspection.scoop.R;


/**
 * - The ReportConfirmationDialogFragment is the dialog that appears when users submit a report from the ReportOptionsDialog
 */
public class ReportConfirmationDialogFragment extends DialogFragment {
    // UI Declarations
    Button okButton;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, 0);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.dialog_report_confirmation, container, false);

        Toolbar toolbar = view.findViewById(R.id.report_confirmation_toolbar);
        toolbar.setBackgroundColor(getResources().getColor(R.color.colorPrimary));

        okButton = view.findViewById(R.id.dialog_report_ok_btn);
        okButton.setOnClickListener(v -> dismiss());

        return view;
    }

    /**
     * Sets dimensions of report dialog layout similar to ReportDialogFragment
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

}
