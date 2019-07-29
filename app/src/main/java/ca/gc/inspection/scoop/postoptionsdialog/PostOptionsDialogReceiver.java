package ca.gc.inspection.scoop.postoptionsdialog;

public interface PostOptionsDialogReceiver {
    interface DeleteCommentReceiver {
        void onDeletePostComment(boolean isPost);
    }

    interface EditCommentReceiver {
        void edit();
    }
}