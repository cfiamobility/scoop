package ca.gc.inspection.scoop.report;

import android.support.annotation.NonNull;
import android.util.Log;

import org.json.JSONObject;

import ca.gc.inspection.scoop.util.NetworkUtils;

import static com.google.gson.internal.$Gson$Preconditions.checkNotNull;

public class ReportPresenter implements ReportContract.Presenter{

    private ReportInteractor mInteractor;
    private ReportContract.View mView;

    /**
     * Constructor that takes in the respective action case View.
     * @param view View that is linked to the respective Presenter
     */
    ReportPresenter(@NonNull ReportContract.View view, NetworkUtils network) {
        mView = checkNotNull(view);
        mInteractor = new ReportInteractor(this, network);
    }




    public void submitReport(String activityId, String posterId, String userId, String reportReason, String reportBody){

        mInteractor.submitReport(activityId, posterId, userId, reportReason, reportBody);
    }

    void reportFailMessage(){
        String message = "Failed to report post. You may have already reported this post. Thank you.";
        mView.setReportFailMessage(message);
    }


    void sendReportEmail(JSONObject response){


        reportConfirmation();
    }

    private void reportConfirmation(){
        mView.reportConfirmation();
        Log.i("REPORT CONFIRMATION", "do i need a JSON object");
    }

}
