package ca.gc.inspection.scoop.profilecomment;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import ca.gc.inspection.scoop.MySingleton;

import static ca.gc.inspection.scoop.profilecomment.LikeState.DOWNVOTE;
import static ca.gc.inspection.scoop.profilecomment.LikeState.NEUTRAL;
import static ca.gc.inspection.scoop.profilecomment.LikeState.UPVOTE;
import static com.google.gson.internal.$Gson$Preconditions.checkNotNull;

/**
 * Presenter for the replying to a post action; it is the most generic presenter
 * related to "posting" actions. Parent presenter for ProfilePostPresenter.
 */

public class ProfileCommentPresenter implements
        ProfileCommentContract.Presenter,
        ProfileCommentContract.Presenter.AdapterAPI {

    @NonNull
    private ProfileCommentContract.View mProfileCommentView;
    private ProfileCommentContract.View.Adapter mAdapter;
    private ProfileCommentInteractor mProfileCommentInteractor;
    protected JSONArray mComments, mImages;   // TODO encapsulate into DataCache class to allow inheritance
    protected ArrayList<ProfileComment> mProfileComments;

    /**
     * Empty constructor called by child classes (ie. ProfilePostPresenter) to allow them to create
     * their own View and Interactor objects
     */
    public ProfileCommentPresenter() {
    }

    protected ProfileComment getProfileCommentByIndex(int i) {
        if (mProfileComments == null)
            return null;
        return mProfileComments.get(i);
    }

    ProfileCommentPresenter(@NonNull ProfileCommentContract.View viewInterface){

        mProfileCommentView = checkNotNull(viewInterface);
        mProfileCommentInteractor = new ProfileCommentInteractor(this);

    }

    @Override
    public void setAdapter(ProfileCommentContract.View.Adapter adapter) {
        mAdapter = adapter;
    }

    @Override
    public void loadDataFromDatabase(MySingleton instance, String currentUser) {
        mProfileCommentInteractor.getUserCommentsAndImages(instance, currentUser);
    }

    public void setData(JSONArray commentsResponse, JSONArray imagesResponse) {
        mComments = commentsResponse;
        mImages = imagesResponse;
        mProfileComments = new ArrayList<>();

        if ((commentsResponse.length() != imagesResponse.length()))
            throw new AssertionError("Error: length of commentsReponse != imagesResponse");

        for (int i=0; i<commentsResponse.length(); i++) {
            try {
                JSONObject jsonComment = mComments.getJSONObject(i);
                JSONObject jsonImage = mImages.getJSONObject(i);
                ProfileComment profileComment = new ProfileComment(jsonComment, jsonImage);
                mProfileComments.add(profileComment);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        mAdapter.refreshAdapter();
        // TODO if adapter has not been set - wait until it has been set and call refreshAdapter?
    }

    /**
     * Description: changes upvote like state depending on the current state
     * @param viewHolderInterface
     * @throws JSONException
     */
    public void changeUpvoteLikeState(MySingleton singleton, ProfileCommentContract.View.ViewHolder viewHolderInterface, int i) throws JSONException{
        ProfileComment profileComment = getProfileCommentByIndex(i);
        String activityid = profileComment.getActivityId();
        String posterid = profileComment.getPosterId();
        LikeState likeState = profileComment.getLikeState();
        int likeCount = Integer.parseInt(profileComment.getLikeCount());
        Log.i("liketype1", String.valueOf(likeState));

        switch(likeState){
            case UPVOTE: //if it's already liked, it'll be set to neutral if pressed
                likeCount -= 1;
                viewHolderInterface.setLikeState(NEUTRAL);
                mProfileCommentInteractor.updateLikes(singleton, NEUTRAL, String.valueOf(likeCount), activityid, posterid, i, viewHolderInterface);
                break;
            case DOWNVOTE: //if it's downvoted, it'll be set to upvote state
                likeCount += 2;
                viewHolderInterface.setLikeState(UPVOTE);
                mProfileCommentInteractor.updateLikes(singleton, UPVOTE, String.valueOf(likeCount), activityid, posterid, i, viewHolderInterface);
                break;
            case NEUTRAL: //if it's neutral, it'll be set to upvote state
                likeCount += 1;
                viewHolderInterface.setLikeState(UPVOTE);
                mProfileCommentInteractor.updateLikes(singleton, UPVOTE, String.valueOf(likeCount), activityid, posterid, i, viewHolderInterface);
                break;
            default: //default will be upvote state, if liketype is null
                likeCount += 1;
                viewHolderInterface.setLikeState(UPVOTE);
                mProfileCommentInteractor.insertLikes(singleton, UPVOTE, activityid, posterid, i, viewHolderInterface); //will insert the like for the first time
                break;
        }
        Log.i("likecount1", String.valueOf(likeCount));
    }

    /**
     * Description: changes downvote like state depending on the current state
     * @param viewHolderInterface
     * @throws JSONException
     */
    public void changeDownvoteLikeState(MySingleton singleton, ProfileCommentContract.View.ViewHolder viewHolderInterface, int i) throws JSONException {
        ProfileComment profileComment = getProfileCommentByIndex(i);
        String activityid = profileComment.getActivityId();
        String posterid = profileComment.getPosterId();
        LikeState likeState = profileComment.getLikeState();
        int likeCount = Integer.parseInt(profileComment.getLikeCount());

        Log.i("liketype2", String.valueOf(likeState));
        switch(likeState){
            case UPVOTE: //if it's liked, it'll be set to downvote state
                likeCount -= 2;
                viewHolderInterface.setLikeState(DOWNVOTE);
                mProfileCommentInteractor.updateLikes(singleton, DOWNVOTE, String.valueOf(likeCount), activityid, posterid, i, viewHolderInterface);
                break;
            case DOWNVOTE: //if it's downvoted, it'll be set to neutral state
                likeCount += 1;
                viewHolderInterface.setLikeState(NEUTRAL);
                mProfileCommentInteractor.updateLikes(singleton, NEUTRAL, String.valueOf(likeCount), activityid, posterid, i, viewHolderInterface);
                break;
            case NEUTRAL: //if it's netural state, it'll be set to downvote state
                likeCount -= 1;
                viewHolderInterface.setLikeState(DOWNVOTE);
                mProfileCommentInteractor.updateLikes(singleton, DOWNVOTE, String.valueOf(likeCount), activityid, posterid, i, viewHolderInterface);
                break;
            default: //default will be downvote state, if liketype is null
                likeCount -= 1;
                viewHolderInterface.setLikeState(DOWNVOTE);
                mProfileCommentInteractor.insertLikes(singleton, DOWNVOTE, activityid, posterid, i, viewHolderInterface); //will insert the downvote for the first time
                break;
        }
        Log.i("likecount2", String.valueOf(likeCount));
    }

    /**
     * Description: updates the like count to new total
     *
     * @param likeCount
     * @throws JSONException
     */
    public void updateLikeCount(ProfileCommentContract.View.ViewHolder viewHolderInterface, int i, String likeCount) {
        getProfileCommentByIndex(i).setLikeCount(likeCount);
        viewHolderInterface.setLikeCount(likeCount); //sets like count to new total
    }

    public void updateLikeState(ProfileCommentContract.View.ViewHolder viewHolderInterface, int i, LikeState likeState) {
        getProfileCommentByIndex(i).setLikeState(likeState);
        viewHolderInterface.setLikeState(likeState);
    }

    public void onBindViewHolderAtPosition(ProfileCommentContract.View.ViewHolder viewHolderInterface, int i) {
        ProfileComment profileComment = getProfileCommentByIndex(i);

        if (profileComment != null) {
            viewHolderInterface.setDate(profileComment.getDate())
                    .setLikeCount(profileComment.getLikeCount())
                    .setPostText(profileComment.getPostText())
                    .setPostTitle(profileComment.getPostTitle())
                    .setUserImageFromString(profileComment.getProfileImageString())
                    .setUserName(profileComment.getValidFullName())
                    .setLikeState(profileComment.getLikeState());
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
        return getProfileCommentByIndex(i).getPosterId();
    }
}
