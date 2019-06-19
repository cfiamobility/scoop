package ca.gc.inspection.scoop.feedpost;

import android.content.Intent;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.view.View;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import ca.gc.inspection.scoop.DisplayPostActivity;
import ca.gc.inspection.scoop.MyCamera;
import ca.gc.inspection.scoop.profilepost.ProfilePostPresenter;


public class FeedPostPresenter extends ProfilePostPresenter implements FeedPostContract.Presenter{
    private JSONObject images;
    private FeedPostViewHolder holder;

    @NonNull
    private FeedPostContract.View mFeedPostView;
    private FeedPostInteractor mFeedPostInteractor;


    public FeedPostPresenter(FeedPostContract.View feedPostView, JSONArray posts, JSONArray images, int i, FeedPostViewHolder holder){
        super(feedPostView, posts, images, i, holder);

        mFeedPostView = feedPostView;
        try {
            this.images = images.getJSONObject(i);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        this.holder = holder;

        mFeedPostView.setPresenter(this);
//        mFeedPostInteractor = new FeedPostInteractor(this);

    }

    @Override
    public void displayImages() throws JSONException {
        if(images != null) { // null check to see if there are images
            formatImage(images.getString("postimagepath"), "post"); //formats post image
            formatImage(images.getString("profileimage"), "user"); //formats profile image
        }else{
            mFeedPostView.hidePostImage(holder); //hides post image
        }

        // tapping on any item from the view holder will go to the display post activity
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.getContext().startActivity(new Intent(v.getContext(), DisplayPostActivity.class));
            }
        });
    }

    /**
     * Description: changes image from a string to a bitmap, then setting image
     * @param image: image to convert
     * @param type: type of image
     */
    public void formatImage(String image, String type){
        Bitmap bitmap = MyCamera.stringToBitmap(image); //converts image string to bitmap
        if(type.equals("post")) {
            if(!image.equals("") && !image.equals("null")) {
                mFeedPostView.setPostImage(bitmap, holder);
            } else{
                mFeedPostView.hidePostImage(holder);
            }
        }else{
            mFeedPostView.setUserImage(bitmap, holder);
        }
    }
//
//    public void getRecyclerView(JSONArray posts, JSONArray images){
//        mFeedPostView.setRecyclerView(posts, images);
//    }
//
//    public void getPosts(MySingleton singleton){
//        mFeedPostInteractor.getFeedPosts(singleton);
//    }

    public String getFeedType(){
        return mFeedPostView.getFeedType();
    }

//    public void getPosts(){
//        String URL = Config.baseIP + "display-post/posts/" + mFeedPostView.getFeedType() + "/" + Config.currentUser;
//        JsonArrayRequest postRequest = new JsonArrayRequest(Request.Method.GET, URL, null, new Response.Listener<JSONArray>() { //makes request for all posts for specific feed
//            @Override
//            public void onResponse(final JSONArray postResponse) {
//                String imageURL = Config.baseIP + "display-post/images/" + mFeedPostView.getFeedType() + "/" + Config.currentUser;
//                JsonArrayRequest imageRequest = new JsonArrayRequest(Request.Method.GET, imageURL, null, new Response.Listener<JSONArray>() { //makes request for all corresponding images
//                    @Override
//                    public void onResponse(JSONArray imageResponse) {
//                        mFeedPostView.setRecyclerView(postResponse, imageResponse);
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

}
