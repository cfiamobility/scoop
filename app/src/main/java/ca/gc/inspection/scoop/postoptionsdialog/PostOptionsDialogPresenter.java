package ca.gc.inspection.scoop.postoptionsdialog;

import android.support.annotation.NonNull;
import ca.gc.inspection.scoop.util.NetworkUtils;
import static com.google.gson.internal.$Gson$Preconditions.checkNotNull;

/**
 * - The PostOptionsDialogPresenter provides methods for communicating and validating information in regards
 *   to saving a post between the View and the Model
 * - This is the Presenter for the Post Options Dialog action case
 */
public class PostOptionsDialogPresenter implements PostOptionsDialogContract.Presenter {

    private PostOptionsDialogInteractor mInteractor;
    private PostOptionsDialogContract.View mView;

    /**
     * Constructor that takes in the respective action case View.
     * @param view View that is linked to the respective Presenter
     */
    PostOptionsDialogPresenter(@NonNull PostOptionsDialogContract.View view) {
        mView = checkNotNull(view);
        mInteractor = new PostOptionsDialogInteractor(this);
    }

    /**
     * Provides save post information from the View and passes it the Interactor
     * @param network Allows save post information to be added to singleton request queue
     * @param activityid ID of the activity in which the user is click on
     * @param userid ID of the user
     */
    public void savePost(NetworkUtils network, final String activityid, final String userid){
        mInteractor.savePost(network, activityid, userid);
    }


    /**
     * Checks if the post being saved is valid or not based on the response from the Interactor
     * Post already saved in the database by the user vs New post being saved by the user
     * @param response response message from the database POST request
     */
    public void validSave(String response) {
        if (response.contains("Invalid save - post already saved")) { // if the user attempts to save a post that has already been saved by them
            mView.setSaveResponseMessage("This post is already saved!");
        } else if (response.contains("Valid save")){ // if user saves a post that is not already saved by them
            mView.setSaveResponseMessage("Post successfully saved!");
        }
    }

}
