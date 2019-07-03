package ca.gc.inspection.scoop.postcomment;

import java.util.ArrayList;
import ca.gc.inspection.scoop.profilecomment.ProfileComment;
import ca.gc.inspection.scoop.profilepost.ProfilePost;
import ca.gc.inspection.scoop.feedpost.FeedPost;

public class PostDataCache {
    private Class mPostDataType;
    private BaseDataCache mDataCache;

    public static PostDataCache createWithType(Class postDataType) {
        if (!PostComment.class.isAssignableFrom(postDataType))
            return null;
        return new PostDataCache(postDataType);
    }

    private PostDataCache(Class postDataType) {
        mPostDataType = postDataType;
        mDataCache = null;
        if (mPostDataType == PostComment.class)
            mDataCache = new DataCache<PostComment>();
        else if (mPostDataType == ProfileComment.class)
            mDataCache = new DataCache<ProfileComment>();
        else if (mPostDataType == ProfilePost.class)
            mDataCache = new DataCache<ProfilePost>();
        else if (mPostDataType == FeedPost.class)
            mDataCache = new DataCache<FeedPost>();
    }

    public PostComment getPostCommentByIndex(int i) {
        if (PostComment.class.isAssignableFrom(mPostDataType))
            return ((DataCache<PostComment>) mDataCache).getItemByIndex(i);
        return null;
    }

    public ProfileComment getProfileCommentByIndex(int i) {
        if (ProfileComment.class.isAssignableFrom(mPostDataType))
            return ((DataCache<ProfileComment>) mDataCache).getItemByIndex(i);
        return null;
    }

    public ProfilePost getProfilePostByIndex(int i) {
        if (ProfilePost.class.isAssignableFrom(mPostDataType))
            return ((DataCache<ProfilePost>) mDataCache).getItemByIndex(i);
        return null;
    }

    public FeedPost getFeedPostByIndex(int i) {
        if (FeedPost.class.isAssignableFrom(mPostDataType))
            return ((DataCache<FeedPost>) mDataCache).getItemByIndex(i);
        return null;
    }

    public int getItemCount() {
        return ((DataCache<PostComment>) mDataCache).getItemCount();
    }

    public ArrayList<PostComment> getPostCommentList() {
        if (PostComment.class.isAssignableFrom(mPostDataType))
            return ((DataCache<PostComment>) mDataCache).mList;
        return null;
    }

    public ArrayList<ProfileComment> getProfileCommentList() {
        if (ProfileComment.class.isAssignableFrom(mPostDataType))
            return ((DataCache<ProfileComment>) mDataCache).mList;
        return null;
    }

    public ArrayList<ProfilePost> getProfilePostList() {
        if (ProfilePost.class.isAssignableFrom(mPostDataType))
            return ((DataCache<ProfilePost>) mDataCache).mList;
        return null;
    }

    public ArrayList<FeedPost> getFeedPostList() {
        if (FeedPost.class.isAssignableFrom(mPostDataType))
            return ((DataCache<FeedPost>) mDataCache).mList;
        return null;
    }

    private class BaseDataCache {

    }

    class DataCache<T extends PostComment> extends BaseDataCache {

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
