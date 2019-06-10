package ca.gc.inspection.scoop.ProfileComment;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;

import org.json.JSONArray;

import java.util.HashMap;
import java.util.Map;

import ca.gc.inspection.scoop.Config;

public class Interactor {
    ProfileCommentContract.View mProfileCommentView;

    public Interactor (ProfileCommentContract.View profileCommentView){
        mProfileCommentView = profileCommentView;
    }

    /**
     * HTTPRequests for comments and images
     * @param userid: userid
     */
    public void getUserComments(final String userid) {
        String url = Config.baseIP + "profile/commenttextfill/" + userid + "/" + Config.currentUser;
        JsonArrayRequest commentRequest = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(final JSONArray commentsResponse) {
                String url = Config.baseIP + "profile/commentimagefill/" + userid;
                final JsonArrayRequest imageRequest = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray imagesResponse) {
                        mProfileCommentView.setRecyclerView(commentsResponse, imagesResponse);
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
                Config.requestQueue.add(imageRequest);
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
        Config.requestQueue.add(commentRequest);
    }

}
