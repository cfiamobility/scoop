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
import ca.gc.inspection.scoop.util.TextFormat;

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
        viewHolderInterface.clearCallBackIdentifier();
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
                /* CallBackIdentifier reset when ViewHolders are recycled so that Presenter knows if the correct
                ViewHolder is still on screen.*/
                if (viewHolderState.isWaitingForResponse())
                    viewHolderInterface.setCallBackIdentifier(activityId);
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

    /**
     * Callback for editing post comment. Updates the ViewHolderStateCache so that if the user receives
     * the database response during a pull down to refresh or when the user has scrolled away, the
     * relevant SnackBar (success or retry) will show when the ViewHolder comes "back into view"
     * (by showing the relevant post comment data).
     *
     * @param success       True if a comment was successfully edited
     * @param interactorBundle  Casted back to an EditCommentBundle. Passed as an InteractorBundle
     *                          so that newPostRequest can call PostRequestReceiver's interface method.
     */
    @Override
    public void onDatabaseResponse(boolean success, InteractorBundle interactorBundle) {

        // Retrieve necessary data from EditCommentBundle
        EditCommentBundle editCommentBundle = (EditCommentBundle) interactorBundle;
        String activityId = editCommentBundle.getActivityId();
        PostCommentContract.View.ViewHolder viewHolderInterface = editCommentBundle.getViewHolder();

        // Use the retrieved activityId to access the correct objects in the caches
        EditCommentData editCommentData = mEditCommentCache.getEditCommentData(activityId);
        ViewHolderState viewHolderState = mViewHolderStateCache.getViewHolderState(activityId);

        if (editCommentData != null && viewHolderState != null) {
            int i = viewHolderState.getPosition();
            String newText = editCommentData.getPostText();

            mViewHolderStateCache.createIfMissingViewHolderState(
                    activityId, false, i, SnackBarState.EDIT_COMMENT_SUCCESS);

            if (viewHolderHasCallBackIdentifier(viewHolderInterface, activityId)) {
                viewHolderInterface.setWaitingForResponse(false);
            }

            if (success)
                onDatabaseResponseSuccess(viewHolderInterface, i, activityId, newText);
            else
                onDatabaseResponseError(viewHolderInterface, i, activityId);
        }
        else Log.d(TAG, "editCommentData and/or viewHolderState is null onDatabaseResponse");
    }

    /**
     * Private helper method for onDatabaseResponse to handle when the comment's changes failed to save.
     * Updates the relevant caches for edit comment text and ViewHolder state so that onBind attaches
     * the correct data.
     *
     * @param viewHolderInterface   Interface to update when the database response is received. Note that the
     *                              callback identifier must be checked(in this case, the activityId) so that
     *                              when the Presenter receives the database response, it knows if the ViewHolder
     *                              is still the correct one. If the ViewHolder was scrolled and recycled, we don't
     *                              want it to update UI as it may be showing a completely different post comment.
     *                              Instead the correct view holder will eventually have its UI updated using the
     *                              onBind methods and Presenter-scoped Cache objects.
     *
     * @param i                     This may shift if comments are added/deleted
     *                              or if the user initiates a pull down to refresh while the viewholder is waiting
     *                              for a database response for editing a comment. Nonetheless, this value
     *                              provides a starting point to search by activityId - it reduces the need
     *                              for an O(n) scan of the DataCache to locate the correct item to update by
     *                              activityId.
     *
     * @param activityId            Unique identifier for the post comment.
     */
    private void onDatabaseResponseError(PostCommentContract.View.ViewHolder viewHolderInterface,
                                         int i, String activityId) {
        mViewHolderStateCache.getViewHolderState(activityId).setSnackBarState(EDIT_COMMENT_RETRY);
        if (viewHolderHasCallBackIdentifier(viewHolderInterface, activityId)) {
            viewHolderInterface.setSnackBarEditCommentRetry(i, activityId);
        }
    }

    /**
     * Private helper method for onDatabaseResponse to handle when the comment was successfully edited.
     *
     * @param viewHolderInterface   Interface to update when the database response is received. Note that the
     *                              callback identifier must be checked(in this case, the activityId) so that
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
     * @param newText               updated post comment text
     */
    private void onDatabaseResponseSuccess(PostCommentContract.View.ViewHolder viewHolderInterface,
                                           int i, String activityId, String newText) {

        // must be updated so that scrolling binds the updated text to the ViewHolders (if the activity was not refreshed)
        updateDataCachePostTextForActivityId(i, activityId, newText);
        mEditCommentCache.removeEditCommentData(activityId);
        mViewHolderStateCache.getViewHolderState(activityId).setSnackBarState(EDIT_COMMENT_SUCCESS);

        /* Update ViewHolder UI if the user has not scrolled away, otherwise rely on the onBind methods
        attaching the correct data using the EditCommentCache and ViewHolderStateCache */
        if (viewHolderHasCallBackIdentifier(viewHolderInterface, activityId)) {
            TextFormat textFormat = getItemByIndex(i).getTextFormat();
            viewHolderInterface.setPostTextWithFormat(newText, textFormat);
            viewHolderInterface.hideEditText();
            viewHolderInterface.setSnackBarEditCommentSuccess(activityId);
        }
    }

    /**
     * Helper method for database callback methods. Checks if the viewHolder is still showing the
     * same post comment which was edited so that the UI can be updated.
     *
     * @param viewHolderInterface       check the callBackIdentifier
     * @param activityId                Unique identifier for post comment.
     * @return
     */
    private boolean viewHolderHasCallBackIdentifier(
            PostCommentContract.View.ViewHolder viewHolderInterface, String activityId) {
        return (viewHolderInterface != null &&
                viewHolderInterface.getCallBackIdentifier() != null &&
                viewHolderInterface.getCallBackIdentifier().equals(activityId));
    }

    /**
     * Helper method for updateDataCachePostTextForActivityId
     *
     * @param i             Position in DataCache to check
     * @param activityId    Unique identifier for post comment.
     * @param newText   update post text to this
     * @return  if a post comment in the DataCache was updated
     */
    private boolean updateDataCachePostTextIfActivityIdMatchesIndex(int i, String activityId, String newText) {
        PostComment postComment = getItemByActivityId(i, activityId);
        if (postComment != null) {
            Log.d(TAG, "Presenter update datacache" + activityId);
            postComment.setPostText(newText);
            return true;
        }
        return false;
    }

    /**
     * Helper method for onDatabaseResponseSuccess. Searches outward from the estimated position for
     * a post comment with a matching activityId. Upon finding the PostComment object, update the
     * post text to newText. The search is necessary if the user adds/removes or refreshes the comments
     * causing the order of the DataCache objects to differ from the original position.
     *
     * @param estimatedPosition     adapter position to start search from
     * @param activityId            unique identifier of post comment to edit
     * @param newText               update post text in DataCache
     */
    private void updateDataCachePostTextForActivityId(int estimatedPosition, String activityId, String newText) {
        int maxDistance = max(estimatedPosition, getItemCount()-estimatedPosition);
        if (updateDataCachePostTextIfActivityIdMatchesIndex(estimatedPosition, activityId, newText))
            return;

        for (int i=1; i<=maxDistance; i++) {
            // terminate loop early if a post comment in the DataCache was updated
            if (updateDataCachePostTextIfActivityIdMatchesIndex(estimatedPosition + i, activityId, newText) ||
                    updateDataCachePostTextIfActivityIdMatchesIndex(estimatedPosition - i, activityId, newText))
                break;
        }
    }

    /**
     * Called when a post comment is added. Attempts to keep estimated adapter positions in ViewHolderStateCache
     * up to date to reduce the amount of scanning required when updating the DataCache by activityId.
     */
    protected void onItemAdded() {
        mViewHolderStateCache.incrementPositionForAll();
    }

    /**
     * Need to update the ViewHolderStateCache otherwise the SnackBar will be reshown every time
     * onBind is called on the ViewHolder.
     *
     * @param activityId    Unique identifier for post comment
     */
    @Override
    public void onSnackBarDismissed(String activityId) {
        mViewHolderStateCache.getViewHolderState(activityId).setSnackBarState(NONE);
    }

    /**
     * Check if unsaved edits exist for a specific post comment. Used to check whether the
     * PostCommentViewHolder should save the changes to the database through the Presenter or
     * show a SnackBar message stating that no changes were made.
     *
     * Disambiguation:
     * This is different from unsavedEditsExist (no parameters) which is used in an Activity/Fragment
     * to check if the user should be prompted to leave their unsaved edits.
     *
     * @param i             Estimated adapter position
     * @param activityId    Unique identifier for post comment
     * @return  True if there are unsaved edits in the EditCommentCache
     */
    @Override
    public boolean unsavedEditsExistForViewHolder(int i, String activityId) {
        if (mEditCommentCache == null)
            return false;
        EditCommentData editCommentData = mEditCommentCache.getEditCommentData(activityId);
        if (editCommentData == null)
            return false;
        PostComment postComment = getItemByActivityId(i, activityId);
        return postComment != null && !postComment.getPostText().equals(editCommentData.getPostText());
    }

    /**
     * We need the post text excluding the edited date when editing a post comment.
     * @param i adapter position
     * @return  post text without the footer
     */
    @Override
    public String getPostTextById(int i) {
        return getItemByIndex(i).getPostText();
    }
}
