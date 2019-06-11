package ca.gc.inspection.scoop;

import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;


public class CreatePostInteractor {

    private CreatePostContract.Presenter mPresenter;

    CreatePostInteractor(CreatePostContract.Presenter presenter) {
        mPresenter = presenter;
    }

    public void sendPostToDatabase(MySingleton singleton, final String userId, final String title, final String text, final String imageBitmap) {
        String url = Config.baseIP + "add-post";

        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
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
        singleton.addToRequestQueue(postRequest);
    }
}
