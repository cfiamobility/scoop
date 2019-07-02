package ca.gc.inspection.scoop.postcomment;

import org.json.JSONException;

import ca.gc.inspection.scoop.base.BasePresenter;
import ca.gc.inspection.scoop.base.BaseView;
import ca.gc.inspection.scoop.util.NetworkUtils;

/**
 * A contract between the View (layer) and Presenter for the replying to a post
 * action. This contract communicates the how the View and Presenter will
 * interact with each other.
 *
 * Since RecyclerView is being used in this package, we must additionally specify the interaction
 * between sub-Views (Adapter/ViewHolder) and the Presenter (via AdapterAPI/ViewHolderAPI).
 *
 * View = PostCommentFragment - is the main View responsible for creating the Presenter and Adapter
 *      View.Adapter = PostCommentAdapter is the subview responsible for creating ViewHolders
 *      View.ViewHolder = PostCommentViewHolder is the subview which displays an individual profile comment
 *
 * Presenter = PostCommentPresenter - there's only one object which implements:
 *      Presenter.AdapterAPI - used by the Adapter to call Presenter methods
 *      Presenter.ViewHolderAPI - used by the ViewHolders to call Presenter methods
 *
 * If communication is required between the View layer and Presenter layer, interface methods must be used.
 *      - if the Presenter needs the View and vice-versa, the interface should be passed in.
 * If communication is required within the View only or Presenter only, the object itself should be passed in
 * to avoid leaking access to internal methods in the contract.
 *
 * Inheritance hierarchy for Posts/Comments:
 *      PostComment - base comment containing profile image, username, text, like/dislike functionality
 *      ^
 *      ProfileComment - extends above but with post title
 *      ^
 *      ProfilePost - extends above but overrides post title and contains comment count
 *      ^
 *      FeedPost - extends above but contains
 */

public interface PostCommentContract {

    interface View extends BaseView<Presenter> {
        /**
         * Implemented by the main View (ie. PostCommentFragment).
         * Methods specified here in the View but not in the nested View.Adapter and View.ViewHolder interfaces
         * explain how the Presenter is to communicate with the main View only.
         */

        interface Adapter {
            void refreshAdapter();
        }

        interface ViewHolder {
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

        void loadDataFromDatabase(NetworkUtils network, String currentUser);

        interface AdapterAPI {
            void setAdapter(PostCommentContract.View.Adapter adapter);
            void onBindViewHolderAtPosition(
                    PostCommentContract.View.ViewHolder postCommentViewHolder, int i);
            int getItemCount();
            String getPosterIdByIndex(int i);   // TODO can move into ViewHolderAPI
            String getActivityIdByIndex(int i); // TODO can move into ViewHolderAPI
        }

        interface ViewHolderAPI {
            void changeUpvoteLikeState(NetworkUtils network, View.ViewHolder viewHolderInterface, int i) throws JSONException;
            void changeDownvoteLikeState(NetworkUtils network, View.ViewHolder viewHolderInterface, int i) throws JSONException;
        }

    }
}
