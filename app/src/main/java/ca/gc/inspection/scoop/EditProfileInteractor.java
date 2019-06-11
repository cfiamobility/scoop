package ca.gc.inspection.scoop;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
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

        singleton.addToRequestQueue(jsonObjectRequest);
    }

    public void getPositionAutoCompleteFromDB(MySingleton singleton, String positionChangedCapitalized) {
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
        singleton.addToRequestQueue(getRequest);
    }

    // takes care of the requests when the text is changed in the building edittext
    public void getAddressAutoCompleteFromDB(MySingleton singleton, String addressChangedCapitalized) {
        // URL TO BE CHANGED - address passed as parameter to nodeJS
        String URL = Config.baseIP + "edituser/addresschanged/" + addressChangedCapitalized;

        // Asking for a JSONArray
        JsonArrayRequest getRequest = new JsonArrayRequest(Request.Method.GET, URL, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                mPresenter.setAddressAutoCompleteFromDB(response);
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
        // Submitting the request
        singleton.addToRequestQueue(getRequest);
    }

    // takes care of the requests when the text is changed in the divisions edittext
    public void getDivisionAutoCompleteFromDB(MySingleton singleton, String divisionChangedCapitalized) {
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
        singleton.addToRequestQueue(jsonArrayRequest);
    }

    public void updateUserInfo(MySingleton singleton, Map<String, String> params) {
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
        singleton.addToRequestQueue(stringRequest);
    }
}
