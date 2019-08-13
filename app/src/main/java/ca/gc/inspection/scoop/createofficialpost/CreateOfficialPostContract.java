package ca.gc.inspection.scoop.createofficialpost;

import android.graphics.Bitmap;

import ca.gc.inspection.scoop.base.BasePresenter;
import ca.gc.inspection.scoop.base.BaseView;
import ca.gc.inspection.scoop.createpost.CreatePostContract;
import ca.gc.inspection.scoop.createpost.InteractorBundle;
import ca.gc.inspection.scoop.util.NetworkUtils;

/**
 * Defines the interaction between the View and Presenter for the Create Official BCP Post action case.
 */
public interface CreateOfficialPostContract {

    interface View extends BaseView<Presenter> {

        void setUserProfileImage(Bitmap profileImageBitmap);

        void onDatabaseResponse(boolean success);
    }

    interface Presenter extends BasePresenter {

        void sendPostToDatabase(NetworkUtils instance, String postTitle, int buildingId, int reasonForClosure, int actionRequired);

        void onDatabaseResponse(boolean success, InteractorBundle interactorBundle);

        void getUserProfileImage(NetworkUtils instance);
    }
}
