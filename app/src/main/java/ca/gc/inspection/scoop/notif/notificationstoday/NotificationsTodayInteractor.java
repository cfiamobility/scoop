package ca.gc.inspection.scoop.notif.notificationstoday;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;

import org.json.JSONArray;

import java.util.HashMap;
import java.util.Map;

import ca.gc.inspection.scoop.Config;
import ca.gc.inspection.scoop.util.NetworkUtils;

/**
 * Handles notification database request from the notifications presenter
 */
public class NotificationsTodayInteractor {

    protected NotificationsTodayPresenter mPresenter;
    public NetworkUtils mNetwork;

    /**
     * reference to the presenter is passed into the interactor so that we can return json results back into the presenter after performing a json request
     * @param presenter a reference to the presenter
     * @param network reference the networksUtils singleton
     */
    public NotificationsTodayInteractor(NotificationsTodayPresenter presenter, NetworkUtils network){
        mNetwork = network;
        mPresenter = presenter;
    }

    /**
     * gets today notifications from the database
     */
    public void getNotifications(String notificationType){
        String todayURL = Config.baseIP + "notifications/" + notificationType + "/" + Config.currentUser; //the url to get notifications related to today with userid
        JsonArrayRequest todayRequest = new JsonArrayRequest(Request.Method.GET, todayURL, null, new Response.Listener<JSONArray>() { //making a get request for today notifications
            @Override
            public void onResponse(final JSONArray notificationResponse) {
                String todayImagesURL = Config.baseIP + "notifications/" + notificationType + "/images/"  + Config.currentUser; //url for getting images related to today with user id
                JsonArrayRequest todayImageRequest = new JsonArrayRequest(Request.Method.GET, todayImagesURL, null, new Response.Listener<JSONArray>() { //making a get request for today images
                    @Override
                    public void onResponse(final JSONArray imageResponse) {
                        //notificationsContract.setTodayRecyclerView(currentTime, requestQueue, notificationResponse, imageResponse); //calls notificationInterface to set the today recycler view
//                       presenter.getTodayNotificationsCallBack(notificationResponse, imageResponse); // return results back to the presenter
                        mPresenter.setData(notificationResponse, imageResponse);
                    }
                    }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
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
                mNetwork.addToRequestQueue(todayImageRequest); //adds image request to request queue
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
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
        mNetwork.addToRequestQueue(todayRequest); //adds today request to request queue
    }
}
