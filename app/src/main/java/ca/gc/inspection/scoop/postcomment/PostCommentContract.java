package ca.gc.inspection.scoop.postcomment;

import org.json.JSONException;

import ca.gc.inspection.scoop.base.BaseView;
import ca.gc.inspection.scoop.editcomment.EditCommentContract;
import ca.gc.inspection.scoop.postoptionsdialog.PostOptionsDialogReceiver;
import ca.gc.inspection.scoop.util.TextFormat;

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
 *      ProfileLike - extends above but overrides post title and contains comment count
 *      ^
 *      ProfilePost - similar to above but used for profile posts
 *      ^
 *      FeedPost - extends above but contains a post image (in addition to the profile image)
 */

public interface PostCommentContract {

    interface View extends BaseView<Presenter> {
        /**
         * Implemented by the main View (ie. PostCommentFragment).
         * Methods specified here in the View but not in the nested View.Adapter and View.ViewHolder interfaces
         * explain how the Presenter is to communicate with the main View only.
         */

        void onLoadedDataFromDatabase();

        interface Adapter {

            void refreshAdapter();

        }

        interface ViewHolder extends
                PostOptionsDialogReceiver.EditCommentReceiver,
                EditCommentContract.View.ViewHolder {

            ViewHolder setPostText(String postText);
            ViewHolder setPostTextWithFormat(String postText, TextFormat textFormat);
            ViewHolder setUserName(String userName);
            ViewHolder setLikeCount(String likeCount);
            ViewHolder setDate(String date);
            ViewHolder setLikeState(LikeState likeState);
            ViewHolder setUserImageFromString(String image);
            ViewHolder hideDate();
            ViewHolder setSavedState(Boolean savedState);

            /**
             * Remove the TextEditorWatcher when setting the edit post text otherwise it may overwrite
             * the current contents of EditCommentCache.
             */
            void removeTextEditorWatcher();
        }
    }

    interface Presenter extends EditCommentContract.Presenter {

        void loadDataFromDatabase(String activityId);

        interface AdapterAPI {

            void setAdapter(PostCommentContract.View.Adapter adapter);
            void onBindViewHolderAtPosition(
                    PostCommentContract.View.ViewHolder postCommentViewHolder, int i);
            int getItemCount();
            String getPosterIdByIndex(int i);
            String getActivityIdByIndex(int i);
            Boolean getSavedStateByIndex(int i);

        }

        interface ViewHolderAPI extends EditCommentContract.Presenter.ViewHolderAPI {

            void changeUpvoteLikeState(View.ViewHolder viewHolderInterface, int i) throws JSONException;
            void changeDownvoteLikeState(View.ViewHolder viewHolderInterface, int i) throws JSONException;
            void updateSavedState(PostCommentContract.View.ViewHolder viewHolderInterface, int i) throws JSONException;

            /**
             * Check if unsaved edits exist for a specific post comment. Used to check whether the
             * PostCommentViewHolder should save the changes to the database through the Presenter or
             * show a SnackBar message stating that no changes were made.
             *
             * Disambiguation:
             * This is different from unsavedEditsExist (no parameters) which is used in an Activity/Fragment
             * to check if the user should be prompted to leave their unsaved edits.
             *
             * @param i             Estimated adapter position
             * @param activityId    Unique identifier for post comment
             * @return  True if there are unsaved edits in the EditCommentCache
             */
            boolean unsavedEditsExistForViewHolder(int i, String activityId);
            String getPostTextById(int i);
        }

    }
}
