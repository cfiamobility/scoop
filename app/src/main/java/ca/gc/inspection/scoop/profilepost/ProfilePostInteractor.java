package ca.gc.inspection.scoop.profilepost;

import com.android.volley.toolbox.JsonArrayRequest;
import ca.gc.inspection.scoop.Config;
import ca.gc.inspection.scoop.util.NetworkUtils;
import ca.gc.inspection.scoop.profilecomment.ProfileCommentInteractor;

import static com.google.gson.internal.$Gson$Preconditions.checkNotNull;


public class ProfilePostInteractor extends ProfileCommentInteractor {
    /**
     * Interactor used to send requests to the network. Inherits from PostCommentInteractor
     * so that Profile post has access to methods such as insert/update likes.
     */

    /**
     * Empty constructor called by child classes (ie. FeedPostInteractor) to allow them to set
     * their own presenter
     */
    public ProfilePostInteractor() {
    }

    ProfilePostInteractor(ProfilePostPresenter presenter, NetworkUtils network) {
        mPresenter = checkNotNull(presenter);
        mNetwork = network;
    }

    /**
     * HTTP Requests to get all the user posts infos
     * @param userid: passes the userid of the profile clicked on
     */
    public void getProfilePosts(final String userid) {
        String url = Config.baseIP + "profile/posttextfill/" + userid + "/" + Config.currentUser;
        String responseUrl = Config.baseIP + "profile/postimagefill/" + userid;
        JsonArrayRequest postRequest = super.newProfileJsonArrayRequest(url, responseUrl);
        mNetwork.addToRequestQueue(postRequest);
    }
}

