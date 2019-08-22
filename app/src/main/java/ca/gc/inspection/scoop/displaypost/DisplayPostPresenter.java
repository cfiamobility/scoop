package ca.gc.inspection.scoop.displaypost;

import android.support.annotation.NonNull;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import ca.gc.inspection.scoop.editpost.EditPostData;
import ca.gc.inspection.scoop.feedpost.FeedPost;
import ca.gc.inspection.scoop.feedpost.FeedPostContract;
import ca.gc.inspection.scoop.feedpost.FeedPostPresenter;
import ca.gc.inspection.scoop.postcomment.PostComment;
import ca.gc.inspection.scoop.postcomment.PostCommentContract;
import ca.gc.inspection.scoop.postcomment.PostDataCache;
import ca.gc.inspection.scoop.util.NetworkUtils;

import static com.google.gson.internal.$Gson$Preconditions.checkNotNull;

class DisplayPostPresenter extends FeedPostPresenter implements
        DisplayPostContract.Presenter,
        DisplayPostContract.Presenter.FragmentAPI,
        DisplayPostContract.Presenter.FragmentAPI.AdapterAPI {

    private static final String TAG = "DisplayPostPresenter";

    private DisplayPostContract.View mActivityView;
    private DisplayPostContract.View.Fragment mFragmentView;
    private DisplayPostContract.View.Fragment.Adapter mAdapter;
    private DisplayPostInteractor mDisplayPostInteractor;
    private boolean wasDataSet = false;
    private boolean refreshingData = false;

    private PostComment getItemByIndex(int i) {
        if (mDataCache == null)
            return null;
        if (i == 0) {
            return mDataCache.getFeedPostByIndex(0);
        }
        else {
            return mDataCache.getPostCommentByIndex(i);
        }
    }

    DisplayPostPresenter(@NonNull DisplayPostContract.View activityViewInterface, NetworkUtils network) {
        setView(activityViewInterface);
        setInteractor(new DisplayPostInteractor(this, network));
        mDataCache = PostDataCache.createWithType(FeedPost.class);
    }

    public void setView(@NonNull DisplayPostContract.View viewInterface) {
        mActivityView = checkNotNull(viewInterface);
    }

    public void setFragmentView(@NonNull DisplayPostContract.View.Fragment fragmentView) {
        mFragmentView = checkNotNull(fragmentView);
    }

    /**
     * set parent interactor as a casted down version without the parent creating a new object
     * @param interactor
     */
    public void setInteractor(@NonNull DisplayPostInteractor interactor) {
        super.setInteractor(interactor);
        mDisplayPostInteractor = checkNotNull(interactor);
    }

    @Override
    public void setAdapter(DisplayPostContract.View.Fragment.Adapter adapter) {
        mAdapter = adapter;
    }

    @Override
    public void loadDataFromDatabase(String activityId) {
        if (!refreshingData) {
            refreshingData = true;
            mDataCache.getFeedPostList().clear();
            wasDataSet = false;
            Log.d(TAG, "data cache length = " + getItemCount());
            mDisplayPostInteractor.getDetailedPost(activityId);
            mDisplayPostInteractor.getPostComments(activityId);
        }
    }

    /**
     * Update the DataCache with the Post data (implemented as a FeedPost) of the first item being
     * displayed in the RecyclerView.
     * @param postTextResponse
     * @param postImageResponse
     */
    public void setDetailedPostData(JSONArray postTextResponse, JSONArray postImageResponse) {
        JSONObject jsonPost = null;
        JSONObject jsonImage = null;
        try {
            jsonPost = postTextResponse.getJSONObject(0);
            jsonImage = postImageResponse.getJSONObject(0);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        FeedPost feedPost = new FeedPost(jsonPost, jsonImage);
        mDataCache.getFeedPostList().add(0, feedPost);

        /* Notify the adapter when both the post and comment data has been loaded - avoids having the
        first PostComment data being casted to a FeedPost during onBind */
        if (wasDataSet) {
            refreshingData = false;
            mAdapter.refreshAdapter();
            mFragmentView.onLoadedDataFromDatabase();
        }
        wasDataSet = true;
    }

    /**
     * Update the DataCache with the PostComments data to be displayed in the RecyclerView.
     * @param commentsResponse
     * @param imagesResponse
     */
    public void setData(JSONArray commentsResponse, JSONArray imagesResponse) {

        if ((commentsResponse.length() != imagesResponse.length()))
            Log.i(TAG, "length of commentsResponse != imagesResponse");

        for (int i=0; i<commentsResponse.length(); i++) {
            JSONObject jsonComment = null;
            JSONObject jsonImage = null;
            try {
                jsonComment = commentsResponse.getJSONObject(i);
                jsonImage = imagesResponse.getJSONObject(i);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            PostComment postComment = new PostComment(jsonComment, jsonImage);
            mDataCache.getPostCommentList().add(postComment);
        }

        /* Notify the adapter when both the post and comment data has been loaded - avoids having the
        first PostComment data being casted to a FeedPost during onBind */
        if (wasDataSet) {
            refreshingData = false;
            mAdapter.refreshAdapter();
            mFragmentView.onLoadedDataFromDatabase();
        }
        wasDataSet = true;
    }

    public void onBindViewHolder(FeedPostContract.View.ViewHolder viewHolderInterface) {
        FeedPost feedPost = (FeedPost) getItemByIndex(0);
        bindFeedPostDataToViewHolder(viewHolderInterface, feedPost);
    }

    public void onBindViewHolderAtPosition(PostCommentContract.View.ViewHolder viewHolderInterface, int i) {
        PostComment postComment = getItemByIndex(i);
        bindPostCommentDataToViewHolder(viewHolderInterface, postComment);
        bindEditCommentDataToViewHolder(viewHolderInterface, postComment, i, mEditCommentCache);
        bindViewHolderStateToViewHolder(viewHolderInterface, postComment, i, mViewHolderStateCache);
    }

    @Override
    public void addPostComment(String currentUserId, String commentText, String activityId, String posterId) {
        mDisplayPostInteractor.addPostComment(currentUserId, commentText, activityId, posterId);
    }

    /**
     * Callback for database response for adding a post comment.
     * Deals with reloading data from the database.
     *
     * @param success       True if a comment was successfully added to the post
     * @param activityId    activityId of the post to reload
     */
    public void onAddPostComment(boolean success, String activityId) {
        if (success) {
            onItemAdded();
            mAdapter.refreshAdapter();
            loadDataFromDatabase(activityId);
        }
        mActivityView.onAddPostComment(success);
    }

    /**
     * EditPostData used to store current state of post to start EditPostActivity.
     * The relevant data is retrieved from the DataCache using the adapter position i.
     * Display post only contains a single post and the rest are comments - this method is only
     * needed for the post item.
     *
     * @param i     adapter position
     * @return EditPostData is a data class which stores the current edits for a post
     */
    @Override
    public EditPostData getEditPostData(int i) {
        if (i == 0) {
            FeedPost feedPost = (FeedPost) getItemByIndex(0);
            return new EditPostData(feedPost.getActivityId(),
                    feedPost.getPostTitle(),
                    feedPost.getPostText(),
                    feedPost.getFeedPostImagePath());
        }
        else {
            PostComment postComment = getItemByIndex(i);
            return new EditPostData(postComment.getActivityId(),
                    null,
                    postComment.getPostText(),
                    null);
        }
    }

    @Override
    public boolean unsavedEditsExist() {
        Log.d(TAG + ".unsavedEditsExist", "mEditCommentCache:" + mEditCommentCache.toString());
        return (mEditCommentCache != null && mEditCommentCache.size() != 0);
    }
}
