package ca.gc.inspection.scoop.profilepost;

import android.support.annotation.NonNull;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import ca.gc.inspection.scoop.MySingleton;
import ca.gc.inspection.scoop.post;
import ca.gc.inspection.scoop.profilecomment.ProfileComment;
import ca.gc.inspection.scoop.profilecomment.ProfileCommentContract;
import ca.gc.inspection.scoop.profilecomment.ProfileCommentPresenter;

import static com.google.gson.internal.$Gson$Preconditions.checkNotNull;


/**
 * performs all logic and HTTP requests for the FeedAdapter
 */
public class ProfilePostPresenter extends ProfileCommentPresenter implements
        ProfilePostContract.Presenter,
        ProfilePostContract.Presenter.AdapterAPI,
        ProfilePostContract.Presenter.ViewHolderAPI {

    private static final String TAG = "ProfilePostPresenter";

    @NonNull
    private ProfilePostContract.View mProfilePostView;
    private ProfilePostContract.View.Adapter mAdapter;
    private ProfilePostInteractor mProfilePostInteractor;
    // TODO extend JSONArray mComments, mImages, and ArrayList mProfileComments from parent
    // - currently using mComments with extra commentsCount field
    private ArrayList<ProfilePost> mProfilePosts;

    // TODO replace overriding method by creating a DataCache object in ProfileCommentPresenter and overriding it here
    @Override
    protected ProfileComment getProfileCommentByIndex(int i) {
        return getProfilePostByIndex(i);
    }

    private ProfilePost getProfilePostByIndex(int i) {
        if (mProfilePosts == null)
            return null;
        return mProfilePosts.get(i);
    }

    /**
     * Empty constructor called by child classes (ie. FeedPostPresenter) to allow them to create
     * their own View and Interactor objects
     */
    protected ProfilePostPresenter() {
    }

    ProfilePostPresenter(@NonNull ProfilePostContract.View viewInterface){

        setView(viewInterface);
        setInteractor(new ProfilePostInteractor(this));

    }

    public void setView(@NonNull ProfilePostContract.View viewInterface) {
        super.setView(viewInterface);
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
    public void loadDataFromDatabase(MySingleton singleton, String currentUser) {
        mProfilePostInteractor.getUserPosts(singleton, currentUser);
    }

    @Override
    public void setData(JSONArray postsResponse, JSONArray imagesResponse) {
        mComments = postsResponse;
        mImages = imagesResponse;
        mProfilePosts = new ArrayList<>();

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
            ProfilePost profilePost = new ProfilePost(jsonPost, jsonImage);
            mProfilePosts.add(profilePost);
        }

        mAdapter.refreshAdapter();
    }

    @Override
    public void onBindViewHolderAtPosition(ProfileCommentContract.View.ViewHolder viewHolderInterface, int i) {
        super.onBindViewHolderAtPosition(viewHolderInterface, i);
        // need to manually call overriden setPostTitle method due to super method casting viewHolderInterface down
        ProfilePost profilePost = getProfilePostByIndex(i);
        if (profilePost != null) {
            ((ProfilePostContract.View.ViewHolder) viewHolderInterface)
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
