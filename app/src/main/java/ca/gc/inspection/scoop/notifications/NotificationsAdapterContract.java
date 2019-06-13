package ca.gc.inspection.scoop.notifications;

import android.graphics.Bitmap;

public interface NotificationsAdapterContract {
    void setActionType(String actionType, NotificationsAdapter.NotificationViewHolder holder);

    void setActivityType(String activityType, NotificationsAdapter.NotificationViewHolder holder);

    void setTime(String time, NotificationsAdapter.NotificationViewHolder holder);

    void hideTime(NotificationsAdapter.NotificationViewHolder holder);

    void setFullName(String fullName, NotificationsAdapter.NotificationViewHolder holder);

    void setImage(Bitmap bitmap, NotificationsAdapter.NotificationViewHolder holder);

    void hideImage(NotificationsAdapter.NotificationViewHolder holder);
}
