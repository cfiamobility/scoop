package ca.gc.inspection.scoop;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

public class CommentController {
    /** simple post command
     *
     * @param context context of displayPost.java
     * @param userId current userID
     * @param comment user inputted comment
     * @param otherPostActivity the post the current user is commenting to
     */
    static void sendCommentToDatabase(Context context, final String userId, final String comment, final String otherPostActivity){
        String URL = Config.baseIP + "add-comment";

        RequestQueue requestQueue = Volley.newRequestQueue(context);

        StringRequest postRequest = new StringRequest(Request.Method.POST, URL,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                        // response
                        Log.d("Response", response);
                        if (response.contains("Success")){
                            Log.i("Info", "We good");
                            displayPost.updateCommentList();
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
                params.put("userid", userId); // post test user
                params.put("activitytype", Integer.toString(Config.commentType));
                params.put("posttext", comment);
                params.put("otheractivityid", otherPostActivity);
                return params;
            }
        };
        requestQueue.add(postRequest);

    }

}
