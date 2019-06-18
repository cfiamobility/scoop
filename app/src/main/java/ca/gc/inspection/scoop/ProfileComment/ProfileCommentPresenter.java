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
        viewHolderInterface.setPostTitle("Replying to " + profileComment.getPostFirstName() + " " + profileComment.getPostLastName() + "'s post");
    }

    /**
     * Description: main method to display a single post
     * @throws JSONException
     */
    public void displayProfileComment(ProfileCommentContract.View.ViewHolder viewHolderInterface, int i) {
        ProfileComment profileComment = mProfileComments.get(i);

        viewHolderInterface.setPostText(profileComment.getPostText());
        viewHolderInterface.formatDate(profileComment.getDate());
        viewHolderInterface.setUserName(getValidFullName(profileComment));
        viewHolderInterface.setLikeState(profileComment.getLikeState());

        displayProfileCommentImages(viewHolderInterface, profileComment);
        displayProfileCommentTitle(viewHolderInterface, profileComment);
    }

    /**
     * Description: checks to see if full name is valid before setting name
     * @throws JSONException
     */
    public String getValidFullName(ProfileComment profileComment) {
        return getValidFirstName(profileComment.getFirstName()) + getValidLastName(profileComment.getLastName());
    }

    /**
     * Description: checks the first name to see if it is valid
     *
     * @param firstName: the first name of user
     * @return first name if not null
     */
    public String getValidFirstName(String firstName) {
        if (!firstName.equals("null")) { //if there is a first name returned
            return firstName;
        } else { //if the first name is not filled
            return "";
        }
    }

    /**
     * Description: checks last name to see if it is valid
     *
     * @param lastName: the last name of user
     * @return last name if not null
     */
    public String getValidLastName(String lastName) {
        if (!lastName.equals("null")) { //if there is a last name returned
            return " " + lastName;
        } else { //if the last name is not filled
            return "";
        }
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
        ProfileComment profileComment = mProfileComments.get(i);
        String activityid = profileComment.getActivityId();
        String posterid = profileComment.getPosterId();
        LikeState likeState = profileComment.getLikeState();
        int likeCount = Integer.parseInt(profileComment.getLikeCount());
        Log.i("liketype1", String.valueOf(likeState));

        switch(likeState){
            case UPVOTE: //if it's already liked, it'll be set to neutral if pressed
                likeCount -= 1;
                viewHolderInterface.setLikeState(NEUTRAL);
                mProfileCommentInteractor.updateLikes(singleton, NEUTRAL, activityid, posterid, i, viewHolderInterface);
                break;
            case DOWNVOTE: //if it's downvoted, it'll be set to upvote state
                likeCount += 2;
                viewHolderInterface.setLikeState(UPVOTE);
                mProfileCommentInteractor.updateLikes(singleton, UPVOTE, activityid, posterid, i, viewHolderInterface);
                break;
            case NEUTRAL: //if it's neutral, it'll be set to upvote state
                likeCount += 1;
                viewHolderInterface.setLikeState(UPVOTE);
                mProfileCommentInteractor.updateLikes(singleton, UPVOTE, activityid, posterid, i, viewHolderInterface);
                break;
            default: //default will be upvote state, if liketype is null
                likeCount += 1;
                viewHolderInterface.setLikeState(UPVOTE);
                mProfileCommentInteractor.insertLikes(singleton, UPVOTE, activityid, posterid, i, viewHolderInterface); //will insert the like for the first time
                break;
        }
        Log.i("likecount1", String.valueOf(likeCount));
        updateLikeCount(String.valueOf(likeCount)); //updates like count to new total
    }

    /**
     * Description: changes downvote like state depending on the current state
     * @param viewHolderInterface
     * @throws JSONException
     */
    public void changeDownvoteLikeState(MySingleton singleton, ProfileCommentContract.View.ViewHolder viewHolderInterface, int i) throws JSONException {
        ProfileComment profileComment = mProfileComments.get(i);
        String activityid = profileComment.getActivityId();
        String posterid = profileComment.getPosterId();
        LikeState likeState = profileComment.getLikeState();
        int likeCount = Integer.parseInt(profileComment.getLikeCount());

        Log.i("liketype2", String.valueOf(likeState));
        switch(likeState){
            case UPVOTE: //if it's liked, it'll be set to downvote state
                likeCount -= 2;
                viewHolderInterface.setLikeState(DOWNVOTE);
                mProfileCommentInteractor.updateLikes(singleton, DOWNVOTE, activityid, posterid, i, viewHolderInterface);
                break;
            case DOWNVOTE: //if it's downvoted, it'll be set to neutral state
                likeCount += 1;
                viewHolderInterface.setLikeState(NEUTRAL);
                mProfileCommentInteractor.updateLikes(singleton, NEUTRAL, activityid, posterid, i, viewHolderInterface);
                break;
            case NEUTRAL: //if it's netural state, it'll be set to downvote state
                likeCount -= 1;
                viewHolderInterface.setLikeState(DOWNVOTE);
                mProfileCommentInteractor.updateLikes(singleton, DOWNVOTE, activityid, posterid, i, viewHolderInterface);
                break;
            default: //default will be downvote state, if liketype is null
                likeCount -= 1;
                viewHolderInterface.setLikeState(DOWNVOTE);
                mProfileCommentInteractor.insertLikes(singleton, DOWNVOTE, activityid, posterid, i, viewHolderInterface); //will insert the downvote for the first time
                break;
        }
        Log.i("likecount2", String.valueOf(likeCount));
        updateLikeCount(String.valueOf(likeCount)); //updates the likecount to new total
    }

    /**
     * Description: updates the like count to new total
     *
     * @param profileComment
     * @param likeCount
     * @throws JSONException
     */
    public void updateLikeCount(ProfileCommentContract.View.ViewHolder viewHolderInterface, int i, int likeCount)
            throws JSONException {
        mProfileComments.get(i).setLikeCount(likeCount);
        viewHolderInterface.setLikeCount(likeCount); //sets like count to new total
    }

    public void updateLikeType(ProfileCommentContract.View.ViewHolder viewHolderInterface, int i)
        throws JSONException {

    }

    public void onBindProfileCommentViewHolderAtPosition(ProfileCommentContract.View.ViewHolder viewHolderInterface, int i) {
        ProfileComment profileComment = mProfileComments.get(i);

        viewHolderInterface.setDate(profileComment.getDate())
                .setLikeCount(profileComment.getLikeCount())
                .setPostText(profileComment.getPostText())
                .setPostTitle(profileComment.getPostTitle())
                .setUserImageFromString(profileComment.getProfileImageString())
                .setUserName(getValidFullName(profileComment))  // TODO move getvalidfullname method to profilecomment?
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
}
