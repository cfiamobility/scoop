package ca.gc.inspection.scoop.createpost;

import ca.gc.inspection.scoop.util.NetworkUtils;
import ca.gc.inspection.scoop.base.BasePresenter;
import ca.gc.inspection.scoop.base.BaseView;

public interface CreatePostContract {
    /**
     * Defines the interaction between the View and Presenter of CreatePostContract
     */

    interface View extends BaseView<Presenter> {

        void onPostCreated(boolean success);
    }

    interface Presenter extends BasePresenter {

        void sendPostToDatabase(NetworkUtils network, final String userId, final String title, final String text, final String imageBitmap);
    }
}
