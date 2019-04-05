package ca.gc.inspection.scoop;

import android.content.Context;
import android.util.Log;
import android.view.View;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

public class CreatePostController {
    public static void sendPostToDatabase(Context context, final String userId, final String title, final String text, final String imagePath) {
        String URL = Config.url + "add-post";

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
                params.put("postImage", imagePath);
                return params;
            }
        };
        requestQueue.add(postRequest);
    }
}
