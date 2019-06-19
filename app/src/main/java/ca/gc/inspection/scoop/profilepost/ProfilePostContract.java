package ca.gc.inspection.scoop.profilepost;

import org.json.JSONException;

import ca.gc.inspection.scoop.MySingleton;
import ca.gc.inspection.scoop.profilecomment.ProfileCommentContract;
import ca.gc.inspection.scoop.base.BasePresenter;
import ca.gc.inspection.scoop.base.BaseView;

/**
 * Interface that inherits from mostgeneric interface
 */
public interface ProfilePostContract extends ProfileCommentContract {

    interface View extends BaseView<Presenter> {
        void setDisplayPostListener(ProfilePostViewHolder holder);

        interface ViewHolder extends ProfileCommentContract.View.ViewHolder {
            ViewHolder setPostTitle(String postTitle);
            ViewHolder setCommentCount(String commentCount);
        }
    }

    interface Presenter extends ProfileCommentContract.Presenter {
        interface AdapterAPI extends ProfileCommentContract.Presenter.AdapterAPI {

        }
    }
}
