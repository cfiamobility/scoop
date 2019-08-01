package ca.gc.inspection.scoop.postoptionsdialog;

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