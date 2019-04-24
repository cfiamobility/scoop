package ca.gc.inspection.scoop;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.widget.RecyclerView;
import android.view.ViewTreeObserver;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;

import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class NotificationsController {
    private RequestQueue requestQueue;
    private Timestamp currentTime;
    private NotificationInterface notificationInterface;
    private SharedPreferences sharedPreferences;


    public NotificationsController(NotificationInterface notificationInterface){
        this.notificationInterface = notificationInterface;
        this.requestQueue = Volley.newRequestQueue(MyApplication.getContext()); //instantiating the request queue for volley
        sharedPreferences = MyApplication.getContext().getSharedPreferences("ca.gc.inspection.scoop", Context.MODE_PRIVATE); //instantiates shared preferences within package
        Date date = new Date(); //getting the current date
        long time = date.getTime(); //getting the current time from the date
        currentTime = new Timestamp(time); //converting the time to a timestamp object
    }

    /**
     * Description: performs the request to get the notifications within the last 24 hours
     */
    public void getTodayNotifications(){
        String todayURL = Config.baseIP + "notifications/todaynotifs/" + Config.currentUser; //the url to get notifications related to today with userid
        JsonArrayRequest todayRequest = new JsonArrayRequest(Request.Method.GET, todayURL, null, new Response.Listener<JSONArray>() { //making a get request for today notifications
            @Override
            public void onResponse(final JSONArray notificationResponse) {
                String todayImagesURL = Config.baseIP + "notifications/todayimages/"  + Config.currentUser; //url for getting images related to today with user id
                JsonArrayRequest todayImageRequest = new JsonArrayRequest(Request.Method.GET, todayImagesURL, null, new Response.Listener<JSONArray>() { //making a get request for today images
                    @Override
                    public void onResponse(final JSONArray imageResponse) {
                        notificationInterface.setTodayRecyclerView(currentTime, requestQueue, notificationResponse, imageResponse); //calls notificationInterface to set the today recycler view
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

    /**
     * Description: performs the request to get the notifications past 24 hours
     */
    public void getRecentNotifications(){
        String recentURL = Config.baseIP + "notifications/recentnotifs/" + Config.currentUser; //the url to get notifications related to recent with userid"1a3860f2-a8f2-4af6-a577-b43f7cc17cdd";
        JsonArrayRequest recentRequest = new JsonArrayRequest(Request.Method.GET, recentURL, null, new Response.Listener<JSONArray>() { //making a get request for recent notifications
            @Override
            public void onResponse(final JSONArray notificationResponse) {
                String recentImagesUrl = Config.baseIP + "notifications/recentimages/"  + Config.currentUser; //url for getting images related to recent with user id
                JsonArrayRequest recentImageRequest = new JsonArrayRequest(Request.Method.GET, recentImagesUrl, null, new Response.Listener<JSONArray>() { //making a get request for recent images
                    @Override
                    public void onResponse(final JSONArray imageResponse) {
                        notificationInterface.setRecentRecyclerView(currentTime, requestQueue, notificationResponse, imageResponse); //calls notificationInterface to set the recent recycler view
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

    /**
     * Description: listens to when the recent recycler view is all laid out
     * @param recentRecyclerView: the recycler view to listen to
     */
    public void listenRecentRecyclerView(final RecyclerView recentRecyclerView){
        final RecyclerViewReadyCallback recyclerViewReadyCallback = new RecyclerViewReadyCallback() {
            @Override
            public void onLayoutReady() throws InterruptedException { //sets all views to visible relevant to recent notifications and sets loading panel to gone
                notificationInterface.showRecentSection();
                notificationInterface.hideLoadingPanel();
                notificationInterface.requestTodayFocus();
            }
        };
        recentRecyclerView.getViewTreeObserver() //listens to see if recyclerview is finished laying out all properties
                .addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        if (recyclerViewReadyCallback != null) {
                            try {
                                recyclerViewReadyCallback.onLayoutReady(); //makes sure to only call onLayoutReady once
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                        recentRecyclerView.getViewTreeObserver().removeOnGlobalLayoutListener(this); //removes the listener
                    }
                });
    }

    /**
     * Description: listens to when today recycler view is all laid out
     * @param todayRecyclerView: the recycler view to listen to
     * @param response: response for today notifications to see if there are any notifications today
     */
    public void listenTodayRecyclerView(final RecyclerView todayRecyclerView, final JSONArray response){
       final RecyclerViewReadyCallback recyclerViewReadyCallback = new RecyclerViewReadyCallback() {
            @Override
            public void onLayoutReady() throws InterruptedException {
                if(response.length() == 0){ //if there is nothing in the today notifications, sets all relevant today sections' visibility to GONE
                    notificationInterface.hideTodaySection();
                    notificationInterface.requestRecentFocus();
                }else { //otherwise sets them to be visible
                    notificationInterface.showTodaySection();
                    notificationInterface.requestTodayFocus();
                }
                notificationInterface.hideLoadingPanel();

            }
        };
        todayRecyclerView.getViewTreeObserver() //listens to see if recyclerview is finished laying out all properties
                .addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        if (recyclerViewReadyCallback != null) {
                            try {
                                recyclerViewReadyCallback.onLayoutReady(); //makes sure to only call onLayoutReady once
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                        todayRecyclerView.getViewTreeObserver().removeOnGlobalLayoutListener(this); //removes the listener
                    }
                });
    }

    /**
     * Description: Notification Interface to implement to perform all required setting up tasks for layout
     */
    public interface NotificationInterface{
        void setRecentRecyclerView(Timestamp currentTime, RequestQueue requestQueue, JSONArray notifications, JSONArray images);
        void setTodayRecyclerView(Timestamp currentTime, RequestQueue requestQueue, JSONArray notifications, JSONArray images);
        void showTodaySection();
        void hideTodaySection();
        void showRecentSection();
        void hideLoadingPanel();
        void requestTodayFocus();
        void requestRecentFocus();
    }

    /**
     * Description: Interface for when the layout is ready after recycler view is done setting up
     */
    public interface RecyclerViewReadyCallback {
        void onLayoutReady() throws InterruptedException;
    }
}
