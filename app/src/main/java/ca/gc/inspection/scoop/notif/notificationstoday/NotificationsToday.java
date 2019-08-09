package ca.gc.inspection.scoop.notif.notificationstoday;

import android.util.Log;

import org.json.JSONObject;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

public class NotificationsToday {

    protected JSONObject mNotification, mImage;

    public static final String NOTIFICATIONS_USER_ID_KEY = "currentuser";
    public static final String NOTIFICATIONS_ACTIVITY_ID_KEY = "activityid";
    public static final String NOTIFICATIONS_MODIFIED_DATE_KEY= "modifieddate";

    public static final String NOTIFICATIONS_ACTIVITY_TYPE_KEY = "activitytype";
    public static final String NOTIFICATIONS_ACTIVITY_REFERENCE_KEY = "activityreference";
    public static final String NOTIFICATIONS_LIKE_TYPE_KEY = "liketype";
    public static final String NOTIFICATIONS_NOTIFIER_FIRSTNAME = "firstname";
    public static final String NOTIFICATIONS_NOTIFIER_LASTNAME = "lastname";
    public static final String NOTIFICATIONS_NOTIFIER_PROFILE_IMAGE = "profileimage";

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
            Log.i("time", String.valueOf(timestamp.getTime()));
            Log.i("hello", String.valueOf(currentTimestamp.getTime()));
            long diff = currentTimestamp.getTime() - timestamp.getTime(); //gets the difference between the two timestamps
            Log.i("helloo", String.valueOf(((diff/(1000*60*60)))));
            String diffHours = String.valueOf((int) ((diff / (1000 * 60 * 60)))); //stores it in a string representing hours


//            // if unit of time since notification creation is 1, then we don't use the pluralized form of "hours" or "minutes"
//            switch(Integer.valueOf(diffHours)){ // switch case for hours
//                case 0:
//                    diffHours = String.valueOf((int) (TimeUnit.MILLISECONDS.toMinutes(diff)));
//                    switch (Integer.valueOf(diffHours)){ // switch case for minutes
//                        case 0:
//                            return getString(R.string.just_now);
//                        case 1:
//                            return diffHours + " " + getString(R.string.minute_ago);
//                        default:
//                            return diffHours + " " + getString(R.string.minutes_ago);
//                    }
//                case 1:
//                    return diffHours + " " + getString(R.string.hour_ago);
//                default:
//                    return diffHours + " " + getString(R.string.hours_ago);
//            }

            // if unit of time since notification creation is 1, then we don't use the pluralized form of "hours" or "minutes"
            switch(Integer.valueOf(diffHours)){ // switch case for hours
                case 0:
                    diffHours = String.valueOf((int) (TimeUnit.MILLISECONDS.toMinutes(diff)));
                    switch (Integer.valueOf(diffHours)){ // switch case for minutes
                        case 0:
                            return "just now";
                        case 1:
                            return diffHours + " " + "minute ago";
                        default:
                            return diffHours + " " + "minutes ago";
                    }
                case 1:
                    return diffHours + " " + "hour ago";
                default:
                    return diffHours + " " + "hours ago";
            }

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private String setRecentTime() {
        try {
            String time = mNotification.getString("modifieddate"); //gets when notification was created/modified
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"); //formats the date accordingly
            Date parsedDate = dateFormat.parse(time); //parses the created date to be in specified date format
            DateFormat properDateFormat = new SimpleDateFormat("MMMM d 'at' h:mm a"); //formats the date to be how we want it to output
            return properDateFormat.format(parsedDate);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private String setTodayTime() {
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
            Log.i("time", String.valueOf(timestamp.getTime()));
            Log.i("hello", String.valueOf(currentTimestamp.getTime()));
            long diff = currentTimestamp.getTime() - timestamp.getTime(); //gets the difference between the two timestamps
            Log.i("helloo", String.valueOf(((diff/(1000*60*60)))));
            String diffHours = String.valueOf((int) ((diff / (1000 * 60 * 60)))); //stores it in a string representing hours


//            // if unit of time since notification creation is 1, then we don't use the pluralized form of "hours" or "minutes"
//            switch(Integer.valueOf(diffHours)){ // switch case for hours
//                case 0:
//                    diffHours = String.valueOf((int) (TimeUnit.MILLISECONDS.toMinutes(diff)));
//                    switch (Integer.valueOf(diffHours)){ // switch case for minutes
//                        case 0:
//                            return getString(R.string.just_now);
//                        case 1:
//                            return diffHours + " " + getString(R.string.minute_ago);
//                        default:
//                            return diffHours + " " + getString(R.string.minutes_ago);
//                    }
//                case 1:
//                    return diffHours + " " + getString(R.string.hour_ago);
//                default:
//                    return diffHours + " " + getString(R.string.hours_ago);
//            }

            // if unit of time since notification creation is 1, then we don't use the pluralized form of "hours" or "minutes"
            switch(Integer.valueOf(diffHours)){ // switch case for hours
                case 0:
                    diffHours = String.valueOf((int) (TimeUnit.MILLISECONDS.toMinutes(diff)));
                    switch (Integer.valueOf(diffHours)){ // switch case for minutes
                        case 0:
                            return "just now";
                        case 1:
                            return diffHours + " " + "minute ago";
                        default:
                            return diffHours + " " + "minutes ago";
                    }
                case 1:
                    return diffHours + " " + "hour ago";
                default:
                    return diffHours + " " + "hours ago";
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


}
