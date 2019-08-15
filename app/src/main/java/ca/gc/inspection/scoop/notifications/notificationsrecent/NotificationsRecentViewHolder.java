package ca.gc.inspection.scoop.notifications.notificationsrecent;

import android.view.View;

import ca.gc.inspection.scoop.notifications.notificationstoday.NotificationsTodayViewHolder;

/**
 * ViewHolder for NotificationRecent; it is the base ViewHolder for notifications and
 * represents a single View of a NotificationRecent
 * Child ViewHolder for NotificationsTodayViewHolder
 */
class NotificationsRecentViewHolder extends NotificationsTodayViewHolder
    implements NotificationsRecentContract.View.ViewHolder{

    NotificationsRecentContract.Presenter.ViewHolderAPI mPresenter;

    /**
     * Constructor that instantiates the Android Views from the given ViewGroup to the member UI variables
     * Inherits all android views from superclass, as a NotificationsRecent contains the same Views as a NotificationsToday
     * @param v viewgroup from item_notifications.xml
     * @param presenter presenter to be referenced
     */
    NotificationsRecentViewHolder(View v, NotificationsRecentContract.Presenter.ViewHolderAPI presenter) {
        super(v, presenter);

        mPresenter = presenter;
    }

}
