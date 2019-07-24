package ca.gc.inspection.scoop.profilelikes;

import com.android.volley.toolbox.JsonArrayRequest;
import ca.gc.inspection.scoop.Config;
import ca.gc.inspection.scoop.profilelikes.ProfileLikesPresenter;
import ca.gc.inspection.scoop.util.NetworkUtils;
import ca.gc.inspection.scoop.profilecomment.ProfileCommentInteractor;

import static com.google.gson.internal.$Gson$Preconditions.checkNotNull;


public class ProfileLikesInteractor extends ProfileCommentInteractor {
    /**
     * Interactor used to send requests to the network. Inherits from ProfileCommentInteractor
     * so that Profile post has access to methods such as insert/update likes.
     */

    /**
     * Empty constructor called by child classes (ie. FeedPostInteractor) to allow them to set
     * their own presenter
     */
    public ProfileLikesInteractor() {
    }

    ProfileLikesInteractor(ProfileLikesPresenter presenter,NetworkUtils network) {
        mPresenter = checkNotNull(presenter);
        mNetwork = network;
    }

    /**
     * HTTP Requests to get all the user posts infos
     * @param userid: passes the userid of the profile clicked on
     */
    public void getProfileLikes(final String userid) {
        String url = Config.baseIP + "profile/getlikes/text/" + userid + "/" + Config.currentUser;
        String responseUrl = Config.baseIP + "profile/getlikes/images/" + userid;
        JsonArrayRequest postRequest = super.newProfileJsonArrayRequest(url, responseUrl);
        mNetwork.addToRequestQueue(postRequest);
    }
}

