package ca.gc.inspection.scoop.ProfilePost;
        import android.util.Log;

        import com.android.volley.AuthFailureError;
        import com.android.volley.Request;
        import com.android.volley.Response;
        import com.android.volley.VolleyError;
        import com.android.volley.toolbox.JsonArrayRequest;

        import org.json.JSONArray;

        import java.util.HashMap;
        import java.util.Map;

        import ca.gc.inspection.scoop.Config;
        import ca.gc.inspection.scoop.MySingleton;


public class ProfilePostInteractor {
    private ProfilePostContract.Presenter mProfilePostPresenter;
    private ProfilePostContract.View mProfilePostView;




    ProfilePostInteractor(ProfilePostContract.View view) {
//        mProfilePostPresenter = presenter;
        mProfilePostView = view;
    }


    /**
     * FROM ProfilePostsController
     */

    /**
     * HTTP Requests to get all the user posts infos
     * @param userid: passes the userid of the profile clicked on
     */
    public void getUserPosts(MySingleton singleton, final String userid) {


        String url = Config.baseIP + "profile/posttextfill/" + userid + "/" + Config.currentUser;
        JsonArrayRequest postRequest = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(final JSONArray postsResponse) {
                String url = Config.baseIP + "profile/postimagefill/"  + userid;
                final JsonArrayRequest imageRequest = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray imagesResponse) {
                        mProfilePostView.setRecyclerView(postsResponse, imagesResponse);
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
                Config.requestQueue.add(imageRequest);
//                  singleton.addToRequestQueue(imageRequest);
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
        Config.requestQueue.add(postRequest);
//          singleton.addToRequestQueue(postRequest);

    }


}

