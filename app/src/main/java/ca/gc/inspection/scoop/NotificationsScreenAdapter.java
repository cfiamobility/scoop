package ca.gc.inspection.scoop;

import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.RequestQueue;

import org.json.JSONArray;
import org.json.JSONException;

import java.sql.Timestamp;

public class NotificationsScreenAdapter extends RecyclerView.Adapter<NotificationViewHolder> implements NotificationsScreenAdapterController.NotificationAdapterInterface{
    private JSONArray notifications, images;
    private RequestQueue requestQueue;

    private String timeType;
    private Timestamp currentTime;

    /**
     *
     * @param notifs: represents the JSONArray for all the notifications relevant to the user
     * @param requestQueue: represents the requestQueue used for adding future requests to
     * @param timeType: represents whether the type of time which should be displayed is for today or for recent notifications
     */
    NotificationsScreenAdapter(JSONArray notifs, JSONArray images,  RequestQueue requestQueue, String timeType, Timestamp currentTime){
        this.notifications = notifs;
        this.images = images;
        this.requestQueue = requestQueue;
        this.timeType = timeType;
        this.currentTime = currentTime;
    }

    /**
     *
     * @param viewGroup: the view group representing the whole recycler view
     * @param i: the index which the view holder is on
     * @return the view holder for the specified row
     */
    @NonNull
    @Override
    public NotificationViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.custom_row_notifications, viewGroup, false);

        return new NotificationViewHolder(v);
    }

    /**
     * Description: used to set all the views for the holder of row i
     * @param holder
     * @param i
     */
    @Override
    public void onBindViewHolder(@NonNull final NotificationViewHolder holder, int i) {
        NotificationsScreenAdapterController controller = new NotificationsScreenAdapterController(holder, i, this, notifications, images, currentTime, timeType, requestQueue);
        try {
            controller.displayNotifications();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void setActionType(String actionType, NotificationViewHolder holder) {
        holder.actionType.setText(actionType);
    }


    @Override
    public void setActivityType(String activityType, NotificationViewHolder holder) {
        holder.activityType.setText(activityType);
    }

    /**
     * Description: sets time formatting for today notifications
     * @param notification: the notification to set time format of
     * @param holder: the holder for the row in recycler view
     *
     */


    @Override
    public void setTime(String time, NotificationViewHolder holder){
        holder.time.setText(time); //sets text to hours ago
    }

    @Override
    public void hideTime(NotificationViewHolder holder) {
        holder.time.setVisibility(View.GONE);
    }

    @Override
    public void setFullName(String fullName, NotificationViewHolder holder) {
        holder.fullName.setText(fullName);
    }
    /**
     * Description: sets time formatting for recent notifications
     * @param notification: the notification to set time format of
     * @param holder: the holder for the row in recycler view
     */

    @Override
    public void setImage(Bitmap bitmap, NotificationViewHolder holder){
        holder.profileImage.setImageBitmap(bitmap);
    }


    /**
     *
     * @return how many rows there will be based on how many notifications there are
     */
    @Override
    public int getItemCount() {
        return notifications.length();
    }


}