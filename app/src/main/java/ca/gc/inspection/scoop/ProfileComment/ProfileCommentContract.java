package ca.gc.inspection.scoop.ProfileComment;


import android.graphics.Bitmap;

import org.json.JSONArray;
import org.json.JSONException;

import ca.gc.inspection.scoop.base.BasePresenter;
import ca.gc.inspection.scoop.base.BaseView;

/**
 * A contract between the View and Presenter for the replying to a post
 * action. This contract communicates the how the View and Presenter will
 * interact with each other.
 */

public interface ProfileCommentContract {

    interface View extends BaseView<Presenter> {
        void setPostText(String postText, ProfileCommentViewHolder holder);
        void setPostTitle(String postTitle, ProfileCommentViewHolder holder);
        void setUserName(String userName, ProfileCommentViewHolder holder);
        void setLikeCount(String likeCount, ProfileCommentViewHolder holder);
        void setDate(String date, ProfileCommentViewHolder holder);
        void setLikeNeutralState(ProfileCommentViewHolder holder);
        void setLikeUpvoteState(ProfileCommentViewHolder holder);
        void setLikeDownvoteState(ProfileCommentViewHolder holder);
        void hideDate(ProfileCommentViewHolder holder);
        void setUserImage(Bitmap image, ProfileCommentViewHolder holder);

        void setRecyclerView(JSONArray comments, JSONArray images);
    }

    interface Presenter extends BasePresenter {
        void displayPost() throws JSONException;
        void formPostTitle() throws JSONException;
        void checkFullName() throws JSONException;
        void changeUpvoteLikeState(String activityid, String posterid) throws JSONException;
        void changeDownvoteLikeState(String activityid, String posterid) throws JSONException;
        void updateLikeCount(String likeCount) throws JSONException;
//        void updateLikes(final String likeType, final String activityid, final String posterid) throws JSONException;
//        void insertLikes(final String likeType, final String activityid, final String posterid) throws JSONException;
        void displayImages() throws JSONException;


        String checkFirstName(String firstName);
        String checkLastName(String lastName);
        void formatDate(String time);
        String checkLikeCount(String likeCount);
        void checkLikeState(String likeState);
        void formatImage(String image);
//        void getUserComments(final String userid);

//        void setPresenterView (ProfileCommentContract.View profileCommentView);
//        ProfileCommentContract.View getPresenterView ();
//        void setPresenterInteractor (ProfileCommentInteractor profileCommentInteractor);
    }
}
