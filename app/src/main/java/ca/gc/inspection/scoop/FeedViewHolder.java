package ca.gc.inspection.scoop;

import android.view.View;
import android.widget.ImageView;

public class FeedViewHolder extends ProfilePostsFeedViewHolder {
    ImageView postImage;

    public FeedViewHolder(View v) {
        super(v);
        postImage = v.findViewById(R.id.post_image);
    }
}
