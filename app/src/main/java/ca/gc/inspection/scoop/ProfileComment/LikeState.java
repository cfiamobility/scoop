package ca.gc.inspection.scoop.ProfileComment;

import java.util.HashMap;

public enum LikeState {
    UPVOTE("1"),
    NEUTRAL("0"),
    DOWNVOTE("-1");

    private String databaseValue;
    private static HashMap<String, LikeState> map = new HashMap<>();

    LikeState(String databaseValue) {
        this.databaseValue = databaseValue;
    }

    static {
        for (LikeState likeState : LikeState.values()) {
            map.put(likeState.databaseValue, likeState);
        }
    }

    public static LikeState getLikeStateFrom(String databaseValue) {
        return map.get(databaseValue);
    }

    public String getDatabaseValue() {
        return this.databaseValue;
    }
}