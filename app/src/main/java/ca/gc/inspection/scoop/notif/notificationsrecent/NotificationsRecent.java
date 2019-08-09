package ca.gc.inspection.scoop.notif.notificationsrecent;

import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import ca.gc.inspection.scoop.notif.notificationstoday.NotificationsToday;

public class NotificationsRecent extends NotificationsToday {

    public NotificationsRecent(JSONObject jsonNotification, JSONObject jsonImage){
        super(jsonNotification, jsonImage);
    }

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
