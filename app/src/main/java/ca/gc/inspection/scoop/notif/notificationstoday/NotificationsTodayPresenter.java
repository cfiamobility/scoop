package ca.gc.inspection.scoop.notif.notificationstoday;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Objects;

import androidx.annotation.NonNull;
import ca.gc.inspection.scoop.util.NetworkUtils;

/**
 * NotificationsTodayPresenter is the Presenter for Notifications Today action case.
 * It is the base Presenter for the Notification inheritance hierarchy and implements the AdapterAPI and
 * ViewHolderAPI to allow to the Adapter and ViewHolder to communicate with the Presenter
 * Specifically, it contains methods for loading data and binding it to the ViewHolder
 */
public class NotificationsTodayPresenter implements
        NotificationsTodayContract.Presenter,
        NotificationsTodayContract.Presenter.AdapterAPI,
        NotificationsTodayContract.Presenter.ViewHolderAPI{

    @NonNull
    private NotificationsTodayContract.View mView;
    private NotificationsTodayInteractor mInteractor;
    private NotificationsTodayContract.View.Adapter mAdapter;
    private boolean refreshingData = false;
    protected NotificationsDataCache mDataCache;
    private notificationType NOTIFICATION_TYPE_KEY = notificationType.TODAY;

    // enum for notification type - more concrete as a data type than just strings
    public enum notificationType{
        TODAY,
        RECENT,
    }

    /**
     * Protected Default Constructor to allow for inheritance between Presenters
     */
    protected NotificationsTodayPresenter(){

    }

    /**
     * Constructor that instantiates the View and constructs/instantiates the Interactor
     * @param view main View
     * @param network singleton NetworkUtils to make network request
     */
    NotificationsTodayPresenter(NotificationsTodayContract.View view, NetworkUtils network){
        mView = view;
        mInteractor = new NotificationsTodayInteractor(this, network);
    }

    /**
     * Instantiates the member variable adapter to allow for communication between the Presenter and Adapter
     * @param adapter recycler view adapter
     */
    public void setAdapter(NotificationsTodayContract.View.Adapter adapter) {
        mAdapter = adapter;
    }

    /**
     * Instantiates the member Data Cache, communicates to the Adapter to prepare the View and calls
     * the Interactor to get data from the database
     */
    public void loadDataFromDatabase() {
        if (!refreshingData) {
            refreshingData = true;

            if (mDataCache == null) {
                mDataCache = NotificationsDataCache.createWithType(NotificationsToday.class);
            } else {
                mDataCache.getNotificationsTodayList().clear();
            }
            mAdapter.refreshAdapter();
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

    /**
     * Helper method to get NotificationsToday object corresponding to a position in the Data Cache
     * @param i position in the Data Cache
     * @return NotificationsToday object at position i in the Data Cache
     */
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


    /**
     * Method that sets instantiates a NotificationsToday object to a corresponding position in the recycler View
     * @param viewHolder corresponding viewHolder in recycler View to be set with NotificationsToday object data
     * @param i position in recycler view
     */
    public void onBindViewHolderAtPosition(NotificationsTodayContract.View.ViewHolder viewHolder, int i) {
        NotificationsToday notification = getItemByIndex(i);
        bindNotificationDataToViewHolder(viewHolder, notification);
    }

    /**
     * Helper method that invokes the viewHolder setter methods with the necessary parameters
     * @param viewHolder viewHolder whose UI is being set
     * @param notification NotificationsToday object with the necessary information
     */
    private static void bindNotificationDataToViewHolder(
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

    /**
     * Gets the user id of a notifier based on its position in the ViewHolder
     * Used to set parameters of setlistener methods invoked in the adapter
     * @param i position of notification in the recycler view
     * @return userid of notifier as a string
     */
    public String getNotifierIdByIndex(int i) {
        return Objects.requireNonNull(getItemByIndex(i)).getNotifierId();
    }

    /**
     * Gets the activityid of a post/comment based on its position in the ViewHolder
     * Used to set parameters of setlistener methods invoked in the adapter
     * @param i position of notification in the recycler view
     * @return activity id as a string
     */
    public String getActivityIdByIndex(int i){
        return Objects.requireNonNull(getItemByIndex(i)).getActivityId();
    }

    /**
     * Gets the activity reference id of a post/comment based on its position in the ViewHolder
     * Used to set parameters of setlistener methods invoked in the adapter
     * @param i position of notification in the recycler view
     * @return activity reference id as a string
     */
    public String getReferenceIdByIndex(int i){
        return Objects.requireNonNull(getItemByIndex(i)).getActivityReferenceId();
    }

}
