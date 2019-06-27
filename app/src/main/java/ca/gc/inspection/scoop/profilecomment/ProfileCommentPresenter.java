package ca.gc.inspection.scoop.profilecomment;

import android.support.annotation.NonNull;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import ca.gc.inspection.scoop.postcomment.PostComment;
import ca.gc.inspection.scoop.postcomment.PostCommentContract;
import ca.gc.inspection.scoop.postcomment.PostCommentPresenter;
import ca.gc.inspection.scoop.util.NetworkUtils;
import static com.google.gson.internal.$Gson$Preconditions.checkNotNull;

/**
 * Presenter for replying to a post action. Tt is the most generic presenter
 * related to "posting" actions. Parent presenter for ProfilePostPresenter.
 * Implements the AdapterAPI and ViewHolderAPI to allow adapter and viewHolder to communicate with
 * the presenter.
 */

public class ProfileCommentPresenter extends PostCommentPresenter implements
        ProfileCommentContract.Presenter,
        ProfileCommentContract.Presenter.AdapterAPI,
        ProfileCommentContract.Presenter.ViewHolderAPI {

    private static final String TAG = "PostCommentPresenter";

    @NonNull
    private ProfileCommentContract.View mProfileCommentView;
    private ProfileCommentContract.View.Adapter mAdapter;
    private ProfileCommentInteractor mProfileCommentInteractor;
    protected ArrayList<ProfileComment> mProfileComments;

    @Override
    protected PostComment getPostCommentByIndex(int i) {
        return getProfileCommentByIndex(i);
    }

    protected ProfileComment getProfileCommentByIndex(int i) {
        if (mProfileComments == null)
            return null;
        return mProfileComments.get(i);
    }

    /**
     * Empty constructor called by child classes (ie. ProfilePostPresenter) to allow them to create
     * their own View and Interactor objects
     */
    public ProfileCommentPresenter() {
    }

    ProfileCommentPresenter(@NonNull ProfileCommentContract.View viewInterface){

        setView(viewInterface);
        setInteractor(new ProfileCommentInteractor(this));

    }

    public void setView(@NonNull ProfileCommentContract.View viewInterface) {
        super.setView(viewInterface);
        mProfileCommentView = checkNotNull(viewInterface);
    }

    public void setInteractor(@NonNull ProfileCommentInteractor interactor) {
        super.setInteractor(interactor);
        mProfileCommentInteractor = checkNotNull(interactor);
    }

    @Override
    public void setAdapter(ProfileCommentContract.View.Adapter adapter) {
        mAdapter = adapter;
    }

    @Override
    public void loadDataFromDatabase(NetworkUtils network, String currentUser) {
        mProfileCommentInteractor.getUserCommentsAndImages(network, currentUser);
    }

    public void setData(JSONArray commentsResponse, JSONArray imagesResponse) {
        mComments = commentsResponse;
        mImages = imagesResponse;
        mProfileComments = new ArrayList<>();

        if ((commentsResponse.length() != imagesResponse.length()))
            Log.i(TAG, "length of commentsReponse != imagesResponse");

        for (int i=0; i<commentsResponse.length(); i++) {
            JSONObject jsonComment = null;
            JSONObject jsonImage = null;
            try {
                jsonComment = mComments.getJSONObject(i);
                jsonImage = mImages.getJSONObject(i);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            ProfileComment profileComment = new ProfileComment(jsonComment, jsonImage);
            mProfileComments.add(profileComment);
        }

        mAdapter.refreshAdapter();
        // TODO if adapter has not been set - wait until it has been set and call refreshAdapter?
    }

    public void onBindViewHolderAtPosition(PostCommentContract.View.ViewHolder viewHolderInterface, int i) {
        super.onBindViewHolderAtPosition(viewHolderInterface, i);

        ProfileComment profileComment = getProfileCommentByIndex(i);
        if (profileComment != null) {
            ((ProfileCommentContract.View.ViewHolder) viewHolderInterface)
                    .setPostTitle(profileComment.getPostTitle());
        }
    }

    /**
     * Gets the item Count of the comments JSONArray
     * @return the length
     */
    @Override
    public int getItemCount() {
        return super.getItemCount();
    }

    @Override
    public String getPosterIdByIndex(int i) {
        return getProfileCommentByIndex(i).getPosterId();
    }
}
