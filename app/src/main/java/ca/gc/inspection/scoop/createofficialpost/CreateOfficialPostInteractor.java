package ca.gc.inspection.scoop.createofficialpost;

import android.support.annotation.NonNull;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

import ca.gc.inspection.scoop.Config;
import ca.gc.inspection.scoop.createpost.CreatePostInteractor;
import ca.gc.inspection.scoop.util.CameraUtils;
import ca.gc.inspection.scoop.util.NetworkUtils;

import static com.google.gson.internal.$Gson$Preconditions.checkNotNull;


public class CreateOfficialPostInteractor extends CreatePostInteractor {

    private CreateOfficialPostPresenter mPresenter;

    CreateOfficialPostInteractor(CreateOfficialPostPresenter presenter) {
        super();
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

        StringRequest postRequest = newPostRequest(mPresenter, null, urlText, params);
        network.addToRequestQueue(postRequest);
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
}
