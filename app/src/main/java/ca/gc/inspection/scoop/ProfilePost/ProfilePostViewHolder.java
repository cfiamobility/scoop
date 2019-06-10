package ca.gc.inspection.scoop.ProfilePost;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import ca.gc.inspection.scoop.PostReply.ReplyPostViewHolder;
import ca.gc.inspection.scoop.R;

public class ProfilePostsFeedPostViewHolder extends ReplyPostViewHolder {

    TextView commentCount;
    ImageView optionsMenu;


    public ProfilePostsFeedPostViewHolder(View v) {
        super(v);
        commentCount = v.findViewById(R.id.comment_count);
        optionsMenu = v.findViewById(R.id.options_menu);
    }
}
