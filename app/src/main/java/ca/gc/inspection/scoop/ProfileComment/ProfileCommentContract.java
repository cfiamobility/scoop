package ca.gc.inspection.scoop.ProfileComment;


import android.graphics.Bitmap;

import org.json.JSONArray;
import org.json.JSONException;

import ca.gc.inspection.scoop.MySingleton;
import ca.gc.inspection.scoop.base.BasePresenter;
import ca.gc.inspection.scoop.base.BaseView;

/**
 * A contract between the View and Presenter for the replying to a post
 * action. This contract communicates the how the View and Presenter will
 * interact with each other.
 */

public interface ProfileCommentContract {

    interface View extends BaseView<Presenter> {
        void setDisplayPostListener(ProfileCommentContract.View.ViewHolder holder, String activityid, String posterid);
        void setDisplayImagesListener(ProfileCommentContract.View.ViewHolder holder);

        interface Adapter {
        }

        interface ViewHolder {
            ProfileCommentContract.View.ViewHolder setPostTitle(String postTitle);
            ProfileCommentContract.View.ViewHolder setPostText(String postText);
            ProfileCommentContract.View.ViewHolder setUserImage(Bitmap image);
            ProfileCommentContract.View.ViewHolder setUserName(String userName);
            ProfileCommentContract.View.ViewHolder setLikeCount(String likeCount);
            ProfileCommentContract.View.ViewHolder setDate(String date);
            ProfileCommentContract.View.ViewHolder setLikeState(LikeState likeState);
            ProfileCommentContract.View.ViewHolder setUserImageFromString(String image);
            ProfileCommentContract.View.ViewHolder hideDate();
            ProfileCommentContract.View.ViewHolder formatDate(String time);
        }
    }

    interface Presenter extends BasePresenter {
        void displayPost() throws JSONException;
        void formPostTitle() throws JSONException;
        void changeUpvoteLikeState(MySingleton singleton, String activityid, String posterid, View.ViewHolder holder) throws JSONException;
        void changeDownvoteLikeState(MySingleton singleton, String activityid, String posterid, View.ViewHolder holder) throws JSONException;
        void updateLikeCount(String likeCount) throws JSONException;
        void displayImages() throws JSONException;
        void loadUserCommentsAndImages(MySingleton instance, String currentUser);

        interface AdapterAPI {
            void onBindProfileCommentViewHolderAtPosition(
                    ProfileCommentContract.View.ViewHolder profileCommentViewHolder, int i);
            int getItemCount();
        }

        interface ViewHolderAPI {
        }
    }
}
