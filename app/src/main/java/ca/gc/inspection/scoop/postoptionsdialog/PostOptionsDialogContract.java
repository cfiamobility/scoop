package ca.gc.inspection.scoop.postoptionsdialog;

import ca.gc.inspection.scoop.base.BasePresenter;
import ca.gc.inspection.scoop.base.BaseView;
import ca.gc.inspection.scoop.util.NetworkUtils;


/**
 * This interface is the contract that communicates the methods between the Post Options Dialog View
 * (PostOptionsDialogFragment) and the Post Options Dialog Presenter (PostOptionsDialogPresenter)
 */
public interface PostOptionsDialogContract {

    /**
     * View interface implemented by PostOptionsDialogFragment
     */

    interface View extends BaseView<Presenter> {
        void setSaveResponseMessage(String message);
    }

    /**
     * Presenter interface implemented by PostOptionsDialogPresenter
     */
    interface Presenter extends BasePresenter {
        void savePost(NetworkUtils network, final String posterId, final String userid);
        void validSave(String response);
    }
}
