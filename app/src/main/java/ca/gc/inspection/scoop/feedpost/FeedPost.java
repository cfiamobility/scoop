package ca.gc.inspection.scoop.feedpost;

import org.json.JSONObject;

import ca.gc.inspection.scoop.profilepost.ProfilePost;

public class FeedPost extends ProfilePost {
    /**
     * Data class which stores information for a single feed post.
     * Should only interact with the Presenter as this class is a helper data class.
     * - Not an inner class of Presenter to simplify inheritance.
     *
     * Extends ProfilePost to include getting post image path.
     *
     * The data is stored in the Json format provided by the database.
     * Static string keys are used to access the relevant values in the Json objects
     *
     */

    public static final String FEED_POST_IMAGE_PATH_KEY = "postimagepath";

    public FeedPost(JSONObject jsonFeedPost, JSONObject jsonImage) {
        super(jsonFeedPost, jsonImage);
    }

    public String getFeedPostImagePath() {
        try {
            return mImage.getString(FEED_POST_IMAGE_PATH_KEY);
        }
        catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }
}
