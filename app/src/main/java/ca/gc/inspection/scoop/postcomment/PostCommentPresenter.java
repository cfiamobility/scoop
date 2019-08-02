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
        PostCommentContract.Presenter.ViewHolderAPI, PostRequestReceiver {

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

    public void updateSavedStatus(PostCommentContract.View.ViewHolder viewHolderInterface, int i, Boolean savedStatus) throws JSONException {
        if (getItemByIndex(i) != null) {
            getItemByIndex(i).setSavedStatus(savedStatus);
            viewHolderInterface.setSavedStatus(savedStatus); //sets like count to new total
            Log.i("saved status in post comment presenter", Boolean.toString(getSavedStatusByIndex(i)));
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
            viewHolderInterface.setDate(postComment.getDate())
                    .setLikeCount(postComment.getLikeCount())
                    .setPostText(postComment.getPostText())
                    .setUserImageFromString(postComment.getProfileImageString())
                    .setUserName(postComment.getValidFullName())
                    .setLikeState(postComment.getLikeState());
        }
    }

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
    public Boolean getSavedStatusByIndex(int i){
        return Objects.requireNonNull(getItemByIndex(i)).getSavedStatus();
    }

    @Override
    public String getPostTextByIndex(int i){
        return Objects.requireNonNull(getItemByIndex(i)).getPostText();
    }

    @Override
    public void sendCommentToDatabase(PostCommentContract.View.ViewHolder viewHolderInterface, int i, String activityId, String newText) {
        viewHolderInterface.setCallBackIdentifier(activityId);
        cacheEditCommentData(activityId, newText);

        mViewHolderStateCache.createIfMissingViewHolderState(activityId, true, i, EDIT_COMMENT_IN_PROGRESS);
        viewHolderInterface.setSnackBarForCommentInProgress(activityId);

        EditCommentBundle editCommentBundle = new EditCommentBundle();
        editCommentBundle.setActivityId(activityId);
        editCommentBundle.setViewHolder(viewHolderInterface);
        Log.d(TAG, "sendCommentToDatabase " + mViewHolderStateCache.toString());

        mPostCommentInteractor.updatePostComment(editCommentBundle, activityId, newText);
    }

    @Override
    public void cacheEditCommentData(String activityId, String postText) {
        mEditCommentCache.insertOrUpdateExistingEditCommentDataWithPostText(activityId, postText);
    }

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

        mViewHolderStateCache.createIfMissingViewHolderState(activityId, false, i, SnackBarState.EDIT_COMMENT_SUCCESS);

        if (success) {
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
        else {
            mViewHolderStateCache.getViewHolderState(activityId).setSnackBarState(EDIT_COMMENT_RETRY);
            if (viewHolderInterface != null &&
                    viewHolderInterface.getCallBackIdentifier() != null &&
                    viewHolderInterface.getCallBackIdentifier().equals(activityId)) {
                viewHolderInterface.setSnackBarEditCommentRetry(i, activityId);
            }
        }
    }

    private boolean updateDataCachePostTextIfActivityIdMatchesIndex(int i, String activityId, String newText) {
        if (i >=0 && i <= getItemCount()) {
            if (getItemByIndex(i) != null && activityId.equals(getItemByIndex(i).getActivityId())) {
                getItemByIndex(i).setPostText(newText);
                Log.d(TAG, "Presenter update datacache" + activityId);
                return true;
            }
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
    public boolean unsavedEditsExist() {
        return (mEditCommentCache != null && mEditCommentCache.size() != 0);
    }

    @Override
    public void clearEditCommentCache() {
        mEditCommentCache = null;
    }

    @Override
    public void clearViewHolderStateCache() {
        mViewHolderStateCache = null;
    }
}
