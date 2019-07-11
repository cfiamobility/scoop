package ca.gc.inspection.scoop.searchposts;

import org.json.JSONObject;

import ca.gc.inspection.scoop.profilecomment.ProfileComment;
import ca.gc.inspection.scoop.profilepost.ProfilePost;

public class SearchPost extends ProfilePost {
    /**
     * Data class which stores information for a single search post.
     * Should only interact with the Presenter as this class is a helper data class.
     * - Not an inner class of Presenter to simplify inheritance.
     *
     * The data is stored in the Json format provided by the database.
     * Static string keys are used to access the relevant values in the Json objects
     *
     */

    protected SearchPost(JSONObject jsonPost, JSONObject jsonImage) {
        super(jsonPost, jsonImage);
    }

}
