package ca.gc.inspection.scoop.notif;

import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import ca.gc.inspection.scoop.R;
import ca.gc.inspection.scoop.util.CameraUtils;
import de.hdodenhof.circleimageview.CircleImageView;

public class NotificationsViewHolder extends RecyclerView.ViewHolder
        implements NotificationsContract.View.ViewHolder{

    NotificationsContract.Presenter.ViewHolderAPI mPresenter;

    CircleImageView profileImage;
    TextView fullName, actionType, activityType, time;

    NotificationsViewHolder(View v, NotificationsContract.Presenter.ViewHolderAPI presenter) {
        super(v);
        profileImage = v.findViewById(R.id.profile_image); //instantiating the profile image imageview
        fullName = v.findViewById(R.id.fullname); //instantiating the full name linearlayout
        actionType = v.findViewById(R.id.actiontype); //instantiating the action type textview
        activityType = v.findViewById(R.id.activitytype); //instantiating the activity type textview
        time = v.findViewById(R.id.time); //instantiating the time textview

        mPresenter = presenter;
    }


    public NotificationsContract.View.ViewHolder setActionType(String actionType) {
        this.actionType.setText(actionType);
        return this;
    }

    public NotificationsContract.View.ViewHolder setActivityType(String activityType) {
        this.activityType.setText(activityType);
        return this;
    }

    public NotificationsContract.View.ViewHolder setTime(String time){
        this.time.setText(time); //sets text to hours ago
        return this;
    }

    public NotificationsContract.View.ViewHolder hideTime() {
        this.time.setVisibility(View.GONE);
        return this;
    }

    public NotificationsContract.View.ViewHolder setFullName(String fullName) {
        this.fullName.setText(fullName);
        return this;
    }

    public NotificationsContract.View.ViewHolder setImage(Bitmap bitmap){
        this.profileImage.setImageBitmap(bitmap);
        return this;
    }

    public NotificationsContract.View.ViewHolder hideImage(){
        this.profileImage.setVisibility(View.GONE);
        return this;
    }

    public NotificationsContract.View.ViewHolder setUserImageFromString(String image){
        if (image != null && !image.isEmpty()) {
            Bitmap bitmap = CameraUtils.stringToBitmap(image); //converts image string to bitmap
            Log.i("image", image);
            profileImage.setImageBitmap(bitmap);
        }
        return this;
    }

//    private void goToPost() {
//        Intent intent = new Intent(MyApplication.getContext(), DisplayPostActivity.class);
//        intent.putExtra("activityid", ); //puts the activity id into the intent
//        MyApplication.getContext().startActivity(intent); //changes to the Post activity
//    }

}
