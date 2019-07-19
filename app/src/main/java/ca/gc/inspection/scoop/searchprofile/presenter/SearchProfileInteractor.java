package ca.gc.inspection.scoop.searchprofile.presenter;

import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;

import org.json.JSONArray;

import java.util.HashMap;
import java.util.Map;

import ca.gc.inspection.scoop.Config;
import ca.gc.inspection.scoop.util.NetworkUtils;

import static com.google.gson.internal.$Gson$Preconditions.checkNotNull;

/**
 * Interactor used to send requests to the network. Inherits from PostCommentInteractor
 * so that Profile post has access to methods such as insert/update likes.
 */
public class SearchProfileInteractor {

    protected SearchProfilePresenter mPresenter;
    public NetworkUtils mNetwork;

    /**
     * Empty constructor called by child classes (ie. FeedPostInteractor) to allow them to set
     * their own presenter
     */
    public SearchProfileInteractor() {
    }

    SearchProfileInteractor(SearchProfilePresenter presenter, NetworkUtils network) {
        mPresenter = checkNotNull(presenter);
        mNetwork = network;
    }

    public JsonArrayRequest newJsonArrayRequest(String url) {
        return new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(final JSONArray response) {
                mPresenter.setData(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        }) {
            public Map<String, String> getHeaders() throws AuthFailureError {
                // inserting the token into the response header that will be sent to the server
                Map<String, String> header = new HashMap<>();
                header.put("authorization", Config.token);
                return header;
            }
        };
    }

    /**
     * HTTP Requests to get search profiles which match a query
     */
    public void getSearchProfile(String query) {
        Log.d("Search Profile Interactor", "getSearchProfile for " + query);
        String url = Config.baseIP + "profile/search/" + query;
        JsonArrayRequest peopleRequest = newJsonArrayRequest(url);
        mNetwork.addToRequestQueue(peopleRequest);
    }
}

