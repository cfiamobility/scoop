package ca.gc.inspection.scoop.notif;

import java.util.ArrayList;

public class NotificationsDataCache {
    private Class mNotificationsDataType;
    private BaseDataCache mDataCache;

    public static NotificationsDataCache createWithType(Class notificationsDataType) {
        if (!Notifications.class.isAssignableFrom(notificationsDataType))
            return null;
        return new NotificationsDataCache(notificationsDataType);
    }

    private NotificationsDataCache(Class notificationsDataType) {
        mNotificationsDataType = notificationsDataType;
        mDataCache = null;
        if (notificationsDataType == Notifications.class)
            mDataCache = new DataCache<>();
//        else if (notificationsDataType == OfficialNotifications.class)
//            mDataCache = new NotificationsDataCache<OfficialNotifications>();
    }


    private class BaseDataCache {

    }

    @SuppressWarnings("unchecked")
    public Notifications getNotificationsByIndex(int i) {
        if (Notifications.class.isAssignableFrom(mNotificationsDataType))
            return ((DataCache<Notifications>) mDataCache).getItemByIndex(i);
        return null;
    }

//    public OfficialNotifications getOfficialNotificationsByIndex(int i) {
//        if (OfficialNotifications.class.isAssignableFrom(mNotificationsDataType))
//            return ((NotificationsDataCache.DataCache<OfficialNotifications>) mDataCache).getItemByIndex(i);
//        return null;
//    }

    @SuppressWarnings("unchecked")
    public int getItemCount() {
        return ((DataCache<Notifications>) mDataCache).getItemCount();
    }

    @SuppressWarnings("unchecked")
    public ArrayList<Notifications> getNotificationsList() {
        if (Notifications.class.isAssignableFrom(mNotificationsDataType))
            return ((DataCache<Notifications>) mDataCache).mList;
        return null;
    }

//    @SuppressWarnings("unchecked")
//    public ArrayList<OfficialNotifications> getOfficialNotificationsList() {
//        if (ProfileComment.class.isAssignableFrom(mNotificationsDataType))
//            return ((NotificationsDataCache.DataCache<OfficialNotifications>) mDataCache).mList;
//        return null;
//    }

    class DataCache<T extends Notifications> extends BaseDataCache {

        ArrayList<T> mList;
        private T t;

        public void set(T t) {
            this.t = t;
        }

        public T get() {
            return t;
        }

        DataCache() {
            mList = new ArrayList<>();
        }

        public ArrayList<T> getList() {
            return mList;
        }

        T getItemByIndex(int i) {
            if (mList == null)
                return null;
            return mList.get(i);
        }

        public int getItemCount() {
            if (mList == null)
                return 0;
            return mList.size();
        }
    }
}
