package ca.gc.inspection.scoop.createpost;

import android.util.Log;

import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

import ca.gc.inspection.scoop.Config;
import ca.gc.inspection.scoop.util.CameraUtils;
import ca.gc.inspection.scoop.util.NetworkUtils;

import static ca.gc.inspection.scoop.Config.DATABASE_RESPONSE_SUCCESS;

class CreatePostInteractor {
    private CreatePostPresenter mPresenter;
    CreatePostInteractor(CreatePostPresenter presenter) {
        mPresenter = presenter;
    }
    /**
     * Sends post to database using RequestQueue.
     *
     * @param network       An instance of the singleton class which encapsulates the RequestQueue
     * @param userId        UserId
     * @param title         Title of the new post
     * @param text          Text body of the new post
     * @param imageBitmap   Bitmap in String format
     */
    void sendPostToDatabase(NetworkUtils network, final String userId, final String title, final String text, final String imageBitmap) {
        String url = Config.baseIP + "post/add-post";
        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                response -> {
                    // response
                    Log.d("Response", response);
                    if (response.contains(DATABASE_RESPONSE_SUCCESS)){
                        Log.i("Info", "We good");
                        mPresenter.onPostCreated(true);
                    }
                    else {
                        mPresenter.onPostCreated(false);
                    }
                },
                error -> {
                    // error
                    Log.d("Error.Response", String.valueOf(error));
                    mPresenter.onPostCreated(false);
                }
        ) {
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String>  params = new HashMap<>();
                params.put("userid", userId); // Post test user
                params.put("activitytype", Integer.toString(Config.postType));
                params.put("posttitle", title);
                params.put("posttext", text);
                params.put("postimage", imageBitmap);
                return params;
            }
            @Override
            public Map<String, String> getHeaders() {
                // inserting the token into the response header that will be sent to the server
                Map<String, String> header = new HashMap<>();
                header.put("authorization", Config.token);
                return header;
            }
        };
        network.addToRequestQueue(postRequest);
    }

    void getUserProfileImage(NetworkUtils network){
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
