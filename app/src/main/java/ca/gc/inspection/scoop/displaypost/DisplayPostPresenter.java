package ca.gc.inspection.scoop.displaypost;

import android.content.Context;
import android.support.annotation.NonNull;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Objects;

import ca.gc.inspection.scoop.feedpost.FeedPost;
import ca.gc.inspection.scoop.feedpost.FeedPostContract;
import ca.gc.inspection.scoop.feedpost.FeedPostPresenter;
import ca.gc.inspection.scoop.postcomment.PostComment;
import ca.gc.inspection.scoop.postcomment.PostCommentContract;
import ca.gc.inspection.scoop.profilepost.ProfilePost;
import ca.gc.inspection.scoop.util.NetworkUtils;

import static com.google.gson.internal.$Gson$Preconditions.checkNotNull;

class DisplayPostPresenter extends FeedPostPresenter implements
        DisplayPostContract.Presenter,
        DisplayPostContract.Presenter.FragmentAPI,
        DisplayPostContract.Presenter.FragmentAPI.AdapterAPI {

    private DisplayPostContract.View mActivityView;
    private DisplayPostContract.View.Fragment mFragmentView;
    private DisplayPostContract.View.Fragment.Adapter mAdapter;
    private DisplayPostInteractor mInteractor;
    private FeedPost mFeedPost;

    private PostComment getItemByIndex(int i) {
        if (i == 0) {
            if (mFeedPost == null)
                return null;
            return mFeedPost;
        }
        else {
            if (mDataCache == null)
                return null;
            return mDataCache.getPostCommentByIndex(i - 1);
        }
    }

    DisplayPostPresenter(@NonNull DisplayPostContract.View activityViewInterface, NetworkUtils network) {
        mInteractor = new DisplayPostInteractor(this, network);
        mActivityView = checkNotNull(activityViewInterface);
    }

    public void setFragmentView(@NonNull DisplayPostContract.View.Fragment fragmentView) {
        mFragmentView = checkNotNull(fragmentView);
    }

    @Override
    public void setAdapter(DisplayPostContract.View.Fragment.Adapter adapter) {
        mAdapter = adapter;
    }

    @Override
    public void loadDataFromDatabase(String activityId, String posterId) {
        mInteractor.getDetailedPost(activityId, posterId);
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
        mFeedPost = new FeedPost(jsonPost, jsonImage);

        mAdapter.refreshAdapter();
    }

    public void setData(JSONArray commentsResponse, JSONArray imagesResponse) {

    }

    public void onBindViewHolder(FeedPostContract.View.ViewHolder viewHolderInterface) {
        bindFeedPostDataToViewHolder(viewHolderInterface, mFeedPost);
    }

    public void onBindViewHolderAtPosition(PostCommentContract.View.ViewHolder viewHolderInterface, int i) {

    }

    @Override
    public void addPostComment(String currentUserId, String commentText, String activityId) {
        mInteractor.addPostComment(currentUserId, commentText, activityId);
    }

    public void updateDisplay() {
        mAdapter.refreshAdapter();
    }

    /**
     * Gets the number of items
     * @return the count
     */
    @Override
    public int getItemCount() {
        int itemCount = 0;
        if (mFeedPost != null)
            itemCount = 1;
//        if (mDataCache != null)
//            itemCount += mDataCache.getItemCount();
        return itemCount;
    }

    @Override
    public String getPosterIdByIndex(int i) {
        return Objects.requireNonNull(getItemByIndex(i)).getPosterId();
    }

    @Override
    public String getActivityIdByIndex(int i) {
        return getItemByIndex(i).getActivityId();
    }
}
