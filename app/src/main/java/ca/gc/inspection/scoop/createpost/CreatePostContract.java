package ca.gc.inspection.scoop.createpost;

import android.graphics.Bitmap;

import ca.gc.inspection.scoop.util.NetworkUtils;
import ca.gc.inspection.scoop.base.BasePresenter;
import ca.gc.inspection.scoop.base.BaseView;

public interface CreatePostContract {
    /**
     * Defines the interaction between the View and Presenter of CreatePostContract
     */

    interface View extends BaseView<Presenter> {
        /**
         * Updates View upon receiving response from server for creating a post. The method name
         * is generic because it is inherited by EditPostContract.
         *
         * @param success   True if the post was created (or edited in EditPostContract)
         */
        void onDatabaseResponse(boolean success);

        /**
         * Sets the profile image from the database response.
         * Called by Presenter's setUserProfileImageFromDatabaseResponse method
         *
         * @param profileImage      created from the database response
         */
        void setUserProfileImage(Bitmap profileImage);
    }

    interface Presenter extends BasePresenter {
        void sendPostToDatabase(NetworkUtils network, final String userId, final String title, final String text, final String imageBitmap);

        /**
         * When starting the create post activity, we need to retrieve to user's profile image to
         * display it.
         *
         * @param network   An instance of the singleton class which encapsulates the RequestQueue
         */
        void getUserProfileImage(NetworkUtils network);
    }
}
