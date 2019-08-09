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
        void onDatabaseResponse(boolean success);
        void setUserProfileImage(Bitmap profileImage);
    }

    interface Presenter extends BasePresenter {
        void sendPostToDatabase(NetworkUtils network, final String userId, final String title, final String text, final String imageBitmap);
        void setUserProfileImage(Bitmap profileImage);
        void getUserProfileImage(NetworkUtils network);
    }
}
