package ca.gc.inspection.scoop.feedpost;

import android.support.annotation.NonNull;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import ca.gc.inspection.scoop.postcomment.PostDataCache;
import ca.gc.inspection.scoop.util.NetworkUtils;
import ca.gc.inspection.scoop.profilepost.ProfilePostPresenter;

import static com.google.gson.internal.$Gson$Preconditions.checkNotNull;


public class FeedPostPresenter extends ProfilePostPresenter implements
        FeedPostContract.Presenter,
        FeedPostContract.Presenter.AdapterAPI,
        FeedPostContract.Presenter.ViewHolderAPI {
    /**
     * Presenter for viewing a feed post.
     * Inherits from ProfilePostPresenter to extend the method for binding data.
     * Implements the AdapterAPI and ViewHolderAPI to allow adapter and viewHolder to communicate with
     * the presenter.
     */

    private static final String TAG = "FeedPostPresenter";

    private FeedPostContract.View mFeedPostView;
    private FeedPostContract.View.Adapter mAdapter;
    private FeedPostInteractor mFeedPostInteractor;

    private FeedPost getItemByIndex(int i) {
        if (mDataCache == null)
            return null;
        return mDataCache.getFeedPostByIndex(i);
    }

    /**
     * Empty constructor called by child classes to allow them to create
     * their own View and Interactor objects
     */
    protected FeedPostPresenter() {
    }

    public FeedPostPresenter(@NonNull FeedPostContract.View viewInterface, NetworkUtils network){

        setView(viewInterface);
        setInteractor(new FeedPostInteractor(this, network));

    }

    public void setView(@NonNull FeedPostContract.View viewInterface) {
        mFeedPostView = checkNotNull(viewInterface);
    }

    /**
     * set parent interactor as a casted down version without the parent creating a new object
     * @param interactor
     */
    public void setInteractor(@NonNull FeedPostInteractor interactor) {
        super.setInteractor(interactor);
        mFeedPostInteractor = checkNotNull(interactor);
    }

    @Override
    public void setAdapter(FeedPostContract.View.Adapter adapter) {
        mAdapter = adapter;
    }

    /**
     * Provides information to the Interactor and invokes different methods based on given feed type
     * @param network Allows save post information to be added to singleton request queue
     * @param feedType Type of feed (Community/Official vs Saved)
     */
    @Override
    public void loadDataFromDatabase(String feedType) {
        if (feedType.equals("saved")){
            mFeedPostInteractor.getSavedPosts();
        } else {
            mFeedPostInteractor.getFeedPosts(feedType);
        }
    }

    @Override
    public void setData(JSONArray feedPostsResponse, JSONArray imagesResponse) {
        mDataCache = PostDataCache.createWithType(FeedPost.class);

        if ((feedPostsResponse.length() != imagesResponse.length()))
            Log.i(TAG, "length of feedPostsResponse != imagesResponse; some users may not have profile images");

        for (int i=0; i<feedPostsResponse.length(); i++) {
            JSONObject jsonFeedPost = null;
            JSONObject jsonImage = null;
            try {
                jsonFeedPost = feedPostsResponse.getJSONObject(i);
                jsonImage = imagesResponse.getJSONObject(i);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            FeedPost feedPost = new FeedPost(jsonFeedPost, jsonImage);
            mDataCache.getFeedPostList().add(feedPost);
        }

        try {
            mAdapter.refreshAdapter();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onBindViewHolderAtPosition(FeedPostContract.View.ViewHolder viewHolderInterface, int i) {
        FeedPost feedPost = getItemByIndex(i);
        bindFeedPostDataToViewHolder(viewHolderInterface, feedPost);
    }

    public static void bindFeedPostDataToViewHolder(
            FeedPostContract.View.ViewHolder viewHolderInterface, FeedPost feedPost) {
        bindProfilePostDataToViewHolder(viewHolderInterface, feedPost);
        if (feedPost != null) {
            viewHolderInterface.setPostImageFromString(feedPost.getFeedPostImagePath());
        }
    }

}
