package ca.gc.inspection.scoop.profilepost;

import org.json.JSONException;

import ca.gc.inspection.scoop.profilecomment.ProfileCommentContract;
import ca.gc.inspection.scoop.base.BasePresenter;
import ca.gc.inspection.scoop.base.BaseView;

/**
 * Interface that inherits from mostgeneric interface
 */
public interface ProfilePostContract extends ProfileCommentContract {

    interface View extends BaseView<Presenter> {
        void setCommentCount(String commentCount, ProfilePostViewHolder holder);
//        void setRecyclerView(JSONArray posts, JSONArray images);
//        String getPosterId();
        void displayPostListener(ProfilePostViewHolder holder);
//        void getUserPosts(MySingleton singleton, final String userId);


    }

    interface Presenter extends BasePresenter {
//        void getUserPosts(final String userid);
        void displayPost() throws JSONException;
        void formPostTitle() throws JSONException;
        void checkCommentCount(String commentCount);
//        void getRecyclerView(JSONArray posts, JSONArray images);
//        void getPosts(MySingleton singleton, final String userId);
    }


}
