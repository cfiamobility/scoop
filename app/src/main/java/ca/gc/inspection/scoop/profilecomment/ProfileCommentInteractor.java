package ca.gc.inspection.scoop.profilecomment;

import com.android.volley.toolbox.JsonArrayRequest;

import ca.gc.inspection.scoop.Config;
import ca.gc.inspection.scoop.postcomment.PostCommentInteractor;
import ca.gc.inspection.scoop.util.NetworkUtils;

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

    public ProfileCommentInteractor(ProfileCommentPresenter presenter, NetworkUtils network){
        mPresenter = checkNotNull(presenter);
        mNetwork = network;
    }

    /**
     * HTTPRequests for comments and profile images
     * @param userid: userid
     */
    public void getProfileComments(final String userid) {
        String url = Config.baseIP + "profile/commenttextfill/" + userid + "/" + Config.currentUser;
        String responseUrl = Config.baseIP + "profile/commentimagefill/" + userid;
        JsonArrayRequest commentRequest = newProfileJsonArrayRequest(url, responseUrl);
        mNetwork.addToRequestQueue(commentRequest);
    }
}
