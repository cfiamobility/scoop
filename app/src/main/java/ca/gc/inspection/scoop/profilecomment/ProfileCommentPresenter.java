package ca.gc.inspection.scoop.profilecomment;

import android.support.annotation.NonNull;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Objects;

import ca.gc.inspection.scoop.postcomment.PostCommentPresenter;
import ca.gc.inspection.scoop.postcomment.PostDataCache;
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
    private boolean refreshingData = false;

    private ProfileComment getItemByIndex(int i) {
        if (mDataCache == null)
            return null;
        return mDataCache.getProfileCommentByIndex(i);
    }

    /**
     * Empty constructor called by child classes (ie. ProfilePostPresenter) to allow them to create
     * their own View and Interactor objects
     */
    protected ProfileCommentPresenter() {
    }

    ProfileCommentPresenter(@NonNull ProfileCommentContract.View viewInterface, NetworkUtils network){
        setView(viewInterface);
        setInteractor(new ProfileCommentInteractor(this, network));
    }

    public void setView(@NonNull ProfileCommentContract.View viewInterface) {
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
    public void loadDataFromDatabase(String currentUser) {
        if (!refreshingData) {
            refreshingData = true;

            if (mDataCache == null)
                mDataCache = PostDataCache.createWithType(ProfileComment.class);
            else mDataCache.getProfileCommentList().clear();
            /* Refresh the adapter right after clearing the DataCache. Prevents the adapter from trying
            to access an item which no longer exists when scrolling during a pull down to refresh */
            mAdapter.refreshAdapter();

            mProfileCommentInteractor.getProfileComments(currentUser);
        }
    }

    /**
     * Update the DataCache with the ProfileComments data to be displayed in the RecyclerView.
     * @param commentsResponse
     * @param imagesResponse
     */
    @Override
    public void setData(JSONArray commentsResponse, JSONArray imagesResponse) {

        if ((commentsResponse.length() != imagesResponse.length()))
            Log.i(TAG, "length of commentsReponse != imagesResponse");

        for (int i=0; i<commentsResponse.length(); i++) {
            JSONObject jsonComment = null;
            JSONObject jsonImage = null;
            try {
                jsonComment = commentsResponse.getJSONObject(i);
                jsonImage = imagesResponse.getJSONObject(i);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            ProfileComment profileComment = new ProfileComment(jsonComment, jsonImage);
            mDataCache.getProfileCommentList().add(profileComment);
        }

        mAdapter.refreshAdapter();
        refreshingData = false;
        mProfileCommentView.onLoadedDataFromDatabase();
    }

    @Override
    public void onBindViewHolderAtPosition(ProfileCommentContract.View.ViewHolder viewHolderInterface, int i) {
        ProfileComment profileComment = getItemByIndex(i);
        bindProfileCommentDataToViewHolder(viewHolderInterface, profileComment);
        bindEditCommentDataToViewHolder(viewHolderInterface, profileComment, i, mEditCommentCache);
        bindViewHolderStateToViewHolder(viewHolderInterface, profileComment, i, mViewHolderStateCache);
    }

    public static void bindProfileCommentDataToViewHolder(
            ProfileCommentContract.View.ViewHolder viewHolderInterface, ProfileComment profileComment) {
        if (profileComment != null) {
            bindPostCommentDataToViewHolder(viewHolderInterface, profileComment);
            viewHolderInterface.setPostTitle(profileComment.getPostTitle());
        }
    }
    
    public String getReferenceIdByIndex(int i){
        return Objects.requireNonNull(getItemByIndex(i)).getReferenceID();
    }
}
