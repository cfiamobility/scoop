package ca.gc.inspection.scoop.postcomment;

import android.support.annotation.NonNull;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import ca.gc.inspection.scoop.util.NetworkUtils;

import static ca.gc.inspection.scoop.postcomment.LikeState.DOWNVOTE;
import static ca.gc.inspection.scoop.postcomment.LikeState.NEUTRAL;
import static ca.gc.inspection.scoop.postcomment.LikeState.UPVOTE;
import static com.google.gson.internal.$Gson$Preconditions.checkNotNull;

/**
 * Presenter for replying to a post action. Tt is the most generic presenter
 * related to "posting" actions. Parent presenter for ProfilePostPresenter.
 * Implements the AdapterAPI and ViewHolderAPI to allow adapter and viewHolder to communicate with
 * the presenter.
 */

public class PostCommentPresenter implements
        PostCommentContract.Presenter,
        PostCommentContract.Presenter.AdapterAPI,
        PostCommentContract.Presenter.ViewHolderAPI {

    private static final String TAG = "PostCommentPresenter";

    @NonNull
    private PostCommentContract.View mPostCommentView;
    private PostCommentContract.View.Adapter mAdapter;
    private PostCommentInteractor mPostCommentInteractor;
    protected JSONArray mComments, mImages;   // TODO encapsulate into DataCache class to allow inheritance
    protected ArrayList<PostComment> mPostComments;

    /**
     * Empty constructor called by child classes (ie. ProfilePostPresenter) to allow them to create
     * their own View and Interactor objects
     */
    public PostCommentPresenter() {
    }

    protected PostComment getPostCommentByIndex(int i) {
        if (mPostComments == null)
            return null;
        return mPostComments.get(i);
    }

    PostCommentPresenter(@NonNull PostCommentContract.View viewInterface){

        setView(viewInterface);
        setInteractor(new PostCommentInteractor(this));

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
    public void loadDataFromDatabase(NetworkUtils network, String currentUser) {
        mPostCommentInteractor.getUserCommentsAndImages(network, currentUser);
    }

    public void setData(JSONArray commentsResponse, JSONArray imagesResponse) {
        mComments = commentsResponse;
        mImages = imagesResponse;
        mPostComments = new ArrayList<>();

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
            PostComment postComment = new PostComment(jsonComment, jsonImage);
            mPostComments.add(postComment);
        }

        mAdapter.refreshAdapter();
        // TODO if adapter has not been set - wait until it has been set and call refreshAdapter?
    }

    /**
     * Description: changes upvote like state depending on the current state
     * @param viewHolderInterface
     * @throws JSONException
     */
    public void changeUpvoteLikeState(NetworkUtils network, PostCommentContract.View.ViewHolder viewHolderInterface, int i) throws JSONException{
        PostComment postComment = getPostCommentByIndex(i);

        if (getPostCommentByIndex(i) != null) {
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
                    mPostCommentInteractor.updateLikes(network, NEUTRAL, String.valueOf(likeCount), activityid, posterid, i, viewHolderInterface);
                    break;
                case DOWNVOTE: //if it's downvoted, it'll be set to upvote state
                    likeCount += 2;
                    updateLikeState(viewHolderInterface, i, UPVOTE);
                    updateLikeCount(viewHolderInterface, i, String.valueOf(likeCount));
                    mPostCommentInteractor.updateLikes(network, UPVOTE, String.valueOf(likeCount), activityid, posterid, i, viewHolderInterface);
                    break;
                case NEUTRAL: //if it's neutral, it'll be set to upvote state
                    likeCount += 1;
                    updateLikeState(viewHolderInterface, i, UPVOTE);
                    updateLikeCount(viewHolderInterface, i, String.valueOf(likeCount));
                    if (likeCount == 1)
                        mPostCommentInteractor.insertLikes(network, UPVOTE, activityid, posterid, i, viewHolderInterface); //will insert the like for the first time
                    else
                        mPostCommentInteractor.updateLikes(network, UPVOTE, String.valueOf(likeCount), activityid, posterid, i, viewHolderInterface);
                    break;
                default: //default will be upvote state, if liketype is null
                    likeCount += 1;
                    updateLikeState(viewHolderInterface, i, UPVOTE);
                    updateLikeCount(viewHolderInterface, i, String.valueOf(likeCount));
                    mPostCommentInteractor.insertLikes(network, UPVOTE, activityid, posterid, i, viewHolderInterface); //will insert the like for the first time
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
    public void changeDownvoteLikeState(NetworkUtils network, PostCommentContract.View.ViewHolder viewHolderInterface, int i) throws JSONException {
        PostComment postComment = getPostCommentByIndex(i);

        if (getPostCommentByIndex(i) != null) {
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
                    mPostCommentInteractor.updateLikes(network, DOWNVOTE, String.valueOf(likeCount), activityid, posterid, i, viewHolderInterface);
                    break;
                case DOWNVOTE: //if it's downvoted, it'll be set to neutral state
                    likeCount += 1;
                    updateLikeState(viewHolderInterface, i, NEUTRAL);
                    updateLikeCount(viewHolderInterface, i, String.valueOf(likeCount));
                    mPostCommentInteractor.updateLikes(network, NEUTRAL, String.valueOf(likeCount), activityid, posterid, i, viewHolderInterface);
                    break;
                case NEUTRAL: //if it's neutral state, it'll be set to downvote state
                    likeCount -= 1;
                    updateLikeState(viewHolderInterface, i, DOWNVOTE);
                    updateLikeCount(viewHolderInterface, i, String.valueOf(likeCount));
                    if (likeCount == -1)
                        mPostCommentInteractor.insertLikes(network, DOWNVOTE, activityid, posterid, i, viewHolderInterface); //will insert the downvote for the first time
                    else
                        mPostCommentInteractor.updateLikes(network, DOWNVOTE, String.valueOf(likeCount), activityid, posterid, i, viewHolderInterface);
                    break;
                default: //default will be downvote state, if liketype is null
                    likeCount -= 1;
                    updateLikeState(viewHolderInterface, i, DOWNVOTE);
                    updateLikeCount(viewHolderInterface, i, String.valueOf(likeCount));
                    mPostCommentInteractor.insertLikes(network, DOWNVOTE, activityid, posterid, i, viewHolderInterface); //will insert the downvote for the first time
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
    public void updateLikeCount(PostCommentContract.View.ViewHolder viewHolderInterface, int i, String likeCount) {
        if (getPostCommentByIndex(i) != null) {
            getPostCommentByIndex(i).setLikeCount(likeCount);
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
    public void updateLikeState(PostCommentContract.View.ViewHolder viewHolderInterface, int i, LikeState likeState) {
        if (getPostCommentByIndex(i) != null) {
            getPostCommentByIndex(i).setLikeState(likeState);
            viewHolderInterface.setLikeState(likeState);
        }
    }

    public void onBindViewHolderAtPosition(PostCommentContract.View.ViewHolder viewHolderInterface, int i) {
        PostComment postComment = getPostCommentByIndex(i);

        if (postComment != null) {
            viewHolderInterface.setDate(postComment.getDate())
                    .setLikeCount(postComment.getLikeCount())
                    .setPostText(postComment.getPostText())
                    .setUserImageFromString(postComment.getProfileImageString())
                    .setUserName(postComment.getValidFullName())
                    .setLikeState(postComment.getLikeState());
        }
    }

    /**
     * Gets the item Count of the comments JSONArray
     * @return the length
     */
    @Override
    public int getItemCount() {
        if (mComments == null)
            return 0;
        return mComments.length();
    }

    @Override
    public String getPosterIdByIndex(int i) {
        return getPostCommentByIndex(i).getPosterId();
    }
}
