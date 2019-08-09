package ca.gc.inspection.scoop.notif.notificationsrecent;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import androidx.annotation.NonNull;
import ca.gc.inspection.scoop.notif.notificationstoday.NotificationsDataCache;
import ca.gc.inspection.scoop.notif.notificationstoday.NotificationsTodayPresenter;
import ca.gc.inspection.scoop.util.NetworkUtils;

public class NotificationsRecentPresenter extends NotificationsTodayPresenter implements
    NotificationsRecentContract.Presenter,
    NotificationsRecentContract.Presenter.AdapterAPI,
    NotificationsRecentContract.Presenter.ViewHolderAPI{

    @NonNull
    private NotificationsRecentContract.View mView;
    private NotificationsRecentInteractor mInteractor;
    private NotificationsRecentContract.View.Adapter mAdapter;
    private boolean refreshingData = false;

    NotificationsRecentPresenter(NotificationsRecentContract.View view, NetworkUtils network){
        mView = view;
        mInteractor = new NotificationsRecentInteractor(this, network);
    }

    public void setAdapter(NotificationsRecentContract.View.Adapter adapter) {
        mAdapter = adapter;
    }

    public void loadDataFromDatabase() {
        if (!refreshingData) {
            refreshingData = true;

            if (mDataCache == null) {
                mDataCache = NotificationsDataCache.createWithType(NotificationsRecent.class);
            } else {
                mDataCache.getNotificationsRecentList().clear();
            }
            mInteractor.getRecentNotifications();
        }
    }

    public void setRecentData(JSONArray notificationResponse, JSONArray imageResponse) {
        for (int i = 0; i < notificationResponse.length(); i++){
            JSONObject jsonNotification = null;
            JSONObject jsonImage = null;
            try {
                jsonNotification = notificationResponse.getJSONObject(i);
                jsonImage = imageResponse.getJSONObject(i);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            NotificationsRecent notification = new NotificationsRecent(jsonNotification,jsonImage);
            mDataCache.getNotificationsRecentList().add(notification);
        }

        mAdapter.refreshAdapter();
        refreshingData = false;
        mView.onLoadedDataFromDatabase();
    }

    private NotificationsRecent getItemByIndex(int i) {
        if (mDataCache == null)
            return null;
        return mDataCache.getNotificationsRecentByIndex(i);
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


    public void onBindViewHolderAtPosition(NotificationsRecentContract.View.ViewHolder viewHolder, int i) {
        NotificationsRecent notification = getItemByIndex(i);
        bindNotificationDataToViewHolder(viewHolder, notification);
    }

    public static void bindNotificationDataToViewHolder(
            NotificationsRecentContract.View.ViewHolder viewHolder, NotificationsRecent notification) {
        if (notification != null) {
            viewHolder.setActionType(notification.getActionType())
                    .setActivityType(notification.getActivityType())
                    .setTime(notification.getModifiedDate())
                    .setFullName(notification.getValidFullName())
                    .setUserImageFromString(notification.getNotifierProfileImage());
        }
    }
    
    
}
