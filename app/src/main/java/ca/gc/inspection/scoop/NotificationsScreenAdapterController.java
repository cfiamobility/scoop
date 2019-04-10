package ca.gc.inspection.scoop;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.ImageView;

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

public class NotificationsScreenAdapterController {
    private JSONObject notification, image;
    private NotificationViewHolder holder;
    private NotificationAdapterInterface notificationAdapterInterface;
    private Map<String, String> ids;
    private Timestamp currentTime;
    private String timeType;

    public NotificationsScreenAdapterController(NotificationViewHolder holder, int i, NotificationAdapterInterface notificationAdapterInterface, JSONArray notifications, JSONArray images, Timestamp currentTime, String timeType){
        this.holder = holder;
        this.notificationAdapterInterface = notificationAdapterInterface;
        try {
            this.notification = notifications.getJSONObject(i);
            this.image = images.getJSONObject(i);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        this.ids = new HashMap<>();
        this.currentTime = currentTime;
        this.timeType = timeType;
    }

    /**
     * Description: displays all notifications
     * @throws JSONException
     */
    public void displayNotifications() throws JSONException {

        new TimeTask().execute(timeType); //executes setting time in a background task thread

        if(!notification.getString("activityid").equals("null")) { //for notifications with an activity id
           setActivity(); //sets activity notification
           setProfileImage(image.getString("activityprofileimage"), holder); //sets profile image
        }else{ //for notifications with a likeid
           setLikes(); //sets like notification
           setProfileImage(image.getString("likesprofileimage"), holder); //sets profile image
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
     * @return time string or null if there isn't a proper date
     */
    private String setTodayTime(){
        try {
            String createdDate = notification.getString("createddate"); //gets when the notification was created
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"); //formats the date accordingly
            Date parsedDate = dateFormat.parse(createdDate); //parses the created date to be in specified date format
            Timestamp timestamp = new Timestamp(parsedDate.getTime()); //creates a timestamp from the date
            long diff = currentTime.getTime() - timestamp.getTime(); //gets the difference between the two timestamps
            String diffHours = String.valueOf((int)((diff/(1000*60*60)))); //stores it in a string representing hours
            return diffHours + " hours ago";
        }catch(Exception e){
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Description: gets when notification was created and returns in proper string format
     * @return time string or null if there isn't a proper date
     */
    private String setRecentTime(){
        try {
            String time = notification.getString("createddate"); //gets when notification was created
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"); //formats the date accordingly
            Date parsedDate = dateFormat.parse(time); //parses the created date to be in specified date format
            DateFormat properDateFormat = new SimpleDateFormat("MMMM d 'at' h:mm a"); //formats the date to be how we want it to output
            return properDateFormat.format(parsedDate);
        }catch(Exception e){
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Description: sets the notification if it's an activity
     * @throws JSONException
     */
    private void setActivity() throws JSONException{
        final String[] actionTypeResponses = new String[]{"X", "posted", "commented on"}; //the possible actionType responses for activityid
        final String referenceActivityId = notification.getString("activityactivityreference"); //gets the id for which the activity is referencing towards
        final int activityType = Integer.parseInt(notification.getString("activityactivitytype")); //gets the activity's activity type
        ids.put("userid", notification.getString("userid")); //puts the corresponding user id into the map
        checkReferenceActivityId(referenceActivityId, notification); //checks reference activity id
        new NameTask().execute(notification.getString("activityfirstname"), notification.getString("activitylastname"));
        notificationAdapterInterface.setActionType(actionTypeResponses[activityType], holder); //sets the action type for corresponding activity type
    }

    /**
     * Description: sets the notification if it's a like
     * @throws JSONException
     */
    private void setLikes() throws JSONException{
        final int activityType = Integer.parseInt(notification.getString("likesactivitytype")); //gets the activity's activity type
        ids.put("userid", notification.getString("likesuserid")); //putting the user id of the like into ids
        checkActivityType(activityType, notification); //checks activity type
        new NameTask().execute(notification.getString("likesfirstname"), notification.getString("likeslastname"));
        notificationAdapterInterface.setActionType("liked", holder); //sets action type as liked
    }

    /**
     * Description: sets the profile image for corresponding notification
     * @param image: the string representation of the image
     * @param holder: the holder for the item in recycler view
     */
    private void setProfileImage(String image, NotificationViewHolder holder) {
        Bitmap bitmap = MyCamera.stringToBitmap(image); //converts image string to bitmap
        notificationAdapterInterface.setImage(bitmap, holder); //sets image for the holder
        holder.profileImage.setOnClickListener(new View.OnClickListener() { //on click for the image
            @Override
            public void onClick(View view) {
                goToProfile(ids); //goes to the profile on click
            }
        });
    }

    /**
     * Description: goes to the user profile indicated
     * @param ids: representing the user id and the activity id
     */
    private void goToProfile(Map<String, String> ids){
        Intent intent = new Intent(MyApplication.getContext(), userprofile.class);
        intent.putExtra("userid", ids.get("userid")); //puts the user id into the intent
        MyApplication.getContext().startActivity(intent); //changes to the user profile activity
    }

    /**
     * Description: goes to the post indicated
     * @param ids: representing the user id and the activity id
     */
    private void goToPost(Map<String, String> ids){
        Intent intent = new Intent(MyApplication.getContext(), post.class);
        intent.putExtra("activityid", ids.get("activityid")); //puts the activity id into the intent
        MyApplication.getContext().startActivity(intent); //changes to the post activity
    }

    /**
     * Description: checks reference activity id and sets activity type accordingly
     * @param referenceActivityId: activity id that is referenced by activity
     * @param activityResponse: response related to the activity
     * @throws JSONException
     */
    private void checkReferenceActivityId(String referenceActivityId, JSONObject activityResponse) throws JSONException {
        final String[] activityTypeResponses = new String[]{"your post", "a new post"}; //the possible activityType responses for activityid
        if(!referenceActivityId.equals("null")) { //if there is a reference activity id (the activity is not a post)
            notificationAdapterInterface.setActivityType(activityTypeResponses[0], holder); //sets the activity type response
            ids.put("activityid", activityResponse.getString("activityactivityreference")); //puts the activity reference id into the ids map

        }else{ //if there is no reference activity id (the activity is a post)
            notificationAdapterInterface.setActivityType(activityTypeResponses[1], holder); //sets the activity type response
            ids.put("activityid", activityResponse.getString("activityactivityid")); //puts the activity id into the ids map

        }
    }

    /**
     * Description: checks the activity type and sets activity type accordingly
     * @param activityType: the activity type of the post comment reply
     * @param likeResponse: response related to the like
     * @throws JSONException
     */
    private void checkActivityType(int activityType, JSONObject likeResponse) throws JSONException {
        final String[] activityTypeResponse = new String[]{"X", "your post", "your comment"}; //the possible activityType responses for userid
        if(activityType == 1){ //if the activity is a post
            ids.put("activityid", likeResponse.getString("likesactivityid")); //enter the activity id of the post into ids
        }else { //if the activity is a comment
            ids.put("activityid", likeResponse.getString("likesactivityreference")); //putting the activity reference of the activity which is a post
        }
        notificationAdapterInterface.setActivityType(activityTypeResponse[activityType], holder); //setting text based on activityTypeResponse
    }

    /**
     * Description: checks the first name to see if it is valid
     * @param firstName: the first name of user
     * @return first name if not null
     */
    private String checkFirstName(String firstName){
        if(!firstName.equals("null")){ //if there is a first name returned
            return firstName;
        }else{ //if the first name is not filled
            return "";
        }
    }

    /**
     * Description: checks last name to see if it is valid
     * @param lastName: the last name of user
     * @return last name if not null
     */
    private String checkLastName(String lastName){
        if(!lastName.equals("null")){ //if there is a last name returned
            return " " + lastName;
        }else{ //if the last name is not filled
            return "";
        }
    }

    /**
     * Description: the notification adapter interface to set all the layouts properly
     */
    public interface NotificationAdapterInterface{
        void setActionType(String actionType, NotificationViewHolder holder);
        void setActivityType(String activityType, NotificationViewHolder holder);
        void setTime(String time, NotificationViewHolder holder);
        void hideTime(NotificationViewHolder holder);
        void setFullName(String fullName, NotificationViewHolder holder);
        void setImage(Bitmap bitmap, NotificationViewHolder holder);
    }

    /**
     * Description: the setting of the time to be performed in the background
     */
    private class TimeTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... timeType) {
            String time;
            if(timeType[0].equals("today")){ //for today notifications, sets appropriate time formatting
                time = setTodayTime();

            }else{ //for recent notifications, sets appropriate time formatting
                time = setRecentTime();
            }
            return time;
        }

        @Override
        protected void onPostExecute(String time){
            if(time !=null) {
                notificationAdapterInterface.setTime(time, holder); //sets time to what is given
            }else{
                notificationAdapterInterface.hideTime(holder); //sets visibility to gone
            }
        }
    }

    /**
     * Description: the checking of names to be performed in background
     */
    private class NameTask extends AsyncTask<String, Void, String>{
        @Override
        protected String doInBackground(String... names){
            return checkFirstName(names[0]) + checkLastName(names[1]); //returns full name
        }

        @Override
        protected void onPostExecute(String fullName){
            notificationAdapterInterface.setFullName(fullName, holder); //sets full name
            holder.fullName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    goToProfile(ids); //goes to profile on click
                }
            });
        }
    }


}
