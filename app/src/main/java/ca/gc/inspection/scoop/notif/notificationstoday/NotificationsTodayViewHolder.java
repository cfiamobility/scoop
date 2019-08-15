package ca.gc.inspection.scoop.notif.notificationstoday;

import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import ca.gc.inspection.scoop.R;
import ca.gc.inspection.scoop.util.CameraUtils;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * ViewHolder for NotificationToday; it is the base ViewHolder for notifications and
 * represents a single View of a NoficationToday
 * Parent ViewHolder for NotificationsRecentViewHolder
 */
public class NotificationsTodayViewHolder extends RecyclerView.ViewHolder
        implements NotificationsTodayContract.View.ViewHolder{

    //Presenter that allows for communication between ViewHolder and Presenter
    NotificationsTodayContract.Presenter.ViewHolderAPI mPresenter;

    //UI declarations
    CircleImageView profileImage;
    ImageView postImage;
    TextView fullName, actionType, activityType, time;

    /**
     * Constructor that instantiates the Android Views from the given ViewGroup to the member UI variables
     * @param v viewgroup from item_notifications.xml
     * @param presenter presenter to be referenced
     */
    public NotificationsTodayViewHolder(View v, NotificationsTodayContract.Presenter.ViewHolderAPI presenter) {
        super(v);
        profileImage = v.findViewById(R.id.profile_image);
        fullName = v.findViewById(R.id.fullname);
        actionType = v.findViewById(R.id.actiontype);
        activityType = v.findViewById(R.id.activitytype);
        time = v.findViewById(R.id.time);
        postImage = v.findViewById(R.id.post_image);

        mPresenter = presenter;
    }

    /**
     * Sets the "action type" text to be displayed in a notification
     * E.g. liked or commented on
     * @param actionType the action being performed
     * @return Modified ViewHolder with updated View content
     */
    public NotificationsTodayContract.View.ViewHolder setActionType(String actionType) {
        this.actionType.setText(actionType);
        return this;
    }

    /**
     * Sets the "activity type" text to be displayed in a notification
     * E.g. a post or comment
     * @param activityType the activity the action is being performed on
     * @return Modified ViewHolder with updated View content
     */
    public NotificationsTodayContract.View.ViewHolder setActivityType(String activityType) {
        this.activityType.setText(activityType);
        return this;
    }

    /**
     * Sets the "time" text to be displayed in a notification
     * @param time modified time string
     * @return Modified ViewHolder with updated View content
     */
    public NotificationsTodayContract.View.ViewHolder setTime(String time){
        this.time.setText(time); //sets text to hours ago
        return this;
    }

    /**
     * Sets the "name" text to be displayed in a notification
     * @param fullName modified full name of notifier string
     * @return Modified ViewHolder with updated View content
     */
    public NotificationsTodayContract.View.ViewHolder setFullName(String fullName) {
        this.fullName.setText(fullName);
        return this;
    }

    /**
     * Sets the profile image of notifier to be displayed in a notification
     * String parameter is converted to bitmap if image exists (which it always should for a profile image)
     * @param image base-64 string of profile image
     * @return Modified ViewHolder with updated View content
     */
    public NotificationsTodayContract.View.ViewHolder setUserImageFromString(String image){
        if (image != null && !image.isEmpty()) {
            Bitmap bitmap = CameraUtils.stringToBitmap(image); //converts image string to bitmap
            profileImage.setImageBitmap(bitmap);
        }
        return this;
    }

    /**
     * Sets the image of a post to be displayed in a notification
     * String parameter is converted to bitmap if exists (some posts do not have images)
     * @param image base-64 string of post image
     * @return Modified ViewHolder with updated View content
     */
    public NotificationsTodayContract.View.ViewHolder setPostImageFromString(String image){
        if (image != null && !image.isEmpty()) {
            Bitmap bitmap = CameraUtils.stringToBitmap(image); //converts image string to bitmap
            postImage.setImageBitmap(bitmap);
        }
        return this;
    }

    /**
     * Gets the activity type (post or comment) of a ViewHolder
     * Note: method is used intra-view (between the main View and ViewHolder) so not necessary to declare in contract
     *       see NotificationsTodayContract for more detailed explanation
     * @return string of ViewHolder's activity type
     */
    String getActivityType(){
        return this.activityType.getText().toString();
    }

}
