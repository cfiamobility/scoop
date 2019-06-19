package ca.gc.inspection.scoop.profilecomment;


import android.graphics.Bitmap;

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
        void changeUpvoteLikeState(MySingleton singleton, View.ViewHolder viewHolderInterface, int i) throws JSONException;
        void changeDownvoteLikeState(MySingleton singleton, View.ViewHolder viewHolderInterface, int i) throws JSONException;
        void loadUserCommentsAndImages(MySingleton instance, String currentUser);

        interface AdapterAPI {
            void onBindProfileCommentViewHolderAtPosition(
                    ProfileCommentContract.View.ViewHolder profileCommentViewHolder, int i);
            int getItemCount();

            String getPosterIdByIndex(int i);
        }

        interface ViewHolderAPI {
        }
    }
}
