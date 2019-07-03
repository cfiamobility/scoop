package ca.gc.inspection.scoop.feedpost;

import com.android.volley.toolbox.JsonArrayRequest;

import ca.gc.inspection.scoop.Config;
import ca.gc.inspection.scoop.util.NetworkUtils;
import ca.gc.inspection.scoop.profilepost.ProfilePostInteractor;

public class FeedPostInteractor extends ProfilePostInteractor {
    /**
     * Interactor used to send requests to the network. Inherits from ProfilePostInteractor (which
     * in turn inherits from PostCommentInteractor) so that Feed post has access to methods
     * such as insert/update likes.
     */

    FeedPostInteractor(FeedPostPresenter presenter, NetworkUtils network) {
        mPresenter = presenter;
        mNetwork = network;
    }

    public void getFeedPosts(String feedType) {
        String URL = Config.baseIP + "display-post/posts/" + feedType + "/" + Config.currentUser;
        String imageURL = Config.baseIP + "display-post/images/" + feedType + "/" + Config.currentUser;
        JsonArrayRequest feedPostRequest = newProfileJsonArrayRequest(URL, imageURL);
        mNetwork.addToRequestQueue(feedPostRequest);
    }
}
