package ca.gc.inspection.scoop.ProfilePost;

import org.json.JSONArray;
import org.json.JSONException;

import ca.gc.inspection.scoop.MySingleton;
import ca.gc.inspection.scoop.ProfileComment.ProfileCommentContract;

/**
 * Interface that inherits from mostgeneric interface
 */
public interface ProfilePostContract extends ProfileCommentContract {

    interface View extends ProfileCommentContract.View {
        void setCommentCount(String commentCount, ProfilePostViewHolder holder);
//        void setRecyclerView(JSONArray posts, JSONArray images);
//        String getUserId();
        void displayPostListener(ProfilePostViewHolder holder);
//        void getUserPosts(MySingleton singleton, final String userId);


    }

    interface Presenter extends ProfileCommentContract.Presenter {
//        void getUserPosts(final String userid);
        void displayPost() throws JSONException;
        void formPostTitle() throws JSONException;
        void checkCommentCount(String commentCount);
//        void getRecyclerView(JSONArray posts, JSONArray images);
//        void getPosts(MySingleton singleton, final String userId);
    }


}