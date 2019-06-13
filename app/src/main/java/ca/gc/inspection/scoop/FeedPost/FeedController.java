//package ca.gc.inspection.scoop.FeedPost;
//
//import com.android.volley.AuthFailureError;
//import com.android.volley.Request;
//import com.android.volley.Response;
//import com.android.volley.VolleyError;
//import com.android.volley.toolbox.JsonArrayRequest;
//
//import org.json.JSONArray;
//
//import java.util.HashMap;
//import java.util.Map;
//
//import ca.gc.inspection.scoop.Config;
//
//public class FeedController {
//    private FeedInterface feedInterface;
//
//    public FeedController(FeedInterface feedInterface){
//        this.feedInterface = feedInterface;
//    }
//
//    /**
//     * Description: gets post and images for the specified feed
//     */
//    public void getPosts(){
//        String URL = Config.baseIP + "display-post/posts/" + feedInterface.getFeedType() + "/" + Config.currentUser;
//        JsonArrayRequest postRequest = new JsonArrayRequest(Request.Method.GET, URL, null, new Response.Listener<JSONArray>() { //makes request for all posts for specific feed
//            @Override
//            public void onResponse(final JSONArray postResponse) {
//                String imageURL = Config.baseIP + "display-post/images/" + feedInterface.getFeedType() + "/" + Config.currentUser;
//                JsonArrayRequest imageRequest = new JsonArrayRequest(Request.Method.GET, imageURL, null, new Response.Listener<JSONArray>() { //makes request for all corresponding images
//                    @Override
//                    public void onResponse(JSONArray imageResponse) {
//                        feedInterface.setRecyclerView(postResponse, imageResponse);
//                    }
//                }, new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//                        error.printStackTrace();
//                    }
//                })
//                { @Override
//                public Map<String, String> getHeaders() throws AuthFailureError {
//                    // inserting the token into the response header that will be sent to the server
//                    Map<String, String> header = new HashMap<>();
//                    header.put("authorization", Config.token);
//                    return header;
//                }};
//
//                Config.requestQueue.add(imageRequest);
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                error.printStackTrace();
//            }
//        })
//        { @Override
//        public Map<String, String> getHeaders() throws AuthFailureError {
//            // inserting the token into the response header that will be sent to the server
//            Map<String, String> header = new HashMap<>();
//            header.put("authorization", Config.token);
//            return header;
//        }};
//        Config.requestQueue.add(postRequest);
//    }
//
//
////    public interface FeedInterface{
////       void setRecyclerView(JSONArray postResponse, JSONArray imageResponse);
////       String getFeedType();
////    }
//}
