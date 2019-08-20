package ca.gc.inspection.scoop.editpost;

import android.support.annotation.NonNull;

import org.json.JSONObject;

import ca.gc.inspection.scoop.base.BasePresenter;
import ca.gc.inspection.scoop.base.BaseView;
import ca.gc.inspection.scoop.createpost.CreatePostContract;
import ca.gc.inspection.scoop.editcomment.EditCommentContract;
import ca.gc.inspection.scoop.editleavedialog.EditLeaveEventListener;
import ca.gc.inspection.scoop.util.NetworkUtils;

/**
 * Defines the interaction between the View and Presenter for the Edit Post action case.
 */
public interface EditPostContract extends CreatePostContract {

    /**
     * Defines the View in the EditPostContract interface to follow MVP architecture.
     * Allows the user to create a new post by adding a title, text, and using the camera or camera roll
     * to add an image.
     */
    interface View extends CreatePostContract.View {

        /**
         * Callback to set post image from database response.
         * The bundle did not contain a post image, so we fetch it from the database and handle
         * the response here.
         *
         * @param image post image from the database
         */
        void onDatabaseImageResponse(String image);

        /**
         * Called by the Presenter to know if their are unsaved edits.
         *
         * @return whether the image, post title, and/or post text was modified.
         */
        boolean unsavedEditsExist();
    }

    /**
     * Defines the Presenter in the EditPostContract interface to follow MVP architecture.
     */
    interface Presenter extends CreatePostContract.Presenter {

        /**
         * Allows the View to access the Interactor and retrieve the post image (which is missing from the bundle)
         * when starting EditPostActivity.
         *
         * @param network       An instance of the singleton class which encapsulates the RequestQueue
         * @param activityId    Unique identifier of a post
         */
        void getPostImage(NetworkUtils network, String activityId);
    }
}
