package ca.gc.inspection.scoop;

import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import java.util.Map;


public class CreatePostInteractor {

    private CreatePostContract.Presenter mPresenter;

    CreatePostInteractor(CreatePostContract.Presenter presenter) {
        mPresenter = presenter;
    }

    public void sendPostToDatabase(MySingleton singleton, final String userId, final String title, final String text, final String imageBitmap) {
        String url = Config.baseIP + "add-post";

        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                        // response
                        Log.d("Response", response);
                        if (response.contains("Success")){
                            Log.i("Info", "We good");
                        }
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // error
                        Log.d("Error.Response", String.valueOf(error));
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams()
            {
                return CreatePostContract.Presenter.getPostParams(userId, title, text, imageBitmap);
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return CreatePostContract.Presenter.getPostHeaders();
            }
        };
        singleton.addToRequestQueue(postRequest);
    }
}
