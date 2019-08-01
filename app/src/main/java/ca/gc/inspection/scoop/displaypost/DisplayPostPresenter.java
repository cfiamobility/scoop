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

        if (wasDataSet) {
            refreshingData = false;
            mAdapter.refreshAdapter();
            mFragmentView.onLoadedDataFromDatabase();
        }
        wasDataSet = true;
    }

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
    public void addPostComment(String currentUserId, String commentText, String activityId) {
        mDisplayPostInteractor.addPostComment(currentUserId, commentText, activityId);
    }

    public void onAddPostComment(boolean success, String activityId) {
        if (success) {
            onItemAdded();
            mAdapter.refreshAdapter();
            loadDataFromDatabase(activityId);
        }
        mActivityView.onAddPostComment(success);
    }

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
}
