package ca.gc.inspection.scoop.profilepost;

import org.json.JSONException;

import ca.gc.inspection.scoop.MySingleton;
import ca.gc.inspection.scoop.profilecomment.ProfileCommentContract;
import ca.gc.inspection.scoop.base.BasePresenter;
import ca.gc.inspection.scoop.base.BaseView;

/**
 * Interface that inherits from mostgeneric interface
 */
public interface ProfilePostContract {

    interface View extends ProfileCommentContract.View {

        interface Adapter extends ProfileCommentContract.View.Adapter {
        }

        interface ViewHolder extends ProfileCommentContract.View.ViewHolder {
            ViewHolder setPostTitle(String postTitle);
            ViewHolder setCommentCount(String commentCount);
        }
    }

    interface Presenter extends ProfileCommentContract.Presenter {
        interface AdapterAPI extends ProfileCommentContract.Presenter.AdapterAPI {
            void setAdapter(ProfilePostContract.View.Adapter adapter);
        }

        interface ViewHolderAPI extends ProfileCommentContract.Presenter.ViewHolderAPI {
        }
    }
}
