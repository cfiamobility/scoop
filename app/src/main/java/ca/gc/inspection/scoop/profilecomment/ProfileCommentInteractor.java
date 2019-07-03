package ca.gc.inspection.scoop.profilecomment;

import ca.gc.inspection.scoop.postcomment.PostCommentInteractor;
import static com.google.gson.internal.$Gson$Preconditions.checkNotNull;

public class ProfileCommentInteractor extends PostCommentInteractor {
    /**
     * Interactor used to send requests to the network
     */

    /**
     * Empty constructor called by child classes (ie. ProfilePostInteractor) to allow them to set
     * their own presenter
     */
    public ProfileCommentInteractor() {
    }

    public ProfileCommentInteractor(ProfileCommentPresenter presenter){
        mPresenter = checkNotNull(presenter);
    }

}
