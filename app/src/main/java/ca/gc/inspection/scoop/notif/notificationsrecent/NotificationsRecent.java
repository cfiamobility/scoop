package ca.gc.inspection.scoop.notif.notificationsrecent;

import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import ca.gc.inspection.scoop.notif.notificationstoday.NotificationsToday;

/**
 * Data class which stores information for a single NotificationRecent Object
 * Should only interact with the Presenter as this class is a helper data class.
 * - Not an inner class of Presenter to simplify inheritance.
 *
 * Note: Inherits almost all methods from super class - please see NotificationsToday class for detailed documentation
 **/

public class NotificationsRecent extends NotificationsToday {

    public NotificationsRecent(JSONObject jsonNotification, JSONObject jsonImage){
        super(jsonNotification, jsonImage);
    }

    /**
     * Overrides superclass method
     * Gets the modified date from the notification JSONObject and parses into correct format to be displayed
     * Note: the reason for modifieddate as opposed to createddate is because users may unlike and relike posts/comments;
     *       when such an action is performed, the notification row is not removed from the table, but rather the active status is set to 0.
     *       If a post is reliked, the active status is set to 1 and we want the date/time of when this post/comment is liked once again.
     * @return
     */
    @Override
    public String getModifiedDate(){
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
}
