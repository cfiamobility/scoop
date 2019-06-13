package ca.gc.inspection.scoop.notifications;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
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

import ca.gc.inspection.scoop.Config;
import ca.gc.inspection.scoop.MyApplication;
import ca.gc.inspection.scoop.NetworkUtils;

public class NotificationsController {
    private RequestQueue requestQueue;
    private Timestamp currentTime;
    private NotificationsContract notificationsContract;
    private NetworkUtils networkUtils;


    public NotificationsController(NotificationsContract notificationsContract, NetworkUtils networkUtils){
        this.networkUtils = networkUtils;
        this.notificationsContract = notificationsContract;
        this.requestQueue = Volley.newRequestQueue(MyApplication.getContext()); //instantiating the request queue for volley
        Date date = new Date(); //getting the current date
        Log.i("date", date.toString());
        long time = date.getTime(); //getting the current time from the date
        currentTime = new Timestamp(time); //converting the time to a timestamp object
    }

    /**
     * Description: performs the request to get the notifications within the last 24 hours
     */
    public void getTodayNotifications(){
        NotificationsInteractor interactor = new NotificationsInteractor(this, networkUtils);
        interactor.getTodayNotifications();
    }
    public void getTodayNotificationsCallBack(JSONArray notificationResponse, JSONArray imageResponse){
        notificationsContract.setTodayRecyclerView(currentTime, requestQueue, notificationResponse, imageResponse);
    }


    /**
     * Description: performs the request to get the notifications past 24 hours
     */
    public void getRecentNotifications(){
        NotificationsInteractor interactor = new NotificationsInteractor(this, networkUtils);
        interactor.getRecentNotifications();
    }
    public void getRecentNotificationsCallBack(JSONArray notificationResponse, JSONArray imageResponse){
        notificationsContract.setRecentRecyclerView(currentTime, requestQueue, notificationResponse, imageResponse); //calls notificationInterface to set the recent recycler view
    }


    /**
     * Description: listens to when the recent recycler view is all laid out
     * @param recentRecyclerView: the recycler view to listen to
     */
    public void listenRecentRecyclerView(final RecyclerView recentRecyclerView){
        final RecyclerViewReadyCallback recyclerViewReadyCallback = new RecyclerViewReadyCallback() {
            @Override
            public void onLayoutReady() throws InterruptedException { //sets all views to visible relevant to recent notifications and sets loading panel to gone
                notificationsContract.showRecentSection();
                notificationsContract.hideLoadingPanel();
                notificationsContract.requestTodayFocus();
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
                    notificationsContract.hideTodaySection();
                    notificationsContract.requestRecentFocus();
                }else { //otherwise sets them to be visible
                    notificationsContract.showTodaySection();
                    notificationsContract.requestTodayFocus();
                }
                notificationsContract.hideLoadingPanel();

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

/*
    */
/**
     * Description: Notification Interface to implement to perform all required setting up tasks for layout
     *//*

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
*/

    /**
     * Description: Interface for when the layout is ready after recycler view is done setting up
     */
    public interface RecyclerViewReadyCallback {
        void onLayoutReady() throws InterruptedException;
    }
}
