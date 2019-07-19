package ca.gc.inspection.scoop.searchprofile.presenter;

import java.util.ArrayList;

public class ProfileDataCache {
    private Class mPostDataType;
    private BaseDataCache mDataCache;

    public static ProfileDataCache createWithType(Class postDataType) {
        if (!SearchProfile.class.isAssignableFrom(postDataType))
            return null;
        return new ProfileDataCache(postDataType);
    }

    private ProfileDataCache(Class postDataType) {
        mPostDataType = postDataType;
        mDataCache = null;
        if (mPostDataType == SearchProfile.class)
            mDataCache = new DataCache<>();
    }

    @SuppressWarnings("unchecked")
    public SearchProfile getSearchProfileByIndex(int i) {
        if (SearchProfile.class.isAssignableFrom(mPostDataType))
            return ((DataCache<SearchProfile>) mDataCache).getItemByIndex(i);
        return null;
    }

    @SuppressWarnings("unchecked")
    public int getItemCount() {
        return ((DataCache<SearchProfile>) mDataCache).getItemCount();
    }

    @SuppressWarnings("unchecked")
    public ArrayList<SearchProfile> getSearchProfileList() {
        if (SearchProfile.class.isAssignableFrom(mPostDataType))
            return ((DataCache<SearchProfile>) mDataCache).mList;
        return null;
    }

    private class BaseDataCache {

    }

    class DataCache<T extends SearchProfile> extends BaseDataCache {

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
