package ca.gc.inspection.scoop.postcomment;

import java.util.ArrayList;
import ca.gc.inspection.scoop.profilecomment.ProfileComment;
import ca.gc.inspection.scoop.profilelikes.ProfileLike;
import ca.gc.inspection.scoop.profilepost.ProfilePost;
import ca.gc.inspection.scoop.feedpost.FeedPost;
import ca.gc.inspection.scoop.searchpost.presenter.SearchPost;

/**
 * Presenter layer data encapsulation class for storing list of posts/comments.
 * Used by Presenters to:
 * - Store post/comment data from database (retrieved using Interactor)
 * - Get data for posts/comments to bind to ViewHolders in a RecyclerView.
 *
 * Why:
 * Despite the post/comment Presenters being in the same inheritance hierarchy, the DataCache type
 * must become more and more derived as we traverse the hierarchy. To avoid casting the post/comment
 * types from PostComment throughout the code, we encapsulate it in PostDataCache.
 *
 * Example: Even though FeedPost subclasses PostComment, the ArrayList<FeedPost> does not subclass
 * PostComment and cannot be cast to an ArrayList<PostComment>
 * This is why we need to dynamically determine the type of ArrayList<post/comment item> depending on
 * the depth of the Presenter in the inheritance hierarchy that we are using.
 * ie. FeedPostPresenter is used in the feedpost package and so we need a DataCache with type FeedPost.
 * This allows us to cast the items down to PostComment. You cannot cast it the other way around.
 *
 *
 * Why can't each Presenter in the hierarchy declare their own concrete DataCache<Post/Comment Type>
 * without using the PostDataCache as a wrapper?
 * Because updating items in the list in, for example, ProfileCommentPresenter will not propagate to the
 * DataCache<PostComment> instance in PostCommentPresenter. This means that any onBind methods which
 * occurs in the PostComment layer would not have the necessary data as it was only set in the ProfileComment
 * layer.
 *
 * (Note that although post/comment related Presenters inherit from each other, static onBind methods
 * are used to provide more granular control to the child classes of what data should be binded and
 * what should not be)
 *
 * PostDataCache allows updates to specific items in the DataCache to be accessible to the more base
 * Presenters in the hierarchy.
 *
 */
public class PostDataCache {
    // Class must directly or indirectly extend from the PostComment class
    private Class mPostDataType;
    // Allows template DataCache type to be dynamically determined upon instantiation
    private BaseDataCache mDataCache;

    // Prevents instantiation of PostDataCache with a post/comment type which is not part of the PostComment hierarchy
    public static PostDataCache createWithType(Class postDataType) {
        // Must be a direct or indirect subclass of PostComment
        if (!PostComment.class.isAssignableFrom(postDataType))
            return null;
        return new PostDataCache(postDataType);
    }

    // Private access means data cache can only be instantiated using the static method createWithType
    private PostDataCache(Class postDataType) {
        mPostDataType = postDataType;
        mDataCache = null;
        if (mPostDataType == PostComment.class)
            // Default type is PostComment
            mDataCache = new DataCache<>();
        else if (mPostDataType == ProfileComment.class)
            mDataCache = new DataCache<ProfileComment>();
        else if (mPostDataType == ProfilePost.class)
            mDataCache = new DataCache<ProfilePost>();
        else if (mPostDataType == FeedPost.class)
            mDataCache = new DataCache<FeedPost>();
        else if (mPostDataType == ProfileLike.class)
            mDataCache = new DataCache<ProfileLike>();
        else if (mPostDataType == SearchPost.class)
            mDataCache = new DataCache<SearchPost>();
    }

    /* Getter methods for the post/comment data objects. When attempting to retrieve a post/comment type,
    we check that the DataCache type is at least as derived as the retrieved type (in other words,
    the retrieved type must be subclassed by the DataCache type).
    As an example, if the DataCache was instantiated with the type ProfileComment, attempting to
    retrieve a more derived FeedPost will return null */

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
    public ProfileLike getProfileLikesByIndex(int i) {
        if (ProfileLike.class.isAssignableFrom(mPostDataType))
            return ((DataCache<ProfileLike>) mDataCache).getItemByIndex(i);
        return null;
    }

    @SuppressWarnings("unchecked")
    public FeedPost getFeedPostByIndex(int i) {
        if (FeedPost.class.isAssignableFrom(mPostDataType))
            return ((DataCache<FeedPost>) mDataCache).getItemByIndex(i);
        return null;
    }

    @SuppressWarnings("unchecked")
    public SearchPost getSearchPostByIndex(int i) {
        if (SearchPost.class.isAssignableFrom(mPostDataType))
            return ((DataCache<SearchPost>) mDataCache).getItemByIndex(i);
        return null;
    }

    /**
     * Type agnostic method used by Presenter to get number of items in PostDataCache
     * @return
     */
    @SuppressWarnings("unchecked")
    public int getItemCount() {
        return ((DataCache<PostComment>) mDataCache).getItemCount();
    }

    /* Used by the Presenter to get the mutable list
     * - trade-off of reducing number of methods over encapsulating implementation details */

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
    public ArrayList<ProfileLike> getProfileLikesList() {
        if (ProfileLike.class.isAssignableFrom(mPostDataType))
            return ((DataCache<ProfileLike>) mDataCache).mList;
        return null;
    }

    @SuppressWarnings("unchecked")
    public ArrayList<FeedPost> getFeedPostList() {
        if (FeedPost.class.isAssignableFrom(mPostDataType))
            return ((DataCache<FeedPost>) mDataCache).mList;
        return null;
    }

    @SuppressWarnings("unchecked")
    public ArrayList<SearchPost> getSearchPostList() {
        if (SearchPost.class.isAssignableFrom(mPostDataType))
            return ((DataCache<SearchPost>) mDataCache).mList;
        return null;
    }

    /**
     * Allows DataCache type to be defined dynamically upon instantiation
     */
    private class BaseDataCache {

    }

    /**
     * Template DataCache which can store and retrieve items in an ArrayList
     * Cannot be private otherwise the list cannot be modified by the Presenter
     * @param <T>   Class which is part of the PostComment inheritance hierarchy
     */
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
