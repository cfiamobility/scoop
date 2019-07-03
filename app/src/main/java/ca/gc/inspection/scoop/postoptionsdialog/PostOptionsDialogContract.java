package ca.gc.inspection.scoop.postoptionsdialog;

import ca.gc.inspection.scoop.base.BasePresenter;
import ca.gc.inspection.scoop.base.BaseView;
import ca.gc.inspection.scoop.util.NetworkUtils;

public interface PostOptionsDialogContract {
    interface View extends BaseView<Presenter> {

    }

    interface Presenter extends BasePresenter {

        void savePost(NetworkUtils network, final String posterId, final String userid);
//        void loadDataFromDatabase(NetworkUtils network, String currentUser);

    }
}
