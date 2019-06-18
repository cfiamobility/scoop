package ca.gc.inspection.scoop.ProfileComment;

import android.util.SparseArray;

public enum LikeState {
    UPVOTE(1),
    NEUTRAL(0),
    DOWNVOTE(-1);

    private int databaseValue;
    private static SparseArray<LikeState> map = new SparseArray<>();

    LikeState(int databaseValue) {
        this.databaseValue = databaseValue;
    }

    static {
        for (LikeState likeState : LikeState.values()) {
            map.put(likeState.databaseValue, likeState);
        }
    }

    public static LikeState valueOf(int databaseValue) {
        return map.get(databaseValue);
    }

    public int getValue() {
        return this.databaseValue;
    }
}