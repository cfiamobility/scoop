package ca.gc.inspection.scoop.notif;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import androidx.annotation.NonNull;
import ca.gc.inspection.scoop.util.NetworkUtils;

public class NotificationsPresenter implements
        NotificationsContract.Presenter,
        NotificationsContract.Presenter.AdapterAPI,
        NotificationsContract.Presenter.ViewHolderAPI{

    private static final String TAG = "NOTIFICATIONS_PRESENTER";

    @NonNull
    private NotificationsContract.View mView;
    private NotificationsInteractor mInteractor;
    private NotificationsContract.View.Adapter mAdapter;
    private boolean refreshingData = false;

    protected NotificationsDataCache mDataCache;
    private int recentEmpty, todayEmpty = 1;

    NotificationsPresenter(NotificationsContract.View view, NetworkUtils network){
        mView = view;
        mInteractor = new NotificationsInteractor(this, network);
    }

    public void setAdapter(NotificationsContract.View.Adapter adapter) {
        mAdapter = adapter;
    }

    public void loadDataFromDatabase() {
        if (!refreshingData) {
            refreshingData = true;

            if (mDataCache == null) {
                mDataCache = NotificationsDataCache.createWithType(Notifications.class);
            } else {
                mDataCache.getNotificationsList().clear();
            }
            mInteractor.getRecentNotifications();
            //        mInteractor.getTodayNotifications();
        }
    }

//    public void setTodayData(JSONArray notificationResponse, JSONArray imageResponse) {
//        if ((notificationResponse.length() != imageResponse.length()))
//            Log.i(TAG, "length of notificationResponse != imageResponse");
//
//
//        Log.i(TAG,Integer.toString(notificationResponse.length()));
//        for (int i = 0; i < notificationResponse.length(); i++){
//            JSONObject jsonNotification = null;
//            JSONObject jsonImage = null;
//            try {
//                jsonNotification = notificationResponse.getJSONObject(i);
//                jsonImage = imageResponse.getJSONObject(i);
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//            Notifications notification = new Notifications(jsonNotification,jsonImage);
//            todayArrayList.add(notification);
//        }
//
//        mAdapter.refreshAdapter();
//        mView.onLoadedDataFromDatabase();
//    }

    public void setRecentData(JSONArray notificationResponse, JSONArray imageResponse){
        checkRecentData(notificationResponse);

        if ((notificationResponse.length() != imageResponse.length()))
            Log.i(TAG, "length of notificationResponse != imageResponse");

        for (int i = 0; i < notificationResponse.length(); i++){
            JSONObject jsonNotification = null;
            JSONObject jsonImage = null;
            try {
                jsonNotification = notificationResponse.getJSONObject(i);
                jsonImage = imageResponse.getJSONObject(i);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            Notifications notification = new Notifications(jsonNotification,jsonImage);
            mDataCache.getNotificationsList().add(notification);
        }
        Log.i(TAG, Integer.toString(getItemCount()));

        mAdapter.refreshAdapter();
        refreshingData = false;
        mView.onLoadedDataFromDatabase();
    }

    /**
     * checks for recent data, returns boolean, if true, allows second POST request for user images
     * @param notificationResponse
     */
    public void checkRecentData(JSONArray notificationResponse){
        if(notificationResponse.length() == 0){
            mView.hideRecentSection();
//            mView.requestTodayFocus();
            recentEmpty = 1;
            Log.i(TAG, "RECENT IS NOT SHOWING");
        } else {
            mView.showRecentSection();
            Log.i(TAG, "SHOWING RECENT");
//            mView.requestRecentFocus();
//            mView.hideLoadingPanel();
            recentEmpty = 0;
        }
        checkNothingNew();
    }

    private void checkNothingNew(){
        Log.i(TAG, Integer.toString(recentEmpty));
        Log.i(TAG, Integer.toString(todayEmpty));
        if (recentEmpty == 1 && todayEmpty == 1){
            mView.showNoNotifications();
            Log.i(TAG, "showing");
        } else {
            mView.hideNoNotifications();
            Log.i(TAG, "hiding");
        }
    }


    private Notifications getItemByIndex(int i) {
        if (mDataCache == null)
            return null;
        return mDataCache.getNotificationsByIndex(i);
    }

    /**
     * Gets the number of items in the DataCache
     * @return the count
     */
    @Override
    public int getItemCount() {
        if (mDataCache == null)
            return 1;
        return mDataCache.getItemCount();
    }


    public void onBindViewHolderAtPosition(NotificationsContract.View.ViewHolder viewHolder, int i) {
        Notifications notification = getItemByIndex(i);
        bindNotificationDataToViewHolder(viewHolder, notification);
        Log.i(TAG, "Binding: " + i);
    }

    public static void bindNotificationDataToViewHolder(
            NotificationsContract.View.ViewHolder viewHolder, Notifications notification) {
        if (notification != null) {
            viewHolder.setActionType(notification.getActionType())
                    .setActivityType(notification.getActivityType())
                    .setTime(notification.getModifiedDate())
                    .setFullName(notification.getValidFullName())
                    .setUserImageFromString(notification.getOtherProfileImage());
        }
    }

}
