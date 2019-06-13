package ca.gc.inspection.scoop.notifications;

import android.content.Context;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;

import org.json.JSONArray;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

import ca.gc.inspection.scoop.Config;
import ca.gc.inspection.scoop.NetworkUtils;

/**
 * Handles notification database request from the notifications controller
 */
public class NotificationsInteractor {


    private RequestQueue requestQueue;
    private NotificationsController controller;

    public NotificationsInteractor(NotificationsController controller, NetworkUtils networkUtils){
        this.requestQueue = networkUtils.getRequestQueue();
        this.controller = controller;
    }

    public void getTodayNotifications(){
        String todayURL = Config.baseIP + "notifications/todaynotifs/" + Config.currentUser; //the url to get notifications related to today with userid
        JsonArrayRequest todayRequest = new JsonArrayRequest(Request.Method.GET, todayURL, null, new Response.Listener<JSONArray>() { //making a get request for today notifications
            @Override
            public void onResponse(final JSONArray notificationResponse) {
                String todayImagesURL = Config.baseIP + "notifications/todayimages/"  + Config.currentUser; //url for getting images related to today with user id
                JsonArrayRequest todayImageRequest = new JsonArrayRequest(Request.Method.GET, todayImagesURL, null, new Response.Listener<JSONArray>() { //making a get request for today images
                    @Override
                    public void onResponse(final JSONArray imageResponse) {
                        //notificationsContract.setTodayRecyclerView(currentTime, requestQueue, notificationResponse, imageResponse); //calls notificationInterface to set the today recycler view
                        controller.getTodayNotificationsCallBack(notificationResponse, imageResponse);
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
                requestQueue.add(todayImageRequest); //adds image request to request queue
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
        requestQueue.add(todayRequest); //adds today request to request queue
    }

    public void getRecentNotifications(){
        String recentURL = Config.baseIP + "notifications/recentnotifs/" + Config.currentUser; //the url to get notifications related to recent with userid"1a3860f2-a8f2-4af6-a577-b43f7cc17cdd";
        JsonArrayRequest recentRequest = new JsonArrayRequest(Request.Method.GET, recentURL, null, new Response.Listener<JSONArray>() { //making a get request for recent notifications
            @Override
            public void onResponse(final JSONArray notificationResponse) {
                String recentImagesUrl = Config.baseIP + "notifications/recentimages/"  + Config.currentUser; //url for getting images related to recent with user id
                JsonArrayRequest recentImageRequest = new JsonArrayRequest(Request.Method.GET, recentImagesUrl, null, new Response.Listener<JSONArray>() { //making a get request for recent images
                    @Override
                    public void onResponse(final JSONArray imageResponse) {
                        //notificationsContract.setRecentRecyclerView(currentTime, requestQueue, notificationResponse, imageResponse); //calls notificationInterface to set the recent recycler view
                        controller.getRecentNotificationsCallBack(notificationResponse, imageResponse);
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
                requestQueue.add(recentImageRequest); //adds image request to request queue
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
        requestQueue.add(recentRequest); //adds recent request to request queue
    }
}
