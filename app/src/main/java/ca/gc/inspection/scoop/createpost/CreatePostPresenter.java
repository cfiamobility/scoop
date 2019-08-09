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
        mInteractor = new CreatePostInteractor(this);
        mView = checkNotNull(view);
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

    public void setUserProfileImage(Bitmap profileImage) {mView.setUserProfileImage(profileImage);}

    public void getUserProfileImage(NetworkUtils network) {mInteractor.getUserProfileImage(network);}
}
