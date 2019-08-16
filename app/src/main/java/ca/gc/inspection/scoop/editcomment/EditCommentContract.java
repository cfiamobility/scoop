package ca.gc.inspection.scoop.editcomment;

import ca.gc.inspection.scoop.base.BasePresenter;
import ca.gc.inspection.scoop.base.BaseView;
import ca.gc.inspection.scoop.postcomment.PostCommentContract;

/**
 * Defines the relationship between the View and Presenter for action cases which implement the
 * edit post comment feature. Currently, only display post allows editing comments.
 */
public class EditCommentContract {

    public interface View extends BaseView<Presenter> {

        /**
         * Interaction between View and Presenter only occurs between the ViewHolder and the Presenter's
         * ViewHolderAPI methods. Nonetheless, the View and Presenter interfaces are provided to remain
         * consistent with MVP architecture.
         */
        interface ViewHolder {

            /**
             * Called during onBind if there are unsaved edits in the EditCommentCache which is relevant to
             * this ViewHolder.
             *
             * @param postText
             */
            ViewHolder setEditPostText(String postText);

            /**
             * Hides the UI for editing the comment text.
             */
            void hideEditText();

            /**
             * Getter for call back identifier.
             *
             * When editing a post comment there is a brief period between sending the comment to the database
             * and receiving the database response. Upon the database response, the presenter needs to know
             * if the ViewHolder is still relevant or if it is displaying a different post comment.
             *
             * If the ViewHolder was scrolled and recycled, we don't want it to update UI as it may be
             * showing a completely different post comment. Instead the correct view holder will eventually
             * have its UI updated using the onBind methods and Presenter-scoped Cache objects.
             *
             * @return String of unique identifier for post comment (ie. activityId)
             */
            String getCallBackIdentifier();

            /**
             * Setter for call back identifier. This is set by the Presenter prior to editing the comment.
             *
             * See documentation for getCallBackIdentifier.
             *
             * @param callBackIdentifier    String of unique identifier for post comment (ie. activityId)
             */
            void setCallBackIdentifier(String callBackIdentifier);

            /**
             * Clears the call back identifier every time we bind new data to the ViewHolder. This occurs
             * when scrolling or refreshing.
             *
             * See documentation for getCallBackIdentifier.
             */
            void clearCallBackIdentifier();

            /**
             * Used by the Presenter's onBind methods to persist whether the post comment is waiting
             * for a database response.
             *
             * @param waitingForResponse    This specific post comment cannot send another database request
             *                              until a response is received.
             */
            void setWaitingForResponse(boolean waitingForResponse);

            /**
             * Used by the Presenter's onBind methods to persist Snackbar for comment in progress.
             *
             * @param activityId        Unique indentifier of post comment.
             */
            void setSnackBarForCommentInProgress(String activityId);

            /**
             * Used by the Presenter's onBind methods to persist Snackbar for comment success.
             *
             * @param activityId        Unique indentifier of post comment.
             */
            void setSnackBarEditCommentSuccess(String activityId);

            /**
             * Used by the Presenter's onBind methods to persist Snackbar for comment retry.
             *
             * @param i                 adapter position
             * @param activityId        Unique indentifier of post comment.
             */
            void setSnackBarEditCommentRetry(int i, String activityId);
        }
    }

    public interface Presenter extends BasePresenter {

        interface ViewHolderAPI {

            /**
             * Called by PostCommentViewHolder to save the edit for a post comment to the database.
             *
             * @param viewHolderInterface   Interface to update when the database response is received. Note that a
             *                              callback identifier must be set (in this case, the activityId) so that
             *                              when the Presenter receives the database response, it knows if the ViewHolder
             *                              is still the correct one. If the ViewHolder was scrolled and recycled, we don't
             *                              want it to update UI as it may be showing a completely different post comment.
             *                              Instead the correct view holder will eventually have its UI updated using the
             *                              onBind methods and Presenter-scoped Cache objects.
             *
             * @param i                     Estimated adapter position. This may shift if comments are added/deleted
             *                              or if the user initiates a pull down to refresh while the viewholder is waiting
             *                              for a database response for editing a comment. Nonetheless, this value
             *                              provides a starting point to search by activityId - it reduces the need
             *                              for an O(n) scan of the DataCache to locate the correct item to update by
             *                              activityId.
             *
             * @param activityId            Unique identifier for the post comment.
             *
             * @param newText               The new text to update the post comment to.
             */
            void sendCommentToDatabase(PostCommentContract.View.ViewHolder viewHolderInterface, int i, String activityId, String newText);

            /**
             * Need to store the unsaved changes in the EditCommentCache so that when the user scrolls through
             * the RecyclerView when editing a comment, the correct data can be re-attached to the view holder.
             *
             * @param activityId    Unique identifier of a post comment. Adapter position may shift and cannot be
             *                      used here.
             * @param postText      The current unsaved text.
             */
            void cacheEditCommentData(String activityId, String postText);

            /**
             * When the user cancels their edit for a specific post comment, we must drop the data from the
             * EditCommentCache and ViewHolderStateCache, otherwise when the user scrolls through the RecyclerView,
             * the dropped changes and irrelevant SnackBar states may show up. It is not enough to set the
             * ViewHolder's waitingForResponse to False, we must update the ViewHolderStateCache to prevent
             * onBind methods from resetting the ViewHolderState to a stale value.
             *
             * @param activityId    Unique identifier of a post comment. Adapter position may shift and cannot be
             *                      used here.
             */
            void onCancelEditComment(String activityId);

            /**
             * Need to update the ViewHolderStateCache otherwise the SnackBar will be reshown every time
             * onBind is called on the ViewHolder.
             *
             * @param activityId    Unique identifier for post comment
             */
            void onSnackBarDismissed(String activityId);
        }
    }
}
