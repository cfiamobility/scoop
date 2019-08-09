//package ca.gc.inspection.scoop.notifications;
//
//import android.support.v7.widget.RecyclerView;
//import android.util.Log;
//import android.view.ViewTreeObserver;
//
//import com.android.volley.RequestQueue;
//import com.android.volley.toolbox.Volley;
//
//import org.json.JSONArray;
//
//import java.sql.Timestamp;
//import java.util.Date;
//
//import ca.gc.inspection.scoop.MyApplication;
//import ca.gc.inspection.scoop.notif.notificationstoday.NotificationsTodayContract;
//import ca.gc.inspection.scoop.notif.notificationstoday.NotificationsTodayInteractor;
//import ca.gc.inspection.scoop.util.NetworkUtils;
//
///**
// * Controller for the notifications view/fragment
// */
//public class NotificationsTodayPresenter implements NotificationsTodayContract.Presenter {
//    private RequestQueue requestQueue;
//    private Timestamp currentTime;
//    private NotificationsTodayContract.View notificationsView;
//    private NetworkUtils networkUtils;
//    private int recentEmpty, todayEmpty;
//
//    public NotificationsTodayPresenter(NotificationsTodayContract.View notificationsView, NetworkUtils networkUtils){
//        this.networkUtils = networkUtils;
//        this.notificationsView = notificationsView;
//        this.requestQueue = Volley.newRequestQueue(MyApplication.getContext()); //instantiating the request queue for volley
//        Date date = new Date(); //getting the current date
//        Log.i("date", date.toString());
//        long time = date.getTime(); //getting the current time from the date
//        currentTime = new Timestamp(time); //converting the time to a timestamp object
//    }
//
//    /**
//     * Description: performs the request to get the notifications within the last 24 hours
//     */
//    public void getTodayNotifications(){
//        NotificationsTodayInteractor interactor = new NotificationsTodayInteractor(this, networkUtils);
//        interactor.getTodayNotifications();
//    }
//
//    /**
//     * Invoked by the interactor after receiving response from a JSONArrayRequest to fetch today notifications from server
//     * @param notificationResponse
//     * @param imageResponse
//     */
//    public void getTodayNotificationsCallBack(JSONArray notificationResponse, JSONArray imageResponse){
//        notificationsView.setTodayRecyclerView(currentTime, requestQueue, notificationResponse, imageResponse);
//        notificationsView.onLoadedDataFromDatabase();
//    }
//
//
//    /**
//     * Description: performs the request to get the notifications past 24 hours
//     */
//    public void getRecentNotifications(){
//        NotificationsTodayInteractor interactor = new NotificationsTodayInteractor(this, networkUtils);
//        interactor.getRecentNotifications();
//    }
//
//    /**
//     * Invoked by the interactor after receiving response from a JSONArrayRequest to fetch recent notifications from server
//     * @param notificationResponse
//     * @param imageResponse
//     */
//    public void getRecentNotificationsCallBack(JSONArray notificationResponse, JSONArray imageResponse){
//        notificationsView.setRecentRecyclerView(currentTime, requestQueue, notificationResponse, imageResponse); //calls notificationInterface to set the recent recycler view
//        notificationsView.onLoadedDataFromDatabase();
//    }
//
//
//    /**
//     * Description: listens to when the recent recycler view is all laid out
//     * @param recentRecyclerView: the recycler view to listen to
//     */
//    public void listenRecentRecyclerView(final RecyclerView recentRecyclerView, final JSONArray response){
//        final RecyclerViewReadyCallback recyclerViewReadyCallback = new RecyclerViewReadyCallback() {
//            @Override
//            public void onLayoutReady() throws InterruptedException { //sets all views to visible relevant to recent notifications and sets loading panel to gone
//                if(response.length() == 0){
//                    notificationsView.hideRecentSection();
//                    notificationsView.requestTodayFocus();
//                    recentEmpty = 1;
//                } else {
//                    notificationsView.showRecentSection();
//                    notificationsView.requestRecentFocus();
//                    notificationsView.hideLoadingPanel();
//                    recentEmpty = 0;
//                }
//                checkNothingNew();
//            }
//        };
//        recentRecyclerView.getViewTreeObserver() //listens to see if recyclerview is finished laying out all properties
//                .addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
//                    @Override
//                    public void onGlobalLayout() {
//                        if (recyclerViewReadyCallback != null) {
//                            try {
//                                recyclerViewReadyCallback.onLayoutReady(); //makes sure to only call onLayoutReady once
//                            } catch (InterruptedException e) {
//                                e.printStackTrace();
//                            }
//                        }
//                        recentRecyclerView.getViewTreeObserver().removeOnGlobalLayoutListener(this); //removes the listener
//                    }
//                });
//    }
//
//    /**
//     * Description: listens to when today recycler view is all laid out
//     * @param todayRecyclerView: the recycler view to listen to
//     * @param response: response for today notifications to see if there are any notifications today
//     */
//    public void listenTodayRecyclerView(final RecyclerView todayRecyclerView, final JSONArray response){
//       final RecyclerViewReadyCallback recyclerViewReadyCallback = new RecyclerViewReadyCallback() {
//            @Override
//            public void onLayoutReady() throws InterruptedException {
//                if(response.length() == 0){ //if there is nothing in the today notifications, sets all relevant today sections' visibility to GONE
//                    notificationsView.hideTodaySection();
//                    notificationsView.requestRecentFocus();
//                    todayEmpty = 1;
//                }else { //otherwise sets them to be visible
//                    notificationsView.showTodaySection();
////                    notificationsView.requestTodayFocus();
//                    notificationsView.hideLoadingPanel();
//                    todayEmpty = 0;
//                }
//                checkNothingNew();
//            }
//        };
//        todayRecyclerView.getViewTreeObserver() //listens to see if recyclerview is finished laying out all properties
//                .addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
//                    @Override
//                    public void onGlobalLayout() {
//                        if (recyclerViewReadyCallback != null) {
//                            try {
//                                recyclerViewReadyCallback.onLayoutReady(); //makes sure to only call onLayoutReady once
//                            } catch (InterruptedException e) {
//                                e.printStackTrace();
//                            }
//                        }
//                        todayRecyclerView.getViewTreeObserver().removeOnGlobalLayoutListener(this); //removes the listener
//                    }
//                });
//    }
//
//
//    private void checkNothingNew(){
//        if (recentEmpty == 1 && todayEmpty == 1){
//            notificationsView.showNoNotifications();
//        } else {
//            notificationsView.hideNoNotifications();
//        }
//    }
//
//    /**
//     * Description: Interface for when the layout is ready after recycler view is done setting up
//     */
//    public interface RecyclerViewReadyCallback {
//        void onLayoutReady() throws InterruptedException;
//    }
//}
