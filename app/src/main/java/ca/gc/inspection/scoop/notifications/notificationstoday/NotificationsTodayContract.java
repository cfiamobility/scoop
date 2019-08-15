package ca.gc.inspection.scoop.notifications.notificationstoday;

import ca.gc.inspection.scoop.base.BasePresenter;
import ca.gc.inspection.scoop.base.BaseView;

/**
 * A contract between the View (layer) and Presenter for the Notifications Today tab
 * This contract communicates the how the View and Presenter will interact with each other.
 *
 * Since RecyclerView is being used in this package, we must additionally specify the interaction
 * between sub-Views (Adapter/ViewHolder) and the Presenter (via AdapterAPI/ViewHolderAPI).
 *
 * View = NotificationsTodayFragment - is the main View responsible for creating the Presenter and Adapter
 *      View.Adapter - NotificationsAdapter is the subview responsible for creating ViewHolders
 *      View.ViewHolder - NotificationsViewHolder is the subview which displays an individual profile comment
 *
 * Presenter = NotificationsPresenter - there's only one object which implements:
 *      Presenter.AdapterAPI - used by the Adapter to call Presenter methods
 *      Presenter.ViewHolderAPI - used by the ViewHolders to call Presenter methods
 *
 * If communication is required between the View layer and Presenter layer, interface methods must be used.
 *      - if the Presenter needs the View and vice-versa, the interface should be passed in.
 * If communication is required within the View only or Presenter only, the object itself should be passed in
 * to avoid leaking access to internal methods in the contract.
 *
 * Inheritance hierarchy for Notifications:
 *      NotificationsToday - base notification containing images, action type, activity type, names, and time
 *      ^
 *      NotificationsRecent - extends above but different format of time
 *     
 */
public interface NotificationsTodayContract {

    /**
     * Implemented by the main View (ie. NotificationsTodayFragment).
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
            interface ViewHolder {
            NotificationsTodayContract.View.ViewHolder setActionType(String actionType);
            NotificationsTodayContract.View.ViewHolder setActivityType(String activityType);
            NotificationsTodayContract.View.ViewHolder setTime(String time);
            NotificationsTodayContract.View.ViewHolder setFullName(String fullName);
            NotificationsTodayContract.View.ViewHolder setUserImageFromString(String image);
            NotificationsTodayContract.View.ViewHolder setPostImageFromString(String image);

        }

        interface Adapter {
            void refreshAdapter();
        }
    }

    /**
     * Implemented by the Presenter (ie. NotificationsTodayPresenter).
     * Methods specified here in the Presenter but not in the nested Presenter.Adapter and Presenter.ViewHolder interfaces
     * explain how the View is to communicate with the Presenter.
     */
    interface Presenter extends BasePresenter {
        void loadDataFromDatabase();

        /**
         * No methods required to communicate between Presenter and ViewHolder
         */
        interface ViewHolderAPI {
        }

        /**
         * Describes how the Adapter is to communicate with the Presenter
         */
        interface AdapterAPI {
            void setAdapter(NotificationsTodayContract.View.Adapter adapter);
            void onBindViewHolderAtPosition(
                    NotificationsTodayContract.View.ViewHolder notificationsViewHolder, int i);
            int getItemCount();

            String getNotifierIdByIndex(int i);
            String getActivityIdByIndex(int i);
            String getReferenceIdByIndex(int i);
        }

    }
}



