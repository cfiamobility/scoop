package ca.gc.inspection.scoop.notif.notificationsrecent;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import androidx.annotation.NonNull;
import ca.gc.inspection.scoop.notif.notificationstoday.NotificationsDataCache;
import ca.gc.inspection.scoop.notif.notificationstoday.NotificationsTodayPresenter;
import ca.gc.inspection.scoop.util.NetworkUtils;

/**
 * NotificationsRecentPresenter is the Presenter for Notifications Recent action case.
 * It is a subclass Presenter for the Notification inheritance hierarchy and implements the AdapterAPI and
 * ViewHolderAPI to allow the Adapter and ViewHolder to communicate with the Presenter
 * Specifically, it contains methods for loading data and binding it to the ViewHolder
 */
public class NotificationsRecentPresenter extends NotificationsTodayPresenter implements
    NotificationsRecentContract.Presenter,
    NotificationsRecentContract.Presenter.AdapterAPI,
    NotificationsRecentContract.Presenter.ViewHolderAPI{

    @NonNull
    private NotificationsRecentContract.View mView;
    private NotificationsRecentInteractor mInteractor;
    private NotificationsRecentContract.View.Adapter mAdapter;
    private boolean refreshingData = false;
    private notificationType NOTIFICATION_TYPE_KEY = notificationType.RECENT;

    /**
     * Constructor that instantiates the View and constructs/instantiates the Interactor
     * @param view main View
     * @param network singleton NetworkUtils to make network request
     */
    NotificationsRecentPresenter(NotificationsRecentContract.View view, NetworkUtils network){
        mView = view;
        mInteractor = new NotificationsRecentInteractor(this, network);
    }

    /**
     * Instantiates the member variable adapter to allow for communication between the Presenter and Adapter
     * @param adapter recycler view adapter
     */
    @Override
    public void setAdapter(NotificationsRecentContract.View.Adapter adapter) {
        mAdapter = adapter;
    }

    /**
     * Instantiates the member Data Cache, communicates to the Adapter to prepare the View and calls
     * the Interactor to get data from the database
     * Note: Not inherited because of different object types/classes (NotificationsRecent vs NotificationsToday)
     */
    @Override
    public void loadDataFromDatabase() {
        if (!refreshingData) {
            refreshingData = true;

            if (mDataCache == null) {
                mDataCache = NotificationsDataCache.createWithType(NotificationsRecent.class);
            } else {
                mDataCache.getNotificationsRecentList().clear();
            }
            mInteractor.getNotifications(NOTIFICATION_TYPE_KEY.toString().toLowerCase());
        }
    }

    /**
     * Invoked on response from the Interactor; if the JSONArray contains results then iterate through add each to
     * the Data Cache
     * Otherwise, display noNotifications content
     * Once data is set, communicate to View that data is loaded
     * @param notificationResponse JSONArray containing data for each notification
     * @param imageResponse JSONArray containing image data for each notification
     */
    @Override
    public void setData(JSONArray notificationResponse, JSONArray imageResponse) {
        if (notificationResponse.length() == 0) {
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
                NotificationsRecent notification = new NotificationsRecent(jsonNotification,jsonImage);
                mDataCache.getNotificationsRecentList().add(notification);
            }
            mView.hideNoNotifications();
        }
        mAdapter.refreshAdapter();
        refreshingData = false;
        mView.onLoadedDataFromDatabase();
    }

    /**
     * Helper method to get NotificationsToday object corresponding to a position in the Data Cache
     * @param i position in the Data Cache
     * @return NotificationsToday object at position i in the Data Cache
     */
    private NotificationsRecent getItemByIndex(int i) {
        if (mDataCache == null)
            return null;
        return mDataCache.getNotificationsRecentByIndex(i);
    }

    /**
     * Method that sets instantiates a NotificationsRecent object to a corresponding position in the recycler View
     * Note: Method is not inherited because of the difference in parameter types (view are not inherited)
     * @param viewHolder corresponding viewHolder in recycler View to be set with NotificationsRecent object data
     * @param i position in recycler view
     */
    public void onBindViewHolderAtPosition(NotificationsRecentContract.View.ViewHolder viewHolder, int i) {
        NotificationsRecent notification = getItemByIndex(i);
        bindNotificationDataToViewHolder(viewHolder, notification);
    }

    /**
     * Helper method that invokes the viewHolder setter methods with the necessary parameters
     * Note: Method is not inherited because of the difference in parameter types (view are not inherited)
     * @param viewHolder viewHolder whose UI is being set
     * @param notification NotificationsRecent object with the necessary information
     */
    private static void bindNotificationDataToViewHolder(
            NotificationsRecentContract.View.ViewHolder viewHolder, NotificationsRecent notification) {
        if (notification != null) {
            viewHolder.setActionType(notification.getActionType())
                    .setActivityType(notification.getActivityType())
                    .setTime(notification.getModifiedDate())
                    .setFullName(notification.getValidFullName())
                    .setUserImageFromString(notification.getNotifierProfileImage())
                    .setPostImageFromString(notification.getPostImage());
        }
    }
    
    
}
