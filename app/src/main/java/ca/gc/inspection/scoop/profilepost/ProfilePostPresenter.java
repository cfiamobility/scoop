package ca.gc.inspection.scoop.profilepost;

import android.support.annotation.NonNull;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Objects;

import ca.gc.inspection.scoop.postcomment.PostDataCache;
import ca.gc.inspection.scoop.profilelikes.ProfileLikesPresenter;
import ca.gc.inspection.scoop.util.NetworkUtils;
import ca.gc.inspection.scoop.profilecomment.ProfileCommentPresenter;

import static com.google.gson.internal.$Gson$Preconditions.checkNotNull;


/**
 * Presenter for viewing a profile post.
 * Inherits from ProfileCommentPresenter to extend the method for binding data.
 * Implements the AdapterAPI and ViewHolderAPI to allow adapter and viewHolder to communicate with
 * the presenter.
 */
public class ProfilePostPresenter extends ProfileLikesPresenter implements
        ProfilePostContract.Presenter,
        ProfilePostContract.Presenter.AdapterAPI,
        ProfilePostContract.Presenter.ViewHolderAPI {

    private static final String TAG = "ProfilePostPresenter";

    @NonNull
    private ProfilePostContract.View mProfilePostView;
    private ProfilePostContract.View.Adapter mAdapter;
    private ProfilePostInteractor mProfilePostInteractor;
    private boolean refreshingData = false;

    private ProfilePost getItemByIndex(int i) {
        if (mDataCache == null)
            return null;
        return mDataCache.getProfilePostByIndex(i);
    }


    /**
     * Empty constructor called by child classes (ie. FeedPostPresenter) to allow them to create
     * their own View and Interactor objects
     */
    protected ProfilePostPresenter() {
    }

    ProfilePostPresenter(@NonNull ProfilePostContract.View viewInterface, NetworkUtils network){

        setView(viewInterface);
        setInteractor(new ProfilePostInteractor(this, network));

    }

    public void setView(@NonNull ProfilePostContract.View viewInterface) {
        mProfilePostView = checkNotNull(viewInterface);
    }

    /**
     * set parent interactor as a casted down version without the parent creating a new object
     * @param interactor
     */
    public void setInteractor(@NonNull ProfilePostInteractor interactor) {
        super.setInteractor(interactor);
        mProfilePostInteractor = checkNotNull(interactor);
    }

    @Override
    public void setAdapter(ProfilePostContract.View.Adapter adapter) {
        mAdapter = adapter;
    }

    @Override
    public void loadDataFromDatabase(String userId) {
        if (!refreshingData) {
            refreshingData = true;

            if (mDataCache == null)
                mDataCache = PostDataCache.createWithType(ProfilePost.class);
            else mDataCache.getProfilePostList().clear();

            mProfilePostInteractor.getProfilePosts(userId);
        }
    }

    @Override
    public void setData(JSONArray postsResponse, JSONArray imagesResponse) {

        if ((postsResponse.length() != imagesResponse.length()))
            Log.i(TAG, "length of postsResponse != imagesResponse");

        for (int i=0; i<postsResponse.length(); i++) {
            JSONObject jsonPost = null;
            JSONObject jsonImage = null;
            try {
                jsonPost = postsResponse.getJSONObject(i);
                jsonImage = imagesResponse.getJSONObject(i);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            ProfilePost profilePost = new ProfilePost(jsonPost, jsonImage);
            mDataCache.getProfilePostList().add(profilePost);
        }

        mAdapter.refreshAdapter();
        refreshingData = false;
        mProfilePostView.onLoadedDataFromDatabase();
    }

    @Override
    public void onBindViewHolderAtPosition(ProfilePostContract.View.ViewHolder viewHolderInterface, int i) {
        ProfilePost profilePost = getItemByIndex(i);
        bindProfilePostDataToViewHolder(viewHolderInterface, profilePost);
    }

    public static void bindProfilePostDataToViewHolder(
            ProfilePostContract.View.ViewHolder viewHolderInterface, ProfilePost profilePost) {
        // call bindPostCommentDataToViewHolder instead of bindProfileCommentDataToViewHolder as we are setting a different title
        bindPostCommentDataToViewHolder(viewHolderInterface, profilePost);
        if (profilePost != null) {
            viewHolderInterface
                    .setPostTitle(profilePost.getPostTitle())
                    .setCommentCount(profilePost.getCommentCount());
        }
    }


    @Override
    public void editPost(int i) {
        ProfilePost profilePost = getItemByIndex(i);
        mProfilePostView.editPost(profilePost.getActivityId(),
                profilePost.getPostTitle(),
                profilePost.getPostText(),
                null);
    }
}
