package ca.gc.inspection.scoop.editpost;

import android.support.annotation.NonNull;

import ca.gc.inspection.scoop.base.BasePresenter;
import ca.gc.inspection.scoop.base.BaseView;
import ca.gc.inspection.scoop.createpost.CreatePostContract;
import ca.gc.inspection.scoop.util.NetworkUtils;

public interface EditPostContract extends CreatePostContract {
    /**
     * Defines the interaction between the View and Presenter of EditPostContract
     */

    interface View extends CreatePostContract.View {

        void onDatabaseResponse(boolean success);

        void onDatabaseImageResponse(String image);
    }

    interface Presenter extends CreatePostContract.Presenter {

        void sendPostToDatabase(NetworkUtils network, final String userId, final String title, final String text, final String imageBitmap);

        void getPostImage(NetworkUtils network, String activityId);
    }
}
