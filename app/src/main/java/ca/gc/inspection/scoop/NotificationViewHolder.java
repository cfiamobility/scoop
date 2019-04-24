package ca.gc.inspection.scoop;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class NotificationViewHolder extends RecyclerView.ViewHolder{
    // each data item is just a string in this case
    ImageView profileImage;
    TextView fullName, actionType, activityType, time;


    /**
     * Description: Viewholder holds each item in the recycler view and holds each piece that altogether makes the item
     * @param v: passed in the view from the custom row layout
     */
    NotificationViewHolder(View v) {
        super(v);
        profileImage = (ImageView) v.findViewById(R.id.item_post_img_profile); //instantiating the profile image imageview
        actionType = (TextView) v.findViewById(R.id.actiontype); //instantiating the action type textview
        activityType = (TextView) v.findViewById(R.id.activitytype); //instantiating the activity type textview
        time = (TextView) v.findViewById(R.id.time); //instantiating the time textview
        fullName = (TextView) v.findViewById(R.id.fullname); //instantiating the full name linearlayout
    }

}
