package ca.gc.inspection.scoop;

import android.view.View;
import android.widget.ImageView;

import ca.gc.inspection.scoop.ProfilePost.ProfilePostViewHolder;

public class FeedPostViewHolder extends ProfilePostViewHolder {
    ImageView postImage;

    public FeedPostViewHolder(View v) {
        super(v);
        postImage = v.findViewById(R.id.post_image);
    }
}
