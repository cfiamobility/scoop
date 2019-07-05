package ca.gc.inspection.scoop.postcomment;

import java.util.HashMap;

public enum LikeState {
    /**
     * Enum to map database values to custom values for readability.
     * The interactor should deal with converting between LikeState enum and corresponding
     * database String values.
     */

    UPVOTE("1"),
    NEUTRAL("0"),
    DOWNVOTE("-1"),
    NULL("null");   // Value if LikeState does not exist in database json response

    private String databaseValue;
    private static HashMap<String, LikeState> map = new HashMap<>();

    LikeState(String databaseValue) {
        this.databaseValue = databaseValue;
    }


    /**
     * Set up the map of database values to LikeStates
     * Cannot be called directly.
     */
    static {
        for (LikeState likeState : LikeState.values()) {
            map.put(likeState.databaseValue, likeState);
        }
    }

    /**
     * Get the LikeState from a databaseValue
     * @param databaseValue String likeState obtained from database Json response
     * @return
     */
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