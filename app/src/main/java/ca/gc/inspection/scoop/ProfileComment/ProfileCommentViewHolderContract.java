package ca.gc.inspection.scoop.ProfileComment;

import android.graphics.Bitmap;

public interface ProfileCommentViewHolderContract {

    void setPostTitle(String postTitle);

    void setPostText(String postText);

    void setUserImage(Bitmap image);

    void setUserName(String userName);

    void setLikeCount(String likeCount);

    void setDate(String date);

    void setLikeDownvoteState();

    void setLikeNeutralState();

    void setLikeUpvoteState();
}
