package ca.gc.inspection.scoop.report;

import android.support.annotation.NonNull;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

import ca.gc.inspection.scoop.Config;
import ca.gc.inspection.scoop.util.NetworkUtils;

import static com.google.gson.internal.$Gson$Preconditions.checkNotNull;

/**
 *  ReportInteractor used by Presenter to create POST request and store data into the Model(database/server)
 *  and creates a GET request on response to have the middle tier send the report-email
 */
public class ReportInteractor {

    protected ReportPresenter mPresenter;
    private NetworkUtils mNetwork;

    public ReportInteractor(@NonNull ReportPresenter presenter, NetworkUtils network) {
        mPresenter = checkNotNull(presenter);
        mNetwork = network;
    }

    /**
     * - A nested POST and GET request that submits the user-generated report information to the reporttable in the database, which is verified as unique
     * - On the first response, a GET request gets the user-generated report data and additional data and prompts the middle-tier to send an email report
     * - On the second response, a report confirmation is called - TO BE IMPLEMENTED send report email from middle tier
     * @param activityId the post that is being reported
     * @param posterId the user whose post is being reported
     * @param userId user who is generating the report
     * @param reportReason reason for the report
     * @param reportBody details of the report
     */
    void submitReport(String activityId, String posterId, String userId, String reportReason, String reportBody){
        String url = Config.baseIP + "report/report-post";
        StringRequest submitRequest = new StringRequest(Request.Method.POST, url, response -> {
            // If specific report exists, send fail message, otherwise get remaining report information and have middle-tier send email report
            if (response.equals("Fail")){
                mPresenter.reportFailMessage();
            } else {
                // Passing params in url instead of body for get request and send report email from middle tier
                //TODO: implement middle tier function to send report email
                String url1 = Config.baseIP + "report/send-email/" + activityId + "/" + posterId + "/" + userId ;

                JsonObjectRequest getEmailInfoRequest = new JsonObjectRequest(Request.Method.GET, url1, null, response1 -> {
                    mPresenter.reportConfirmation();

                }, error -> {
                    // error
                    Log.d("Error.Response", String.valueOf(error));
                }
                ){

                    @Override
                    public Map<String, String> getHeaders() {
                        // inserting the token into the response header that will be sent to the server
                        Map<String, String> header = new HashMap<>();
                        header.put("authorization", Config.token);
                        return header;
                    }
                };
                mNetwork.addToRequestQueue(getEmailInfoRequest);
            }
        }, error -> {
            // error
            Log.d("Error.Response", String.valueOf(error));
        }
        ){
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String>  params = new HashMap<>();
                params.put("activityId", activityId);
                params.put("userId", userId);
                params.put("reportReason", reportReason);
                params.put("reportBody", reportBody);

                return params;
            }

            @Override
            public Map<String, String> getHeaders() {
                // inserting the token into the response header that will be sent to the server
                Map<String, String> header = new HashMap<>();
                header.put("authorization", Config.token);
                return header;
            }
        };
        mNetwork.addToRequestQueue(submitRequest);
    }


}


