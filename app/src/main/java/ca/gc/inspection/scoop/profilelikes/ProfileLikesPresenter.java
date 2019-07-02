package ca.gc.inspection.scoop.profilelikes;

import android.support.annotation.NonNull;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import ca.gc.inspection.scoop.profilelikes.ProfileLike;
import ca.gc.inspection.scoop.profilelikes.ProfileLikesContract;
import ca.gc.inspection.scoop.profilelikes.ProfileLikesInteractor;
import ca.gc.inspection.scoop.util.NetworkUtils;
import ca.gc.inspection.scoop.profilecomment.ProfileComment;
import ca.gc.inspection.scoop.profilecomment.ProfileCommentContract;
import ca.gc.inspection.scoop.profilecomment.ProfileCommentPresenter;

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
    // TODO extend JSONArray mComments, mImages, and ArrayList mProfileComments from parent
    // - currently using mComments with extra commentsCount field
    private ArrayList<ProfileLike> mProfileLikess;

    // TODO replace overriding method by creating a DataCache object in ProfileCommentPresenter and overriding it here
    @Override
    protected ProfileComment getProfileCommentByIndex(int i) {
        return getProfileLikesByIndex(i);
    }

    private ProfileLike getProfileLikesByIndex(int i) {

        if (mProfileLikess == null)
            return null;
        return mProfileLikess.get(i);
    }

    /**
     * Empty constructor called by child classes (ie. FeedPostPresenter) to allow them to create
     * their own View and Interactor objects
     */
    protected ProfileLikesPresenter() {
    }

    ProfileLikesPresenter(@NonNull ProfileLikesContract.View viewInterface){

        setView(viewInterface);
        setInteractor(new ProfileLikesInteractor(this));

    }

    public void setView(@NonNull ProfileLikesContract.View viewInterface) {
        super.setView(viewInterface);
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
    public void loadDataFromDatabase(NetworkUtils network, String currentUser) {
        mProfileLikesInteractor.getUserPosts(network, currentUser);
    }

    @Override
    public void setData(JSONArray postsResponse, JSONArray imagesResponse) {
        mComments = postsResponse;
        mImages = imagesResponse;
        mProfileLikess = new ArrayList<>();

        if ((postsResponse.length() != imagesResponse.length()))
            Log.i(TAG, "length of postsResponse != imagesResponse");

        for (int i=0; i<postsResponse.length(); i++) {
            JSONObject jsonPost = null;
            JSONObject jsonImage = null;
            try {
                jsonPost = mComments.getJSONObject(i);
                jsonImage = mImages.getJSONObject(i);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            ProfileLike profilePost = new ProfileLike(jsonPost, jsonImage);
            mProfileLikess.add(profilePost);
        }

        mAdapter.refreshAdapter();
    }

    @Override
    public void onBindViewHolderAtPosition(ProfileCommentContract.View.ViewHolder viewHolderInterface, int i) {
        super.onBindViewHolderAtPosition(viewHolderInterface, i);
        // need to manually call overriden setPostTitle method due to super method casting viewHolderInterface down
        ProfileLike profilePost = getProfileLikesByIndex(i);
        if (profilePost != null) {
            ((ProfileLikesContract.View.ViewHolder) viewHolderInterface)
                    .setPostTitle(profilePost.getPostTitle())
                    .setCommentCount(profilePost.getCommentCount());
        }
    }

    /**
     * Gets the item Count of the posts JSONArray
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
        return getProfileCommentByIndex(i).getPosterId();
    }
}
