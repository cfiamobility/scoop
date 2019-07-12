package ca.gc.inspection.scoop.feedpost;

import android.graphics.Bitmap;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import ca.gc.inspection.scoop.util.CameraUtils;
import ca.gc.inspection.scoop.profilepost.ProfilePostViewHolder;
import ca.gc.inspection.scoop.R;

public class FeedPostViewHolder extends ProfilePostViewHolder
        implements FeedPostContract.View.ViewHolder {
    /**
     * ViewHolder for viewing a feed post.
     */

    FeedPostContract.Presenter.ViewHolderAPI mPresenter;

    ImageView postImage;

    public FeedPostViewHolder(View v, FeedPostContract.Presenter.ViewHolderAPI presenter) {
        super(v, presenter);
        postImage = v.findViewById(R.id.post_image);
        mPresenter = presenter;
    }

    /**
     * Description: sets image of post
     * @param image: image of post
     */
    @Override
    public FeedPostContract.View.ViewHolder setPostImageFromString(String image) {
        if (image != null && !image.isEmpty()) {
            Bitmap bitmap = CameraUtils.stringToBitmap(image); //converts image string to bitmap
            Log.i("image", image);
            postImage.setImageBitmap(bitmap);
            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            layoutParams.addRule(RelativeLayout.BELOW, R.id.post_text);
            layoutParams.setMargins(0, 30, 0, 0);
            postImage.setLayoutParams(layoutParams);
            showPostImage();
        }
        else hidePostImage();
        return this;
    }

    /**
     * Description: hides post image if there is none
     */
    private void hidePostImage() {
        postImage.setVisibility(View.GONE);
    }

    /**
     * Description: show post image if there is one
     */
    private void showPostImage() {
        postImage.setVisibility(View.VISIBLE);
    }

}
