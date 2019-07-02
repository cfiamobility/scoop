package ca.gc.inspection.scoop.displaypost;

import android.support.annotation.NonNull;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

import ca.gc.inspection.scoop.Config;
import ca.gc.inspection.scoop.util.NetworkUtils;

import static com.google.gson.internal.$Gson$Preconditions.checkNotNull;

class DisplayPostInteractor {

    private DisplayPostPresenter mPresenter;

    DisplayPostInteractor(@NonNull DisplayPostPresenter presenter) {
        mPresenter = checkNotNull(presenter);
    }

    /** simple Post command
     *
     * @param network NetworkUtils singleton
     * @param userId current userID
     * @param comment user inputted comment
     * @param otherPostActivity the Post the current user is commenting to
     */
    public static void sendCommentToDatabase(NetworkUtils network, final String userId, final String comment, final String otherPostActivity){
        String URL = Config.baseIP + "add-comment";

        StringRequest postRequest = new StringRequest(Request.Method.POST, URL,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                        // response
                        Log.d("Response", response);
                        if (response.contains("Success")){
                            Log.i("Info", "We good");
//                            DisplayPostActivity.updateCommentList();
                        }
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // error
                        Log.d("Error.Response", String.valueOf(error));
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String>  params = new HashMap<>();
                params.put("userid", userId); // Post test user
                params.put("activitytype", Integer.toString(Config.commentType));
                params.put("posttext", comment);
                params.put("activityreference", otherPostActivity);
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

        // Stops the application from sending the data twice. Don't know why it works, but it does.
        postRequest.setRetryPolicy(new DefaultRetryPolicy(
                0,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        network.addToRequestQueue(postRequest);
    }
}
