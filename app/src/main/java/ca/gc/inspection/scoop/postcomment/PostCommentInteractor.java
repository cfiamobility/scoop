package ca.gc.inspection.scoop.postcomment;

import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import ca.gc.inspection.scoop.Config;
import ca.gc.inspection.scoop.createpost.InteractorBundle;
import ca.gc.inspection.scoop.util.NetworkUtils;

import static ca.gc.inspection.scoop.Config.DATABASE_RESPONSE_SUCCESS;
import static ca.gc.inspection.scoop.Config.USERID_KEY;
import static ca.gc.inspection.scoop.createpost.CreatePostInteractor.newPostRequest;
import static ca.gc.inspection.scoop.postcomment.PostComment.PROFILE_COMMENT_ACTIVITYID_KEY;
import static ca.gc.inspection.scoop.postcomment.PostComment.PROFILE_COMMENT_LIKE_POSTERID_KEY;
import static ca.gc.inspection.scoop.postcomment.PostComment.PROFILE_COMMENT_LIKE_TYPE_KEY;
import static ca.gc.inspection.scoop.postcomment.PostCommentAdapter.ADAPTER_POSITION_KEY;
import static com.google.gson.internal.$Gson$Preconditions.checkNotNull;

public class PostCommentInteractor {
    /**
     * Interactor used to send requests to the network
     */

    protected PostCommentPresenter mPresenter;
    public NetworkUtils mNetwork;

    /**
     * Empty constructor called by child classes (ie. ProfilePostInteractor) to allow them to set
     * their own presenter
     */
    public PostCommentInteractor() {
    }

    public PostCommentInteractor(PostCommentPresenter presenter, NetworkUtils network){
        mPresenter = checkNotNull(presenter);
        mNetwork = network;
    }

    public JsonArrayRequest newProfileJsonArrayRequest(String url, String responseUrl) {
        return new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(final JSONArray response) {
                final JsonArrayRequest imageRequest = new JsonArrayRequest(Request.Method.GET, responseUrl, null, new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray imagesResponse) {
                        // if called by ProfilePostInteractor or FeedPostInteractor, updateData calls the overridden method
                        mPresenter.setData(response, imagesResponse);
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
     * Description: updates likes in table and adds notifications if like type is 1
     * @param likeType : current like type
     * @param activityid : activity id of post
     * @param posterid : user id of poster of post
     * @param viewHolderInterface
     * @throws JSONException
     */
    public void updateLikes(LikeState likeType, String likeCount, final String activityid, final String posterid,
            int i, PostCommentContract.View.ViewHolder viewHolderInterface) {

        Log.i("hello", "should be here");
        String URL = Config.baseIP + "post/update-like";
        //sends a PUT request to update new likes
        StringRequest request = newUpdateLikesStringRequest(
                Request.Method.PUT, URL, likeType, likeCount, activityid, posterid, i, viewHolderInterface);
        request.setRetryPolicy(new DefaultRetryPolicy(
                30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        mNetwork.addToRequestQueue(request);
    }

    /**
     * Helper method which creates a POST request to insert new likes
     * @param requestMethod
     * @param URL
     * @param likeType
     * @param activityid
     * @param posterid
     * @param i
     * @param viewHolderInterface
     * @return
     */
    public StringRequest newUpdateLikesStringRequest(
            int requestMethod, String URL, LikeState likeType, String likeCount, final String activityid,
            final String posterid, int i, PostCommentContract.View.ViewHolder viewHolderInterface) {

        return new StringRequest(requestMethod, URL, new Response.Listener<String>() {
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
                Log.i("PostCommentInteractor", "getParams for insert/updateLikes");
                Map<String, String> params = new HashMap<>();
                params.put(PROFILE_COMMENT_LIKE_TYPE_KEY, likeType.getDatabaseValue());
                params.put(PROFILE_COMMENT_ACTIVITYID_KEY, activityid);
                // See PostComment documentation for disambiguation between posterId and userId
                params.put(PROFILE_COMMENT_LIKE_POSTERID_KEY, posterid);
                params.put(USERID_KEY, Config.currentUser);
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
    }

    /**
     * HTTPRequests for post comments and images
     * @param activityId: activityId of Post
     */
    public void getPostComments(String activityId) {
        String url = Config.baseIP + "post/display-comments/text/" + activityId + "/" + Config.currentUser;
        String responseUrl = Config.baseIP + "post/display-comments/images/" + activityId;
        JsonArrayRequest commentRequest = newProfileJsonArrayRequest(url, responseUrl);
        mNetwork.addToRequestQueue(commentRequest);
    }

    public void updatePostComment(InteractorBundle interactorBundle, final String activityId, final String text) {
        String url = Config.baseIP + "post/edit-post/text/";

        Map<String, String>  params = new HashMap<>();
        params.put("activityid", activityId);
        params.put("activitytype", Integer.toString(Config.commentType));
        params.put("posttext", text);

        StringRequest postRequest = newPostRequest(mPresenter, interactorBundle, url, params);
        mNetwork.addToRequestQueue(postRequest);
    }


    /**
     * Verifies save post information generated by user attempting to save a post
     * - Response contains error message if save failed
     * - Response contains success message if save successful
     * @param activityid ID of the activity in which the user is click on
     * @param userid ID of the user
     */
    public void savePost(final String activityid, final String userid) {
        String url = Config.baseIP + "post/save";

        StringRequest postRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
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
                params.put("activityid", activityid);
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
        mNetwork.addToRequestQueue(postRequest);
    }

    /**
     * Verifies unsave post information generated by user attempting to unsave a post
     * - Response contains error message if save failed
     * - Response contains success message if save successful
     * @param activityid ID of the activity in which the user is click on
     * @param userid ID of the user
     */
    public void unsavePost(final String activityid, final String userid) {
        String url = Config.baseIP + "post/unsave";

        StringRequest postRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // error
                Log.d("Error.Response", String.valueOf(error));
            }
        }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("activityid", activityid);
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
        mNetwork.addToRequestQueue(postRequest);
    }

}
