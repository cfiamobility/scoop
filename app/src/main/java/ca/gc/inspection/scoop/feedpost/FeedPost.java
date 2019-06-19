package ca.gc.inspection.scoop.feedpost;

import org.json.JSONObject;

import ca.gc.inspection.scoop.profilepost.ProfilePost;

class FeedPost extends ProfilePost {

    public static final String FEED_POST_IMAGE_PATH_KEY = "postimagepath";

    FeedPost(JSONObject jsonFeedPost, JSONObject jsonImage) {
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
