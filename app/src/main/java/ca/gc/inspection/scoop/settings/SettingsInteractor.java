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

/**
 * Integrator used by the SettingsPresenter to fetch data from the server/database
 */
class SettingsInteractor {

    private SettingsPresenter mPresenter;
    private NetworkUtils mNetwork;

    SettingsInteractor(@NonNull SettingsPresenter presenter, NetworkUtils network) {
        mPresenter = checkNotNull(presenter);
        mNetwork = network;
    }

    /**
     * Sends settings which the user edited to the server to be updated in the database
     * @param settingsMap Hash map containing ONLY settings which have been edited. Setting which the user didn't touch won't be sent
     */
    public void updateSettings(HashMap<String, String> settingsMap) {
        String URL = Config.baseIP + "settings/updateSettings";

        StringRequest postRequest = new StringRequest(Request.Method.POST, URL,
                response -> {
                    if (response.contains(DATABASE_RESPONSE_SUCCESS)){
                        mPresenter.updateLocalPreferences();
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

    /**
     * Fetches all the user's setting values from the database
     *  - called upon initially opening the settings activity
     */
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

    }


    /**
     * Fetches all the user's setting values from the database
     * - called upon destroying the settings activity
     */
    public void getUserSettings() {
        String URL = Config.baseIP + "settings/getUserSettings";

        StringRequest postRequest = new StringRequest(Request.Method.POST, URL,
                response -> {
                    // response
                    Log.d("Response", response);

                    try {
                        mPresenter.publishLocalPreferences(new JSONArray(response));
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
    }
}
