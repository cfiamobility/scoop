package ca.gc.inspection.scoop.postoptionsdialog;

import android.support.annotation.NonNull;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import java.util.HashMap;
import java.util.Map;

import ca.gc.inspection.scoop.Config;
import ca.gc.inspection.scoop.util.NetworkUtils;

import static com.google.gson.internal.$Gson$Preconditions.checkNotNull;


/**
 * PostOptionsDialogInteractor used by the Presenter to create POST request and store data into the Model(database/server)
 */
class PostOptionsDialogInteractor {

    private PostOptionsDialogPresenter mPresenter;

    public PostOptionsDialogInteractor(@NonNull PostOptionsDialogPresenter presenter) {
        mPresenter = checkNotNull(presenter);
    }

    public void deletePost(NetworkUtils network, final String activityid, final String userid) {
        Log.i("got", "got here");
        String url = Config.baseIP + "post/remove-post-comment";

        StringRequest putRequest = new StringRequest(Request.Method.PUT, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i("RESPONSE", response);
                mPresenter.setDeleteResponseMessage(response);
                mPresenter.refresh();
            }
        }, new Response.ErrorListener()
        {
            @Override
            public void onErrorResponse(VolleyError error) {
                // error
                Log.d("Error.Response", String.valueOf(error));
            }
        }
        ){
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String>  params = new HashMap<>();
                params.put("activityid", activityid);
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
        network.addToRequestQueue(putRequest);
    }
}
