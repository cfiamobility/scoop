package ca.gc.inspection.scoop.ProfileComment;

import android.support.annotation.NonNull;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ca.gc.inspection.scoop.MySingleton;
import static com.google.gson.internal.$Gson$Preconditions.checkNotNull;

/**
 * Presenter for the replying to a post action; it is the most generic presenter
 * related to "posting" actions. Parent presenter for ProfilePostPresenter.
 */

public class ProfileCommentPresenter implements ProfileCommentContract.Presenter {
    private JSONObject post, profileImage;
    private Map<String, String> likeProperties;
    // TODO presenter stores all the adapter data
    private List<ProfileComment> profileComments;

    @NonNull
    private ProfileCommentContract.View mProfileCommentView;
    private ProfileCommentInteractor mProfileCommentInteractor;
    private JSONArray mComments, mImages;


    public ProfileCommentPresenter(@NonNull ProfileCommentContract.View profileCommentView){
        mProfileCommentView = checkNotNull(profileCommentView);
        try {
            this.post = posts.getJSONObject(i);
            this.profileImage = profileImages.getJSONObject(i);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        likeProperties = new HashMap<>(); //map of liketype and likecount of specified post

        mProfileCommentInteractor = new ProfileCommentInteractor(this);
    }
    /**
     *
     * @throws JSONException
     */
    public void displayImages() throws JSONException {
        if(profileImage != null) { // null check to see if there are images
            mProfileCommentView.formatImage(profileImage.getString("profileimage"), holder); //formats profile image
        }
        mProfileCommentView.setDisplayImagesListener(holder);
    }

    /**
     * Description: main method to display a single post
     * @throws JSONException
     */
    public void displayPost() throws JSONException {
        final String activityid = post.getString("activityid");
        final String posterid = post.getString("userid");
        likeProperties.put("liketype", post.getString("liketype")); //puts liketype into properties map
        likeProperties.put("likecount", checkLikeCount(post.getString("likecount"))); //puts likecount into properties map
        mProfileCommentView.setPostText(post.getString("posttext"), holder);
        formatDate(post.getString("createddate"));
        checkFullName();
        checkLikeState(likeProperties.get("liketype"));
        mProfileCommentView.setDisplayPostListener(holder, activityid, posterid);
    }

    public void formPostTitle() throws JSONException {
        mProfileCommentView.setPostTitle("Replying to " + post.getString("postfirstname") + " " + post.getString("postlastname") + "'s post", holder);
    }

    /**
     * Description: checks to see if full name is valid before setting name
     * @throws JSONException
     */
    public void checkFullName() throws JSONException {
        String fullName = checkFirstName(post.getString("firstname")) + checkLastName(post.getString("lastname"));
        mProfileCommentView.setUserName(fullName, holder);
    }

    /**
     * Description: checks the first name to see if it is valid
     *
     * @param firstName: the first name of user
     * @return first name if not null
     */
    public String checkFirstName(String firstName) {
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
    public String checkLastName(String lastName) {
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
        String defaultCount = "0";
        if(likeCount.equals("null")){
            mProfileCommentView.setLikeCount(defaultCount, holder);
            return defaultCount;
        }else{
            mProfileCommentView.setLikeCount(likeCount, holder);
            return likeCount;
        }
    }

    /**
     * Description: checks likeType currently on the post and sets upvote and downvote buttons accordingly
     * @param likeState: like type of post
     */
    public void checkLikeState(LikeState likeState){
        Log.i("likestate", likeState.toString());

        switch(likeState){
            case UPVOTE: mProfileCommentView.setLikeUpvoteState(holder);
                break;
            case DOWNVOTE: mProfileCommentView.setLikeDownvoteState(holder);
                break;
            case NEUTRAL: mProfileCommentView.setLikeNeutralState(holder);
                break;
            default: mProfileCommentView.setLikeNeutralState(holder);
                break;
        }
    }

    @Override
    public void loadUserCommentsAndImages(MySingleton instance, String currentUser) {
        mProfileCommentInteractor.getUserCommentsAndImages(instance, currentUser);
    }

    public void setData(JSONArray commentsResponse, JSONArray imagesResponse) {
        mComments = commentsResponse;
        mImages = imagesResponse;
    }

    /**
     * Description: changes upvote like state depending on the current state
     * @param activityid: activity id of the post
     * @param posterid: user who posted the post
     * @throws JSONException
     */
    public void changeUpvoteLikeState(MySingleton singleton, String activityid, String posterid) throws JSONException{
        String likeType = likeProperties.get("liketype"); //gets current like type
        int likeCount = Integer.parseInt(likeProperties.get("likecount")); //gets current like count
        Log.i("liketype1", String.valueOf(likeType));
        switch(likeType){
            case "1": //if it's already liked, it'll be set to neutral if pressed
                likeCount -= 1;
                mProfileCommentView.setLikeNeutralState(holder);
                mProfileCommentInteractor.updateLikes(singleton, "0", activityid, posterid, post, likeProperties);
                break;
            case "-1": //if it's downvoted, it'll be set to upvote state
                likeCount += 2;
                mProfileCommentView.setLikeUpvoteState(holder);
                mProfileCommentInteractor.updateLikes(singleton, "1", activityid, posterid, post, likeProperties);
                break;
            case "0": //if it's neutral, it'll be set to upvote state
                likeCount += 1;
                mProfileCommentView.setLikeUpvoteState(holder);
                mProfileCommentInteractor.updateLikes(singleton, "1", activityid, posterid, post, likeProperties);
                break;
            default: //default will be upvote state, if liketype is null
                likeCount += 1;
                mProfileCommentInteractor.insertLikes(singleton, "1", activityid, posterid, post, likeProperties); //will insert the like for the first time
                mProfileCommentView.setLikeUpvoteState(holder);
                break;
        }
        Log.i("likecount1", String.valueOf(likeCount));
        updateLikeCount(String.valueOf(likeCount)); //updates like count to new total
    }

    /**
     * Description: changes downvote like state depending on the current state
     * @param activityid: activity id of the post
     * @param posterid: poster id of the post
     * @throws JSONException
     */
    public void changeDownvoteLikeState(MySingleton singleton, String activityid, String posterid) throws JSONException {
        String likeType = likeProperties.get("liketype"); //gets current like type
        int likeCount = Integer.parseInt(likeProperties.get("likecount")); //gets current like count
        Log.i("liketype2", String.valueOf(likeType));
        switch(likeType){
            case "1": //if it's liked, it'll be set to downvote state
                likeCount -= 2;
                mProfileCommentView.setLikeDownvoteState(holder);
                mProfileCommentInteractor.updateLikes(singleton, "-1", activityid, posterid, post, likeProperties);
                break;
            case "-1": //if it's downvoted, it'll be set to neutral state
                likeCount += 1;
                mProfileCommentView.setLikeNeutralState(holder);
                mProfileCommentInteractor.updateLikes(singleton, "0", activityid, posterid, post, likeProperties);
                break;
            case "0": //if it's netural state, it'll be set to downvote state
                likeCount -= 1;
                mProfileCommentView.setLikeDownvoteState(holder);
                mProfileCommentInteractor.updateLikes(singleton, "-1", activityid, posterid, post, likeProperties);
                break;
            default: //default will be downvote state, if liketype is null
                likeCount -= 1;
                mProfileCommentInteractor.insertLikes(singleton, "-1", activityid, posterid, post, likeProperties); //will insert the downvote for the first time
                mProfileCommentView.setLikeDownvoteState(holder);
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
        likeProperties.put("likecount", likeCount); //updates in map
        post.put("likecount", likeCount); //updates in post object
        mProfileCommentView.setLikeCount(likeCount,holder); //sets like count to new total
    }

    public void onBindProfileCommentViewHolderAtPosition(ProfileCommentContract.View.ViewHolder profileCommentViewHolder, int i) {
        ProfileComment profileComment = profileComments.get(i);

        profileCommentViewHolder.setDate(profileComment.getDate())
                .setLikeCount(profileComment.getLikeCount())
                .setPostText(profileComment.getPostText())
                .setPostTitle(profileComment.getPostTitle())
                .setUserImage(profileComment.getProfileImage())
                .setUserName(profileComment.getUserName())
                .setLikeState(profileComment.getLikeState());
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
