package ca.gc.inspection.scoop.notifications;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

import ca.gc.inspection.scoop.MainActivity;
import ca.gc.inspection.scoop.MyApplication;
import ca.gc.inspection.scoop.util.CameraUtils;
import ca.gc.inspection.scoop.*;
import ca.gc.inspection.scoop.Post;

/**
 * Controls logic of notifications recyclerView items/view holders
 */

public class NotificationsAdapterController {
    private JSONObject notification, image;
    private NotificationsAdapter.NotificationViewHolder holder;
    //private NotificationsAdapterInterface notificationsAdapterContract;
    private NotificationsAdapterInterface.View notificationsAdapterInterface;
    private Map<String, String> ids;
    private Timestamp currentTime;
    private String timeType;
    private Context context;

    public NotificationsAdapterController(NotificationsAdapter.NotificationViewHolder holder, int i, NotificationsAdapterInterface.View notificationsAdapterInterface, JSONArray notifications, JSONArray images, Timestamp currentTime, String timeType, Context context) {
        this.holder = holder;
        this.notificationsAdapterInterface = notificationsAdapterInterface;
        try {
            this.notification = notifications.getJSONObject(i);
            this.image = images.getJSONObject(i);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        this.ids = new HashMap<>();
        this.currentTime = currentTime;
        this.timeType = timeType;
        this.context = context;
    }

    /**
     * Description: displays all notifications
     *
     * @throws JSONException
     */
    public void displayNotifications() throws JSONException {

        new TimeTask().execute(timeType); //executes setting time in a background task thread

        if (!notification.getString("activityid").equals("null")) { //for notifications with an activity id
            setActivity(); //sets activity notification

            if(image != null) {
                setProfileImage(image.getString("activityprofileimage")); //sets profile image
            }else{
                notificationsAdapterInterface.hideImage(holder);
            }
        } else { //for notifications with a likeid
            setLikes(); //sets like notification

            if(image !=null) {
                setProfileImage(image.getString("likesprofileimage")); //sets profile image
            }else{
                notificationsAdapterInterface.hideImage(holder);
            }
        }

        holder.activityType.setOnClickListener(new View.OnClickListener() { //sets on click listener for when activity type is clicked
            @Override
            public void onClick(View view) {
                goToPost(ids); //calls goToPost
            }
        });

    }

    /**
     * Description: gets when the notification was created and returns in proper string format
     *
     * @return time string or null if there isn't a proper date
     */
    private String setTodayTime() {
        try {
            String createdDate = notification.getString("createddate"); //gets when the notification was created
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"); //formats the date accordingly
            dateFormat.setTimeZone(TimeZone.getTimeZone("UTC")); // set timezone format of timestamp from server to UTC.
                // NOTE: time zone of database is EDT, but, when queried, the database returns timestamps in UTC
            Date parsedDate = dateFormat.parse(createdDate); //parses the created date to be in specified date format
            SimpleDateFormat currentTimeFormat = new SimpleDateFormat();
            TimeZone gmtTime = TimeZone.getTimeZone("GMT");
            currentTimeFormat.setTimeZone(gmtTime);
            String date = currentTimeFormat.format(new Date());
            Date currentTime = currentTimeFormat.parse(date);
            Timestamp currentTimestamp = new Timestamp(currentTime.getTime());
            Log.i("postdate", date);
            Log.i("realpostdate", parsedDate.toString());
            Timestamp timestamp = new Timestamp(parsedDate.getTime()); //creates a timestamp from the date
            Log.i("time", String.valueOf(timestamp.getTime()));
            Log.i("hello", String.valueOf(currentTimestamp.getTime()));
            long diff = currentTimestamp.getTime() - timestamp.getTime(); //gets the difference between the two timestamps
            Log.i("helloo", String.valueOf(((diff/(1000*60*60)))));
            String diffHours = String.valueOf((int) ((diff / (1000 * 60 * 60)))); //stores it in a string representing hours


            // if unit of time since notification creation is 1, then we don't use the pluralized form of "hours" or "minutes"
            switch(Integer.valueOf(diffHours)){ // switch case for hours
                case 0:
                    diffHours = String.valueOf((int) (TimeUnit.MILLISECONDS.toMinutes(diff)));
                    switch (Integer.valueOf(diffHours)){ // switch case for minutes
                        case 0:
                            return context.getString(R.string.just_now);
                        case 1:
                            return diffHours + " " + context.getString(R.string.minute_ago);
                        default:
                            return diffHours + " " + context.getString(R.string.minutes_ago);
                    }
                case 1:
                    return diffHours + " " + context.getString(R.string.hour_ago);
                default:
                    return diffHours + " " + context.getString(R.string.hours_ago);
            }

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Description: gets when notification was created and returns in proper string format
     *
     * @return time string or null if there isn't a proper date
     */
    private String setRecentTime() {
        try {
            String time = notification.getString("createddate"); //gets when notification was created
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"); //formats the date accordingly
            Date parsedDate = dateFormat.parse(time); //parses the created date to be in specified date format
            DateFormat properDateFormat = new SimpleDateFormat("MMMM d 'at' h:mm a"); //formats the date to be how we want it to output
            return properDateFormat.format(parsedDate);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Description: sets the notification if it's an activity
     *
     * @throws JSONException
     */
    private void setActivity() throws JSONException {
        final String[] actionTypeResponses = new String[]{"X", "posted", "commented on"}; //the possible actionType responses for activityid
        final String referenceActivityId = notification.getString("activityactivityreference"); //gets the id for which the activity is referencing towards
        final int activityType = Integer.parseInt(notification.getString("activityactivitytype")); //gets the activity's activity type
        ids.put("userid", notification.getString("userid")); //puts the corresponding user id into the map
        checkReferenceActivityId(referenceActivityId, notification); //checks reference activity id
        new NameTask().execute(notification.getString("activityfirstname"), notification.getString("activitylastname"));
        notificationsAdapterInterface.setActionType(actionTypeResponses[activityType], holder); //sets the action type for corresponding activity type
    }

    /**
     * Description: sets the notification if it's a like
     *
     * @throws JSONException
     */
    private void setLikes() throws JSONException {
        final int activityType = Integer.parseInt(notification.getString("likesactivitytype")); //gets the activity's activity type
        ids.put("userid", notification.getString("likesuserid")); //putting the user id of the like into ids
        checkActivityType(activityType, notification); //checks activity type
        new NameTask().execute(notification.getString("likesfirstname"), notification.getString("likeslastname"));
        notificationsAdapterInterface.setActionType(MyApplication.getContext().getResources().getString(R.string.liked), holder); //sets action type as liked
    }

    /**
     * Description: sets the profile image for corresponding notification
     *
     * @param image:  the string representation of the image
     *
     */
    private void setProfileImage(String image) {
        Bitmap bitmap = CameraUtils.stringToBitmap(image); //converts image string to bitmap
        notificationsAdapterInterface.setImage(bitmap, holder); //sets image for the holder
        holder.profileImage.setOnClickListener(new View.OnClickListener() { //on click for the image
            @Override
            public void onClick(View view) {
                MainActivity.otherUserClicked(ids.get("userid"));
            }
        });
    }

    /**
     * Description: goes to the Post indicated
     *
     * @param ids: representing the user id and the activity id
     */
    private void goToPost(Map<String, String> ids) {
        Intent intent = new Intent(MyApplication.getContext(), Post.class);
        intent.putExtra("activityid", ids.get("activityid")); //puts the activity id into the intent
        MyApplication.getContext().startActivity(intent); //changes to the Post activity
    }

    /**
     * Description: checks reference activity id and sets activity type accordingly
     *
     * @param referenceActivityId: activity id that is referenced by activity
     * @param activityResponse:    response related to the activity
     * @throws JSONException
     */
    private void checkReferenceActivityId(String referenceActivityId, JSONObject activityResponse) throws JSONException {
        final String[] activityTypeResponses = new String[]{"your post", "a new post"}; //the possible activityType responses for activityid
        if (!referenceActivityId.equals("null")) { //if there is a reference activity id (the activity is not a post)
            notificationsAdapterInterface.setActivityType(activityTypeResponses[0], holder); //sets the activity type response
            ids.put("activityid", activityResponse.getString("activityactivityreference")); //puts the activity reference id into the ids map

        } else { //if there is no reference activity id (the activity is a post)
            notificationsAdapterInterface.setActivityType(activityTypeResponses[1], holder); //sets the activity type response
            ids.put("activityid", activityResponse.getString("activityactivityid")); //puts the activity id into the ids map

        }
    }

    /**
     * Description: checks the activity type and sets activity type accordingly
     *
     * @param activityType: the activity type of the Post comment reply
     * @param likeResponse: response related to the like
     * @throws JSONException
     */
    private void checkActivityType(int activityType, JSONObject likeResponse) throws JSONException {
        final String[] activityTypeResponse = new String[]{"X", "your Post", "your comment"}; //the possible activityType responses for userid
        if (activityType == 1) { //if the activity is a Post
            ids.put("activityid", likeResponse.getString("likesactivityid")); //enter the activity id of the Post into ids
        } else { //if the activity is a comment
            ids.put("activityid", likeResponse.getString("likesactivityreference")); //putting the activity reference of the activity which is a Post
        }
        notificationsAdapterInterface.setActivityType(activityTypeResponse[activityType], holder); //setting text based on activityTypeResponse
    }

    /**
     * Description: checks the first name to see if it is valid
     *
     * @param firstName: the first name of user
     * @return first name if not null
     */
    private String checkFirstName(String firstName) {
        if (!firstName.equals("null")) { //if there is a first name returned
            return firstName;
        } else { //if the first name is not filled
            return "";
        }
    }

    /**
     * Description: checks last name to see if it is valid
     *
     * @param lastName: the last name of user
     * @return last name if not null
     */
    private String checkLastName(String lastName) {
        if (!lastName.equals("null")) { //if there is a last name returned
            return " " + lastName;
        } else { //if the last name is not filled
            return "";
        }
    }


    /**
     * Description: the setting of the time to be performed in the background
     */
    private class TimeTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... timeType) {
            String time;
            if (timeType[0].equals("today")) { //for today notifications, sets appropriate time formatting
                time = setTodayTime();

            } else { //for recent notifications, sets appropriate time formatting
                time = setRecentTime();
            }
            return time;
        }

        @Override
        protected void onPostExecute(String time) {
            if (time != null) {
                notificationsAdapterInterface.setTime(time, holder); //sets time to what is given
            } else {
                notificationsAdapterInterface.hideTime(holder); //sets visibility to gone
            }
        }
    }

    /**
     * Description: the checking of names to be performed in background
     */
    private class NameTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... names) {
            return checkFirstName(names[0]) + checkLastName(names[1]); //returns full name
        }

        @Override
        protected void onPostExecute(String fullName) {
            notificationsAdapterInterface.setFullName(fullName, holder); //sets full name
            holder.fullName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    MainActivity.otherUserClicked(ids.get("userid"));
                }
            });
        }
    }


}
