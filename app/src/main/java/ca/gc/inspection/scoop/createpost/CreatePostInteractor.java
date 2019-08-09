package ca.gc.inspection.scoop.createpost;

import android.support.annotation.NonNull;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

import ca.gc.inspection.scoop.Config;
import ca.gc.inspection.scoop.util.CameraUtils;
import ca.gc.inspection.scoop.util.NetworkUtils;

import static ca.gc.inspection.scoop.Config.DATABASE_RESPONSE_SUCCESS;
import static com.google.gson.internal.$Gson$Preconditions.checkNotNull;

public class CreatePostInteractor {
    private CreatePostPresenter mPresenter;

   /**
     * Empty constructor to allow inheritance
     */
    public CreatePostInteractor() {
    }

    protected CreatePostInteractor(CreatePostPresenter presenter) {
        setPresenter(presenter);
    }

    /**
     * Can be called by child Interactor to set the parent's presenter as a casted down version without
     * the parent creating a new object
     *
     * @param presenter    Handles database callbacks
     */
    public void setPresenter(@NonNull CreatePostPresenter presenter) {
        mPresenter = checkNotNull(presenter);
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

        Map<String, String>  params = new HashMap<>();
        params.put("userid", userId);
        params.put("activitytype", Integer.toString(Config.postType));
        params.put("posttitle", title);
        params.put("posttext", text);
        params.put("postimage", imageBitmap);

        Log.d("CreatePostInteractor", params.toString());

        StringRequest postRequest = newPostRequest(mPresenter, null, url, params);
        network.addToRequestQueue(postRequest);
    }

    /**
     * Static helper method to create a StringRequest of type Post.
     * Unlike GET, POST passes parameters in the message body instead of url
     * @param postRequestReceiver   Instance which implements the PostRequestReceiver to receive callbacks
     * @param interactorBundle      Any implementation of interactor Bundle
     * @param url
     * @param params
     * @return
     */
    public static StringRequest newPostRequest(PostRequestReceiver postRequestReceiver, InteractorBundle interactorBundle,
                                               String url, Map<String, String> params) {
        return new StringRequest(Request.Method.POST, url,
                response -> {
                    // response
                    Log.d("Response", response.getClass().toString() + ": " + response);
                    if (response.contains(DATABASE_RESPONSE_SUCCESS)){
                        Log.i("Info", "We good");
                        postRequestReceiver.onDatabaseResponse(true, interactorBundle);
                    }
                    else {
                        postRequestReceiver.onDatabaseResponse(false, interactorBundle);
                    }
                },
                error -> {
                    // error
                    Log.d("Error.Response", String.valueOf(error));
                    postRequestReceiver.onDatabaseResponse(false, interactorBundle);
                }
        ) {
            @Override
            protected Map<String, String> getParams()
            {
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
    }

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
