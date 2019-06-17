package ca.gc.inspection.scoop.ProfileComment;

import android.graphics.Bitmap;

public class ProfileComment {
    /**
     * Data class which stores information for a single profile comment
     */

    private String mUserName, mDate, mPostText, mPostTitle, mLikeCount;
    private Bitmap mProfileImage;
    private LikeState mLikeState;

    ProfileComment(String userName, String date, String postText, String postTitle, String likeCount, Bitmap profileImage, LikeState likeState) {
        mUserName = userName;
        mDate = date;
        mPostText = postText;
        mPostTitle = postTitle;
        mLikeCount = likeCount;
        mProfileImage = profileImage;
        mLikeState = likeState;
    }

    public String getUserName() {
        return mUserName;
    }

    public String getDate() {
        return mDate;
    }

    public String getPostText() {
        return mPostText;
    }

    public String getPostTitle() {
        return mPostTitle;
    }

    public String getLikeCount() {
        return mLikeCount;
    }

    public Bitmap getProfileImage() {
        return mProfileImage;
    }

    public LikeState getLikeState() {
        return mLikeState;
    }
}
