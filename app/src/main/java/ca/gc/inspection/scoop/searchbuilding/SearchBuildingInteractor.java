package ca.gc.inspection.scoop.searchbuilding;
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
 * Interactor for the search building activity
 */
public class SearchBuildingInteractor {

    private SearchBuildingPresenter mPresenter;
    private NetworkUtils mNetwork;

    public SearchBuildingInteractor(SearchBuildingPresenter presenter, NetworkUtils network) {

            mPresenter = checkNotNull(presenter);
            mNetwork = checkNotNull(network);
    }

    /**
     * fetches list of all building addresses from the data base
     */
    public void getAllBuildings() {
        String URL = Config.baseIP + "profile/getallbuildings";

        // Requesting response be sent back as a JSON Object
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, URL, null,  new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                mPresenter.updateData(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("error", error.toString());
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                // inserting the token into the response header that will be sent to the server
                Map<String, String> header = new HashMap<>();
                header.put("authorization", Config.token);
                return header;
            }
        };

        mNetwork.addToRequestQueue(jsonArrayRequest);

    }
}
