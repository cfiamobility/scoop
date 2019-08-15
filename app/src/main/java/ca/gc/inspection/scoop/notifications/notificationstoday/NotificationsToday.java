package ca.gc.inspection.scoop.notifications.notificationstoday;

import android.util.Log;

import org.json.JSONObject;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

import ca.gc.inspection.scoop.MyApplication;
import ca.gc.inspection.scoop.R;

/**
 * Data class which stores information for a single NotificationToday Object - a notification within 24 hours
 * Should only interact with the Presenter as this class is a helper data class.
 * - Not an inner class of Presenter to simplify inheritance.
 *
 * The data is stored in the JSON format provided by the database.
 * Static string keys are used to access the relevant values in the JSONObjects
 *
 * Warning: accessing missing values in the Json could retrieve a string spelling out "null" instead
 * of the empty string
 *
 * Note: all getter methods for JSONObject strings require a try catch statement
 **/

public class NotificationsToday {

    //JSONObject member variables - notification data and image data
    protected JSONObject mNotification, mImage;

    //Static string keys used to access values in JSONObjects
    public static final String NOTIFICATIONS_NOTIFIER_ID_KEY = "notifierid";
    public static final String NOTIFICATIONS_ACTIVITY_ID_KEY = "activityid";
    public static final String NOTIFICATIONS_MODIFIED_DATE_KEY= "modifieddate";
    public static final String NOTIFICATIONS_ACTIVITY_TYPE_KEY = "activitytype";
    public static final String NOTIFICATIONS_ACTIVITY_REFERENCE_KEY = "activityreference";
    public static final String NOTIFICATIONS_LIKE_TYPE_KEY = "liketype";
    public static final String NOTIFICATIONS_NOTIFIER_FIRSTNAME = "firstname";
    public static final String NOTIFICATIONS_NOTIFIER_LASTNAME = "lastname";
    public static final String NOTIFICATIONS_NOTIFIER_PROFILE_IMAGE = "profileimage";
    public static final String NOTIFICATIONS_POST_IMAGE = "postimage";

    /**
     * Public Constructor that assigns the JSONObjects from the Interactor network request to member variables
     * @param jsonNotification JSONObject that holds information to be displayed
     * @param jsonImage JSONObject that holds image data to be displayed - mainly profile images and possibly post images
     */
    public NotificationsToday(JSONObject jsonNotification, JSONObject jsonImage){
        mNotification = jsonNotification;
        mImage = jsonImage;
    }

    //TODO: Jared please document this one ahhahaahah
    public String getModifiedDate() {
        try {
            String modifiedDate = mNotification.getString(NOTIFICATIONS_MODIFIED_DATE_KEY); //gets when the notification was modified
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
            Log.i("TODAY SERVER TIME", String.valueOf(timestamp.getTime()));
            Log.i("TODAY APP TIME", String.valueOf(currentTimestamp.getTime()));
            long diff = currentTimestamp.getTime() - timestamp.getTime(); //gets the difference between the two timestamps
            Log.i("TODAY DIFF", String.valueOf(diff));
            Log.i("TODAY RAW", String.valueOf(((diff/(1000*60*60)))));
            String diffHours = String.valueOf((int) ((diff / (1000 * 60 * 60)))); //stores it in a string representing hours


            // if unit of time since notification creation is 1, then we don't use the pluralized form of "hours" or "minutes"
            switch(Integer.valueOf(diffHours)){ // switch case for hours
                case 0:
                    diffHours = String.valueOf((int) (TimeUnit.MILLISECONDS.toMinutes(diff)));
                    Log.i("TODAY MINUTES", String.valueOf(diffHours));
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

    /**
     * Gets the poster id of an activity from the notification JSONObject
     * This is the user id of the notifier - considered to be the user who's "creating" the notification
     * E.g. The user who is liking or commenting on a post
     * @return
     */
    public String getNotifierId(){
        try {
            return mNotification.getString(NOTIFICATIONS_NOTIFIER_ID_KEY);
        }
        catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    /**
     * Gets the first name of the notifier from the notification JSONObject
     * The notifier is considered to be the user who's "creating" the notification
     * E.g. The user who is liking or commenting on a post
     * @return string of first name
     */
    private String getNotifierFirstName() {
        try {
            return mNotification.getString(NOTIFICATIONS_NOTIFIER_FIRSTNAME);
        }
        catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    /**
     * Gets the last name of the notifier from the notification JSONObject
     * The notifier is considered to be the user who's "creating" the notification
     * E.g. The user who is liking or commenting on a post
     * @return string of first name
     */
    private String getNotifierLastName() {
        try {
            return mNotification.getString(NOTIFICATIONS_NOTIFIER_LASTNAME);
        }
        catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    /**
     * Method that concatenates the first and last names of the Notifier
     * If neither of the names are empty, then return with a space, otherwise simply return both
     * @return Concatenated string of first and last name
     */
    public String getValidFullName() {
        if (!getNotifierFirstName().equals("") && !getNotifierLastName().equals(""))
            return getNotifierFirstName() + " " + getNotifierLastName();
        return getNotifierFirstName() + getNotifierLastName();
    }

    /**
     * Gets the profile image of the notifier from the image JSONObject, which is in the format of a base-64 string
     * The notifier is considered to be the user who's "creating" the notification
     * E.g. The user who is liking or commenting on a post
     * @return base-64 string of the notifiers profile image
     */
    public String getNotifierProfileImage() {
        try {
            return mImage.getString(NOTIFICATIONS_NOTIFIER_PROFILE_IMAGE);
        }
        catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    /**
     * Gets the post image of the activity that is being "acted upon" from the image JSONObject
     * E.g. A post that is being liked or commented on
     * Note: String is empty if the activity is a comment, or the post does not have an image to display
     * @return base-64 string of the post image
     */
    public String getPostImage() {
        try {
            return mImage.getString(NOTIFICATIONS_POST_IMAGE);
        }
        catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    /**
     * Gets the action type of a notification based on the Like Type of the notification JSONObject
     * The action type is considered to be the action being "acted" on a post or comment
     * If the Like Type is 1 (meaning the post is liked), then return the string for a like action
     * Otherwise the post is unrelated to likes and return the string for a comment action
     * @return string of the action type for the notification
     */
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

    /**
     * Gets the activity type of a notification from the notification JSONObject
     * The activity type is considered to be the activity that is being "acted upon"
     * E.g. a post or a comment
     * @return string of the activity type for the notification
     */
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

    /**
     * Gets the activity id of a post/comment from the notification JSONObject
     * This is the activity id of the activity that is being "acted upon"
     * E.g. the post or comment that is being liked
     * @return string of the activity id
     */
    public String getActivityId(){
        try {
            return mNotification.getString(NOTIFICATIONS_ACTIVITY_ID_KEY);
        }
        catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    /**
     * Gets the reference activity id of a comment from the notification JSONObject
     * This is the activity id of a post that is being commented on and is used strictly for referencing
     * the post that is being commented on in a notification
     * @return
     */
    public String getActivityReferenceId(){
        try {
            return mNotification.getString(NOTIFICATIONS_ACTIVITY_REFERENCE_KEY);
        }
        catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

}
