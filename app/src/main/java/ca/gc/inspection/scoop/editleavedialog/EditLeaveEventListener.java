package ca.gc.inspection.scoop.editleavedialog;


/**
 * Provides callback methods for EditLeaveDialog's buttons. Implemented by the View layer such as
 * an Activity, Fragment, or ViewHolder.
 */
public interface EditLeaveEventListener {

    /**
     * Variable length params are used as leaving EditPostActivity and DisplayPostActivity does not
     * require a return value when leaving unsaved edits but cancelling an edit for a post comment
     * must send the activityId.
     *
     * Note: Once ViewHolders are refactored to include activityId as the unique identifier, params
     * may not be necessary.
     *
     * @param params    variable length string. Currently only used by PostCommentViewHolder to pass
     *                  the activityId.
     */
    void confirmLeaveEvent(String... params);

    void cancelLeaveEvent();
}
