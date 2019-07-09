package ca.gc.inspection.scoop.profilecomment;

import org.json.JSONObject;
import ca.gc.inspection.scoop.postcomment.PostComment;

public class ProfileComment extends PostComment {
    /**
     * Data class which stores information for a single profile comment.
     * Should only interact with the Presenter as this class is a helper data class.
     * - Not an inner class of Presenter to simplify inheritance.
     *
     * Extends PostComment by including post title.
     *
     * The data is stored in the Json format provided by the database.
     * Static string keys are used to access the relevant values in the Json objects
     *
     */

    /**
     * Constructor for the data object.
     * Should only be instantiated by the Presenter.
     *
     * @param jsonComment       Contains json data of the comment data from the database
     * @param jsonImage         Contains json data of the profile image from the database
     */
    protected ProfileComment(JSONObject jsonComment, JSONObject jsonImage) {
        super(jsonComment, jsonImage);
    }

    /**
     * Returns the post title for comments - this method is overridden in ProfilePost.
     * Hardcoded string values can be replaced with strings xml.
     * @return post title string
     */
    public String getPostTitle() {
        return "Replying to " + getPostFirstName() + " " + getPostLastName() + "'s post";
    }

    public static final String PROFILE_COMMENT_REFERENCE_ID = "activityreference";

    public String getReferenceID() {
        try {
            return mComment.getString(PROFILE_COMMENT_REFERENCE_ID);
        }
        catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }
}
