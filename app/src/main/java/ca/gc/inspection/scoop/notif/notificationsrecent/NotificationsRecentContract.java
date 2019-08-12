package ca.gc.inspection.scoop.notif.notificationsrecent;

import ca.gc.inspection.scoop.base.BaseView;
import ca.gc.inspection.scoop.notif.notificationstoday.NotificationsTodayContract;

public interface NotificationsRecentContract extends NotificationsTodayContract {

    /**
     * interface to be implemented by the NotificationsFragment class
     */
    interface View extends BaseView<Presenter> {
        void onLoadedDataFromDatabase();

        void showNoNotifications();
        void hideNoNotifications();

        interface ViewHolder extends NotificationsTodayContract.View.ViewHolder{
        }

        interface Adapter extends NotificationsTodayContract.View.Adapter{
            void refreshAdapter();
        }
    }
    /**
     * interface to be implemented by the NotificationsRecentPresenter class
     */
    interface Presenter extends NotificationsTodayContract.Presenter{
        void loadDataFromDatabase();

        interface AdapterAPI extends NotificationsTodayContract.Presenter.AdapterAPI{
            void setAdapter(NotificationsRecentContract.View.Adapter adapter);
            void onBindViewHolderAtPosition(
                    NotificationsRecentContract.View.ViewHolder notificationsViewHolder, int i);
        }

        interface ViewHolderAPI extends NotificationsTodayContract.Presenter.ViewHolderAPI {
        }

    }
}



