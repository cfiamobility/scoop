package ca.gc.inspection.scoop.editpost;

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


public class EditPostInteractor extends CreatePostInteractor {

    private EditPostPresenter mPresenter;

    EditPostInteractor(EditPostPresenter presenter) {
        super(presenter);
        mPresenter = presenter;
    }

    public void sendPostToDatabase(NetworkUtils network, final String activityId, final String title, final String text, final String imageBitmap) {
        String urlPrefix = Config.baseIP + "post/edit-post/";
        String urlText = urlPrefix + "text/";

        Map<String, String>  params = new HashMap<>();
        params.put("activityid", activityId);
        params.put("activitytype", Integer.toString(Config.postType));
        params.put("posttitle", title);
        params.put("posttext", text);

        StringRequest postRequest = newPostRequest(urlText, params);
        network.addToRequestQueue(postRequest);

        if (imageBitmap != null) {
            String urlImage = urlPrefix + "image/";
            Map<String, String>  imageParams = new HashMap<>();
            imageParams.put("activityid", activityId);
            imageParams.put("userid", Config.currentUser);
            imageParams.put("postimage", imageBitmap);

            StringRequest imagePostRequest = newPostRequest(urlImage, imageParams);
            network.addToRequestQueue(imagePostRequest);
        }
    }

    public void getPostImage(NetworkUtils network, final String activityId) {
        Log.d("EditPostInteractor", "activityId:"+activityId);
        String url = Config.baseIP + "post/get-image/";

        Map<String, String>  params = new HashMap<>();
        params.put("activityid", activityId);

        StringRequest postRequest = newImagePostRequest(url, params);
        network.addToRequestQueue(postRequest);
    }

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
