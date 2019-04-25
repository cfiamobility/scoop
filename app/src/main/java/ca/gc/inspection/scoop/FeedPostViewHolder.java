package ca.gc.inspection.scoop;

import android.view.View;
import android.widget.ImageView;

public class FeedPostViewHolder extends ProfilePostsFeedViewHolder {
    ImageView postImage;

    public FeedPostViewHolder(View v) {
        super(v);
        postImage = v.findViewById(R.id.post_image);
    }
}
