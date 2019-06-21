package ca.gc.inspection.scoop.profilecomment;

import android.util.Log;

import org.json.JSONObject;

import static ca.gc.inspection.scoop.Config.USERID_KEY;
import static ca.gc.inspection.scoop.profilecomment.LikeState.NULL;

public class ProfileComment {
    /**
     * Data class which stores information for a single profile comment.
     * Should only interact with the Presenter as this class is a helper data class.
     * - Not an inner class of Presenter to simplify inheritance.
     *
     * The data is stored in the Json format provided by the database.
     * Static string keys are used to access the relevant values in the Json objects
     *
     * Warning: accessing missing values in the Json could retrieve a string spelling out "null" instead
     * of the empty string
     *
     * TODO: use mComment.isNull(key) for null checking
     */
    protected JSONObject mComment, mImage;

    public static final String PROFILE_COMMENT_ACTIVITYID_KEY = "activityid";
    /*  Disambiguation: PosterId vs UserId
        1. "userid" is the UserId (Json key) of the comment poster.
        2. Only in the context of likes/dislikes do we deal with "posterid" and "userid"
            - "userid" is now the UserId (Json key) of the person liking/disliking a comment/post.
            - "posterid" is the UserId (Json key) of the user who created the comment/post which was liked/disliked.
        3. Java methods such as getPosterId mainly refers to the comment poster, which can be accessed
            using the "userid" Json key
            - Only ProfileCommentInteractor uses the "posterid" Json key when handling likes/dislikes

        TODO: make the meaning of posterId and userId more consistent
     */
    public static final String PROFILE_COMMENT_LIKE_POSTERID_KEY = "posterid";
    public static final String PROFILE_COMMENT_DATE_KEY = "createddate";
    public static final String PROFILE_COMMENT_PROFILE_IMAGE_KEY = "profileimage";
    public static final String PROFILE_COMMENT_POST_FIRST_NAME_KEY = "postfirstname";
    public static final String PROFILE_COMMENT_POST_LAST_NAME_KEY = "postlastname";
    public static final String PROFILE_COMMENT_FIRST_NAME_KEY = "firstname";
    public static final String PROFILE_COMMENT_LAST_NAME_KEY = "lastname";
    public static final String PROFILE_COMMENT_LIKE_TYPE_KEY = "liketype";
    public static final String PROFILE_COMMENT_LIKE_COUNT_KEY = "likecount";
    public static final String PROFILE_COMMENT_POST_TEXT_KEY = "posttext";

    /**
     * Constructor for the data object.
     * Should only be instantiated by the Presenter.
     *
     * @param jsonComment       Contains json data of the comment data from the database
     * @param jsonImage         Contains json data of the profile image from the database
     */
    protected ProfileComment(JSONObject jsonComment, JSONObject jsonImage) {
        mComment = jsonComment;
        mImage = jsonImage;
    }

    /**
     * ActivityId used as a primary key in database tables
     * @return activityId string
     */
    public String getActivityId() {
        try {
            return mComment.getString(PROFILE_COMMENT_ACTIVITYID_KEY);
        }
        catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    /**
     * (*) See above for disambiguation of userId and posterId
     * Get the UserId of the comment poster
     * @return UserId string if it exists, otherwise return an empty string
     */
    public String getPosterId() {
        try {
            return mComment.getString(USERID_KEY);
        }
        catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    /**
     * @return date string
     */
    public String getDate() {
        try {
            return mComment.getString(PROFILE_COMMENT_DATE_KEY);
        }
        catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public String getFirstName() {
        try {
            return mComment.getString(PROFILE_COMMENT_FIRST_NAME_KEY);
        }
        catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public String getLastName() {
        try {
            return mComment.getString(PROFILE_COMMENT_LAST_NAME_KEY);
        }
        catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public String getPostFirstName() {
        try {
            return mComment.getString(PROFILE_COMMENT_POST_FIRST_NAME_KEY);
        }
        catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public String getPostLastName() {
        try {
            return mComment.getString(PROFILE_COMMENT_POST_LAST_NAME_KEY);
        }
        catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public String getPostText() {
        try {
            return mComment.getString(PROFILE_COMMENT_POST_TEXT_KEY);
        }
        catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public String getProfileImageString() {
        try {
            return mImage.getString(PROFILE_COMMENT_PROFILE_IMAGE_KEY);
        }
        catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public String getLikeCount() {
        String likeCount = "";
        try {
            likeCount = mComment.getString(PROFILE_COMMENT_LIKE_COUNT_KEY);
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        Log.d("Get like count: ", likeCount);
        if (likeCount != null && !likeCount.isEmpty() && !likeCount.equals("null"))
            return likeCount;
        else return "0";
    }

    public LikeState getLikeState() {
        String likeTypeString = "";
        try {
            likeTypeString = mComment.getString(PROFILE_COMMENT_LIKE_TYPE_KEY);
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        if (likeTypeString != null && !likeTypeString.isEmpty() && !likeTypeString.equals("null")) {
            Log.d("get like type: ", likeTypeString);
            return LikeState.getLikeStateFrom(likeTypeString);
        }
        else return NULL;
    }

    public void setLikeCount(String likeCount) {
        try {
            mComment.put(PROFILE_COMMENT_LIKE_COUNT_KEY, likeCount);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setLikeState(LikeState likeState) {
        try {
            mComment.put(PROFILE_COMMENT_LIKE_TYPE_KEY, likeState.getDatabaseValue());
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Returns the post title for comments - this method is overridden in ProfilePost.
     * Hardcoded string values can be replaced with strings xml.
     * @return post title string
     */
    public String getPostTitle() {
        return "Replying to " + getPostFirstName() + " " + getPostLastName() + "'s post";
    }

    /**
     * Description: returns a valid full name format.
     * Add spacing only if both first and last names are non empty
     */
    public String getValidFullName() {
        if (!getFirstName().equals("") && !getLastName().equals(""))
            return getFirstName() + " " + getLastName();
        return getFirstName() + getLastName();
    }
}
