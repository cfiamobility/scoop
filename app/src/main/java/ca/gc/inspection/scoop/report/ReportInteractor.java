package ca.gc.inspection.scoop.report;

import android.support.annotation.NonNull;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONObject;

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
    //requires private class variable for the NetworkUtils because of nested requests
    private NetworkUtils mNetwork;

    public ReportInteractor(@NonNull ReportPresenter presenter, NetworkUtils network) {
        mPresenter = checkNotNull(presenter);
        mNetwork = network;
    }

    /**
     * - A nested POST and GET request that submits the user-generated report information to the reporttable in the database, which is verified as unique
     * - On the first response, a GET request gets the user-generated report data and additional data and prompts the middle-tier to send an email report
     * - On the second response, a report confirmation is called
     * @param activityId the post that is being reported
     * @param posterId the user whose post is being reported
     * @param userId user who is generating the report
     * @param reportReason reason for the report
     * @param reportBody details of the report
     */
    void submitReport(String activityId, String posterId, String userId, String reportReason, String reportBody){

        Log.i("MIDDLE-TIER CHECK", "sending data to report table");
        String url = Config.baseIP + "report/report-post";

        StringRequest submitRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i("RESPONSE", response);

                // If specific report exists, send fail message, otherwise get remaining report information and have middle-tier send email report
                if (response.equals("Fail")){
                    mPresenter.reportFailMessage();
                } else {
                    Log.i("MIDDLE-TIER CHECK", "getting data from report table");
                    // Passing params in url instead of body for get request
                    String url = Config.baseIP + "report/send-email/" + activityId + "/" + posterId + "/" + userId ;

                    JsonObjectRequest getEmailInfoRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            Log.i("RESPONSE", response.toString());
                            mPresenter.reportConfirmation();

                        }
                    }, new Response.ErrorListener()
                    {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            // error
                            Log.d("Error.Response", String.valueOf(error));
                        }
                    }
                    ){

                        @Override
                        public Map<String, String> getHeaders() throws AuthFailureError {
                            // inserting the token into the response header that will be sent to the server
                            Map<String, String> header = new HashMap<>();
                            header.put("authorization", Config.token);
                            return header;
                        }
                    };
                    mNetwork.addToRequestQueue(getEmailInfoRequest);
                }
            }
        }, new Response.ErrorListener()
        {
            @Override
            public void onErrorResponse(VolleyError error) {
                // error
                Log.d("Error.Response", String.valueOf(error));
            }
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
            public Map<String, String> getHeaders() throws AuthFailureError {
                // inserting the token into the response header that will be sent to the server
                Map<String, String> header = new HashMap<>();
                header.put("authorization", Config.token);
                return header;
            }
        };
        mNetwork.addToRequestQueue(submitRequest);
    }


}


