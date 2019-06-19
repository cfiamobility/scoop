package ca.gc.inspection.scoop.feedpost;


import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;

import org.json.JSONArray;

import java.util.HashMap;
import java.util.Map;

import ca.gc.inspection.scoop.Config;
import ca.gc.inspection.scoop.MySingleton;
import ca.gc.inspection.scoop.profilepost.ProfilePostInteractor;

public class FeedPostInteractor extends ProfilePostInteractor {

    FeedPostInteractor(FeedPostPresenter presenter) {
        mPresenter = presenter;
    }

    public void getFeedPosts(MySingleton singleton, String feedType) {
        String URL = Config.baseIP + "display-post/posts/" + feedType + "/" + Config.currentUser;
        String imageURL = Config.baseIP + "display-post/images/" + feedType + "/" + Config.currentUser;
        JsonArrayRequest feedPostRequest = newProfileJsonArrayRequest(URL, imageURL);
        Config.requestQueue.add(feedPostRequest);
//        singleton.addToRequestQueue(commentRequest);
    }
}
