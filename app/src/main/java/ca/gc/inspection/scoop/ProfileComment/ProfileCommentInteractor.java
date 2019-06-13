package ca.gc.inspection.scoop.ProfileComment;

import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import ca.gc.inspection.scoop.Config;
import ca.gc.inspection.scoop.MySingleton;
import ca.gc.inspection.scoop.post;

public class ProfileCommentInteractor {
    private ProfileCommentContract.Presenter mProfileCommentPresenter;

    public ProfileCommentInteractor(ProfileCommentContract.Presenter profileCommentPresenter){
        mProfileCommentPresenter = profileCommentPresenter;
    }

    /**
     * HTTPRequests for comments and images
     * @param userid: userid
     */
    public void getUserComments(MySingleton singleton, final String userid) {
        String url = Config.baseIP + "profile/commenttextfill/" + userid + "/" + Config.currentUser;
        JsonArrayRequest commentRequest = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(final JSONArray commentsResponse) {
                String url = Config.baseIP + "profile/commentimagefill/" + userid;
                final JsonArrayRequest imageRequest = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray imagesResponse) {
                        mProfileCommentPresenter.getRecyclerView(commentsResponse, imagesResponse);
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }) {
                    public Map<String, String> getHeaders() throws AuthFailureError {
                        // inserting the token into the response header that will be sent to the server
                        Map<String, String> header = new HashMap<>();
                        header.put("authorization", Config.token);
                        return header;
                    }
                };
//                Config.requestQueue.add(imageRequest);
                singleton.addToRequestQueue(imageRequest);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {
            public Map<String, String> getHeaders() throws AuthFailureError {
                // inserting the token into the response header that will be sent to the server
                Map<String, String> header = new HashMap<>();
                header.put("authorization", Config.token);
                return header;
            }
        };
//        Config.requestQueue.add(commentRequest);
        singleton.addToRequestQueue(commentRequest);

    }

    /**
     * Description: updates likes in table and adds notifications if like type is 1
     * @param likeType: current like type
     * @param activityid: activity id of post
     * @param posterid: user id of poster of post
     * @throws JSONException
     */
    public void updateLikes(MySingleton singleton, final String likeType, final String activityid, final String posterid,
                            JSONObject post, Map<String, String> likeProperties) throws JSONException {
        post.put("liketype", likeType); //updates post object
        likeProperties.put("liketype", likeType); //updates properties map
        Log.i("hello", "should be here");
        String URL = Config.baseIP + "display-post/updatelikes";
        StringRequest request = new StringRequest(Request.Method.PUT, URL, new Response.Listener<String>() { //sends a PUT request to update new likes
            @Override
            public void onResponse(String response) {
                Log.i("response", response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        }){
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                Log.i("hello", "cmoff");
                params.put("liketype", likeProperties.get("liketype"));
                params.put("activityid",activityid);
                params.put("posterid", posterid);
                params.put("userid", Config.currentUser);
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
        request.setRetryPolicy(new DefaultRetryPolicy(
                30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        Config.requestQueue.add(request);
    }

    /**
     * Description: inserts likes in table and adds notifications if like type is 1
     * @param likeType: current like type
     * @param activityid: activity id of post
     * @param posterid: user id of poster of post
     * @throws JSONException
     */
    public void insertLikes(MySingleton singleton, final String likeType, final String activityid, final String posterid,
                            JSONObject post, Map<String, String> likeProperties) throws JSONException {
        post.put("liketype", likeType); //updates post object
        likeProperties.put("liketype", likeType); //updates properties map
        Log.i("hello", "should be here");
        String URL = Config.baseIP + "display-post/insertlikes";
        StringRequest request = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() { //sends a POST request to insert new like
            @Override
            public void onResponse(String response) {
                Log.i("response", response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        }){
            @Override
            protected Map<String, String> getParams(){
                Map<String, String> params = new HashMap<>();
                params.put("liketype", likeType);
                params.put("activityid", activityid);
                params.put("posterid", posterid);
                params.put("userid", Config.currentUser);
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
        request.setRetryPolicy(new DefaultRetryPolicy(
                30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
//        Config.requestQueue.add(request);
        singleton.addToRequestQueue(request);

    }



}
