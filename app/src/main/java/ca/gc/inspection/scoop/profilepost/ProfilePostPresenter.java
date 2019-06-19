package ca.gc.inspection.scoop.profilepost;

import android.support.annotation.NonNull;

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
        ProfilePostContract.Presenter.AdapterAPI {

    @NonNull
    private ProfilePostContract.View mProfilePostView;
    private ProfilePostInteractor mProfilePostInteractor;
    // TODO extend JSONArray mComments, mImages, and ArrayList mProfileComments from parent
    // - currently using mComments with extra commentsCount field
    protected ArrayList<ProfilePost> mProfilePosts;

    // TODO replace overriding method by creating a DataCache object in ProfileCommentPresenter and overriding it here
    @Override
    protected ProfileComment getProfileCommentByIndex(int i) {
        return getProfilePostByIndex(i);
    }

    private ProfilePost getProfilePostByIndex(int i) {
        return mProfilePosts.get(i);
    }

    public ProfilePostPresenter(@NonNull ProfilePostContract.View viewInterface){

        mProfilePostView = checkNotNull(viewInterface);
        mProfilePostInteractor = new ProfilePostInteractor(this);

    }

    @Override
    public void loadDataFromDatabase(MySingleton singleton, String currentUser) {
        mProfilePostInteractor.getUserPosts(singleton, currentUser);
    }

    public void setData(JSONArray postsResponse, JSONArray imagesResponse) {
        mComments = postsResponse;
        mImages = imagesResponse;
        mProfilePosts = new ArrayList<>();

        if ((postsResponse.length() != imagesResponse.length()))
            throw new AssertionError("Error: length of postsResponse != imagesResponse");

        for (int i=0; i<postsResponse.length(); i++) {
            try {
                JSONObject jsonPost = mComments.getJSONObject(i);
                JSONObject jsonImage = mImages.getJSONObject(i);
                ProfilePost profilePost = new ProfilePost(jsonPost, jsonImage);
                mProfilePosts.add(profilePost);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onBindViewHolderAtPosition(ProfileCommentContract.View.ViewHolder viewHolderInterface, int i) {
        super.onBindViewHolderAtPosition(viewHolderInterface, i);
        // need to manually call overriden setPostTitle method due to super method casting viewHolderInterface down
        ProfilePost profilePost = getProfilePostByIndex(i);
        ((ProfilePostContract.View.ViewHolder) viewHolderInterface)
                .setPostTitle(profilePost.getPostTitle())
                .setCommentCount(profilePost.getCommentCount());
    }

        /**
         * Gets the item Count of the comments JSONArray
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
