package ca.gc.inspection.scoop;

import android.content.Context;
import android.util.Log;


import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

public class CreatePostController {
    /** SendPostToDatabase
     * Simple post request to store the newly created post to the postcomment table
     * @param context Context of CreatePostController.java
     * @param userId current user's userid
     * @param title userinputted title (Mandatory)
     * @param text userinputted test (Mandatory)
     * @param imageBitmap userinputted image (Optional)
     */
    public static void sendPostToDatabase(Context context, final String userId, final String title, final String text, final String imageBitmap) {
        String URL = Config.baseIP + "add-post";

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
                params.put("activitytype", Integer.toString(Config.postType));
                params.put("posttitle", title);
                params.put("posttext", text);
                params.put("postimage", imageBitmap);
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
        requestQueue.add(postRequest);
    }
}
