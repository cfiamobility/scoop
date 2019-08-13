package ca.gc.inspection.scoop.createpostoptionsdialog;


/**
 * Implemented by the View layer to provide callback methods for the CreatePostOptionsDialog.
 */
public interface CreatePostOptionsDialogReceiver {

    interface CreateCommunityPostReceiver {
        void createCommunityPost();
    }

    interface CreateOfficialPostBPCReceiver {
        void createOfficialPostBCP();
    }
}