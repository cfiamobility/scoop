package ca.gc.inspection.scoop.editprofile;

import android.support.annotation.NonNull;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

import ca.gc.inspection.scoop.Config;
import ca.gc.inspection.scoop.TabFragment;
import ca.gc.inspection.scoop.util.NetworkUtils;

import static ca.gc.inspection.scoop.Config.DATABASE_RESPONSE_SUCCESS;
import static com.google.gson.internal.$Gson$Preconditions.checkNotNull;

class EditProfileInteractor {
    private EditProfilePresenter mPresenter;
    private NetworkUtils mNetwork;

    EditProfileInteractor(@NonNull EditProfilePresenter presenter, NetworkUtils network) {
        mPresenter = checkNotNull(presenter);
        mNetwork = network;
    }

    void initialFill() {
        // URL TO BE CHANGED - userID passed as a parameter to NodeJS
        String URL = Config.baseIP + "edituser/getinitial/" + EditProfileActivity.userID;

        // Requesting response be sent back as a JSON Object
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, URL, null,
                response -> mPresenter.setInitialFill(response), error -> Log.i("error", error.toString())) {
            @Override
            public Map<String, String> getHeaders() {
                // inserting the token into the response header that will be sent to the server
                Map<String, String> header = new HashMap<>();
                header.put("authorization", Config.token);
                return header;
            }
        };
        mNetwork.addToRequestQueue(jsonObjectRequest);
    }

    void getPositionAutoCompleteFromDB(String positionChangedCapitalized) {
        // URL TO BE CHANGED - position entered passed to NodeJS as a parameter
        String URL = Config.baseIP + "edituser/positionchanged/" + positionChangedCapitalized;

        // Asking for an array from response (will send back 3 objects in an array)
        JsonArrayRequest getRequest = new JsonArrayRequest(Request.Method.GET, URL, null,
                response -> mPresenter.setPositionAutoCompleteFromDB(response), error -> {}) {
            @Override
            public Map<String, String> getHeaders() {
                // inserting the token into the response header that will be sent to the server
                Map<String, String> header = new HashMap<>();
                header.put("authorization", Config.token);
                return header;
            }
        };
        mNetwork.addToRequestQueue(getRequest);
    }

    // takes care of the requests when the text is changed in the divisions edittext
    void getDivisionAutoCompleteFromDB(String divisionChangedCapitalized) {
        // Inputted division is passed as a parameter to NodeJS
        String URL = Config.baseIP + "edituser/divisionchanged/" + divisionChangedCapitalized;

        // Asking for a JSONArray back
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, URL, null,
                response -> mPresenter.setDivisionAutoCompleteFromDB(response), error -> {}) {
            @Override
            public Map<String, String> getHeaders() {
                // inserting the token into the response header that will be sent to the server
                Map<String, String> header = new HashMap<>();
                header.put("authorization", Config.token);
                return header;
            }
        };
        // submitting the request
        mNetwork.addToRequestQueue(jsonArrayRequest);
    }

    void updateUserInfo(Map<String, String> params) {
        // Request url
        String URL = Config.baseIP + "edituser/updatedatabase";

        // Asking for a string back
        StringRequest stringRequest = new StringRequest(Request.Method.PUT, URL, response -> {
            if (response.equals(DATABASE_RESPONSE_SUCCESS)) {
                TabFragment.refresh();
                mPresenter.onProfileUpdated(true);
            }
            else mPresenter.onProfileUpdated(false);
        }, error -> mPresenter.onProfileUpdated(false)) {
            // Inputting the hashmap into the get params method
            @Override
            protected Map<String, String> getParams() {
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
        // submitting the request
        mNetwork.addToRequestQueue(stringRequest);
    }
}
