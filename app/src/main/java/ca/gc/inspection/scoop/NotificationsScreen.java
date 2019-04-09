package ca.gc.inspection.scoop;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.RequestQueue;

import org.json.JSONArray;

import java.sql.Timestamp;

public class NotificationsScreen extends Fragment implements NotificationsScreenController.NotificationInterface{


    private RecyclerView todayRecyclerView, recentRecyclerView;
    private RecyclerView.Adapter todayAdapter, recentAdapter;
    private RecyclerView.LayoutManager todayLayoutManager, recentLayoutManager;
    private TextView today, recent;
    private NotificationsScreenController notificationsScreenController;
    private View view;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
         view = inflater.inflate(R.layout.fragment_notifications_screen, container, false);
         return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);

        notificationsScreenController = new NotificationsScreenController(this);

        new TodayTask().execute();
        new RecentTask().execute();
        notificationsScreenController.getTodayImages();
        notificationsScreenController.getRecentImages();


        today = (TextView) view.findViewById(R.id.today); //instantiating the today textview
        recent = (TextView) view.findViewById(R.id.recent); //instantiating the recent textview
    }

  

    @Override
    public void setRecentRecyclerView(Timestamp currentTime, RequestQueue requestQueue, JSONArray notificationResponse, JSONArray imageResponse) {
        recentRecyclerView = (RecyclerView) view.findViewById(R.id.recentrecyclerview); //instantiating the recyclerview
        recentRecyclerView.setHasFixedSize(true);
        recentLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false); //instantiates how the layout should look like for recyclerview
        recentRecyclerView.setLayoutManager(recentLayoutManager); //sets the layout manager to one chosen
        recentAdapter = new NotificationsScreenAdapter(notificationResponse, imageResponse, requestQueue, "recent", currentTime); //instantiates the adapter
        recentRecyclerView.setAdapter(recentAdapter); //sets the adapter
        notificationsScreenController.listenRecentRecyclerView(recentRecyclerView);

    }

    @Override
    public void setTodayRecyclerView(Timestamp currentTime, RequestQueue requestQueue, final JSONArray response) {
        todayRecyclerView = (RecyclerView) view.findViewById(R.id.todayrecyclerview); //instantiating the recyclerview
        todayRecyclerView.setHasFixedSize(true); //
        todayLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false); //instantiates how the layout should look like for recyclerview
        todayRecyclerView.setLayoutManager(todayLayoutManager); //sets the layout manager to one chosen
        todayAdapter = new NotificationsScreenAdapter(response, requestQueue, "today", currentTime); //instantiates the adapter
        todayRecyclerView.setAdapter(todayAdapter); //sets the adapter
        notificationsScreenController.listenTodayRecyclerView(todayRecyclerView, response);

    }

    @Override
    public void showTodaySection() {
        today.setVisibility(View.VISIBLE);
        todayRecyclerView.setVisibility(View.VISIBLE);
        view.findViewById(R.id.view1).setVisibility(View.VISIBLE);
        view.findViewById(R.id.view2).setVisibility(View.VISIBLE);
    }

    @Override
    public void hideTodaySection() {
        todayRecyclerView.setVisibility(View.GONE);
        today.setVisibility(View.GONE);
        view.findViewById(R.id.view1).setVisibility(View.GONE);
        view.findViewById(R.id.view2).setVisibility(View.GONE);
    }

    @Override
    public void showRecentSection() {
        view.findViewById(R.id.view3).setVisibility(View.VISIBLE);
        view.findViewById(R.id.view4).setVisibility(View.VISIBLE);
        recent.setVisibility(View.VISIBLE);
        recentRecyclerView.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideLoadingPanel(){
        view.findViewById(R.id.loadingPanel).setVisibility(View.GONE); //sets the loading screen to gone
    }

    @Override
    public void requestFocus(){
        view.findViewById(R.id.todayrecyclerview).setFocusable(false);
        view.findViewById(R.id.view1).requestFocus();
    }

    private class TodayTask extends AsyncTask<Void, Void, Void>{

        @Override
        protected Void doInBackground(Void... voids) {
            notificationsScreenController.getTodayNotifications();
            return null;
        }
    }

    private class RecentTask extends AsyncTask<Void, Void, Void>{

        @Override
        protected Void doInBackground(Void... voids) {
            notificationsScreenController.getRecentNotifications();
            return null;
        }
    }

}
