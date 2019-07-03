package ca.gc.inspection.scoop.postoptionsdialog;

import android.support.annotation.NonNull;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;

import java.util.HashMap;
import java.util.Map;

import ca.gc.inspection.scoop.Config;
import ca.gc.inspection.scoop.util.NetworkUtils;

import static com.google.gson.internal.$Gson$Preconditions.checkNotNull;

class PostOptionsDialogInteractor {

    private PostOptionsDialogPresenter mPresenter;

    public PostOptionsDialogInteractor(@NonNull PostOptionsDialogPresenter presenter) {
        mPresenter = checkNotNull(presenter);
    }

//    public void getSavePostData(NetworkUtils network, final String userid) {
//        String url = Config.baseIP + "post/get-save-post-data";
//
//        StringRequest getRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
//            @Override
//            public void onResponse(String response) {
//                Log.d("Response", response);
//                // grabs the activityid of the post by user with the given userid
//                mPresenter.getActivityId(response);
//            }
//        },
//        new Response.ErrorListener()
//        {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                // error
//                Log.d("Error.Response", String.valueOf(error));
//            }
//        }
//        ){
//            @Override
//            protected Map<String, String> getParams()
//            {
//                Map<String, String>  params = new HashMap<>();
//                params.put("postUser", userid); // the user whose post we are saving - this gets modified in the middle tier
//                params.put("userid", userid); //current user
//
//                return params;
//            }
//
//            @Override
//            public Map<String, String> getHeaders() throws AuthFailureError {
//                // inserting the token into the response header that will be sent to the server
//                Map<String, String> header = new HashMap<>();
//                header.put("authorization", Config.token);
//                return header;
//            }
//        };
//         network.addToRequestQueue(getRequest);
//    }



    public void savePost(NetworkUtils network, final String posterId, final String userid) {

        Log.i("hello", "sending data to save post table");
        String url = Config.baseIP + "post/save-post";

        StringRequest postRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("Response", response);
//                // grabs the activityid of the post by user with the given userid
//                mPresenter.getActivityId(response);
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
        ){
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String>  params = new HashMap<>();
                params.put("posterId", posterId);
                params.put("userid", userid); // Post test user

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
        network.addToRequestQueue(postRequest);
    }
}
