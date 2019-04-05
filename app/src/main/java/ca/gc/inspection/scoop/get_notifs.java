package ca.gc.inspection.scoop;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class get_notifs extends AppCompatActivity{


    private RecyclerView todayRecyclerView, recentRecyclerView;
    private RecyclerView.Adapter todayAdapter, recentAdapter;
    private RecyclerView.LayoutManager todayLayoutManager, recentLayoutManager;
    private RequestQueue requestQueue;
    private RecyclerViewReadyCallback recyclerViewReadyCallback;
    private Timestamp currentTime;
    private TextView today, recent;

    public interface RecyclerViewReadyCallback {
        void onLayoutReady() throws InterruptedException;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.get_notifs);

        today = (TextView) findViewById(R.id.today); //instantiating the today textview
        recent = (TextView) findViewById(R.id.recent); //instantiating the recent textview
        getNotifs(); //calling the getNotifs method

        Date date = new Date(); //getting the current date
        long time = date.getTime(); //getting the current time from the date
        currentTime = new Timestamp(time); //converting the time to a timestamp object
    }

    /**
     *  Description: used to get and display all the notifications pertaining to the current user
     */
    private void getNotifs(){
        SharedPreferences sharedPreferences = this.getSharedPreferences("ca.gc.inspection.scoop", Context.MODE_PRIVATE); //instantiates shared preferences within package
        String todayURL = "http://10.0.2.2:3000/todaynotifs/" + "1a3860f2-a8f2-4af6-a577-b43f7cc17cdd"; //sharedPreferences.getString("userId", "lol"); //the url to get notifications related to today with userid obtained from shared preferences
        String recentURL = "http://10.0.2.2:3000/recentnotifs/" + "1a3860f2-a8f2-4af6-a577-b43f7cc17cdd"; //sharedPreferences.getString("userId", "lol"); //the url to get notifications related to recent with userid obtained from shared preferences


        requestQueue = Volley.newRequestQueue(getApplicationContext()); //instantiating the request queue for volley
        JsonArrayRequest todayRequest = new JsonArrayRequest(Request.Method.GET, recentURL, null, new Response.Listener<JSONArray>() { //making a get request for today notifications
            @Override
            public void onResponse(final JSONArray response) {
                todayRecyclerView = (RecyclerView) findViewById(R.id.todayrecyclerview); //instantiating the recyclerview
                todayRecyclerView.setHasFixedSize(true); //
                todayLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false); //instantiates how the layout should look like for recyclerview
                todayRecyclerView.setLayoutManager(todayLayoutManager); //sets the layout manager to one chosen
                todayAdapter = new MyAdapter(response, requestQueue, getApplicationContext(), "today", currentTime); //instantiates the adapter
                todayRecyclerView.setAdapter(todayAdapter); //sets the adapter
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
                recyclerViewReadyCallback = new RecyclerViewReadyCallback() {
                    @Override
                    public void onLayoutReady() throws InterruptedException {
                        Thread.sleep(1250); //sleeps app for 1.25 seconds as it loads
                        if(response.length() == 0){ //if there is nothing in the today notifications, sets all relevant today sections' visibility to GONE
                            todayRecyclerView.setVisibility(View.GONE);
                            today.setVisibility(View.GONE);
                            findViewById(R.id.view1).setVisibility(View.GONE);
                            findViewById(R.id.view2).setVisibility(View.GONE);
                        }else { //otherwise sets them to be visible
                            today.setVisibility(View.VISIBLE);
                            todayRecyclerView.setVisibility(View.VISIBLE);
                            findViewById(R.id.view1).setVisibility(View.VISIBLE);
                            findViewById(R.id.view2).setVisibility(View.VISIBLE);
                        }
                        findViewById(R.id.loadingPanel).setVisibility(View.GONE); //sets the loading screen to gone
                    }
                };
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });

        JsonArrayRequest recentRequest = new JsonArrayRequest(Request.Method.GET, recentURL, null, new Response.Listener<JSONArray>() { //making a get request for recent notifications
            @Override
            public void onResponse(JSONArray response) {
                recentRecyclerView = (RecyclerView) findViewById(R.id.recentrecyclerview); //instantiating the recyclerview
                recentRecyclerView.setHasFixedSize(true);
                recentLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false); //instantiates how the layout should look like for recyclerview
                recentRecyclerView.setLayoutManager(recentLayoutManager); //sets the layout manager to one chosen
                recentAdapter = new MyAdapter(response, requestQueue, getApplicationContext(), "recent", currentTime); //instantiates the adapter
                recentRecyclerView.setAdapter(recentAdapter); //sets the adapter
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
                recyclerViewReadyCallback = new RecyclerViewReadyCallback() {
                    @Override
                    public void onLayoutReady() throws InterruptedException { //sets all views to visible relevant to recent notifications and sets loading panel to gone
                        Thread.sleep(1250); //sleeps app for 1.25 seconds as it loads
                        findViewById(R.id.view3).setVisibility(View.VISIBLE);
                        findViewById(R.id.view4).setVisibility(View.VISIBLE);
                        findViewById(R.id.loadingPanel).setVisibility(View.GONE);
                        recent.setVisibility(View.VISIBLE);
                        recentRecyclerView.setVisibility(View.VISIBLE);
                    }
                };
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
        requestQueue.add(todayRequest); //adds the today request to the request queue
        requestQueue.add(recentRequest); //adds the recent request to the request queue
    }
}
