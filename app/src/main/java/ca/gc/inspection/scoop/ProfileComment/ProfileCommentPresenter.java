package ca.gc.inspection.scoop.ProfileComment;

import android.support.annotation.NonNull;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import ca.gc.inspection.scoop.MySingleton;

import static ca.gc.inspection.scoop.ProfileComment.LikeState.DOWNVOTE;
import static ca.gc.inspection.scoop.ProfileComment.LikeState.NEUTRAL;
import static ca.gc.inspection.scoop.ProfileComment.LikeState.UPVOTE;
import static com.google.gson.internal.$Gson$Preconditions.checkNotNull;

/**
 * Presenter for the replying to a post action; it is the most generic presenter
 * related to "posting" actions. Parent presenter for ProfilePostPresenter.
 */

public class ProfileCommentPresenter implements ProfileCommentContract.Presenter {
    // TODO: like properties for each post
    private Map<String, String> likeProperties;
    private ArrayList<ProfileComment> mProfileComments;

    @NonNull
    private ProfileCommentContract.View mProfileCommentView;
    private ProfileCommentInteractor mProfileCommentInteractor;
    private JSONArray mComments, mImages;

    public ProfileCommentPresenter(@NonNull ProfileCommentContract.View viewInterface){
        mProfileCommentView = checkNotNull(viewInterface);

        mProfileCommentInteractor = new ProfileCommentInteractor(this);

        likeProperties = new HashMap<>(); //map of liketype and likecount of specified post
    }
    /**
     *
     * @throws JSONException
     */
    public void displayProfileCommentImages(ProfileCommentContract.View.ViewHolder viewHolderInterface, ProfileComment profileComment) {
        if(profileComment.getProfileImage() != null) { // null check to see if there are images
            viewHolderInterface.formatImage(profileComment.getProfileImage()); //formats profile image
        }
        mProfileCommentView.setDisplayImagesListener(viewHolderInterface);
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
        final String activityid = profileComment.getActivityId();
        final String posterid = profileComment.getUserId();
        likeProperties.put(PROFILE_COMMENT_POST_LIKE_TYPE_KEY, profileComment.getLikeState(PROFILE_COMMENT_POST_LIKE_TYPE_KEY)); //puts liketype into properties map
        likeProperties.put(PROFILE_COMMENT_POST_LIKE_COUNT_KEY, checkLikeCount(profileComment.getLikeCount())); //puts likecount into properties map
        viewHolderInterface.setPostText(profileComment.getPostText());
        viewHolderInterface.formatDate(profileComment.getDate());
        viewHolderInterface.setUserName(getValidFullName(profileComment));
        checkLikeState(likeProperties.get(PROFILE_COMMENT_POST_LIKE_TYPE_KEY));
        mProfileCommentView.setDisplayPostListener(viewHolderInterface, activityid, posterid);

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

    /**
     * Description: initial setting of likeCount and checks if likeCount is null
     * @param likeCount: the number of likes on a post
     * @return the proper like count
     */
    public String checkLikeCount(String likeCount){
        // TODO refactor
        String defaultCount = "0";
        if(likeCount.equals("null")){
            mProfileCommentView.setLikeCount(defaultCount, holder);
            return defaultCount;
        }else{
            mProfileCommentView.setLikeCount(likeCount, holder);
            return likeCount;
        }
    }

    @Override
    public void loadUserCommentsAndImages(MySingleton instance, String currentUser) {
        mProfileCommentInteractor.getUserCommentsAndImages(instance, currentUser);
    }

    public void setData(JSONArray commentsResponse, JSONArray imagesResponse) {
        mComments = commentsResponse;
        mImages = imagesResponse;

        // TODO: parse JSON comments/images data and store in List of ProfileComment data objects
//        try {
//            this.post = posts.getJSONObject(i);
//            this.profileImage = profileImages.getJSONObject(i);
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
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
     * @param activityid : activity id of the post
     * @param posterid : user who posted the post
     * @param viewHolderInterface
     * @throws JSONException
     */
    public void changeUpvoteLikeState(MySingleton singleton, String activityid, String posterid, ProfileCommentContract.View.ViewHolder viewHolderInterface) throws JSONException{
        // TODO refactor
        String likeType = likeProperties.get(PROFILE_COMMENT_POST_LIKE_TYPE_KEY); //gets current like type
        int likeCount = Integer.parseInt(likeProperties.get(PROFILE_COMMENT_POST_LIKE_COUNT_KEY)); //gets current like count
        Log.i("liketype1", String.valueOf(likeType));
        switch(likeType){
            case "1": //if it's already liked, it'll be set to neutral if pressed
                likeCount -= 1;
                viewHolderInterface.setLikeState(NEUTRAL);
                mProfileCommentInteractor.updateLikes(singleton, "0", activityid, posterid, post, likeProperties);
                break;
            case "-1": //if it's downvoted, it'll be set to upvote state
                likeCount += 2;
                viewHolderInterface.setLikeState(UPVOTE);
                mProfileCommentInteractor.updateLikes(singleton, "1", activityid, posterid, post, likeProperties);
                break;
            case "0": //if it's neutral, it'll be set to upvote state
                likeCount += 1;
                viewHolderInterface.setLikeState(UPVOTE);
                mProfileCommentInteractor.updateLikes(singleton, "1", activityid, posterid, post, likeProperties);
                break;
            default: //default will be upvote state, if liketype is null
                likeCount += 1;
                viewHolderInterface.setLikeState(UPVOTE);
                mProfileCommentInteractor.insertLikes(singleton, "1", activityid, posterid, post, likeProperties); //will insert the like for the first time
                break;
        }
        Log.i("likecount1", String.valueOf(likeCount));
        updateLikeCount(String.valueOf(likeCount)); //updates like count to new total
    }

    /**
     * Description: changes downvote like state depending on the current state
     * @param activityid : activity id of the post
     * @param posterid : poster id of the post
     * @param viewHolderInterface
     * @throws JSONException
     */
    public void changeDownvoteLikeState(MySingleton singleton, String activityid, String posterid, ProfileCommentContract.View.ViewHolder viewHolderInterface) throws JSONException {
        // TODO refactor
        String likeType = likeProperties.get(PROFILE_COMMENT_POST_LIKE_TYPE_KEY); //gets current like type
        int likeCount = Integer.parseInt(likeProperties.get(PROFILE_COMMENT_POST_LIKE_COUNT_KEY)); //gets current like count
        Log.i("liketype2", String.valueOf(likeType));
        switch(likeType){
            case "1": //if it's liked, it'll be set to downvote state
                likeCount -= 2;
                viewHolderInterface.setLikeState(DOWNVOTE);
                mProfileCommentInteractor.updateLikes(singleton, "-1", activityid, posterid, post, likeProperties);
                break;
            case "-1": //if it's downvoted, it'll be set to neutral state
                likeCount += 1;
                viewHolderInterface.setLikeState(NEUTRAL);
                mProfileCommentInteractor.updateLikes(singleton, "0", activityid, posterid, post, likeProperties);
                break;
            case "0": //if it's netural state, it'll be set to downvote state
                likeCount -= 1;
                viewHolderInterface.setLikeState(DOWNVOTE);
                mProfileCommentInteractor.updateLikes(singleton, "-1", activityid, posterid, post, likeProperties);
                break;
            default: //default will be downvote state, if liketype is null
                likeCount -= 1;
                viewHolderInterface.setLikeState(DOWNVOTE);
                mProfileCommentInteractor.insertLikes(singleton, "-1", activityid, posterid, post, likeProperties); //will insert the downvote for the first time
                break;
        }
        Log.i("likecount2", String.valueOf(likeCount));
        updateLikeCount(String.valueOf(likeCount)); //updates the likecount to new total
    }

    /**
     * Description: updates the like count to new total
     * @param likeCount
     * @throws JSONException
     */
    public void updateLikeCount(String likeCount) throws JSONException {
        // TODO refactor
        likeProperties.put(PROFILE_COMMENT_POST_LIKE_COUNT_KEY, likeCount); //updates in map
        post.put(PROFILE_COMMENT_POST_LIKE_COUNT_KEY, likeCount); //updates in post object
        mProfileCommentView.setLikeCount(likeCount,holder); //sets like count to new total
    }

    public void onBindProfileCommentViewHolderAtPosition(ProfileCommentContract.View.ViewHolder viewHolderInterface, int i) {
        ProfileComment profileComment = mProfileComments.get(i);

        viewHolderInterface.setDate(profileComment.getDate())
                .setLikeCount(profileComment.getLikeCount())
                .setPostText(profileComment.getPostText())
                .setPostTitle(profileComment.getPostTitle())
                .setUserImage(profileComment.getProfileImage())
                .setUserName(profileComment.getUserName())
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
