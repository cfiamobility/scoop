package ca.gc.inspection.scoop;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonArrayRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class NotificationsScreenAdapter extends RecyclerView.Adapter<NotificationViewHolder> implements NotificationAdapterController.NotificationAdapterInterface{
    private JSONArray notifications;
    private RequestQueue requestQueue;

    private String timeType;
    private Timestamp currentTime;

    /**
     *
     * @param notifs: represents the JSONArray for all the notifications relevant to the user
     * @param requestQueue: represents the requestQueue used for adding future requests to
     * @param context: represents the context of the activity which is relevant to the adapter
     * @param timeType: represents whether the type of time which should be displayed is for today or for recent notifications
     */
    NotificationsScreenAdapter(JSONArray notifs, RequestQueue requestQueue, String timeType, Timestamp currentTime){
        this.notifications = notifs;
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
        NotificationAdapterController controller = new NotificationAdapterController(holder, i, this, notifications, currentTime, timeType, requestQueue);
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
    public void hideActionType(NotificationViewHolder holder) {
        holder.actionType.setVisibility(View.GONE);
    }

    @Override
    public void setActivityType(String activityType, NotificationViewHolder holder) {
        holder.activityType.setText(activityType);
    }

    @Override
    public void hideActivityType(NotificationViewHolder holder) {
        holder.activityType.setVisibility(View.GONE);
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



    /**
     *
     * @return how many rows there will be based on how many notifications there are
     */
    @Override
    public int getItemCount() {
        return notifications.length();
    }


}