package ca.gc.inspection.scoop.profilecomment;

import org.json.JSONException;

import ca.gc.inspection.scoop.MySingleton;
import ca.gc.inspection.scoop.base.BasePresenter;
import ca.gc.inspection.scoop.base.BaseView;

/**
 * A contract between the View (layer) and Presenter for the replying to a post
 * action. This contract communicates the how the View and Presenter will
 * interact with each other.
 *
 * Since RecyclerView is being used in this package, we must additionally specify the interaction
 * between sub-Views (Adapter/ViewHolder) and the Presenter (via AdapterAPI/ViewHolderAPI).
 *
 * View = ProfileCommentFragment - is the main View responsible for creating the Presenter and Adapter
 *      View.Adapter = ProfileCommentAdapter is the subview responsible for creating ViewHolders
 *      View.ViewHolder = ProfileCommentViewHolder is the subview which displays an individual profile comment
 *
 * Presenter = ProfileCommentPresenter - there's only one object which implements:
 *      Presenter.AdapterAPI - used by the Adapter to call Presenter methods
 *      Presenter.ViewHolderAPI - used by the ViewHolders to call Presenter methods
 *
 * If communication is required between the View layer and Presenter layer, interface methods must be used.
 *      - if the Presenter needs the View and vice-versa, the interface should be passed in.
 * If communication is required within the View only or Presenter only, the object itself should be passed in
 * to avoid leaking access to internal methods in the contract.
 *
 * ProfileCommentContract is the base contract and is extended by ProfilePostContract which
 * is further extended by FeedPostContract.
 */

public interface ProfileCommentContract {

    interface View extends BaseView<Presenter> {
        /**
         * Implemented by the main View (ie. ProfileCommentFragment).
         * Methods specified here in the View but not in the nested View.Adapter and View.ViewHolder interfaces
         * explain how the Presenter is to communicate with the main View only.
         */

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
