package ca.gc.inspection.scoop.notif.notificationstoday;

import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import ca.gc.inspection.scoop.R;
import ca.gc.inspection.scoop.util.CameraUtils;
import de.hdodenhof.circleimageview.CircleImageView;

public class NotificationsTodayViewHolder extends RecyclerView.ViewHolder
        implements NotificationsTodayContract.View.ViewHolder{

    NotificationsTodayContract.Presenter.ViewHolderAPI mPresenter;

    CircleImageView profileImage;
    ImageView postImage;
    TextView fullName, actionType, activityType, time;

    public NotificationsTodayViewHolder(View v, NotificationsTodayContract.Presenter.ViewHolderAPI presenter) {
        super(v);
        profileImage = v.findViewById(R.id.profile_image); //instantiating the profile image imageview
        fullName = v.findViewById(R.id.fullname); //instantiating the full name linearlayout
        actionType = v.findViewById(R.id.actiontype); //instantiating the action type textview
        activityType = v.findViewById(R.id.activitytype); //instantiating the activity type textview
        time = v.findViewById(R.id.time); //instantiating the time textview
        postImage = v.findViewById(R.id.post_image);

        mPresenter = presenter;
    }


    public NotificationsTodayContract.View.ViewHolder setActionType(String actionType) {
        this.actionType.setText(actionType);
        return this;
    }

    public NotificationsTodayContract.View.ViewHolder setActivityType(String activityType) {
        this.activityType.setText(activityType);
        return this;
    }

    public NotificationsTodayContract.View.ViewHolder setTime(String time){
        this.time.setText(time); //sets text to hours ago
        return this;
    }

    public NotificationsTodayContract.View.ViewHolder hideTime() {
        this.time.setVisibility(View.GONE);
        return this;
    }

    public NotificationsTodayContract.View.ViewHolder setFullName(String fullName) {
        this.fullName.setText(fullName);
        return this;
    }

    public NotificationsTodayContract.View.ViewHolder setImage(Bitmap bitmap){
        this.profileImage.setImageBitmap(bitmap);
        return this;
    }

    public NotificationsTodayContract.View.ViewHolder hideImage(){
        this.profileImage.setVisibility(View.GONE);
        return this;
    }

    public NotificationsTodayContract.View.ViewHolder setUserImageFromString(String image){
        if (image != null && !image.isEmpty()) {
            Bitmap bitmap = CameraUtils.stringToBitmap(image); //converts image string to bitmap
            profileImage.setImageBitmap(bitmap);
        }
        return this;
    }

    public NotificationsTodayContract.View.ViewHolder setPostImageFromString(String image){
        Log.i("VIEWHOLDER", image);
        if (image != null && !image.isEmpty()) {
            Bitmap bitmap = CameraUtils.stringToBitmap(image); //converts image string to bitmap
            postImage.setImageBitmap(bitmap);
        }
        return this;
    }

    String getActivityType(){
        return this.activityType.getText().toString();
    }

}
