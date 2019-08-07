package ca.gc.inspection.scoop.postcomment;

import android.support.annotation.NonNull;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Objects;

import ca.gc.inspection.scoop.createpost.InteractorBundle;
import ca.gc.inspection.scoop.createpost.PostRequestReceiver;
import ca.gc.inspection.scoop.editcomment.EditCommentBundle;
import ca.gc.inspection.scoop.editcomment.EditCommentCache;
import ca.gc.inspection.scoop.editcomment.EditCommentData;
import ca.gc.inspection.scoop.postcomment.ViewHolderState.SnackBarState;
import ca.gc.inspection.scoop.Config;
import ca.gc.inspection.scoop.util.NetworkUtils;

import static ca.gc.inspection.scoop.postcomment.LikeState.DOWNVOTE;
import static ca.gc.inspection.scoop.postcomment.LikeState.NEUTRAL;
import static ca.gc.inspection.scoop.postcomment.LikeState.UPVOTE;
import static ca.gc.inspection.scoop.postcomment.ViewHolderState.SnackBarState.*;
import static com.google.gson.internal.$Gson$Preconditions.checkNotNull;
import static java.lang.Integer.max;

/**
 * Presenter for replying to a post action. Tt is the most generic presenter
 * related to "posting" actions. Parent presenter for ProfilePostPresenter.
 * Implements the AdapterAPI and ViewHolderAPI to allow adapter and viewHolder to communicate with
 * the presenter.
 */

