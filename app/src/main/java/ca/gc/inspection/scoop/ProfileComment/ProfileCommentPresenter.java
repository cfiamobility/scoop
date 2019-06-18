package ca.gc.inspection.scoop.ProfileComment;

import android.support.annotation.NonNull;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import ca.gc.inspection.scoop.MySingleton;

import static ca.gc.inspection.scoop.ProfileComment.LikeState.DOWNVOTE;
import static ca.gc.inspection.scoop.ProfileComment.LikeState.NEUTRAL;
import static ca.gc.inspection.scoop.ProfileComment.LikeState.UPVOTE;
import static com.google.gson.internal.$Gson$Preconditions.checkNotNull;

/**
 * Presenter for the replying to a post action; it is the most generic presenter
 * related to "posting" actions. Parent presenter for ProfilePostPresenter.
 */

public class ProfileCommentPresenter implements
        ProfileCommentContract.Presenter,
        ProfileCommentContract.Presenter.AdapterAPI,
        ProfileCommentContract.Presenter.ViewHolderAPI {

    @NonNull
    private ProfileCommentContract.View mProfileCommentView;
    private ProfileCommentInteractor mProfileCommentInteractor;
    private JSONArray mComments, mImages;
    private ArrayList<ProfileComment> mProfileComments;

    private ProfileComment getProfileCommentByIndex(int i) {
        return mProfileComments.get(i);
    }

    public ProfileCommentPresenter(@NonNull ProfileCommentContract.View viewInterface){

        mProfileCommentView = checkNotNull(viewInterface);
        mProfileCommentInteractor = new ProfileCommentInteractor(this);

    }

    public void displayProfileCommentImages(ProfileCommentContract.View.ViewHolder viewHolderInterface, ProfileComment profileComment) {
        if(profileComment.getProfileImageString() != null) { // null check to see if there are images
            viewHolderInterface.setUserImageFromString(profileComment.getProfileImageString()); //formats profile image
        }
    }

    public void displayProfileCommentTitle(ProfileCommentContract.View.ViewHolder viewHolderInterface, ProfileComment profileComment) {
        viewHolderInterface.setPostTitle(profileComment.getPostTitle());
    }

    /**
     * Description: main method to display a single post
     * @throws JSONException
     */
    public void displayProfileComment(ProfileCommentContract.View.ViewHolder viewHolderInterface, int i) {
        ProfileComment profileComment = getProfileCommentByIndex(i);

        viewHolderInterface.setPostText(profileComment.getPostText());
        viewHolderInterface.formatDate(profileComment.getDate());
        viewHolderInterface.setUserName(profileComment.getValidFullName());
        viewHolderInterface.setLikeState(profileComment.getLikeState());
        viewHolderInterface.setLikeCount(profileComment.getLikeCount());

        displayProfileCommentImages(viewHolderInterface, profileComment);
        displayProfileCommentTitle(viewHolderInterface, profileComment);
    }

    @Override
    public void loadUserCommentsAndImages(MySingleton instance, String currentUser) {
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

    public void onBindProfileCommentViewHolderAtPosition(ProfileCommentContract.View.ViewHolder viewHolderInterface, int i) {
        ProfileComment profileComment = getProfileCommentByIndex(i);

        viewHolderInterface.setDate(profileComment.getDate())
                .setLikeCount(profileComment.getLikeCount())
                .setPostText(profileComment.getPostText())
                .setPostTitle(profileComment.getPostTitle())
                .setUserImageFromString(profileComment.getProfileImageString())
                .setUserName(profileComment.getValidFullName())
                .setLikeState(profileComment.getLikeState());

        displayProfileComment(viewHolderInterface, i);
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
