package ca.gc.inspection.scoop.notif.notificationstoday;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Objects;

import androidx.annotation.NonNull;
import ca.gc.inspection.scoop.util.NetworkUtils;

public class NotificationsTodayPresenter implements
        NotificationsTodayContract.Presenter,
        NotificationsTodayContract.Presenter.AdapterAPI,
        NotificationsTodayContract.Presenter.ViewHolderAPI{

    private static final String TAG = "NOTIFICATIONS_PRESENTER";

    @NonNull
    private NotificationsTodayContract.View mView;
    private NotificationsTodayInteractor mInteractor;
    private NotificationsTodayContract.View.Adapter mAdapter;
    private boolean refreshingData = false;
    protected NotificationsDataCache mDataCache;
    private String NOTIFICATION_TYPE_KEY = "today";

    public NotificationsTodayPresenter(){

    }

    public NotificationsTodayPresenter(NotificationsTodayContract.View view, NetworkUtils network){
        mView = view;
        mInteractor = new NotificationsTodayInteractor(this, network);
    }

    public void setAdapter(NotificationsTodayContract.View.Adapter adapter) {
        mAdapter = adapter;
    }

    public void loadDataFromDatabase() {
        if (!refreshingData) {
            refreshingData = true;

            if (mDataCache == null) {
                mDataCache = NotificationsDataCache.createWithType(NotificationsToday.class);
            } else {
                mDataCache.getNotificationsTodayList().clear();
            }
            mAdapter.refreshAdapter();
            mInteractor.getNotifications(NOTIFICATION_TYPE_KEY);
        }
    }

    public void setData(JSONArray notificationResponse, JSONArray imageResponse) {
        if (notificationResponse.length() == 0){
            mView.showNoNotifications();
        } else {
            for (int i = 0; i < notificationResponse.length(); i++){
                JSONObject jsonNotification = null;
                JSONObject jsonImage = null;
                try {
                    jsonNotification = notificationResponse.getJSONObject(i);
                    jsonImage = imageResponse.getJSONObject(i);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                NotificationsToday notification = new NotificationsToday(jsonNotification,jsonImage);
                mDataCache.getNotificationsTodayList().add(notification);
            }
            mView.hideNoNotifications();
        }
        mAdapter.refreshAdapter();
        refreshingData = false;
        mView.onLoadedDataFromDatabase();
    }

    private NotificationsToday getItemByIndex(int i) {
        if (mDataCache == null)
            return null;
        return mDataCache.getNotificationsTodayByIndex(i);
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


    public void onBindViewHolderAtPosition(NotificationsTodayContract.View.ViewHolder viewHolder, int i) {
        NotificationsToday notification = getItemByIndex(i);
        bindNotificationDataToViewHolder(viewHolder, notification);
        Log.i(TAG, "Binding: " + i);
    }

    public static void bindNotificationDataToViewHolder(
            NotificationsTodayContract.View.ViewHolder viewHolder, NotificationsToday notification) {
        if (notification != null) {
            viewHolder.setActionType(notification.getActionType())
                    .setActivityType(notification.getActivityType())
                    .setTime(notification.getModifiedDate())
                    .setFullName(notification.getValidFullName())
                    .setUserImageFromString(notification.getNotifierProfileImage())
                    .setPostImageFromString(notification.getPostImage());
        }
    }

    public String getPosterIdByIndex(int i) {
        return Objects.requireNonNull(getItemByIndex(i)).getPosterId();
    }

    public String getActivityIdByIndex(int i){
        return Objects.requireNonNull(getItemByIndex(i)).getActivityId();
    }

    public String getReferenceIdByIndex(int i){
        return Objects.requireNonNull(getItemByIndex(i)).getActivityReferenceId();
    }

}
