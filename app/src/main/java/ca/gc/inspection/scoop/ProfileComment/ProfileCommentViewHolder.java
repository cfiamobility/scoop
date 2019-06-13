package ca.gc.inspection.scoop.ProfileComment;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import ca.gc.inspection.scoop.R;

/**
 * ViewHolder for replying to a post action; it is the most generic View Holder
 * and contains the minimum views (no comment count, options menus, or images)
 * related to "posting" actions. Parent View Holder for ProfilePostViewHolder.
 */

public class ProfileCommentViewHolder extends RecyclerView.ViewHolder {

    public TextView username, date, postText, postTitle, likeCount;
    public ImageView profileImage, upvote, downvote;


    public ProfileCommentViewHolder(View v) {
        super(v);
        username = v.findViewById(R.id.name);
        profileImage = v.findViewById(R.id.profile_image);
        date = v.findViewById(R.id.date);
        postText = v.findViewById(R.id.post_text);
        postTitle = v.findViewById(R.id.post_title);
        likeCount = v.findViewById(R.id.vote_count);
        upvote = v.findViewById(R.id.up_vote);
        downvote = v.findViewById(R.id.down_vote);
    }
}
