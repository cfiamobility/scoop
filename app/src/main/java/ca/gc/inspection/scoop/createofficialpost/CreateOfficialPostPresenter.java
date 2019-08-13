package ca.gc.inspection.scoop.createofficialpost;

import android.graphics.Bitmap;
import android.support.annotation.NonNull;

import ca.gc.inspection.scoop.base.BasePresenter;
import ca.gc.inspection.scoop.createpost.CreatePostPresenter;
import ca.gc.inspection.scoop.createpost.InteractorBundle;
import ca.gc.inspection.scoop.createpost.PostRequestReceiver;
import ca.gc.inspection.scoop.util.NetworkUtils;

import static com.google.gson.internal.$Gson$Preconditions.checkNotNull;

public class CreateOfficialPostPresenter implements
        CreateOfficialPostContract.Presenter,
        PostRequestReceiver {
    /**
     * Implements the Presenter in the CreateOfficialPostContract interface to follow MVP architecture.
     *
     */

    private CreateOfficialPostInteractor mInteractor;
    private CreateOfficialPostContract.View mView;

    CreateOfficialPostPresenter(@NonNull CreateOfficialPostContract.View view) {
        setInteractor(new CreateOfficialPostInteractor(this));
        setView(view);
    }

    /**
     * @param view
     */
    public void setView(@NonNull CreateOfficialPostContract.View view) {
        mView = checkNotNull(view);
    }

    /**
     * @param interactor    Handles network access.
     */
    public void setInteractor(@NonNull CreateOfficialPostInteractor interactor) {
        mInteractor = checkNotNull(interactor);
    }

    /*** sendPostToDatabase
     * Simple Post request to store the newly created Post to the postcomment table
     *
     * @param network   An instance of the singleton class which encapsulates the RequestQueue
     */
    @Override
    public void sendPostToDatabase(NetworkUtils network, String postTitle, int buildingId, int reasonForClosure, int actionRequired) {
        mInteractor.sendPostToDatabase(network, postTitle, buildingId, reasonForClosure, actionRequired);
    }

    /**
     * Callback for the database response when creating an official BCP post.
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
