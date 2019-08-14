package ca.gc.inspection.scoop.createofficialpost;

import android.support.annotation.NonNull;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;

import java.util.HashMap;
import java.util.Map;

import ca.gc.inspection.scoop.Config;
import ca.gc.inspection.scoop.createpost.CreatePostInteractor;
import ca.gc.inspection.scoop.util.CameraUtils;
import ca.gc.inspection.scoop.util.NetworkUtils;

import static ca.gc.inspection.scoop.searchbuilding.SearchBuildingInteractor.getAllBuildingsForReceiver;
import static com.google.gson.internal.$Gson$Preconditions.checkNotNull;


public class CreateOfficialPostInteractor {

    private CreateOfficialPostPresenter mPresenter;

    CreateOfficialPostInteractor(CreateOfficialPostPresenter presenter) {
        setPresenter(presenter);
    }

    /**
     * @param presenter    Handles database callbacks
     */
    public void setPresenter(@NonNull CreateOfficialPostPresenter presenter) {
//        super.setPresenter(presenter);
        mPresenter = checkNotNull(presenter);
    }

    /**
     * Saves the changes to the post title, text, and image (if it was modified) to the database.
     */
    public void sendPostToDatabase(
            NetworkUtils network, String postTitle, int buildingId, int reasonForClosure, int actionRequired) {

        String urlPrefix = Config.baseIP + "post/edit-post/";
        String urlText = urlPrefix + "text/";

        Map<String, String>  params = new HashMap<>();
        params.put("userid", Config.currentUser);

//        StringRequest postRequest = newPostRequest(mPresenter, null, urlText, params);
//        network.addToRequestQueue(postRequest);
    }

    // TODO reuse code from create post
    public void getUserProfileImage(NetworkUtils network){
        String url = Config.baseIP + "post/create-post-profile-image/" + Config.currentUser;
        StringRequest getRequest = new StringRequest(Request.Method.GET, url,
                response -> mPresenter.setUserProfileImage(CameraUtils.stringToBitmap(response)),
                error -> mPresenter.setUserProfileImage(null)) {
            @Override
            public Map<String, String> getHeaders() {
                // inserting the token into the response header that will be sent to the server
                Map<String, String> header = new HashMap<>();
                header.put("authorization", Config.token);
                return header;
            }
        };
        network.addToRequestQueue(getRequest);
    }

    public void getAllBuildings(NetworkUtils network) {
        getAllBuildingsForReceiver(network, mPresenter);
    }

    public void getAllReasons(NetworkUtils network) {
        String URL = Config.baseIP + "post/official/bcp/getallreasons";

        // Requesting response be sent back as a JSON Object
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, URL, null,  new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                mPresenter.setBuildingClosureReasonsData(response);
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

        network.addToRequestQueue(jsonArrayRequest);
    }

    public void getAllActions(NetworkUtils network) {
        String URL = Config.baseIP + "post/official/bcp/getallactions";

        // Requesting response be sent back as a JSON Object
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, URL, null,  new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                mPresenter.setRequiredActionsData(response);
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

        network.addToRequestQueue(jsonArrayRequest);
    }
}
