package ca.gc.inspection.scoop.postoptionsdialog;

import ca.gc.inspection.scoop.base.BasePresenter;
import ca.gc.inspection.scoop.base.BaseView;

public interface PostOptionsDialogContract {
    interface View extends BaseView<PostOptionsDialogContract.Presenter> {

    }

    interface Presenter extends BasePresenter {

        void savePost();
    }
}
