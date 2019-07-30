package ca.gc.inspection.scoop.editpost;

import android.support.annotation.NonNull;

import org.json.JSONObject;

import ca.gc.inspection.scoop.base.BasePresenter;
import ca.gc.inspection.scoop.base.BaseView;
import ca.gc.inspection.scoop.createpost.CreatePostContract;
import ca.gc.inspection.scoop.util.NetworkUtils;

public interface EditPostContract extends CreatePostContract {
    /**
     * Defines the interaction between the View and Presenter of EditPostContract
     */

    interface View extends CreatePostContract.View {

        void onDatabaseImageResponse(String image);
    }

    interface Presenter extends CreatePostContract.Presenter {

        void getPostImage(NetworkUtils network, String activityId);
    }
}
