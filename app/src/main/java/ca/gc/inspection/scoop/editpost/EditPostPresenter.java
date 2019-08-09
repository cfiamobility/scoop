package ca.gc.inspection.scoop.editpost;

import android.support.annotation.NonNull;

import ca.gc.inspection.scoop.createpost.CreatePostPresenter;
import ca.gc.inspection.scoop.createpost.InteractorBundle;
import ca.gc.inspection.scoop.editleavedialog.EditLeaveEventListener;
import ca.gc.inspection.scoop.util.NetworkUtils;

import static com.google.gson.internal.$Gson$Preconditions.checkNotNull;

public class EditPostPresenter extends CreatePostPresenter implements
        EditPostContract.Presenter {
    /**
     * Implements the Presenter in the EditPostContract interface to follow MVP architecture.
     *
     */

    private EditPostInteractor mInteractor;
    private EditPostContract.View mView;

    EditPostPresenter(@NonNull EditPostContract.View view) {
        setInteractor(new EditPostInteractor(this));
        setView(view);
    }

    /**
     * set parent view as a casted down version without the parent creating a new object
     *
     * @param view
     */
    public void setView(@NonNull EditPostContract.View view) {
        super.setView(view);
        mView = checkNotNull(view);
    }

    /**
     * set parent interactor as a casted down version without the parent creating a new object
     *
     * @param interactor    Handles network access.
     */
    public void setInteractor(@NonNull EditPostInteractor interactor) {
        super.setInteractor(interactor);
        mInteractor = checkNotNull(interactor);
    }

    /*** sendPostToDatabase
     * Simple Post request to store the newly created Post to the postcomment table
     *
     * @param network   An instance of the singleton class which encapsulates the RequestQueue
     * @param activityId    unique identifier of a post
     * @param title     user inputted title (Mandatory)
     * @param text      user inputted test (Mandatory)
     * @param imageBitmap   user inputted image (Optional)
     */
    @Override
    public void sendPostToDatabase(NetworkUtils network, final String activityId, final String title, final String text, final String imageBitmap) {
        mInteractor.sendPostToDatabase(network, activityId, title, text, imageBitmap);
    }

    /**
     * Allows the View to access the Interactor and retrieve the post image (which is missing from the bundle)
     * when starting EditPostActivity.
     *
     * @param network       An instance of the singleton class which encapsulates the RequestQueue
     * @param activityId    Unique identifier of a post
     */
    @Override
    public void getPostImage(NetworkUtils network, String activityId) {
        mInteractor.getPostImage(network, activityId);
    }

    /**
     * Routes the database callback to the View layer to set the post image from the database image
     * response.
     *
     * @param image String from the database.
     */
    public void onDatabaseImageResponse(String image) {
        mView.onDatabaseImageResponse(image);
    }

    /**
     * Callback for the database response when editing a post.
     * Routes to the view to handle Android UI changes.
     *
     * @param success           True if the post was edited
     * @param interactorBundle  Data class not used but must be present to implement PostRequestReceiver
     */
    @Override
    public void onDatabaseResponse(boolean success, InteractorBundle interactorBundle) {
        mView.onDatabaseResponse(success);
    }
}
