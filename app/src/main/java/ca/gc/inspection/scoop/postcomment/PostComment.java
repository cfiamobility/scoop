package ca.gc.inspection.scoop.postcomment;

import android.util.Log;

import org.json.JSONObject;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

import ca.gc.inspection.scoop.util.TextFormat;

import static ca.gc.inspection.scoop.Config.TIME_ZONE_FORMAT;
import static ca.gc.inspection.scoop.Config.USERID_KEY;
import static ca.gc.inspection.scoop.postcomment.LikeState.NULL;

public class PostComment {
    /**
     * Data class which stores information for a single post comment.
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
    protected TextFormat mTextFormat = null;
    protected static final String MODIFIED_DATE_LABEL = "Edited: ";
    protected static final String MODIFIED_JUST_NOW = "Just now";

    public static final String PROFILE_COMMENT_ACTIVITYID_KEY = "activityid";
    /*  Disambiguation: PosterId vs UserId
        1. "userid" is the UserId (Json key) of the comment poster.
        2. Only in the context of likes/dislikes do we deal with "posterid" and "userid"
            - "userid" is now the UserId (Json key) of the person liking/disliking a comment/post.
            - "posterid" is the UserId (Json key) of the user who created the comment/post which was liked/disliked.
        3. Java methods such as getPosterId mainly refers to the comment poster, which can be accessed
            using the "userid" Json key
            - Only PostCommentInteractor uses the "posterid" Json key when handling likes/dislikes

        TODO: make the meaning of posterId and userId more consistent
     */
    public static final String PROFILE_COMMENT_LIKE_POSTERID_KEY = "posterid";
    public static final String PROFILE_COMMENT_DATE_KEY = "createddate";
    public static final String PROFILE_COMMENT_MODIFIED_DATE_KEY = "modifieddate";
    public static final String PROFILE_COMMENT_PROFILE_IMAGE_KEY = "profileimage";
    public static final String PROFILE_COMMENT_POST_FIRST_NAME_KEY = "postfirstname";
    public static final String PROFILE_COMMENT_POST_LAST_NAME_KEY = "postlastname";
    public static final String PROFILE_COMMENT_FIRST_NAME_KEY = "firstname";
    public static final String PROFILE_COMMENT_LAST_NAME_KEY = "lastname";
    public static final String PROFILE_COMMENT_LIKE_TYPE_KEY = "liketype";
    public static final String PROFILE_COMMENT_LIKE_COUNT_KEY = "likecount";
    public static final String PROFILE_COMMENT_POST_TEXT_KEY = "posttext";
    public static final String PROFILE_COMMENT_SAVED_STATE_KEY = "savedstatus";

    /**
     * Constructor for the data object.
     * Should only be instantiated by the Presenter.
     *
     * @param jsonComment       Contains json data of the comment data from the database
     * @param jsonImage         Contains json data of the profile image from the database
     */
    public PostComment(JSONObject jsonComment, JSONObject jsonImage) {
        mComment = jsonComment;
        mImage = jsonImage;
        String modifiedDatefooter = null;
        if (getModifiedDate() != null && !getModifiedDate().isEmpty() && !getModifiedDate().equals(getCreatedDate()))
            modifiedDatefooter = MODIFIED_DATE_LABEL + getFormattedModifiedDate();
        mTextFormat = new TextFormat(null, getPostText(), modifiedDatefooter);
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
    public String getCreatedDate() {
        try {
            return mComment.getString(PROFILE_COMMENT_DATE_KEY);
        }
        catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    /**
     * @return date string
     */
    public String getModifiedDate() {
        try {
            return mComment.getString(PROFILE_COMMENT_MODIFIED_DATE_KEY);
        }
        catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    /**
     * @return the modified date formatted with the time zone defined in the Config class.
     */
    public String getFormattedModifiedDate() {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(TIME_ZONE_FORMAT)
                .withZone(ZoneId.systemDefault());

        ZonedDateTime zonedModifiedDate = ZonedDateTime.parse(getModifiedDate());
        return dateTimeFormatter.format(zonedModifiedDate);
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

    /**
     * Used to update the text of a post comment in the DataCache.
     * Needed for editing a post - if the user does not pull down to refresh, the DataCache becomes stale.
     * This means that scrolling up and down the RecyclerView will cause stale data to be binded to the
     * view holders instead of the edited comment text.
     *
     * @param text  post comment text
     */
    public void setPostText(String text) {
        try {
            mComment.put(PROFILE_COMMENT_POST_TEXT_KEY, text);
        }
        catch (Exception e) {
            e.printStackTrace();
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
     * Description: returns a valid full name format.
     * Add spacing only if both first and last names are non empty
     */
    public String getValidFullName() {
        if (!getFirstName().equals("") && !getLastName().equals(""))
            return getFirstName() + " " + getLastName();
        return getFirstName() + getLastName();
    }

    /**
     * Gets the saved status from the JSONObject and parses to a Boolean value
     * @return the saved status of a post
     */
    public Boolean getSavedState(){
        try{
            if (mComment.getString(PROFILE_COMMENT_SAVED_STATE_KEY).equals("null")){
                return false;
            } else {
                return Boolean.parseBoolean(mComment.getString(PROFILE_COMMENT_SAVED_STATE_KEY));
            }
        } catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    public void setSavedState(Boolean savedState){
        try {
            mComment.put(PROFILE_COMMENT_SAVED_STATE_KEY, savedState);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Used by the Presenter's onBind methods to format the post text. Formatting includes highlighting
     * any number of words as well as an italicized footer which can contain any number of lines.
     * @return
     */
    public TextFormat getTextFormat() {
        return mTextFormat;
    }
}
