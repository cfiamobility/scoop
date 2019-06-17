package ca.gc.inspection.scoop.ProfileComment;


import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;

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
        void hideDate(ProfileCommentViewHolder holder);
        void formatImage(String image, ProfileCommentViewHolder holder);
        void setRecyclerView(JSONArray comments, JSONArray images);
        void displayPostListener(ProfileCommentViewHolder holder, String activityid, String posterid);
        void displayImagesListener(ProfileCommentViewHolder holder);

//        MySingleton getSingleton();

        interface ViewHolder {

            ProfileCommentContract.View.ViewHolder setPostTitle(String postTitle);

            ProfileCommentContract.View.ViewHolder setPostText(String postText);

            ProfileCommentContract.View.ViewHolder setUserImage(Bitmap image);

            ProfileCommentContract.View.ViewHolder setUserName(String userName);

            ProfileCommentContract.View.ViewHolder setLikeCount(String likeCount);

            ProfileCommentContract.View.ViewHolder setDate(String date);

            ProfileCommentContract.View.ViewHolder setLikeState(LikeState likeState);
        }

    }

    interface Presenter extends BasePresenter {
        void displayPost() throws JSONException;
        void formPostTitle() throws JSONException;
        void checkFullName() throws JSONException;
        void changeUpvoteLikeState(MySingleton singleton, String activityid, String posterid) throws JSONException;
        void changeDownvoteLikeState(MySingleton singleton, String activityid, String posterid) throws JSONException;
        void updateLikeCount(String likeCount) throws JSONException;
        void displayImages() throws JSONException;


        String checkFirstName(String firstName);
        String checkLastName(String lastName);
        void formatDate(String time);
        String checkLikeCount(String likeCount);
        void checkLikeState(String likeState);

        void getUserComments(MySingleton instance, String currentUser);

        void setRecyclerView(JSONArray commentsResponse, JSONArray imagesResponse);

        void onBindProfileCommentViewHolderAtPosition(
                ProfileCommentContract.View.ViewHolder profileCommentViewHolder, int i);
//        void getRecyclerView(JSONArray posts, JSONArray images);
//        void getPosts(MySingleton singleton, final String userid);
//        void getUserComments(final String userid);

//        void setPresenterView (ProfileCommentContract.View profileCommentView);
//        ProfileCommentContract.View getPresenterView ();
//        void setPresenterInteractor (ProfileCommentInteractor profileCommentInteractor);
    }
}
