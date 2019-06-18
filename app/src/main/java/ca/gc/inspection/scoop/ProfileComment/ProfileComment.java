package ca.gc.inspection.scoop.ProfileComment;

import org.json.JSONObject;
public class ProfileComment {
    /**
     * Data class which stores information for a single profile comment
     */
    private JSONObject mComment, mImage;

    private static final String PROFILE_COMMENT_ACTIVITYID_KEY = "activityid";
    private static final String PROFILE_COMMENT_USERID_KEY = "userid";
    private static final String PROFILE_COMMENT_DATE_KEY = "createddate";
    private static final String PROFILE_COMMENT_PROFILE_IMAGE_KEY = "profileimage";
    private static final String PROFILE_COMMENT_POST_FIRST_NAME_KEY = "postfirstname";
    private static final String PROFILE_COMMENT_POST_LAST_NAME_KEY = "postlastname";
    private static final String PROFILE_COMMENT_FIRST_NAME_KEY = "firstname";
    private static final String PROFILE_COMMENT_LAST_NAME_KEY = "lastname";
    private static final String PROFILE_COMMENT_POST_LIKE_TYPE_KEY = "liketype";
    private static final String PROFILE_COMMENT_POST_LIKE_COUNT_KEY = "likecount";
    private static final String PROFILE_COMMENT_POST_POST_TEXT_KEY = "posttext";

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
    public String getUserId() {
        try {
            return mComment.getString(PROFILE_COMMENT_USERID_KEY);
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
            return mComment.getString(PROFILE_COMMENT_POST_POST_TEXT_KEY);
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
            return mComment.getString(PROFILE_COMMENT_POST_LIKE_COUNT_KEY);
        }
        catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public LikeState getLikeState() {
        try {
            int likeTypeInteger = mComment.getInt(PROFILE_COMMENT_POST_LIKE_TYPE_KEY);
            return LikeState.valueOf(likeTypeInteger);
        }
        catch (Exception e) {
            e.printStackTrace();
            return LikeState.NEUTRAL;
        }
    }
}
