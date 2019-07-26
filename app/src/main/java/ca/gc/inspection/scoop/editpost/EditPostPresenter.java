package ca.gc.inspection.scoop.editpost;

import android.support.annotation.NonNull;

import ca.gc.inspection.scoop.createpost.CreatePostPresenter;
import ca.gc.inspection.scoop.util.NetworkUtils;

import static com.google.gson.internal.$Gson$Preconditions.checkNotNull;

public class EditPostPresenter extends CreatePostPresenter implements EditPostContract.Presenter {
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
     * @param userId    current user's userid
     * @param title     user inputted title (Mandatory)
     * @param text      user inputted test (Mandatory)
     * @param imageBitmap   user inputted image (Optional)
     */
    @Override
    public void sendPostToDatabase(NetworkUtils network, final String userId, final String title, final String text, final String imageBitmap) {
        mInteractor.sendPostToDatabase(network, userId, title, text, imageBitmap);
    }

    public void onPostCreated(boolean success) {
        mView.onDatabaseResponse(success);
    }
}
