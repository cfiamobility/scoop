package ca.gc.inspection.scoop.profilelikes;

import android.util.Log;

import org.json.JSONObject;

import ca.gc.inspection.scoop.profilecomment.ProfileComment;

import static ca.gc.inspection.scoop.profilepost.ProfilePost.PROFILE_POST_COMMENT_COUNT_KEY;
import static ca.gc.inspection.scoop.profilepost.ProfilePost.PROFILE_POST_TITLE_KEY;

public class ProfileLike extends ProfileComment {
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

    protected ProfileLike(JSONObject jsonPost, JSONObject jsonImage) {
        super(jsonPost, jsonImage);
    }

    /**
     * Overrides getPostTitle in ProfileComment to return the appropriate title for a post
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
