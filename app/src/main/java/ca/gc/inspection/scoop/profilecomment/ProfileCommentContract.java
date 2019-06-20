package ca.gc.inspection.scoop.profilecomment;


import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;

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

        interface Adapter {
            void refreshAdapter();
        }

        interface ViewHolder {
            ViewHolder setPostTitle(String postTitle);
            ViewHolder setPostText(String postText);
            ViewHolder setUserName(String userName);
            ViewHolder setLikeCount(String likeCount);
            ViewHolder setDate(String date);
            ViewHolder setLikeState(LikeState likeState);
            ViewHolder setUserImageFromString(String image);
            ViewHolder hideDate();
        }
    }

    interface Presenter extends BasePresenter {

        void loadDataFromDatabase(MySingleton instance, String currentUser);

        interface AdapterAPI {
            void setAdapter(ProfileCommentContract.View.Adapter adapter);
            void onBindViewHolderAtPosition(
                    ProfileCommentContract.View.ViewHolder profileCommentViewHolder, int i);
            int getItemCount();

            String getPosterIdByIndex(int i);
        }

        interface ViewHolderAPI {
            void changeUpvoteLikeState(MySingleton singleton, View.ViewHolder viewHolderInterface, int i) throws JSONException;
            void changeDownvoteLikeState(MySingleton singleton, View.ViewHolder viewHolderInterface, int i) throws JSONException;
        }

    }
}
