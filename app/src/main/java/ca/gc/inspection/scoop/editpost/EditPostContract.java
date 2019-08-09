package ca.gc.inspection.scoop.editpost;

import android.support.annotation.NonNull;

import org.json.JSONObject;

import ca.gc.inspection.scoop.base.BasePresenter;
import ca.gc.inspection.scoop.base.BaseView;
import ca.gc.inspection.scoop.createpost.CreatePostContract;
import ca.gc.inspection.scoop.editcomment.EditCommentContract;
import ca.gc.inspection.scoop.editleavedialog.EditLeaveEventListener;
import ca.gc.inspection.scoop.util.NetworkUtils;

/**
 * Defines the interaction between the View and Presenter for the Edit Post action case.
 */
public interface EditPostContract extends CreatePostContract {

    interface View extends CreatePostContract.View {

        void onDatabaseImageResponse(String image);

        boolean unsavedEditsExist();
    }

    interface Presenter extends CreatePostContract.Presenter {

        void getPostImage(NetworkUtils network, String activityId);
    }
}
