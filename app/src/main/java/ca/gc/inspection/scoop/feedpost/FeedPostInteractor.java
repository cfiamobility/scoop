package ca.gc.inspection.scoop.feedpost;

import com.android.volley.toolbox.JsonArrayRequest;

import ca.gc.inspection.scoop.Config;
import ca.gc.inspection.scoop.util.NetworkUtils;
import ca.gc.inspection.scoop.profilepost.ProfilePostInteractor;

/**
 * Interactor used to send requests to the network. Inherits from ProfilePostInteractor (which
 * in turn inherits from PostCommentInteractor) so that Feed post has access to methods
 * such as insert/update likes.
 */
public class FeedPostInteractor extends ProfilePostInteractor {

    /**
     * Empty constructor called by child classes (ie. DisplayPostInteractor) to allow them to set
     * their own presenter
     */
    public FeedPostInteractor() {
    }

    FeedPostInteractor(FeedPostPresenter presenter, NetworkUtils network) {
        mPresenter = presenter;
        mNetwork = network;
    }

    public void getFeedPosts(String feedType) {
        String URL = Config.baseIP + "post/feed-text/" + feedType + "/" + Config.currentUser;
        String imageURL = Config.baseIP + "post/feed-images/" + feedType + "/" + Config.currentUser;
        JsonArrayRequest feedPostRequest = newProfileJsonArrayRequest(URL, imageURL);
        mNetwork.addToRequestQueue(feedPostRequest);
    }

    public void getSavedPosts() {
        String URL = Config.baseIP + "post/display-saved-post/"+ Config.currentUser;
        String imageURL = Config.baseIP + "post/display-saved-post-images/" + Config.currentUser;
        JsonArrayRequest savedPostRequest = newProfileJsonArrayRequest(URL, imageURL);
        mNetwork.addToRequestQueue(savedPostRequest);
    }
}
