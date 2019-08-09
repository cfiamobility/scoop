package ca.gc.inspection.scoop.editpost;

import android.support.annotation.NonNull;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

import ca.gc.inspection.scoop.Config;
import ca.gc.inspection.scoop.createpost.CreatePostInteractor;
import ca.gc.inspection.scoop.util.NetworkUtils;

import static ca.gc.inspection.scoop.Config.DATABASE_RESPONSE_SUCCESS;
import static com.google.gson.internal.$Gson$Preconditions.checkNotNull;


public class EditPostInteractor extends CreatePostInteractor {

    private EditPostPresenter mPresenter;

    EditPostInteractor(EditPostPresenter presenter) {
        super();
        setPresenter(presenter);
    }

    /**
     * set parent presenter as a casted down version without the parent creating a new object
     *
     * @param presenter    Handles database callbacks
     */
    public void setPresenter(@NonNull EditPostPresenter presenter) {
        super.setPresenter(presenter);
        mPresenter = checkNotNull(presenter);
    }

    /**
     * Saves the changes to the post title, text, and image (if it was modified) to the database.
     *
     * @param network       An instance of the singleton class which encapsulates the RequestQueue
     * @param activityId    Unique identifier of a post
     * @param title         Even if the title was not edited, we still pass in the current title
     * @param text          Even if the text was not edited, we still pass in the current text
     * @param imageBitmap   Bitmap in String format. If this is null, no changes are made to the
     *                      database.
     */
    public void sendPostToDatabase(NetworkUtils network, final String activityId, final String title, final String text, final String imageBitmap) {
        String urlPrefix = Config.baseIP + "post/edit-post/";
        String urlText = urlPrefix + "text/";

        Map<String, String>  params = new HashMap<>();
        params.put("activityid", activityId);
        params.put("activitytype", Integer.toString(Config.postType));
        params.put("posttitle", title);
        params.put("posttext", text);

        StringRequest postRequest = newPostRequest(mPresenter, null, urlText, params);
        network.addToRequestQueue(postRequest);

        if (imageBitmap != null) {
            String urlImage = urlPrefix + "image/";
            Map<String, String>  imageParams = new HashMap<>();
            imageParams.put("activityid", activityId);
            imageParams.put("userid", Config.currentUser);
            imageParams.put("postimage", imageBitmap);

            StringRequest imagePostRequest = newPostRequest(mPresenter, null, urlImage, imageParams);
            network.addToRequestQueue(imagePostRequest);
        }
    }

    /**
     * Retrieves the post image from the database. Called if EditPostActivity did not
     * receive the post image from the bundle.
     *
     * @param network       Singleton network access object which encapsulates access to RequestQueue.
     * @param activityId    Unique identifier for a post.
     */
    public void getPostImage(NetworkUtils network, final String activityId) {
        Log.d("EditPostInteractor", "activityId:"+activityId);
        String url = Config.baseIP + "post/get-image/";

        Map<String, String>  params = new HashMap<>();
        params.put("activityid", activityId);

        StringRequest postRequest = newImagePostRequest(url, params);
        network.addToRequestQueue(postRequest);
    }

    /**
     * Helper method to create the StringRequest for getPostImage.
     *
     * @param url       route in the middle-tier to get the post image
     * @param params    contains activityId
     * @return a StringRequest which can be added to the NetworkUtils' RequestQueue.
     */
    public StringRequest newImagePostRequest(String url, Map<String, String> params) {
        return new StringRequest(Request.Method.POST, url,
                mPresenter::onDatabaseImageResponse,
                error -> mPresenter.onDatabaseImageResponse(null)
        ) {
            @Override
            protected Map<String, String> getParams()
            {
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                // inserting the token into the response header that will be sent to the server
                Map<String, String> header = new HashMap<>();
                header.put("authorization", Config.token);
                return header;
            }
        };
    }
}
