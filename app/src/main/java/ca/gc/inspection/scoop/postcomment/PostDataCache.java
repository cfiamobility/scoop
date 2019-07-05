package ca.gc.inspection.scoop.postcomment;

import java.util.ArrayList;
import ca.gc.inspection.scoop.profilecomment.ProfileComment;
import ca.gc.inspection.scoop.profilelikes.ProfileLikes;
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
            mDataCache = new DataCache<>();
        else if (mPostDataType == ProfileComment.class)
            mDataCache = new DataCache<ProfileComment>();
        else if (mPostDataType == ProfilePost.class)
            mDataCache = new DataCache<ProfilePost>();
        else if (mPostDataType == FeedPost.class)
            mDataCache = new DataCache<FeedPost>();
        else if (mPostDataType == ProfileLikes.class)
            mDataCache = new DataCache<ProfileLikes>();
    }

    @SuppressWarnings("unchecked")
    public PostComment getPostCommentByIndex(int i) {
        if (PostComment.class.isAssignableFrom(mPostDataType))
            return ((DataCache<PostComment>) mDataCache).getItemByIndex(i);
        return null;
    }

    @SuppressWarnings("unchecked")
    public ProfileComment getProfileCommentByIndex(int i) {
        if (ProfileComment.class.isAssignableFrom(mPostDataType))
            return ((DataCache<ProfileComment>) mDataCache).getItemByIndex(i);
        return null;
    }

    @SuppressWarnings("unchecked")
    public ProfilePost getProfilePostByIndex(int i) {
        if (ProfilePost.class.isAssignableFrom(mPostDataType))
            return ((DataCache<ProfilePost>) mDataCache).getItemByIndex(i);
        return null;
    }

    @SuppressWarnings("unchecked")
    public ProfileLikes getProfileLikesByIndex(int i){
        if (ProfileLikes.class.isAssignableFrom(mPostDataType))
            return ((DataCache<ProfileLikes>) mDataCache).getItemByIndex(i);
        return null;
    }

    @SuppressWarnings("unchecked")
    public FeedPost getFeedPostByIndex(int i) {
        if (FeedPost.class.isAssignableFrom(mPostDataType))
            return ((DataCache<FeedPost>) mDataCache).getItemByIndex(i);
        return null;
    }

    @SuppressWarnings("unchecked")
    public int getItemCount() {
        return ((DataCache<PostComment>) mDataCache).getItemCount();
    }

    @SuppressWarnings("unchecked")
    public ArrayList<PostComment> getPostCommentList() {
        if (PostComment.class.isAssignableFrom(mPostDataType))
            return ((DataCache<PostComment>) mDataCache).mList;
        return null;
    }

    @SuppressWarnings("unchecked")
    public ArrayList<ProfileComment> getProfileCommentList() {
        if (ProfileComment.class.isAssignableFrom(mPostDataType))
            return ((DataCache<ProfileComment>) mDataCache).mList;
        return null;
    }

    @SuppressWarnings("unchecked")
    public ArrayList<ProfilePost> getProfilePostList() {
        if (ProfilePost.class.isAssignableFrom(mPostDataType))
            return ((DataCache<ProfilePost>) mDataCache).mList;
        return null;
    }

    @SuppressWarnings("unchecked")
    public ArrayList<ProfileLikes> getProfileLikesList() {
        if (ProfileLikes.class.isAssignableFrom(mPostDataType))
            return ((DataCache<ProfileLikes>) mDataCache).mList;
        return null;
    }

    @SuppressWarnings("unchecked")
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
