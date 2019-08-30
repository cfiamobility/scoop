package ca.gc.inspection.scoop.searchprofile;

import android.widget.ImageView;
import android.widget.TextView;

/**
 * Allows setUserInfoListener (static method) to access common UI elements from both PostCommentViewHolder
 * (including its subclasses) and SearchProfileViewHolder using a common interface.
 * These UI elements share the functionality that clicking on them brings the user to the poster's profile
 */
public interface UserProfileListener {
    TextView getUserName();
    ImageView getProfileImage();
}
