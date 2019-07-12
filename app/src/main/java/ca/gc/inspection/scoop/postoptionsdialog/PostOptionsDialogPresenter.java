package ca.gc.inspection.scoop.postoptionsdialog;

import android.support.annotation.NonNull;
import android.util.Log;

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


   public void setSaveResponseMessage(String response){
        mView.setSaveResponseMessage(response);
   }


}
