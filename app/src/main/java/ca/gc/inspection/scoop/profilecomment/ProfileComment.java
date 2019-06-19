package ca.gc.inspection.scoop.profilecomment;

import org.json.JSONObject;

import static ca.gc.inspection.scoop.Config.USERID_KEY;

public class ProfileComment {
    /**
     * Data class which stores information for a single profile comment
     */
    private JSONObject mComment, mImage;

    public static final String PROFILE_COMMENT_ACTIVITYID_KEY = "activityid";
    public static final String PROFILE_COMMENT_LIKE_POSTERID_KEY = "posterid";  // TODO document difference between posterid and userid
    public static final String PROFILE_COMMENT_DATE_KEY = "createddate";
    public static final String PROFILE_COMMENT_PROFILE_IMAGE_KEY = "profileimage";
    public static final String PROFILE_COMMENT_POST_FIRST_NAME_KEY = "postfirstname";
    public static final String PROFILE_COMMENT_POST_LAST_NAME_KEY = "postlastname";
    public static final String PROFILE_COMMENT_FIRST_NAME_KEY = "firstname";
    public static final String PROFILE_COMMENT_LAST_NAME_KEY = "lastname";
    public static final String PROFILE_COMMENT_LIKE_TYPE_KEY = "liketype";
    public static final String PROFILE_COMMENT_LIKE_COUNT_KEY = "likecount";
    public static final String PROFILE_COMMENT_POST_TEXT_KEY = "posttext";

    ProfileComment(JSONObject jsonComment, JSONObject jsonImage) {
        mComment = jsonComment;
        mImage = jsonImage;
    }

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
     * Get the UserId of the poster
     * @return UserId string if it exists, otherwise return an empty string
     */
    // TODO document difference between posterid and userid
    public String getPosterId() {
        try {
            return mComment.getString(USERID_KEY);
        }
        catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

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
        try {
            return mComment.getString(PROFILE_COMMENT_LIKE_COUNT_KEY);
        }
        catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public LikeState getLikeState() {
        try {
            String likeTypeString = mComment.getString(PROFILE_COMMENT_LIKE_TYPE_KEY);
            return LikeState.valueOf(likeTypeString);
        }
        catch (Exception e) {
            e.printStackTrace();
            return LikeState.NEUTRAL;
        }
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
