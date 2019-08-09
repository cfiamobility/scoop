package ca.gc.inspection.scoop.notif.notificationsrecent;

import android.view.View;

import ca.gc.inspection.scoop.notif.notificationstoday.NotificationsTodayViewHolder;

public class NotificationsRecentViewHolder extends NotificationsTodayViewHolder
    implements NotificationsRecentContract.View.ViewHolder{

    NotificationsRecentContract.Presenter.ViewHolderAPI mPresenter;

    NotificationsRecentViewHolder(View v, NotificationsRecentContract.Presenter.ViewHolderAPI presenter) {
        super(v, presenter);

        mPresenter = presenter;
    }

}
