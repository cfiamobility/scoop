package ca.gc.inspection.scoop.notifications;

import com.android.volley.RequestQueue;

import org.json.JSONArray;

import java.sql.Timestamp;

public interface NotificationsContract {

    /**
     * Description: Notification Interface to implement to perform all required setting up tasks for layout
     */

    void setRecentRecyclerView(Timestamp currentTime, RequestQueue requestQueue, JSONArray notifications, JSONArray images);
    void setTodayRecyclerView(Timestamp currentTime, RequestQueue requestQueue, JSONArray notifications, JSONArray images);
    void showTodaySection();
    void hideTodaySection();
    void showRecentSection();
    void hideLoadingPanel();
    void requestTodayFocus();
    void requestRecentFocus();


}
