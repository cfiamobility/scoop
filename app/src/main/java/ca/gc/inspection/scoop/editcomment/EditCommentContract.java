package ca.gc.inspection.scoop.editcomment;

import ca.gc.inspection.scoop.base.BasePresenter;
import ca.gc.inspection.scoop.base.BaseView;
import ca.gc.inspection.scoop.editleavedialog.EditLeaveEventListener;
import ca.gc.inspection.scoop.postcomment.PostCommentContract;

/**
 * Defines the relationship between the View and Presenter
 */
public class EditCommentContract {

    public interface View extends BaseView<Presenter> {

        interface ViewHolder {

            ViewHolder setEditPostText(String postText);

            void hideEditText();

            String getCallBackIdentifier();

            void setCallBackIdentifier(String activityId);

            void clearCallBackIdentifier();

            void setWaitingForResponse(boolean waitingForResponse);

            void setSnackBarForCommentInProgress(String activityId);

            void setSnackBarEditCommentSuccess(String activityId);

            void setSnackBarEditCommentRetry(int i, String activityId);
        }
    }

    public interface Presenter extends BasePresenter {

        interface ViewHolderAPI {

            void sendCommentToDatabase(PostCommentContract.View.ViewHolder viewHolderInterface, int i, String activityId, String newText);

            void cacheEditCommentData(String activityId, String postText);

            void onCancelEditComment(String activityId);

            void onSnackBarDismissed(String activityId);
        }
    }
}
