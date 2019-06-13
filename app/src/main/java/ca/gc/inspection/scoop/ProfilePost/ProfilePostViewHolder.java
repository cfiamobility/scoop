package ca.gc.inspection.scoop.ProfilePost;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import ca.gc.inspection.scoop.ProfileComment.ProfileCommentViewHolder;
import ca.gc.inspection.scoop.R;

public class ProfilePostViewHolder extends ProfileCommentViewHolder {

    public TextView commentCount;
    public ImageView optionsMenu;


    public ProfilePostViewHolder(View v) {
        super(v);
        commentCount = v.findViewById(R.id.comment_count);
        optionsMenu = v.findViewById(R.id.options_menu);
    }
}
