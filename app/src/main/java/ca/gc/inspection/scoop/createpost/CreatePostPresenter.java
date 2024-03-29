package ca.gc.inspection.scoop.createpost;

import android.graphics.Bitmap;
import android.support.annotation.NonNull;

import org.json.JSONObject;

import ca.gc.inspection.scoop.util.NetworkUtils;
import static com.google.gson.internal.$Gson$Preconditions.checkNotNull;

public class CreatePostPresenter implements CreatePostContract.Presenter, PostRequestReceiver {
    /**
     * Implements the Presenter in the CreatePostContract interface to follow MVP architecture.
     *
     */
    private CreatePostInteractor mInteractor;
    private CreatePostContract.View mView;

    protected CreatePostPresenter() {
    }

    CreatePostPresenter(@NonNull CreatePostContract.View view) {
        setInteractor(new CreatePostInteractor(this));
        setView(view);
    }

    /**
     * Can be called by child Presenter to set the parent's view as a casted down version without
     * the parent creating a new object
     *
     * @param view
     */
    public void setView(@NonNull CreatePostContract.View view) {
        mView = checkNotNull(view);
    }

    /**
     * Can be called by child Presenter to set the parent's interactor as a casted down version without
     * the parent creating a new object
     *
     * @param interactor    Handles network access
     */
    public void setInteractor(@NonNull CreatePostInteractor interactor) {
        mInteractor = checkNotNull(interactor);
    }

    /*** sendPostToDatabase
     * Simple Post request to store the newly created Post to the postcomment table
     *
     * @param network NetworkUtils
     * @param userId    current user's userid
     * @param title     user inputted title (Mandatory)
     * @param text      user inputted test (Mandatory)
     * @param imageBitmap   user inputted image (Optional)
     */
    @Override
    public void sendPostToDatabase(NetworkUtils network, final String userId, final String title, final String text, final String imageBitmap) {
        mInteractor.sendPostToDatabase(network, userId, title, text, imageBitmap);
    }

    /**
     * Callback for the database response when creating a post.
     * Routes to the view to handle Android UI changes.
     *
     * @param success           True if the post was created
     * @param interactorBundle  Data class not used but must be present to implement PostRequestReceiver
     */
    @Override
    public void onDatabaseResponse(boolean success, InteractorBundle interactorBundle) {
        mView.onDatabaseResponse(success);
    }

    public void setUserProfileImageFromDatabaseResponse(Bitmap profileImage) {mView.setUserProfileImage(profileImage);}

    @Override
    public void getUserProfileImage(NetworkUtils network) {mInteractor.getUserProfileImage(network);}
}
