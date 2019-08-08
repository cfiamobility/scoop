package ca.gc.inspection.scoop.postoptionsdialog;


/**
 * Implemented by the View layer to provide callback methods for the PostOptionsDialog.
 */
public interface PostOptionsDialogReceiver {
    interface DeleteCommentReceiver {
        void onDeletePostComment(boolean isPost);
    }

    interface EditCommentReceiver {
        void onEditComment(int i, String activityId);

        void dismissSnackBar();
    }

    interface EditPostReceiver {
        void onEditPost(int i);
    }
}