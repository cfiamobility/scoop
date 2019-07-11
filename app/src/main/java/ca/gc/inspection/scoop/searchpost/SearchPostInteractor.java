package ca.gc.inspection.scoop.searchpost;

import com.android.volley.toolbox.JsonArrayRequest;

import ca.gc.inspection.scoop.Config;
import ca.gc.inspection.scoop.profilepost.ProfilePostInteractor;
import ca.gc.inspection.scoop.util.NetworkUtils;

import static com.google.gson.internal.$Gson$Preconditions.checkNotNull;


public class SearchPostInteractor extends ProfilePostInteractor {
    /**
     * Interactor used to send requests to the network. Inherits from PostCommentInteractor
     * so that Profile post has access to methods such as insert/update likes.
     */

    /**
     * Empty constructor called by child classes (ie. FeedPostInteractor) to allow them to set
     * their own presenter
     */
    public SearchPostInteractor() {
    }

    SearchPostInteractor(SearchPostPresenter presenter, NetworkUtils network) {
        mPresenter = checkNotNull(presenter);
        mNetwork = network;
    }

    /**
     * HTTP Requests to get search posts which match a query
     * @param userid: passes the userid of the profile clicked on
     */
    public void getSearchPosts(final String userid, String query) {
        String url = Config.baseIP + "post/text/" + userid + "/" + query;
        String responseUrl = Config.baseIP + "post/images/" + query;
        JsonArrayRequest postRequest = super.newProfileJsonArrayRequest(url, responseUrl);
        mNetwork.addToRequestQueue(postRequest);
    }
}