public class PostCommentPresenter implements
        PostCommentContract.Presenter,
        PostCommentContract.Presenter.AdapterAPI,
        PostCommentContract.Presenter.ViewHolderAPI,
        PostRequestReceiver {

    private static final String TAG = "PostCommentPresenter";

    @NonNull
    private PostCommentContract.View mPostCommentView;
    private PostCommentContract.View.Adapter mAdapter;
    private PostCommentInteractor mPostCommentInteractor;
    protected PostDataCache mDataCache;
    protected EditCommentCache mEditCommentCache = new EditCommentCache();
    protected ViewHolderStateCache mViewHolderStateCache = new ViewHolderStateCache();

    private PostComment getItemByIndex(int i) {
        if (mDataCache == null || getItemCount() < i+1)
            return null;
        return mDataCache.getPostCommentByIndex(i);
    }

    private PostComment getItemByActivityId(int i, String activityId) {
        if (i >=0 && i <= getItemCount()) {
            if (getItemByIndex(i) != null && activityId.equals(getItemByIndex(i).getActivityId())) {
                return getItemByIndex(i);
            }
        }
        return null;
    }

    /**
     * Empty constructor called by child classes (ie. ProfileCommentPresenter) to allow them to create
     * their own View and Interactor objects
     */
    protected PostCommentPresenter() {
    }

    PostCommentPresenter(@NonNull PostCommentContract.View viewInterface, NetworkUtils network){

        setView(viewInterface);
        setInteractor(new PostCommentInteractor(this, network));

    }

    public void setView(@NonNull PostCommentContract.View viewInterface) {
        mPostCommentView = checkNotNull(viewInterface);
    }

    public void setInteractor(@NonNull PostCommentInteractor interactor) {
        mPostCommentInteractor = checkNotNull(interactor);
    }

    @Override
    public void setAdapter(PostCommentContract.View.Adapter adapter) {
        mAdapter = adapter;
    }

    @Override
    public void loadDataFromDatabase(String currentUser) {
        if (mDataCache == null)
            mDataCache = PostDataCache.createWithType(PostComment.class);
        else mDataCache.getPostCommentList().clear();

        mPostCommentInteractor.getPostComments(currentUser);
    }

    public void setData(JSONArray commentsResponse, JSONArray imagesResponse) {
        if ((commentsResponse.length() != imagesResponse.length()))
            Log.i(TAG, "length of commentsResponse != imagesResponse");

        for (int i=0; i<commentsResponse.length(); i++) {
            JSONObject jsonComment = null;
            JSONObject jsonImage = null;
            try {
                jsonComment = commentsResponse.getJSONObject(i);
                jsonImage = imagesResponse.getJSONObject(i);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            PostComment postComment = new PostComment(jsonComment, jsonImage);
            mDataCache.getPostCommentList().add(postComment);
        }

        mAdapter.refreshAdapter();
        mPostCommentView.onLoadedDataFromDatabase();

    }

    /**
     * Description: changes upvote like state depending on the current state
     * @param viewHolderInterface
     * @throws JSONException
     */
    public void changeUpvoteLikeState(PostCommentContract.View.ViewHolder viewHolderInterface, int i) throws JSONException{
        PostComment postComment = getItemByIndex(i);

        if (getItemByIndex(i) != null) {
            String activityid = postComment.getActivityId();
            String posterid = postComment.getPosterId();
            LikeState likeState = postComment.getLikeState();
            int likeCount = Integer.parseInt(postComment.getLikeCount());
            Log.i("liketype1", likeState.getDatabaseValue());

            switch (likeState) {
                case UPVOTE: //if it's already liked, it'll be set to neutral if pressed
                    likeCount -= 1;
                    updateLikeState(viewHolderInterface, i, NEUTRAL);
                    updateLikeCount(viewHolderInterface, i, String.valueOf(likeCount));
                    mPostCommentInteractor.updateLikes(NEUTRAL, String.valueOf(likeCount), activityid, posterid, i, viewHolderInterface);
                    break;
                case DOWNVOTE: //if it's downvoted, it'll be set to upvote state
                    likeCount += 2;
                    updateLikeState(viewHolderInterface, i, UPVOTE);
                    updateLikeCount(viewHolderInterface, i, String.valueOf(likeCount));
                    mPostCommentInteractor.updateLikes(UPVOTE, String.valueOf(likeCount), activityid, posterid, i, viewHolderInterface);
                    break;
                case NEUTRAL: //if it's neutral, it'll be set to upvote state
                    likeCount += 1;
                    updateLikeState(viewHolderInterface, i, UPVOTE);
                    updateLikeCount(viewHolderInterface, i, String.valueOf(likeCount));
                    mPostCommentInteractor.updateLikes(UPVOTE, String.valueOf(likeCount), activityid, posterid, i, viewHolderInterface);
                    break;
                default: //default will be upvote state, if liketype is null
                    likeCount += 1;
                    updateLikeState(viewHolderInterface, i, UPVOTE);
                    updateLikeCount(viewHolderInterface, i, String.valueOf(likeCount));
                    mPostCommentInteractor.updateLikes(UPVOTE, String.valueOf(likeCount), activityid, posterid, i, viewHolderInterface); //will insert the like for the first time
                    break;
            }
            Log.i("likecount1", String.valueOf(likeCount));
        }
    }

    /**
     * Description: changes downvote like state depending on the current state
     * @param viewHolderInterface
     * @throws JSONException
     */
    public void changeDownvoteLikeState(PostCommentContract.View.ViewHolder viewHolderInterface, int i) throws JSONException {
        PostComment postComment = getItemByIndex(i);

        if (getItemByIndex(i) != null) {
            String activityid = postComment.getActivityId();
            String posterid = postComment.getPosterId();
            LikeState likeState = postComment.getLikeState();
            int likeCount = Integer.parseInt(postComment.getLikeCount());

            Log.i("liketype2", String.valueOf(likeState));
            switch (likeState) {
                case UPVOTE: //if it's liked, it'll be set to downvote state
                    likeCount -= 2;
                    updateLikeState(viewHolderInterface, i, DOWNVOTE);
                    updateLikeCount(viewHolderInterface, i, String.valueOf(likeCount));
                    mPostCommentInteractor.updateLikes(DOWNVOTE, String.valueOf(likeCount), activityid, posterid, i, viewHolderInterface);
                    break;
                case DOWNVOTE: //if it's downvoted, it'll be set to neutral state
                    likeCount += 1;
                    updateLikeState(viewHolderInterface, i, NEUTRAL);
                    updateLikeCount(viewHolderInterface, i, String.valueOf(likeCount));
                    mPostCommentInteractor.updateLikes(NEUTRAL, String.valueOf(likeCount), activityid, posterid, i, viewHolderInterface);
                    break;
                case NEUTRAL: //if it's neutral state, it'll be set to downvote state
                    likeCount -= 1;
                    updateLikeState(viewHolderInterface, i, DOWNVOTE);
                    updateLikeCount(viewHolderInterface, i, String.valueOf(likeCount));
                    mPostCommentInteractor.updateLikes(DOWNVOTE, String.valueOf(likeCount), activityid, posterid, i, viewHolderInterface);
                    break;
                default: //default will be downvote state, if liketype is null
                    likeCount -= 1;
                    updateLikeState(viewHolderInterface, i, DOWNVOTE);
                    updateLikeCount(viewHolderInterface, i, String.valueOf(likeCount));
                    mPostCommentInteractor.updateLikes(DOWNVOTE, String.valueOf(likeCount), activityid, posterid, i, viewHolderInterface); //will insert the downvote for the first time
                    break;
            }
            Log.i("likecount2", String.valueOf(likeCount));
        }
    }

    /**
     * Description: updates the like count to new total
     *
     * @param likeCount
     * @throws JSONException
     */
    private void updateLikeCount(PostCommentContract.View.ViewHolder viewHolderInterface, int i, String likeCount) {
        if (getItemByIndex(i) != null) {
            getItemByIndex(i).setLikeCount(likeCount);
            viewHolderInterface.setLikeCount(likeCount); //sets like count to new total
        }
    }

    /**
     * Description: updates the like state
     *
     * @param viewHolderInterface
     * @param i
     * @param likeState
     */
    private void updateLikeState(PostCommentContract.View.ViewHolder viewHolderInterface, int i, LikeState likeState) {
        if (getItemByIndex(i) != null) {
            getItemByIndex(i).setLikeState(likeState);
            viewHolderInterface.setLikeState(likeState);
        }
    }

    @Override
    public void onBindViewHolderAtPosition(PostCommentContract.View.ViewHolder viewHolderInterface, int i) {
        PostComment postComment = getItemByIndex(i);
        bindPostCommentDataToViewHolder(viewHolderInterface, postComment);
        bindEditCommentDataToViewHolder(viewHolderInterface, postComment, i, mEditCommentCache);
        bindViewHolderStateToViewHolder(viewHolderInterface, postComment, i, mViewHolderStateCache);
    }

    public static void bindPostCommentDataToViewHolder(
            PostCommentContract.View.ViewHolder viewHolderInterface, PostComment postComment) {
        Log.d(TAG, "bindPostCommentDataToViewHolder");
        if (postComment != null) {
            Log.d(TAG, "activityId: " + postComment.getActivityId() + ", posttext: " + postComment.getPostText());
            viewHolderInterface.clearCallBackIdentifier();
            viewHolderInterface.setDate(postComment.getCreatedDate())
                    .setLikeCount(postComment.getLikeCount())
                    .setPostTextWithFormat(postComment.getPostText(), postComment.getTextFormat())
                    .setUserImageFromString(postComment.getProfileImageString())
                    .setUserName(postComment.getValidFullName())
                    .setLikeState(postComment.getLikeState())
                    .setSavedState(postComment.getSavedState());
        }
    }


    /**
     * Updates a viewHolder's saved state by setting it to the opposite Boolean value of its current state
     * UI is updated and then the appropriate network request is made through a call to the Interactor
     * @param viewHolderInterface interface used to communicate with the View/viewHolder
     * @param i position of the view
     * @throws JSONException
     */
    public void updateSavedState(PostCommentContract.View.ViewHolder viewHolderInterface, int i) throws JSONException {
        PostComment postComment = getItemByIndex(i);
        String activityid = postComment.getActivityId();
        if (getItemByIndex(i) != null) {
            Boolean savedState = postComment.getSavedState();
            getItemByIndex(i).setSavedState(!savedState);
            viewHolderInterface.setSavedState(!savedState);
            if (!savedState){
                mPostCommentInteractor.savePost(activityid, Config.currentUser);
            } else {
                mPostCommentInteractor.unsavePost(activityid, Config.currentUser);
            }
        }
    }

    /**
     * Helper method to bind the unsaved edit comment text to a post comment. Called when RecyclerView
     * scrolls and new data has to be attached to the existing view holders.
     *
     * @param viewHolderInterface   Call interface method to setup the edit comment UI (if unsaved edit exists).
     * @param postComment           DataCache item which contains the activityId of the comment.
     *                              Serves as the unique id of a comment since the adapter position
     *                              can change if the DataCache is reordered or if comments are added/deleted.
     * @param i                     Adapter position - the index to retrieve an element from the DataCache.
     * @param editCommentCache      Cache of EditCommentData objects to retrieve the correct unsaved edit
     *                              text data by activityId.
     */
    public static void bindEditCommentDataToViewHolder(
            PostCommentContract.View.ViewHolder viewHolderInterface, PostComment postComment, int i, EditCommentCache editCommentCache) {
        Log.d(TAG, "bindEditCommentDataToViewHolder");
        if (postComment != null && editCommentCache != null) {
            EditCommentData editCommentData = editCommentCache.getEditCommentData(postComment.getActivityId());
            if (editCommentData != null) {
                String postText = editCommentData.getPostText();
                Log.d(TAG, "editCommentCache: " + editCommentCache.toString());
                Log.d(TAG, "activityIds: " + editCommentData.getActivityId() + ", " + postComment.getActivityId());
                viewHolderInterface.onEditComment(i, postComment.getActivityId());
                viewHolderInterface.setEditPostText(postText);
            }
            else {
                viewHolderInterface.hideEditText();
            }
        }
    }

    /**
     * Helper method to bind the state of Snackbars and whether it can send data to the database through
     * the Presenter or if it is waiting for a response.
     * Called when RecyclerView scrolls or the page is refreshed and allows the state of SnackBar messages,
     * such as the retry send action, to be preserved.
     *
     * @param viewHolderInterface   Call interface method to set the SnackBar and waitingForResponse state.
     * @param postComment           DataCache item which contains the activityId of the comment.
     *                              Serves as the unique id of a comment since the adapter position
     *                              can change if the DataCache is reordered or if comments are added/deleted.
     * @param i                     Adapter position - the index to retrieve an element from the DataCache.
     * @param viewHolderStateCache  Cache of ViewHolderState objects to retrieve the correct SnackBar and
     *                              waitingForResponse state by activityId.
     */
    public static void bindViewHolderStateToViewHolder(
            PostCommentContract.View.ViewHolder viewHolderInterface, PostComment postComment,
            int i, ViewHolderStateCache viewHolderStateCache) {
        Log.d(TAG, "bindViewHolderStateToViewHolder");
        if (postComment != null && viewHolderStateCache != null) {
            String activityId = postComment.getActivityId();
            ViewHolderState viewHolderState = viewHolderStateCache.getViewHolderState(activityId);
            Log.d(TAG, "viewHolderStateCache: " + viewHolderStateCache.toString());
            if (viewHolderState != null) {
                Log.d(TAG, "viewHolderStateCache: " + viewHolderStateCache.toString());
                SnackBarState snackBarState = viewHolderState.getSnackBarState();
                switch (snackBarState) {
                    case NONE:
                        viewHolderInterface.dismissSnackBar();
                        break;
                    case EDIT_COMMENT_IN_PROGRESS:
                        viewHolderInterface.setSnackBarForCommentInProgress(activityId);
                        break;
                    case EDIT_COMMENT_SUCCESS:
                        viewHolderInterface.setSnackBarEditCommentSuccess(activityId);
                        break;
                    case EDIT_COMMENT_RETRY:
                        viewHolderInterface.setSnackBarEditCommentRetry(i, activityId);
                        break;
                }

                viewHolderInterface.setWaitingForResponse(viewHolderState.isWaitingForResponse());
            }
        }
    }

    /**
     * Gets the number of items in the DataCache
     * @return the count
     */
    @Override
    public int getItemCount() {
        if (mDataCache == null)
            return 0;
        return mDataCache.getItemCount();
    }

    @Override
    public String getPosterIdByIndex(int i) {
        return Objects.requireNonNull(getItemByIndex(i)).getPosterId();
    }

    @Override
    public String getActivityIdByIndex(int i){
        return Objects.requireNonNull(getItemByIndex(i)).getActivityId();
    }

    @Override
    public Boolean getSavedStateByIndex(int i){
        return Objects.requireNonNull(getItemByIndex(i)).getSavedState();
    }

    /**
     * Called by PostCommentViewHolder to save the edit for a post comment to the database.
     *
     * @param viewHolderInterface   Interface to update when the database response is received. Note that a
     *                              callback identifier must be set (in this case, the activityId) so that
     *                              when the Presenter receives the database response, it knows if the ViewHolder
     *                              is still the correct one. If the ViewHolder was scrolled and recycled, we don't
     *                              want it to update UI as it may be showing a completely different post comment.
     *                              Instead the correct view holder will eventually have its UI updated using the
     *                              onBind methods and Presenter-scoped Cache objects.
     *
     * @param i                     Estimated adapter position. This may shift if comments are added/deleted
     *                              or if the user initiates a pull down to refresh while the viewholder is waiting
     *                              for a database response for editing a comment. Nonetheless, this value
     *                              provides a starting point to search by activityId - it reduces the need
     *                              for an O(n) scan of the DataCache to locate the correct item to update by
     *                              activityId.
     *
     * @param activityId            Unique identifier for the post comment.
     *
     * @param newText               The new text to update the post comment to.
     */
    @Override
    public void sendCommentToDatabase(PostCommentContract.View.ViewHolder viewHolderInterface, int i, String activityId, String newText) {
        viewHolderInterface.setCallBackIdentifier(activityId);
        cacheEditCommentData(activityId, newText);

        mViewHolderStateCache.createIfMissingViewHolderState(activityId, true, i, EDIT_COMMENT_IN_PROGRESS);
        viewHolderInterface.setSnackBarForCommentInProgress(activityId);

        // Put all the data in the EditCommentBundle that we'll need again in the Presenter's callback method
        EditCommentBundle editCommentBundle = new EditCommentBundle();
        editCommentBundle.setActivityId(activityId);
        editCommentBundle.setViewHolder(viewHolderInterface);
        Log.d(TAG, "sendCommentToDatabase " + mViewHolderStateCache.toString());

        mPostCommentInteractor.updatePostComment(editCommentBundle, activityId, newText);
    }

    /**
     * Need to store the unsaved changes in the EditCommentCache so that when the user scrolls through
     * the RecyclerView when editing a comment, the correct data can be re-attached to the view holder.
     *
     * @param activityId    Unique identifier of a post comment. Adapter position may shift and cannot be
     *                      used here.
     * @param postText      The current unsaved text.
     */
    @Override
    public void cacheEditCommentData(String activityId, String postText) {
        mEditCommentCache.insertOrUpdateExistingEditCommentDataWithPostText(activityId, postText);
    }

    /**
     * When the user cancels their edit for a specific post comment, we must drop the data from the
     * EditCommentCache and ViewHolderStateCache, otherwise when the user scrolls through the RecyclerView,
     * the dropped changes and irrelevant SnackBar states may show up. It is not enough to set the
     * ViewHolder's waitingForResponse to False, we must update the ViewHolderStateCache to prevent
     * onBind methods from resetting the ViewHolderState to a stale value.
     *
     * @param activityId    Unique identifier of a post comment. Adapter position may shift and cannot be
     *                      used here.
     */
    @Override
    public void onCancelEditComment(String activityId) {
        mEditCommentCache.removeEditCommentData(activityId);
        ViewHolderState viewHolderState = mViewHolderStateCache.getViewHolderState(activityId);
        if (viewHolderState != null) {
            viewHolderState.setWaitingForResponse(false);
            viewHolderState.setSnackBarState(NONE);
        }
    }

    @Override
    public void onDatabaseResponse(boolean success, InteractorBundle interactorBundle) {

        EditCommentBundle editCommentBundle = (EditCommentBundle) interactorBundle;
        String activityId = editCommentBundle.getActivityId();
        PostCommentContract.View.ViewHolder viewHolderInterface = editCommentBundle.getViewHolder();
        EditCommentData editCommentData = mEditCommentCache.getEditCommentData(activityId);
        ViewHolderState viewHolderState = mViewHolderStateCache.getViewHolderState(activityId);
        int i = viewHolderState.getPosition();
        String newText = editCommentData.getPostText();

        mViewHolderStateCache.createIfMissingViewHolderState(
                activityId, false, i, SnackBarState.EDIT_COMMENT_SUCCESS);

        if (success)
            onDatabaseResponseSuccess(viewHolderInterface, i, activityId, newText);
        else
            onDatabaseResponseError(viewHolderInterface, i, activityId);
    }

    private void onDatabaseResponseError(PostCommentContract.View.ViewHolder viewHolderInterface,
                                         int i, String activityId) {
        mViewHolderStateCache.getViewHolderState(activityId).setSnackBarState(EDIT_COMMENT_RETRY);
        if (viewHolderInterface != null &&
                viewHolderInterface.getCallBackIdentifier() != null &&
                viewHolderInterface.getCallBackIdentifier().equals(activityId)) {
            viewHolderInterface.setSnackBarEditCommentRetry(i, activityId);
        }
    }

    private void onDatabaseResponseSuccess(PostCommentContract.View.ViewHolder viewHolderInterface,
                                           int i, String activityId, String newText) {

        updateDataCachePostTextForActivityId(i, activityId, newText);
        mEditCommentCache.removeEditCommentData(activityId);
        mViewHolderStateCache.incrementPositionForAll();
        mViewHolderStateCache.getViewHolderState(activityId).setSnackBarState(EDIT_COMMENT_SUCCESS);

        if (viewHolderInterface != null &&
                viewHolderInterface.getCallBackIdentifier() != null &&
                viewHolderInterface.getCallBackIdentifier().equals(activityId)) {
            viewHolderInterface.setPostText(newText);
            viewHolderInterface.hideEditText();
            viewHolderInterface.setSnackBarEditCommentSuccess(activityId);
        }
    }

    private boolean updateDataCachePostTextIfActivityIdMatchesIndex(int i, String activityId, String newText) {
        PostComment postComment = getItemByActivityId(i, activityId);
        if (postComment != null) {
            Log.d(TAG, "Presenter update datacache" + activityId);
            postComment.setPostText(newText);
        }
        return false;
    }

    private void updateDataCachePostTextForActivityId(int estimatedPosition, String activityId, String newText) {
        int maxDistance = max(estimatedPosition, getItemCount()-estimatedPosition);
        if (updateDataCachePostTextIfActivityIdMatchesIndex(estimatedPosition, activityId, newText))
            return;

        for (int i=1; i<=maxDistance; i++) {
            if (updateDataCachePostTextIfActivityIdMatchesIndex(estimatedPosition + i, activityId, newText) ||
                    updateDataCachePostTextIfActivityIdMatchesIndex(estimatedPosition - i, activityId, newText))
                break;
        }
    }

    protected void onItemAdded() {
        mViewHolderStateCache.incrementPositionForAll();
    }

    @Override
    public void onSnackBarDismissed(String activityId) {
        mViewHolderStateCache.getViewHolderState(activityId).setSnackBarState(NONE);
    }

    @Override
    public boolean unsavedEditsExist(int i, String activityId) {
        if (mEditCommentCache == null)
            return false;
        EditCommentData editCommentData = mEditCommentCache.getEditCommentData(activityId);
        if (editCommentData == null)
            return false;
        PostComment postComment = getItemByActivityId(i, activityId);
        return postComment != null && !postComment.getPostText().equals(editCommentData.getPostText());
    }
}
