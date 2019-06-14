package ca.gc.inspection.scoop.ProfileComment;

import android.graphics.Bitmap;

public interface ProfileCommentViewHolderContract {

    ProfileCommentViewHolder setPostTitle(String postTitle);

    ProfileCommentViewHolder setPostText(String postText);

    ProfileCommentViewHolder setUserImage(Bitmap image);

    ProfileCommentViewHolder setUserName(String userName);

    ProfileCommentViewHolder setLikeCount(String likeCount);

    ProfileCommentViewHolder setDate(String date);

    ProfileCommentViewHolder setLikeState(LikeState likeState);
}
