package ca.gc.inspection.scoop.notif.notificationstoday;

import java.util.ArrayList;

import ca.gc.inspection.scoop.notif.notificationsrecent.NotificationsRecent;

public class NotificationsDataCache {
    private Class mNotificationsDataType;
    private BaseDataCache mDataCache;

    public static NotificationsDataCache createWithType(Class notificationsDataType) {
        if (!NotificationsToday.class.isAssignableFrom(notificationsDataType))
            return null;
        return new NotificationsDataCache(notificationsDataType);
    }

    private NotificationsDataCache(Class notificationsDataType) {
        mNotificationsDataType = notificationsDataType;
        mDataCache = null;
        if (mNotificationsDataType == NotificationsToday.class)
            mDataCache = new DataCache<>();
        else if (mNotificationsDataType == NotificationsRecent.class)
            mDataCache = new DataCache<NotificationsRecent>();
    }

    private class BaseDataCache {
    }

    @SuppressWarnings("unchecked")
    public NotificationsToday getNotificationsTodayByIndex(int i) {
        if (NotificationsToday.class.isAssignableFrom(mNotificationsDataType))
            return ((DataCache<NotificationsToday>) mDataCache).getItemByIndex(i);
        return null;
    }

    @SuppressWarnings("unchecked")
    public NotificationsRecent getNotificationsRecentByIndex(int i) {
        if (NotificationsRecent.class.isAssignableFrom(mNotificationsDataType))
            return ((DataCache<NotificationsRecent>) mDataCache).getItemByIndex(i);
        return null;
    }

    @SuppressWarnings("unchecked")
    public int getItemCount() {
        return ((DataCache<NotificationsToday>) mDataCache).getItemCount();
    }

    @SuppressWarnings("unchecked")
    public ArrayList<NotificationsToday> getNotificationsTodayList() {
        if (NotificationsToday.class.isAssignableFrom(mNotificationsDataType))
            return ((DataCache<NotificationsToday>) mDataCache).mList;
        return null;
    }

    @SuppressWarnings("unchecked")
    public ArrayList<NotificationsRecent> getNotificationsRecentList() {
        if (NotificationsRecent.class.isAssignableFrom(mNotificationsDataType))
            return ((DataCache<NotificationsRecent>) mDataCache).mList;
        return null;
    }


    class DataCache<T extends NotificationsToday> extends BaseDataCache {

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
