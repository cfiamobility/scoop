package ca.gc.inspection.scoop.editprofile;

import android.support.annotation.NonNull;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import ca.gc.inspection.scoop.Config;
import ca.gc.inspection.scoop.util.NetworkUtils;
import ca.gc.inspection.scoop.TabFragment;

import static com.google.gson.internal.$Gson$Preconditions.checkNotNull;

public class EditProfileInteractor {

    private EditProfilePresenter mPresenter;

    EditProfileInteractor(@NonNull EditProfilePresenter presenter) {
        mPresenter = checkNotNull(presenter);
    }

    public void initialFill(NetworkUtils network) {
        // URL TO BE CHANGED - userID passed as a parameter to NodeJS
        String URL = Config.baseIP + "edituser/getinitial/" + EditProfileActivity.userID;

        // Requesting response be sent back as a JSON Object
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, URL, null,  new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                mPresenter.setInitialFill(response);
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

        network.addToRequestQueue(jsonObjectRequest);
    }

    public void getPositionAutoCompleteFromDB(NetworkUtils network, String positionChangedCapitalized) {
        // URL TO BE CHANGED - position entered passed to NodeJS as a parameter
        String URL = Config.baseIP + "edituser/positionchanged/" + positionChangedCapitalized;

        // Asking for an array from response (will send back 3 objects in an array)
        JsonArrayRequest getRequest = new JsonArrayRequest(Request.Method.GET, URL, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                mPresenter.setPositionAutoCompleteFromDB(response);
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
        network.addToRequestQueue(getRequest);
    }

    // takes care of the requests when the text is changed in the divisions edittext
    public void getDivisionAutoCompleteFromDB(NetworkUtils network, String divisionChangedCapitalized) {
        // Inputted division is passed as a parameter to NodeJS
        String URL = Config.baseIP + "edituser/divisionchanged/" + divisionChangedCapitalized;

        // Asking for a JSONArray back
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, URL, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                mPresenter.setDivisionAutoCompleteFromDB(response);
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
        // submitting the request
        network.addToRequestQueue(jsonArrayRequest);
    }

    public void updateUserInfo(NetworkUtils network, Map<String, String> params) {
        // Request url
        String URL = Config.baseIP + "edituser/updatedatabase";

        // Asking for a string back
        StringRequest stringRequest = new StringRequest(Request.Method.PUT, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response.equals("success")) {
                    TabFragment.refresh();
                    mPresenter.finishUpdateUserInfo();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {}
        }) {
            // Inputting the hashmap into the get params method
            @Override
            protected Map<String, String> getParams() {
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

        // submitting the request
        network.addToRequestQueue(stringRequest);
    }
}
