package ca.gc.inspection.scoop.searchpeople.presenter;

import java.util.ArrayList;

public class ProfileDataCache {
    private Class mPostDataType;
    private BaseDataCache mDataCache;

    public static ProfileDataCache createWithType(Class postDataType) {
        if (!SearchPeople.class.isAssignableFrom(postDataType))
            return null;
        return new ProfileDataCache(postDataType);
    }

    private ProfileDataCache(Class postDataType) {
        mPostDataType = postDataType;
        mDataCache = null;
        if (mPostDataType == SearchPeople.class)
            mDataCache = new DataCache<>();
    }

    @SuppressWarnings("unchecked")
    public SearchPeople getSearchPeopleByIndex(int i) {
        if (SearchPeople.class.isAssignableFrom(mPostDataType))
            return ((DataCache<SearchPeople>) mDataCache).getItemByIndex(i);
        return null;
    }

    @SuppressWarnings("unchecked")
    public int getItemCount() {
        return ((DataCache<SearchPeople>) mDataCache).getItemCount();
    }

    @SuppressWarnings("unchecked")
    public ArrayList<SearchPeople> getSearchPeopleList() {
        if (SearchPeople.class.isAssignableFrom(mPostDataType))
            return ((DataCache<SearchPeople>) mDataCache).mList;
        return null;
    }

    private class BaseDataCache {

    }

    class DataCache<T extends SearchPeople> extends BaseDataCache {

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
