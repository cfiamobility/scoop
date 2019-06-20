package ca.gc.inspection.scoop.profilecomment;

import java.util.HashMap;

public enum LikeState {
    UPVOTE("1"),
    NEUTRAL("0"),
    DOWNVOTE("-1"),
    NULL("null");

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

    /**
     * Method used in interactor to make the purpose of the value explicit
     * @return
     */
    public String getDatabaseValue() {
        return this.databaseValue;
    }

    public String valueOf() {
        return getDatabaseValue();
    }
}