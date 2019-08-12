package ca.gc.inspection.scoop.notif.notificationstoday;

import android.util.Log;

import org.json.JSONObject;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

import ca.gc.inspection.scoop.MyApplication;
import ca.gc.inspection.scoop.R;

public class NotificationsToday {

    protected JSONObject mNotification, mImage;

    public static final String NOTIFICATIONS_POSTER_ID_KEY = "posterid";
    public static final String NOTIFICATIONS_ACTIVITY_ID_KEY = "activityid";
    public static final String NOTIFICATIONS_MODIFIED_DATE_KEY= "modifieddate";

    public static final String NOTIFICATIONS_ACTIVITY_TYPE_KEY = "activitytype";
    public static final String NOTIFICATIONS_ACTIVITY_REFERENCE_KEY = "activityreference";
    public static final String NOTIFICATIONS_LIKE_TYPE_KEY = "liketype";
    public static final String NOTIFICATIONS_NOTIFIER_FIRSTNAME = "firstname";
    public static final String NOTIFICATIONS_NOTIFIER_LASTNAME = "lastname";
    public static final String NOTIFICATIONS_NOTIFIER_PROFILE_IMAGE = "profileimage";
    public static final String NOTIFICATIONS_POST_IMAGE = "postimage";

    public NotificationsToday(JSONObject jsonNotification, JSONObject jsonImage){
        mNotification = jsonNotification;
        mImage = jsonImage;
    }

    public String getModifiedDate() {
        try {
            String modifiedDate = mNotification.getString(NOTIFICATIONS_MODIFIED_DATE_KEY); //gets when the notification was created/modified
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"); //formats the date accordingly
            dateFormat.setTimeZone(TimeZone.getTimeZone("UTC")); // set timezone format of timestamp from server to UTC.
            // NOTE: time zone of database is EDT, but, when queried, the database returns timestamps in UTC
            Date parsedDate = dateFormat.parse(modifiedDate); //parses the created date to be in specified date format
            SimpleDateFormat currentTimeFormat = new SimpleDateFormat();
            TimeZone gmtTime = TimeZone.getTimeZone("GMT");
            currentTimeFormat.setTimeZone(gmtTime);
            String date = currentTimeFormat.format(new Date());
            Date currentTime = currentTimeFormat.parse(date);
            Timestamp currentTimestamp = new Timestamp(currentTime.getTime());
            Log.i("postdate", date);
            Log.i("realpostdate", parsedDate.toString());
            Timestamp timestamp = new Timestamp(parsedDate.getTime()); //creates a timestamp from the date
            Log.i("SERVER TIME", String.valueOf(timestamp.getTime()));
            Log.i("APP TIME", String.valueOf(currentTimestamp.getTime()));
            long diff = currentTimestamp.getTime() - timestamp.getTime(); //gets the difference between the two timestamps
            Log.i("RAW", String.valueOf(((diff/(1000*60*60)))));
            String diffHours = String.valueOf((int) ((diff / (1000 * 60 * 60)))); //stores it in a string representing hours


            // if unit of time since notification creation is 1, then we don't use the pluralized form of "hours" or "minutes"
            switch(Integer.valueOf(diffHours)){ // switch case for hours
                case 0:
                    diffHours = String.valueOf((int) (TimeUnit.MILLISECONDS.toMinutes(diff)));
                    switch (Integer.valueOf(diffHours)){ // switch case for minutes
                        case 0:
                            return MyApplication.getContext().getResources().getString(R.string.just_now);
                        case 1:
                            return diffHours + " " + MyApplication.getContext().getResources().getString(R.string.minute_ago);
                        default:
                            return diffHours + " " + MyApplication.getContext().getResources().getString(R.string.minutes_ago);
                    }
                case 1:
                    return diffHours + " " + MyApplication.getContext().getResources().getString(R.string.hour_ago);
                default:
                    return diffHours + " " + MyApplication.getContext().getResources().getString(R.string.hours_ago);
            }

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    public String getValidFullName() {
        try {
            if (!getNotifierFirstName().equals("") && !getNotifierLastName().equals(""))
            return getNotifierFirstName() + " " + getNotifierLastName();
        return getNotifierFirstName() + getNotifierLastName();
        }
        catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    private String getNotifierFirstName() {
        try {
            return mNotification.getString(NOTIFICATIONS_NOTIFIER_FIRSTNAME);
        }
        catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    private String getNotifierLastName() {
        try {
            return mNotification.getString(NOTIFICATIONS_NOTIFIER_LASTNAME);
        }
        catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }


    public String getNotifierProfileImage() {
        try {
            return mImage.getString(NOTIFICATIONS_NOTIFIER_PROFILE_IMAGE);
        }
        catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public String getPostImage() {
        try {
            return mImage.getString(NOTIFICATIONS_POST_IMAGE);
        }
        catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public String getActionType() {
        try {
            if (mNotification.getString(NOTIFICATIONS_LIKE_TYPE_KEY).equals("1")){
                return "liked your";
            } else {
                return "commented on your";
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public String getActivityType(){
        try {
            int activityType = Integer.parseInt(mNotification.getString(NOTIFICATIONS_ACTIVITY_TYPE_KEY));
            if (activityType == 1){
                return "post";
            } else if (activityType == 2){
                return "comment";
            }
            return "";
        }
        catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public String getActivityId(){
        try {
            return mNotification.getString(NOTIFICATIONS_ACTIVITY_ID_KEY);
        }
        catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public String getActivityReferenceId(){
        try {
            return mNotification.getString(NOTIFICATIONS_ACTIVITY_REFERENCE_KEY);
        }
        catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public String getPosterId(){
        try {
            return mNotification.getString(NOTIFICATIONS_POSTER_ID_KEY);
        }
        catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

}
