package ca.gc.inspection.scoop;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class FeedViewHolder extends RecyclerView.ViewHolder{
        TextView username, date, postText, postTitle, likeCount, commentCount;
        ImageView optionsMenu, profileImage, upvote, downvote, postImage;


        public FeedViewHolder(View v) {
            super(v);
            username = v.findViewById(R.id.name);
            optionsMenu = v.findViewById(R.id.options_menu);
            profileImage = v.findViewById(R.id.profile_image);
            date = v.findViewById(R.id.date);
            postText = v.findViewById(R.id.post_text);
            postTitle = v.findViewById(R.id.post_title);
            likeCount = v.findViewById(R.id.vote_count);
            commentCount = v.findViewById(R.id.comment_count);
            upvote = v.findViewById(R.id.up_vote);
            downvote = v.findViewById(R.id.down_vote);
            postImage = v.findViewById(R.id.post_image);
        }
}
