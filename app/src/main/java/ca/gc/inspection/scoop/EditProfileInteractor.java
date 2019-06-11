package ca.gc.inspection.scoop;

import android.support.annotation.NonNull;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static com.google.gson.internal.$Gson$Preconditions.checkNotNull;

public class EditProfileInteractor {

    private EditProfilePresenter mPresenter;

    EditProfileInteractor(@NonNull EditProfilePresenter presenter) {
        mPresenter = checkNotNull(presenter);
    }

    public void initialFill(MySingleton singleton) {
        // URL TO BE CHANGED - userID passed as a parameter to NodeJS
        String URL = Config.baseIP + "edituser/getinitial/" + EditProfileActivity.userID;

        // Requesting response be sent back as a JSON Object
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, URL, null,  new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                EditProfileActivity.setInitialFill(response);
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

        singleton.addToRequestQueue(jsonObjectRequest);
    }


    public void positionAutoComplete(MySingleton singleton, String positionChangedCapped) {
        // URL TO BE CHANGED - position entered passed to NodeJS as a parameter
        String URL = Config.baseIP + "edituser/positionchanged/" + positionChangedCapped;

        // Asking for an array from response (will send back 3 objects in an array)
        JsonArrayRequest getRequest = new JsonArrayRequest(Request.Method.GET, URL, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                mPresenter.positionAutoSetup(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {}
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                // inserting the token into the response header that will be sent to the server
                Map<String, String> header = new HashMap<>();
                header.put("authorization", Config.token);
                return header;
            }
        };
        singleton.addToRequestQueue(getRequest);
    }
}
