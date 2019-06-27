package ca.gc.inspection.scoop.feedpost;

import com.android.volley.toolbox.JsonArrayRequest;

import ca.gc.inspection.scoop.Config;
import ca.gc.inspection.scoop.util.NetworkUtils;
import ca.gc.inspection.scoop.profilepost.ProfilePostInteractor;

public class FeedPostInteractor extends ProfilePostInteractor {
    /**
     * Interactor used to send requests to the network. Inherits from ProfilePostInteractor (which
     * in turn inherits from ProfileCommentInteractor) so that Feed post has access to methods
     * such as insert/update likes.
     */

    FeedPostInteractor(FeedPostPresenter presenter) {
        mPresenter = presenter;
    }

    public void getFeedPosts(NetworkUtils network, String feedType) {
        String URL = Config.baseIP + "post/display-post/" + feedType + "/" + Config.currentUser;
        String imageURL = Config.baseIP + "post/display-post-images/" + feedType + "/" + Config.currentUser;
        JsonArrayRequest feedPostRequest = newProfileJsonArrayRequest(URL, imageURL);
        Config.requestQueue.add(feedPostRequest);
//        network.addToRequestQueue(commentRequest);
    }
}
