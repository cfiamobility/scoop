package ca.gc.inspection.scoop.notif.notificationstoday;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonArrayRequest;

import java.util.HashMap;
import java.util.Map;

import ca.gc.inspection.scoop.Config;
import ca.gc.inspection.scoop.util.NetworkUtils;

/**
 * NotificationsTodayInteractor used by Presenter to create GET request for the notifications table
 */
public class NotificationsTodayInteractor {

    protected NotificationsTodayPresenter mPresenter;
    public NetworkUtils mNetwork;

    /**
     * Public constructor
     * Note: The reason for passing in an instance of NetworkUtils through the constructor is that it allows us to assign it to the mNetwork
     *       member variable (a class accessible variable, rather than a local variable that is assigned through a method parameter).
     *       This makes it more convenient to add to the request queue for nested network requests
     * @param presenter A reference to the presenter is passed in through so that we can return the JSON results back into the presenter
     * @param network  A reference to a NetworkUtils instance is required to send network request
     */
    public NotificationsTodayInteractor(NotificationsTodayPresenter presenter, NetworkUtils network){
        mNetwork = network;
        mPresenter = presenter;
    }

    /**
     * Creates two GET requests for notification data
     * - Relevant NotificationToday information (e.g. notifications within 24 hours, notifier info)
     * - Profile images and post images (if they exist for a post
     * Note: Uses lambdas instead of new anonymous response classes,
     *       Uses method reference instead of lambdas/new anonymous response error classes
     *       for readability and to follow suggestions from Android Style Guide
     * @param notificationType type of notification; converted from public notificationType enum (TODAY, RECENT, OFFICIAL)
     */
    public void getNotifications(String notificationType){
        String todayURL = Config.baseIP + "notifications/" + notificationType + "/" + Config.currentUser; //the url to get notifications related to today with userid
        // making a get request for today notifications
        JsonArrayRequest todayRequest = new JsonArrayRequest(Request.Method.GET, todayURL, null, notificationResponse -> {
            String todayImagesURL = Config.baseIP + "notifications/" + notificationType + "/images/" + Config.currentUser; //url for getting images related to today with user id
            // making a get request for today images
            JsonArrayRequest todayImageRequest = new JsonArrayRequest(Request.Method.GET, todayImagesURL, null, imageResponse -> {
                // presenter.getTodayNotificationsCallBack(notificationResponse, imageResponse); // return results back to the presenter
                mPresenter.setData(notificationResponse, imageResponse);
            }, Throwable::printStackTrace) {
                @Override
                public Map<String, String> getHeaders() {
                    // inserting the token into the response header that will be sent to the server
                    Map<String, String> header = new HashMap<>();
                    header.put("authorization", Config.token);
                    return header;
                }
            };
            mNetwork.addToRequestQueue(todayImageRequest); //adds image request to request queue
        }, Throwable::printStackTrace) {
            @Override
            public Map<String, String> getHeaders() {
                // inserting the token into the response header that will be sent to the server
                Map<String, String> header = new HashMap<>();
                header.put("authorization", Config.token);
                return header;
            }
        };
        mNetwork.addToRequestQueue(todayRequest); //adds today request to request queue
    }
}
