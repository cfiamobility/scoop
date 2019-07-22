package ca.gc.inspection.scoop.profilepost;

import org.json.JSONObject;

import ca.gc.inspection.scoop.profilecomment.ProfileComment;
import ca.gc.inspection.scoop.profilelikes.ProfileLike;

public class ProfilePost extends ProfileLike {
    /**
     * Data class which stores information for a single profile post.
     * Should only interact with the Presenter as this class is a helper data class.
     * - Not an inner class of Presenter to simplify inheritance.
     *
     * Extends ProfileComment by including comment count.
     *
     * The data is stored in the Json format provided by the database.
     * Static string keys are used to access the relevant values in the Json objects
     *
     */

    public static final String PROFILE_POST_TITLE_KEY = "posttitle";
    public static final String PROFILE_POST_COMMENT_COUNT_KEY = "commentcount";

    protected ProfilePost(JSONObject jsonPost, JSONObject jsonImage) {
        super(jsonPost, jsonImage);
    }

    /**
     * Overrides getPostTitle in PostComment to return the appropriate title for a post
     * @return post title string
     */
    @Override
    public String getPostTitle() {
        try {
            return mComment.getString(PROFILE_POST_TITLE_KEY);
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public String getCommentCount() {
        try {
            return mComment.getString(PROFILE_POST_COMMENT_COUNT_KEY);
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }



}
