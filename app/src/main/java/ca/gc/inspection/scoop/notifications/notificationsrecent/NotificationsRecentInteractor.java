package ca.gc.inspection.scoop.notifications.notificationsrecent;

import ca.gc.inspection.scoop.notifications.notificationstoday.NotificationsTodayInteractor;
import ca.gc.inspection.scoop.util.NetworkUtils;

/**
 * NotificationsRecentInteractor used by Presenter to create GET request for the notifications table
 * Uses super class's getNotifications() method
 */
public class NotificationsRecentInteractor extends NotificationsTodayInteractor {

    protected NotificationsRecentPresenter mPresenter;
    public NetworkUtils mNetwork;

    /**
     * Public constructor
     * Note: The reason for passing in an instance of NetworkUtils through the constructor is that it allows us to assign it to the mNetwork
     *       member variable (a class accessible variable, rather than a local variable that is assigned through a method parameter).
     *       This makes it more convenient to add to the request queue for nested network requests
     * Supers parent class constructor
     * @param presenter A reference to the presenter is passed in through so that we can return the JSON results back into the presenter
     * @param network  A reference to a NetworkUtils instance is required to send network request
     */
    public NotificationsRecentInteractor(NotificationsRecentPresenter presenter, NetworkUtils network){
        super(presenter, network);
    }
    
}
