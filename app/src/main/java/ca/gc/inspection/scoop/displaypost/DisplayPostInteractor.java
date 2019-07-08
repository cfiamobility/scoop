package ca.gc.inspection.scoop.displaypost;

import android.support.annotation.NonNull;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;

import java.util.HashMap;
import java.util.Map;

import ca.gc.inspection.scoop.Config;
import ca.gc.inspection.scoop.feedpost.FeedPostInteractor;
import ca.gc.inspection.scoop.util.NetworkUtils;

import static com.google.gson.internal.$Gson$Preconditions.checkNotNull;

class DisplayPostInteractor extends FeedPostInteractor {

    private DisplayPostPresenter mDisplayPostPresenter;

    DisplayPostInteractor(@NonNull DisplayPostPresenter presenter, NetworkUtils network) {
        // Need to set mPresenter of superclass
        mPresenter = checkNotNull(presenter);
        mDisplayPostPresenter = checkNotNull(presenter);
        mNetwork = network;
    }

    /** add post comment to a d
     *
     * @param userId current userID
     * @param comment user inputted comment
     * @param otherPostActivity the Post the current user is commenting to
     */
    public void addPostComment(final String userId, final String comment, final String otherPostActivity){
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
                            mDisplayPostPresenter.updateDisplay();
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

        mNetwork.addToRequestQueue(postRequest);
    }

    /**
     * Create JsonArrayRequest to get the post (excluding comments)
     * @param url
     * @param responseUrl
     * @return
     */
    public JsonArrayRequest newSingleDisplayPostJsonArrayRequest(String url, String responseUrl) {
        return new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(final JSONArray response) {
                final JsonArrayRequest imageRequest = new JsonArrayRequest(Request.Method.GET, responseUrl, null, new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray imagesResponse) {
                        mDisplayPostPresenter.setDetailedPostData(response, imagesResponse);
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("Error.Response", String.valueOf(error));
                    }
                }) {
                    public Map<String, String> getHeaders() throws AuthFailureError {
                        // inserting the token into the response header that will be sent to the server
                        Map<String, String> header = new HashMap<>();
                        header.put("authorization", Config.token);
                        return header;
                    }
                };
                mNetwork.addToRequestQueue(imageRequest);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        }) {
            public Map<String, String> getHeaders() throws AuthFailureError {
                // inserting the token into the response header that will be sent to the server
                Map<String, String> header = new HashMap<>();
                header.put("authorization", Config.token);
                return header;
            }
        };
    }

    /**
     * HTTPRequest to get the post (excluding comments)
     * @param activityId: activityId of Post
     */
    public void getDetailedPost(String activityId) {
        String url = Config.baseIP + "post/detailed-post/text/" + activityId + "/" + Config.currentUser;
        String responseUrl = Config.baseIP + "post/detailed-post/image/" + activityId;
        JsonArrayRequest commentRequest = newSingleDisplayPostJsonArrayRequest(url, responseUrl);
        mNetwork.addToRequestQueue(commentRequest);
    }
}
