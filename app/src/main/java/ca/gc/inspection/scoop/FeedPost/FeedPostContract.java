package ca.gc.inspection.scoop.FeedPost;

import android.graphics.Bitmap;

import org.json.JSONArray;
import org.json.JSONException;

import ca.gc.inspection.scoop.MySingleton;
import ca.gc.inspection.scoop.ProfilePost.ProfilePostContract;


public interface FeedPostContract extends ProfilePostContract {

    interface View extends ProfilePostContract.View {
        void setPostImage(Bitmap image, FeedPostViewHolder holder);
        void hidePostImage(FeedPostViewHolder holder);
//        void setRecyclerView(JSONArray postResponse, JSONArray imageResponse);
        String getFeedType();
    }


    interface Presenter extends ProfilePostContract.Presenter {
        void displayImages() throws JSONException;
        void formatImage(String image, String type);
        void getPosts(MySingleton singleton);
        String getFeedType();

    }

}

