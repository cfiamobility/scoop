package ca.gc.inspection.scoop.editleavedialog;


/**
 * Defined relationship between the View and Presenter when prompting a user to leave an activity
 * which contains unsaved edits for a Post or Comment.
 * This contract is
 */
public interface EditLeaveEventListener {

    /**
     * Defines how the Presenter can interact with the View (Activity/Fragment).
     * Not necessary in this case but kept to keep contract structure consistent with MVP and to make
     * it easier to extend in the future.
     */
    interface View {

        /**
         * Provides callback methods for EditLeaveDialog's buttons
         */
        interface EditLeaveDialogAPI {
            void confirmLeaveEvent();
            void cancelLeaveEvent();
        }
    }

    /**
     * Defines how the View (Activity/Fragment) can interact with the Presenter
     */
    interface Presenter {

        boolean unsavedEditsExist();

    }

}
