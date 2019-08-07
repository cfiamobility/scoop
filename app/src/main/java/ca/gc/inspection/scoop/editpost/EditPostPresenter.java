package ca.gc.inspection.scoop.editpost;

import android.support.annotation.NonNull;

import ca.gc.inspection.scoop.createpost.CreatePostPresenter;
import ca.gc.inspection.scoop.createpost.InteractorBundle;
import ca.gc.inspection.scoop.editleavedialog.EditLeaveEventListener;
import ca.gc.inspection.scoop.util.NetworkUtils;

import static com.google.gson.internal.$Gson$Preconditions.checkNotNull;

public class EditPostPresenter extends CreatePostPresenter implements
        EditPostContract.Presenter,
        EditLeaveEventListener.Presenter {
    /**
     * Implements the Presenter in the EditPostContract interface to follow MVP architecture.
     *
     */

    private EditPostInteractor mInteractor;
    private EditPostContract.View mView;

    EditPostPresenter(@NonNull EditPostContract.View view) {
        mInteractor = new EditPostInteractor(this);
        mView = checkNotNull(view);
    }

    /*** sendPostToDatabase
     * Simple Post request to store the newly created Post to the postcomment table
     *
     * @param network NetworkUtils
     * @param activityId
     * @param title     user inputted title (Mandatory)
     * @param text      user inputted test (Mandatory)
     * @param imageBitmap   user inputted image (Optional)
     */
    @Override
    public void sendPostToDatabase(NetworkUtils network, final String activityId, final String title, final String text, final String imageBitmap) {
        mInteractor.sendPostToDatabase(network, activityId, title, text, imageBitmap);
    }

    @Override
    public void getPostImage(NetworkUtils network, String activityId) {
        mInteractor.getPostImage(network, activityId);
    }

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

    /**
     * Calls EditLeaveEventListener.View
     * The cost of this extra method invocation is reasonable to follow MVP architecture
     * @return if there are unsaved edits
     */
    @Override
    public boolean unsavedEditsExist() {
        return mView.unsavedEditsExist();
    }
}
