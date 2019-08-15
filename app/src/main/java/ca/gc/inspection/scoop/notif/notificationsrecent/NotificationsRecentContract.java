package ca.gc.inspection.scoop.notif.notificationsrecent;

import ca.gc.inspection.scoop.base.BaseView;
import ca.gc.inspection.scoop.notif.notificationstoday.NotificationsTodayContract;

/**
 * Subclass of NotificationsTodayContract and describes the communication between the View and Presenter
 * for the Notifications Recent Tab
 * See superclass (NotificationsTodayContract) for detailed documentation
 */
public interface NotificationsRecentContract extends NotificationsTodayContract {

    /**
     * Implemented by the main View (ie. NotificationsRecentFragment).
     * Methods specified here in the View but not in the nested View.Adapter and View.ViewHolder interfaces
     * explain how the Presenter is to communicate with the main View only.
     */
    interface View extends BaseView<Presenter> {
        void onLoadedDataFromDatabase();

        void showNoNotifications();
        void hideNoNotifications();

        /**
         * Setter methods to be used in a chain within the Presenter
         */
        interface ViewHolder extends NotificationsTodayContract.View.ViewHolder{
        }

        interface Adapter extends NotificationsTodayContract.View.Adapter{
            void refreshAdapter();
        }
    }

    /**
     * Implemented by the Presenter (ie. NotificationsRecentPresenter).
     * Methods specified here in the Presenter but not in the nested Presenter.Adapter and Presenter.ViewHolder interfaces
     * explain how the View is to communicate with the Presenter.
     */
    interface Presenter extends NotificationsTodayContract.Presenter{
        void loadDataFromDatabase();

        /**
         * No methods required to communicate between Presenter and ViewHolder
         */
        interface ViewHolderAPI extends NotificationsTodayContract.Presenter.ViewHolderAPI {
        }

        /**
         * Describes how the Adapter is to communicate with the Presenter
         * Note: getter methods are inherited from superclass
         */
        interface AdapterAPI extends NotificationsTodayContract.Presenter.AdapterAPI{
            void setAdapter(NotificationsRecentContract.View.Adapter adapter);
            void onBindViewHolderAtPosition(
                    NotificationsRecentContract.View.ViewHolder notificationsViewHolder, int i);
        }

    }
}



