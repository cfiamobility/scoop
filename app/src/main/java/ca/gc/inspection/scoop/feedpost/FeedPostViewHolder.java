package ca.gc.inspection.scoop.feedpost;

import android.view.View;
import android.widget.ImageView;

import ca.gc.inspection.scoop.profilepost.ProfilePostViewHolder;
import ca.gc.inspection.scoop.R;

public class FeedPostViewHolder extends ProfilePostViewHolder {
    ImageView postImage;

    public FeedPostViewHolder(View v) {
        super(v);
        postImage = v.findViewById(R.id.post_image);
    }
}
