package ca.gc.inspection.scoop.ReplyPost;


import android.graphics.Bitmap;

import com.android.volley.AuthFailureError;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.Map;

import ca.gc.inspection.scoop.base.BasePresenter;
import ca.gc.inspection.scoop.base.BaseView;

/**
 * A contract between the View and Presenter for the replying to a post
 * action. This contract communicates the how the View and Presenter will
 * interact with each other.
 */

public interface ReplyPostContract {

    interface View extends BaseView<Presenter> {
        void setPostText(String postText, ReplyPostViewHolder holder);
        void setPostTitle(String postTitle, ReplyPostViewHolder holder);
        void setUserName(String userName, ReplyPostViewHolder holder);
        void setLikeCount(String likeCount, ReplyPostViewHolder holder);
        void setDate(String date, ReplyPostViewHolder holder);
        void setLikeNeutralState(ReplyPostViewHolder holder);
        void setLikeUpvoteState(ReplyPostViewHolder holder);
        void setLikeDownvoteState(ReplyPostViewHolder holder);
        void hideDate(ReplyPostViewHolder holder);
        void setUserImage(Bitmap image, ReplyPostViewHolder holder);

        void setRecyclerView(JSONArray comments, JSONArray images);
    }

    interface Presenter extends BasePresenter {
        void displayPost() throws JSONException;
        void formPostTitle() throws JSONException;
        void checkFullName() throws JSONException;
        String checkFirstName(String firstName);
        String checkLastName(String lastName);
        void formatDate(String time);
        String checkLikeCount(String likeCount);
        void checkLikeState(String likeState);
        void changeUpvoteLikeState(String activityid, String posterid) throws JSONException;
        void changeDownvoteLikeState(String activityid, String posterid) throws JSONException;
        void updateLikeCount(String likeCount) throws JSONException;
        void updateLikes(final String likeType, final String activityid, final String posterid) throws JSONException;
        void insertLikes(final String likeType, final String activityid, final String posterid) throws JSONException;
        void displayImages() throws JSONException;
        void formatImage(String image);

        void getUserComments(final String userid);
    }
}
