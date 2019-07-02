package ca.gc.inspection.scoop.feedpost;

import android.support.annotation.NonNull;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import ca.gc.inspection.scoop.postcomment.PostCommentContract;
import ca.gc.inspection.scoop.util.NetworkUtils;
import ca.gc.inspection.scoop.profilecomment.ProfileComment;
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

    // TODO extend JSONArray mComments, mImages, and ArrayList mPostComments from parent
    // - currently assume mComments stores only community feed fragment data
    private ArrayList<FeedPost> mFeedPosts;

    // TODO replace overriding method by creating a DataCache object in PostCommentPresenter and overriding it here
    @Override
    protected ProfileComment getProfileCommentByIndex(int i) {
        return getFeedPostByIndex(i);
    }

    private FeedPost getFeedPostByIndex(int i) {
        if (mFeedPosts == null)
            return null;
        return mFeedPosts.get(i);
    }

    /**
     * Empty constructor called by child classes to allow them to create
     * their own View and Interactor objects
     */
    protected FeedPostPresenter() {
    }

    public FeedPostPresenter(@NonNull FeedPostContract.View viewInterface){

        setView(viewInterface);
        setInteractor(new FeedPostInteractor(this));

    }

    public void setView(@NonNull FeedPostContract.View viewInterface) {
        super.setView(viewInterface);
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

    @Override
    public void loadDataFromDatabase(NetworkUtils network, String feedType) {
        mFeedPostInteractor.getFeedPosts(network, feedType);
    }

    @Override
    public void setData(JSONArray feedPostsResponse, JSONArray imagesResponse) {
        mComments = feedPostsResponse;
        mImages = imagesResponse;
        mFeedPosts = new ArrayList<>();

        if ((feedPostsResponse.length() != imagesResponse.length()))
            Log.i(TAG, "length of feedPostsResponse != imagesResponse; some users may not have profile images");

        for (int i=0; i<feedPostsResponse.length(); i++) {
            JSONObject jsonFeedPost = null;
            JSONObject jsonImage = null;
            try {
                jsonFeedPost = mComments.getJSONObject(i);
                jsonImage = mImages.getJSONObject(i);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            FeedPost feedPost = new FeedPost(jsonFeedPost, jsonImage);
            mFeedPosts.add(feedPost);
        }

        try {
            mAdapter.refreshAdapter();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onBindViewHolderAtPosition(PostCommentContract.View.ViewHolder viewHolderInterface, int i) {
        super.onBindViewHolderAtPosition(viewHolderInterface, i);
        FeedPost feedPost = getFeedPostByIndex(i);
        if (feedPost != null) {
            ((FeedPostContract.View.ViewHolder) viewHolderInterface).setPostImageFromString(feedPost.getFeedPostImagePath());
        }
    }

    /**
     * Gets the item Count of the feedposts JSONArray
     * @return the length
     */
    // TODO refactor when DataCache object is implemented
    @Override
    public int getItemCount() {
        return super.getItemCount();
    }

    // TODO refactor when DataCache object is implemented
    @Override
    public String getPosterIdByIndex(int i) {
        return getFeedPostByIndex(i).getPosterId();
    }

}
