package ca.gc.inspection.scoop.createpost;


/**
 * Receives database response for newPostRequest. Implemented by Presenter classes.
 *
 * See InteractorBundle documentation.
 */
public interface PostRequestReceiver {

    void onDatabaseResponse(boolean success, InteractorBundle interactorBundle);
}
