package ca.gc.inspection.scoop.notifications;

import android.support.v7.widget.RecyclerView;

import com.android.volley.RequestQueue;

import org.json.JSONArray;

import java.sql.Timestamp;

import ca.gc.inspection.scoop.base.BasePresenter;
import ca.gc.inspection.scoop.base.BaseView;


/**
 * Contract between notifications view and presenter (see MVP architecture for more info on contracts)
 */
public interface NotificationsContract {

    /**
     * interface to be implemented by the NotificationsFragment class
     */
    interface View extends BaseView<Presenter> {
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
     * interface to be implemented by the NotificationsPresenter class
     */
    interface Presenter extends BasePresenter {
        void listenRecentRecyclerView(RecyclerView recentRecyclerView);

        void listenTodayRecyclerView(RecyclerView todayRecyclerView, JSONArray response);

        void getTodayNotifications();

        void getRecentNotifications();
    }


}
