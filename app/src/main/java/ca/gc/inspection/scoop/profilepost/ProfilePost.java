package ca.gc.inspection.scoop.profilepost;

import com.google.gson.JsonObject;

import org.json.JSONObject;

import ca.gc.inspection.scoop.profilecomment.ProfileComment;

public class ProfilePost extends ProfileComment {
    public static final String PROFILE_POST_TITLE_KEY = "posttitle";
    public static final String PROFILE_POST_COMMENT_COUNT_KEY = "commentcount";

    protected ProfilePost(JSONObject jsonPost, JSONObject jsonImage) {
        super(jsonPost, jsonImage);
    }

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
