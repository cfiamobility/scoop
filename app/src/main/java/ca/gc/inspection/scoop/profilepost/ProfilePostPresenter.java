package ca.gc.inspection.scoop.profilepost;

import android.support.annotation.NonNull;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import ca.gc.inspection.scoop.profilecomment.ProfileCommentPresenter;


/**
 * performs all logic and HTTP requests for the FeedAdapter
 */
public class ProfilePostPresenter extends ProfileCommentPresenter implements ProfilePostContract.Presenter {
    private JSONObject post;
    private ProfilePostViewHolder holder;
    private Map<String, String> likeProperties;
    private static final String TAG = "presenter";


    @NonNull
    private ProfilePostContract.View mProfilePostView;
    private ProfilePostInteractor mProfilePostInteractor;


    /**
     * @param profilePostView: inherits from MostGenericInterface and declares all its methods
     * @param posts: JSONArray of posts from the database
     * @param images: JSONArray of profile images from the database
     * @param i: counter for the array adapter
     * @param holder: view holder that contains all the front end declarations
     */
    public ProfilePostPresenter(ProfilePostContract.View profilePostView, JSONArray posts, JSONArray images, int i, ProfilePostViewHolder holder){
        super(profilePostView, posts, images, i, holder);
        mProfilePostView = profilePostView;
        try {
            this.post = posts.getJSONObject(i);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        this.holder = holder;
        likeProperties = new HashMap<>(); //map of liketype and likecount of specified post

        mProfilePostView.setPresenter(this);
//        mProfilePostInteractor = new ProfilePostInteractor(this);
//        mProfilePostInteractor.getUserPosts(Config.currentUser);
    }

//    @Override
//    public void setPresenterView (ProfilePostContract.View profilePostView){
//        mProfilePostView = profilePostView;
//    }

//    @Override
//    public ProfilePostContract.View getPresenterView (){
//        return mProfilePostView;
//    }

        /**
         * Super runs the display post in MostGeneric
         * Displays the posts in the profile
         * @throws JSONException
         */
    @Override
    public void displayPost() throws JSONException{
        super.displayPost();
        checkCommentCount(post.getString("commentcount"));
        mProfilePostView.displayPostListener(holder);


//        // to get the options menu to appear
//        holder.optionsMenu.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                PostOptionsDialog bottomSheetDialog = new PostOptionsDialog();
//                final Context context = v.getContext();
//                FragmentManager fragmentManager = ((AppCompatActivity)context).getSupportFragmentManager();
//                bottomSheetDialog.show(fragmentManager, "bottomSheet");
//            }
//        });
    }

    /**
     * Sets the post title
     * @throws JSONException
     */
    @Override
    public void formPostTitle() throws JSONException {
        mProfilePostView.setPostTitle(post.getString("posttitle"), holder);
    }

    /**
     * Description: checks current comment count and sets item accordingly
     * @param commentCount: current comment count
     */
    public void checkCommentCount(String commentCount){
        String defaultCount = "0";
        if(!commentCount.equals("null")){
            mProfilePostView.setCommentCount(commentCount, holder);
        }else{
            mProfilePostView.setCommentCount(defaultCount, holder);
        }
    }


//    public void getRecyclerView(JSONArray posts, JSONArray images){
//        mProfilePostView.setRecyclerView(posts, images);
//    }

    /**
     * GET USER POSTS
     * @param singleton
     * @param userId
     */

  /*  @Override
    public void getPosts(MySingleton singleton, final String userId){
        mProfilePostInteractor.getUserPosts(singleton, userId);

    }*/

//    /**
//     * FROM ProfilePostsController
//     */
//
//    /**
//     * HTTP Requests to get all the user posts infos
//     * @param userid: passes the userid of the profile clicked on
//     */
//    public void getUserPosts(final String userid) {
//        String url = Config.baseIP + "profile/posttextfill/" + userid + "/" + Config.currentUser;
//        JsonArrayRequest postRequest = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
//            @Override
//            public void onResponse(final JSONArray postsResponse) {
//                String url = Config.baseIP + "profile/postimagefill/"  + userid;
//                final JsonArrayRequest imageRequest = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
//                    @Override
//                    public void onResponse(JSONArray imagesResponse) {
//                        mProfilePostView.setRecyclerView(postsResponse, imagesResponse);
//                    }
//                }, new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//
//                    }
//                }) {
//                    public Map<String, String> getHeaders() throws AuthFailureError {
//                        // inserting the token into the response header that will be sent to the server
//                        Map<String, String> header = new HashMap<>();
//                        header.put("authorization", Config.token);
//                        return header;
//                    }
//                };
//                Config.requestQueue.add(imageRequest);
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//
//            }
//        }) {
//            public Map<String, String> getHeaders() throws AuthFailureError {
//                // inserting the token into the response header that will be sent to the server
//                Map<String, String> header = new HashMap<>();
//                header.put("authorization", Config.token);
//                return header;
//            }
//        };
//        Config.requestQueue.add(postRequest);
//    }


}
