package ca.gc.inspection.scoop.settings;

import android.support.annotation.NonNull;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import ca.gc.inspection.scoop.Config;
import ca.gc.inspection.scoop.util.NetworkUtils;

import static ca.gc.inspection.scoop.Config.DATABASE_RESPONSE_SUCCESS;
import static com.google.gson.internal.$Gson$Preconditions.checkNotNull;

class SettingsInteractor {

    private SettingsPresenter mPresenter;
    private NetworkUtils mNetwork;

    SettingsInteractor(@NonNull SettingsPresenter presenter, NetworkUtils network) {
        mPresenter = checkNotNull(presenter);
        mNetwork = network;
    }

    public void updateSettings(HashMap<String, String> settingsMap) {
        String URL = Config.baseIP + "settings/updateSettings";

        StringRequest postRequest = new StringRequest(Request.Method.POST, URL,
                response -> {
                    // response
                    Log.d("Response", response);
                    if (response.contains(DATABASE_RESPONSE_SUCCESS)){
                        mPresenter.updateLocalPreferences();
                    }
                    else {
                        //TODO: HANDLE ERROR
                    }
                },
                error -> {
                    // error
                    Log.d("Error.Response", String.valueOf(error));
                }
        ) {
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String> params = new HashMap<>(settingsMap); // shallow copy settingsMap
                params.put("userid", Config.currentUser);
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
        mNetwork.addToRequestQueue(postRequest);

    }

    public void loadSettings() {
        String URL = Config.baseIP + "settings/getUserSettings";

        StringRequest postRequest = new StringRequest(Request.Method.POST, URL,
                response -> {
                    // response
                    Log.d("Response", response);

                        try {
                            mPresenter.setSettings(new JSONArray(response));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                },
                error -> {
                    // error
                    Log.d("Error.Response", String.valueOf(error));
                }
        ) {
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String> params = new HashMap<>(); // shallow copy settingsMap
                params.put("userid", Config.currentUser);
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
        mNetwork.addToRequestQueue(postRequest);

        /**

        // Requesting response be sent back as a JSON Object
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.POST, URL, null,  new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                mPresenter.setSettings(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("error", error.toString());
            }
        }) {

            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String> params = new HashMap<>(); // shallow copy settingsMap
                params.put("userid", Config.currentUser);
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

        mNetwork.addToRequestQueue(jsonArrayRequest);
         */

    }
}
