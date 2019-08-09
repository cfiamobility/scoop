package ca.gc.inspection.scoop.notif.notificationsrecent;

import ca.gc.inspection.scoop.notif.notificationstoday.NotificationsTodayInteractor;
import ca.gc.inspection.scoop.util.NetworkUtils;

public class NotificationsRecentInteractor extends NotificationsTodayInteractor {

    protected NotificationsRecentPresenter mPresenter;
    public NetworkUtils mNetwork;

    /**
     * reference to the presenter is passed into the interactor so that we can return json results back into the presenter after performing a json request
     * @param presenter a reference to the presenter
     * @param network reference the networksUtils singleton
     */
    public NotificationsRecentInteractor(NotificationsRecentPresenter presenter, NetworkUtils network){
        super(presenter, network);
    }
    
}
