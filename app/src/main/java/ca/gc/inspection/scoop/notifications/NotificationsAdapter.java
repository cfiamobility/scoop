//package ca.gc.inspection.scoop.notifications;
//
//import android.content.Context;
//import android.graphics.Bitmap;
//import android.support.annotation.NonNull;
//import android.support.v7.widget.RecyclerView;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.ImageView;
//import android.widget.TextView;
//
//import com.android.volley.RequestQueue;
//
//import org.json.JSONArray;
//import org.json.JSONException;
//
//import java.sql.Timestamp;
//
//import ca.gc.inspection.scoop.R;
//
///**
// * Adapter used by the notifications recycler view
// */
//public class NotificationsTodayAdapter extends RecyclerView.Adapter<NotificationsTodayAdapter.NotificationViewHolder> implements NotificationsAdapterInterface.View{
//    private JSONArray notifications, images;
//    private RequestQueue requestQueue;
//
//    private String timeType;
//    private Timestamp currentTime;
//    private Context context;
//
//    /**
//     *
//     * @param notifs: represents the JSONArray for all the notifications relevant to the user
//     * @param requestQueue: represents the requestQueue used for adding future requests to
//     * @param timeType: represents whether the type of time which should be displayed is for today or for recent notifications
//     */
//    NotificationsTodayAdapter(JSONArray notifs, JSONArray images, RequestQueue requestQueue, String timeType, Timestamp currentTime, Context context){
//        this.notifications = notifs;
//        this.images = images;
//        this.requestQueue = requestQueue;
//        this.timeType = timeType;
//        this.currentTime = currentTime;
//        this.context = context;
//    }
//
//    /**
//     *
//     * @param viewGroup: the view group representing the whole recycler view
//     * @param i: the index which the view holder is on
//     * @return the view holder for the specified row
//     */
//    @NonNull
//    @Override
//    public NotificationsTodayAdapter.NotificationViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
//        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_notifications, viewGroup, false);
//
//        return new NotificationViewHolder(v);
//    }
//
//    /**
//     * Description: used to set all the views for the holder of row i
//     * @param holder
//     * @param i
//     */
//    @Override
//    public void onBindViewHolder(@NonNull final NotificationsTodayAdapter.NotificationViewHolder holder, int i) {
//        NotificationsAdapterController controller = new NotificationsAdapterController(holder, i, this, notifications, images, currentTime, timeType, context);
//        try {
//            controller.displayNotifications();
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//    }
//
//    @Override
//    public void setActionType(String actionType, NotificationViewHolder holder) {
//        holder.actionType.setText(actionType);
//    }
//
//    @Override
//    public void setActivityType(String activityType, NotificationViewHolder holder) {
//        holder.activityType.setText(activityType);
//    }
//
//    @Override
//    public void setTime(String time, NotificationViewHolder holder){
//        holder.time.setText(time); //sets text to hours ago
//    }
//
//    @Override
//    public void hideTime(NotificationViewHolder holder) {
//        holder.time.setVisibility(View.GONE);
//    }
//
//    @Override
//    public void setFullName(String fullName, NotificationViewHolder holder) {
//        holder.fullName.setText(fullName);
//    }
//
//    @Override
//    public void setImage(Bitmap bitmap, NotificationViewHolder holder){
//        holder.profileImage.setImageBitmap(bitmap);
//    }
//
//    @Override
//    public void hideImage(NotificationViewHolder holder){
//        holder.profileImage.setVisibility(View.GONE);
//    }
//
//    /**
//     * @return how many rows there will be based on how many notifications there are
//     */
//    @Override
//    public int getItemCount() {
//        return notifications.length();
//    }
//
//
//    public class NotificationViewHolder extends RecyclerView.ViewHolder{
//        // each data item is just a string in this case
//        ImageView profileImage;
//        TextView fullName, actionType, activityType, time;
//
//        /**
//         * Description: Viewholder holds each item in the recycler view and holds each piece that altogether makes the item
//         * @param v: passed in the view from the custom row layout
//         */
//        NotificationViewHolder(View v) {
//            super(v);
//            profileImage = (ImageView) v.findViewById(R.id.profile_image); //instantiating the profile image imageview
//            actionType = (TextView) v.findViewById(R.id.actiontype); //instantiating the action type textview
//            activityType = (TextView) v.findViewById(R.id.activitytype); //instantiating the activity type textview
//            time = (TextView) v.findViewById(R.id.time); //instantiating the time textview
//            fullName = (TextView) v.findViewById(R.id.fullname); //instantiating the full name linearlayout
//        }
//
//    }
//
//
//}