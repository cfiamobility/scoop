package ca.gc.inspection.scoop;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.RequestQueue;

import org.json.JSONArray;

import java.sql.Timestamp;

public class NotificationsFragment extends Fragment implements NotificationsController.NotificationInterface{


    private RecyclerView todayRecyclerView, recentRecyclerView;
    private RecyclerView.Adapter todayAdapter, recentAdapter;
    private RecyclerView.LayoutManager todayLayoutManager, recentLayoutManager;
    private TextView today, recent;
    private NotificationsController notificationsScreenController;
    private View view;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
         view = inflater.inflate(R.layout.fragment_notifications, container, false);
         return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);

        notificationsScreenController = new NotificationsController(this); //instantiates controller for notifications screen

        new TodayTask().execute(); //executes async task for today notifications
        new RecentTask().execute(); //executes async task for recent notifications


        today = (TextView) view.findViewById(R.id.fragment_notifications_txt_today); //instantiating the today textview
        recent = (TextView) view.findViewById(R.id.fragment_notifications_txt_recent); //instantiating the recent textview

    }


    /**
     * Description: sets the recycler view for recent notifications
     * @param currentTime: the current time of when notifications were loaded
     * @param requestQueue: request queue to add requests to
     * @param notificationResponse: all the notifications received
     * @param imageResponse: all the images received
     */
    @Override
    public void setRecentRecyclerView(Timestamp currentTime, RequestQueue requestQueue, JSONArray notificationResponse, JSONArray imageResponse) {
        recentRecyclerView = (RecyclerView) view.findViewById(R.id.fragment_notifications_rv_recent); //instantiating the recyclerview
        recentRecyclerView.setHasFixedSize(true);
        recentLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false); //instantiates how the layout should look like for recyclerview
        recentRecyclerView.setLayoutManager(recentLayoutManager); //sets the layout manager to one chosen
        recentAdapter = new NotificationsAdapter(notificationResponse, imageResponse, requestQueue, "recent", currentTime); //instantiates the adapter
        recentRecyclerView.setAdapter(recentAdapter); //sets the adapter
        notificationsScreenController.listenRecentRecyclerView(recentRecyclerView);

    }

    /**
     * Description: sets the recycler view for today notifications
     * @param currentTime: the current time of when the notifications were loaded
     * @param requestQueue: request queue to add requests to
     * @param notificationResponse: all the notifications received
     * @param imageResponse: all the images received
     */
    @Override
    public void setTodayRecyclerView(Timestamp currentTime, RequestQueue requestQueue, JSONArray notificationResponse, JSONArray imageResponse) {
        todayRecyclerView = (RecyclerView) view.findViewById(R.id.fragment_notifications_rv_today); //instantiating the recyclerview
        todayRecyclerView.setHasFixedSize(true); //
        todayLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false); //instantiates how the layout should look like for recyclerview
        todayRecyclerView.setLayoutManager(todayLayoutManager); //sets the layout manager to one chosen
        todayAdapter = new NotificationsAdapter(notificationResponse, imageResponse, requestQueue, "today", currentTime); //instantiates the adapter
        todayRecyclerView.setAdapter(todayAdapter); //sets the adapter
        notificationsScreenController.listenTodayRecyclerView(todayRecyclerView, notificationResponse);

    }

    /**
     * Description: shows the today portion of the fragment
     */
    @Override
    public void showTodaySection() {
        today.setVisibility(View.VISIBLE);
        todayRecyclerView.setVisibility(View.VISIBLE);
        view.findViewById(R.id.view1).setVisibility(View.VISIBLE);
        view.findViewById(R.id.view2).setVisibility(View.VISIBLE);
    }

    /**
     * Description: hides the today portion of the fragment
     */
    @Override
    public void hideTodaySection() {
        todayRecyclerView.setVisibility(View.GONE);
        today.setVisibility(View.GONE);
        view.findViewById(R.id.view1).setVisibility(View.GONE);
        view.findViewById(R.id.view2).setVisibility(View.GONE);
    }

    /**
     * Description: shows the recent portion of the fragment
     */
    @Override
    public void showRecentSection() {
        view.findViewById(R.id.view3).setVisibility(View.VISIBLE);
        view.findViewById(R.id.view4).setVisibility(View.VISIBLE);
        recent.setVisibility(View.VISIBLE);
        recentRecyclerView.setVisibility(View.VISIBLE);
    }

    /**
     * Description: hides the loading panel
     */
    @Override
    public void hideLoadingPanel(){
        view.findViewById(R.id.fragment_notifications_rl_loading_panel).setVisibility(View.GONE); //sets the loading screen to gone
    }

    /**
     * Description: requests focus to the top of the page rather than the today recycler view
     */
    @Override
    public void requestTodayFocus(){
        view.findViewById(R.id.fragment_notifications_rv_today).setFocusable(false);
        view.findViewById(R.id.view1).requestFocus();
    }

    /**
     * Description: requests focus to the top of the recent view rather than the recent recycler view
     */
    @Override
    public void requestRecentFocus(){
        view.findViewById(R.id.fragment_notifications_rv_recent).setFocusable(false);
        view.findViewById(R.id.view3).requestFocus();
    }

    /**
     * Description: performs the call to the notifications screen controller in the background
     */
    private class TodayTask extends AsyncTask<Void, Void, Void>{

        @Override
        protected Void doInBackground(Void... voids) {
            notificationsScreenController.getTodayNotifications(); //gets the today notifications
            return null;
        }
    }

    /**
     * Description: performs the call to the notifications screen controller in the background
     */
    private class RecentTask extends AsyncTask<Void, Void, Void>{

        @Override
        protected Void doInBackground(Void... voids) {
            notificationsScreenController.getRecentNotifications(); //gets the recent notifications
            return null;
        }
    }

}
