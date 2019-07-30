package ca.gc.inspection.scoop.createpost;

public interface PostRequestReceiver {

    void onDatabaseResponse(boolean success, InteractorBundle interactorBundle);
}
