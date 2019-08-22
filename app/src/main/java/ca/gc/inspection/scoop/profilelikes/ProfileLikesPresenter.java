package ca.gc.inspection.scoop.profilelikes;
import android.support.annotation.NonNull;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import ca.gc.inspection.scoop.editpost.EditPostData;
import ca.gc.inspection.scoop.postcomment.PostDataCache;
import ca.gc.inspection.scoop.profilecomment.ProfileCommentPresenter;
import ca.gc.inspection.scoop.util.NetworkUtils;

import static com.google.gson.internal.$Gson$Preconditions.checkNotNull;


/**
 * Presenter for viewing a profile post.
 * Inherits from ProfileCommentPresenter to extend the method for binding data.
 * Implements the AdapterAPI and ViewHolderAPI to allow adapter and viewHolder to communicate with
 * the presenter.
 */
public class ProfileLikesPresenter extends ProfileCommentPresenter implements
        ProfileLikesContract.Presenter,
        ProfileLikesContract.Presenter.AdapterAPI,
        ProfileLikesContract.Presenter.ViewHolderAPI {

    private static final String TAG = "ProfileLikesPresenter";

    @NonNull
    private ProfileLikesContract.View mProfileLikesView;
    private ProfileLikesContract.View.Adapter mAdapter;
    private ProfileLikesInteractor mProfileLikesInteractor;
    private boolean refreshingData = false;

    private ProfileLike getItemByIndex(int i) {
        if (mDataCache == null)
            return null;
        return mDataCache.getProfileLikesByIndex(i);
    }


    /**
     * Empty constructor called by child classes (ie. FeedPostPresenter) to allow them to create
     * their own View and Interactor objects
     */
    protected ProfileLikesPresenter() {
    }

    ProfileLikesPresenter(@NonNull ProfileLikesContract.View viewInterface, NetworkUtils network){

        setView(viewInterface);
        setInteractor(new ProfileLikesInteractor(this, network));

    }

    public void setView(@NonNull ProfileLikesContract.View viewInterface) {
        mProfileLikesView = checkNotNull(viewInterface);
    }

    /**
     * set parent interactor as a casted down version without the parent creating a new object
     * @param interactor
     */
    public void setInteractor(@NonNull ProfileLikesInteractor interactor) {
        super.setInteractor(interactor);
        mProfileLikesInteractor = checkNotNull(interactor);
    }

    @Override
    public void setAdapter(ProfileLikesContract.View.Adapter adapter) {
        mAdapter = adapter;
    }

    @Override
    public void loadDataFromDatabase(String userId) {
        if (!refreshingData) {
            refreshingData = true;

            if (mDataCache == null)
                mDataCache = PostDataCache.createWithType(ProfileLike.class);
            else mDataCache.getProfileLikesList().clear();

            mProfileLikesInteractor.getProfileLikes(userId);
        }
    }

    /**
     * Update the DataCache with the ProfileLikes data to be displayed in the RecyclerView.
     * @param postsResponse
     * @param imagesResponse
     */
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
            ProfileLike profileLike = new ProfileLike(jsonPost, jsonImage);
            mDataCache.getProfileLikesList().add(profileLike);
        }

        mAdapter.refreshAdapter();
        refreshingData = false;
        mProfileLikesView.onLoadedDataFromDatabase();
    }

    @Override
    public void onBindViewHolderAtPosition(ProfileLikesContract.View.ViewHolder viewHolderInterface, int i) {
        ProfileLike profileLike = getItemByIndex(i);
        bindProfileLikesDataToViewHolder(viewHolderInterface, profileLike);
    }

    public static void bindProfileLikesDataToViewHolder(
            ProfileLikesContract.View.ViewHolder viewHolderInterface, ProfileLike profileLike) {
        // call bindPostCommentDataToViewHolder instead of bindProfileCommentDataToViewHolder as we are setting a different title
        bindPostCommentDataToViewHolder(viewHolderInterface, profileLike);
        if (profileLike != null) {
            viewHolderInterface
                    .setPostTitle(profileLike.getPostTitle())
                    .setCommentCount(profileLike.getCommentCount())
                    .setSavedState(profileLike.getSavedState());

        }
    }

    /**
     * EditPostData used to store current state of post to start EditPostActivity.
     * The relevant data is retrieved from the DataCache using the adapter position i.
     *
     * @param i     adapter position
     * @return EditPostData is a data class which stores the current edits for a post
     */
    @Override
    public EditPostData getEditPostData(int i) {
        ProfileLike profileLike = getItemByIndex(i);
        return new EditPostData(profileLike.getActivityId(),
                profileLike.getPostTitle(),
                profileLike.getPostText(),
                null);
    }
}