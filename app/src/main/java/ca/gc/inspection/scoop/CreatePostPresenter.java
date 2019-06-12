package ca.gc.inspection.scoop;

import android.support.annotation.NonNull;

import static com.google.gson.internal.$Gson$Preconditions.checkNotNull;

public class CreatePostPresenter implements CreatePostContract.Presenter {

    private CreatePostInteractor mInteractor;
    private CreatePostContract.View mView;

    CreatePostPresenter(@NonNull CreatePostContract.View view) {
        mInteractor = new CreatePostInteractor(this);
        mView = checkNotNull(view);
    }

    @Override
    public void start() {
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
}
