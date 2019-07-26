package ca.gc.inspection.scoop.createpost;

import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

import ca.gc.inspection.scoop.Config;
import ca.gc.inspection.scoop.util.NetworkUtils;

import static ca.gc.inspection.scoop.Config.DATABASE_RESPONSE_SUCCESS;


public class CreatePostInteractor {

    private CreatePostPresenter mPresenter;

    public CreatePostInteractor() {
    }

    protected CreatePostInteractor(CreatePostPresenter presenter) {
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
    public void sendPostToDatabase(NetworkUtils network, final String userId, final String title, final String text, final String imageBitmap) {
        String url = Config.baseIP + "post/add-post";

        Map<String, String>  params = new HashMap<>();
        params.put("userid", userId);
        params.put("activitytype", Integer.toString(Config.postType));
        params.put("posttitle", title);
        params.put("posttext", text);
        params.put("postimage", imageBitmap);

        Log.d("CreatePostInteractor", params.toString());

        StringRequest postRequest = newPostRequest(url, params);
        network.addToRequestQueue(postRequest);
    }

    public StringRequest newPostRequest(String url, Map<String, String> params) {
        return new StringRequest(Request.Method.POST, url,
                response -> {
                    // response
                    Log.d("Response", response);
                    if (response.contains(DATABASE_RESPONSE_SUCCESS)){
                        Log.i("Info", "We good");
                        mPresenter.onDatabaseResponse(true);
                    }
                    else {
                        mPresenter.onDatabaseResponse(false);
                    }
                },
                error -> {
                    // error
                    Log.d("Error.Response", String.valueOf(error));
                    mPresenter.onDatabaseResponse(false);
                }
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
