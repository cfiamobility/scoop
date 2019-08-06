package ca.gc.inspection.scoop.editleavedialog;

public interface EditLeaveEventListener {

    interface View {

        void confirmLeaveEvent();
        void cancelLeaveEvent();
    }

    interface Presenter {

        boolean unsavedEditsExist();

    }

}
