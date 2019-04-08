package ca.gc.inspection.scoop;

import android.content.Intent;
import android.graphics.Bitmap;
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

public class NotificationAdapterController {
    private JSONObject notification;
    private NotificationViewHolder holder;
    private NotificationAdapterInterface notificationAdapterInterface;
    private Map<String, String> ids;
    private Timestamp currentTime;
    private String timeType;
    private RequestQueue requestQueue;

    public NotificationAdapterController(NotificationViewHolder holder, int i, NotificationAdapterInterface notificationAdapterInterface, JSONArray response, Timestamp currentTime, String timeType, RequestQueue requestQueue){
        this.holder = holder;
        this.notificationAdapterInterface = notificationAdapterInterface;
        try {
            this.notification = response.getJSONObject(i);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        this.ids = new HashMap<>();
        this.currentTime = currentTime;
        this.timeType = timeType;
        this.requestQueue = requestQueue;
    }

    public void displayNotifications() throws JSONException {
        if(timeType.equals("today")){ //for today notifications, sets appropriate time formatting
            setTodayTime();
        }else{ //for recent notifications, sets appropriate time formatting
            setRecentTime();
        }
        if(!notification.getString("activityid").equals("null")) { //for notifications with an activity id
            getActivity(notification.getString("activityid"), null);
        }else{
            getLikes();
        }

    }

    private void setTodayTime(){
        try {
            String createdDate = notification.getString("createddate"); //gets when the notification was created
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"); //formats the date accordingly
            Date parsedDate = dateFormat.parse(createdDate); //parses the created date to be in specified date format
            Timestamp timestamp = new Timestamp(parsedDate.getTime()); //creates a timestamp from the date
            long diff = currentTime.getTime() - timestamp.getTime(); //gets the difference between the two timestamps
            String diffHours = String.valueOf((int)((diff/(1000*60*60)))); //stores it in a string representing hours
            notificationAdapterInterface.setTime(diffHours + " hours ago", holder);

        }catch(Exception e){
            e.printStackTrace();
            notificationAdapterInterface.hideTime(holder); //sets visibility to gone
        }
    }

    private void setRecentTime(){
        try {
            String time = notification.getString("createddate"); //gets when notification was created
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"); //formats the date accordingly
            Date parsedDate = dateFormat.parse(time); //parses the created date to be in specified date format
            DateFormat properDateFormat = new SimpleDateFormat("MMMM d 'at' h:mm a"); //formats the date to be how we want it to output
            notificationAdapterInterface.setTime(properDateFormat.format(parsedDate), holder); //sets text to appropriate formatted date
        }catch(Exception e){
            e.printStackTrace();
            notificationAdapterInterface.hideTime(holder); //sets visibility to gone
        }
    }

    private void getActivity(String activityId, final JSONObject likeResponse) throws JSONException {
        final String[] actionTypeResponses = new String[]{"X", "posted", "commented on"}; //the possible actionType responses for activityid
        String activityURL = Config.baseIP + "notifications/activitynotifs/" + activityId; //url to get activity notifications
        JsonArrayRequest activityRequest = new JsonArrayRequest(Request.Method.GET, activityURL, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                try {
                    JSONObject activityResponse = response.getJSONObject(0); //gets the JSONobject from the JSONarray
                    final String referenceActivityId = activityResponse.getString("activityreference"); //gets the id for which the activity is referencing towards
                    final int activityType = Integer.parseInt(activityResponse.getString("activitytype")); //gets the activity's activity type
                    String fullName;

                    if(likeResponse == null){
                        ids.put("userid", activityResponse.getString("userid")); //puts the corresponding user id into the map
                        checkReferenceActivityId(referenceActivityId, activityResponse);
                        //getUserImage(activityResponse.getString("userid"), holder); //gets the user image based on the user id
                        fullName = checkFirstName(activityResponse.getString("firstname")) + checkLastName(activityResponse.getString("lastname"));
                        notificationAdapterInterface.setActionType(actionTypeResponses[activityType], holder); //sets the action type for corresponding activity type
                    }else{
                        ids.put("userid", likeResponse.getString("userid")); //putting the user id of the like into ids
                        checkActivityType(activityType, activityResponse);
                        //getUserImage(likeResponse.get("userid"), holder);
                        fullName = checkFirstName(likeResponse.getString("firstname")) + checkLastName(likeResponse.getString("lastname"));
                        notificationAdapterInterface.setActionType("liked", holder);
                    }
                    notificationAdapterInterface.setFullName(fullName, holder);

                    holder.fullName.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            goToProfile(ids);
                        }
                    });

                    holder.activityType.setOnClickListener(new View.OnClickListener() { //sets on click listener for when activity type is clicked
                        @Override
                        public void onClick(View view) {
                            goToPost(ids); //calls goToPost
                        }
                    });


                }catch(JSONException e){
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
        requestQueue.add(activityRequest);
    }

    private void getLikes() throws JSONException {

        String likesURL = Config.baseIP + "notifications/likenotifs/" + notification.getString("likeid"); //the url to get like notifications
        JsonArrayRequest likeRequest = new JsonArrayRequest(Request.Method.GET, likesURL, null, new Response.Listener<JSONArray>() { //making a like request
            @Override
            public void onResponse(JSONArray response) {
                try {
                    JSONObject likeResponse = response.getJSONObject(0); //converting the response to a JSONObject
                    String activityId = likeResponse.getString("activityid"); //getting the activity id related to the like
                    getActivity(activityId, likeResponse);
                }catch(JSONException e){
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
        requestQueue.add(likeRequest); //adds likerequest to request queue
    }


    /**
     * Description: gets the user image and sets the image view
     * @param userId: the user id that you want the user image of
     * @param holder: the holder for the image view
     */
    private void getUserImage(String userId, @NonNull final NotificationViewHolder holder){
        String imageURL = Config.baseIP + "notifications/userpic/" + userId;
        ImageRequest imageRequest = new ImageRequest(imageURL, new Response.Listener<Bitmap>() {
            @Override
            public void onResponse(Bitmap response) {
                if(response != null) {
                    holder.profileImage.setImageBitmap(response);
                }else{
                    holder.profileImage.setVisibility(View.GONE);
                }
            }
        }, 0, 0, ImageView.ScaleType.CENTER_CROP, Bitmap.Config.RGB_565, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });

//        requestQueue.add(imageRequest);
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
     * @param ids
     */
    private void goToPost(Map<String, String> ids){
        Intent intent = new Intent(MyApplication.getContext(), post.class);
        intent.putExtra("activityid", ids.get("activityid")); //puts the activity id into the intent
        MyApplication.getContext().startActivity(intent); //changes to the post activity
    }

    private void checkReferenceActivityId(String referenceActivityId, JSONObject activityResponse) throws JSONException {
        final String[] activityTypeResponses = new String[]{"your post", "a new post"}; //the possible activityType responses for activityid
        if(!referenceActivityId.equals("null")) { //if there is a reference activity id (the activity is not a post)
            notificationAdapterInterface.setActivityType(activityTypeResponses[0], holder); //sets the activity type response
            ids.put("activityid", activityResponse.getString("activityreference")); //puts the activity reference id into the ids map

        }else{ //if there is no reference activity id (the activity is a post)
            notificationAdapterInterface.setActivityType(activityTypeResponses[1], holder); //sets the activity type response
            ids.put("activityid", activityResponse.getString("activityid")); //puts the activity id into the ids map

        }
    }

    private void checkActivityType(int activityType, JSONObject activityResponse) throws JSONException {
        final String[] activityTypeResponse = new String[]{"X", "your post", "your comment"}; //the possible activityType responses for userid
        if(activityType == 1){ //if the activity is a post
            ids.put("activityid", activityResponse.getString("activityid")); //enter the activity id of the post into ids
            holder.activityType.setText(activityTypeResponse[activityType]); //setting text based on activityTypeResponse
        }else { //if the activity is a comment
            ids.put("activityid", activityResponse.getString("activityreference")); //putting the activity reference of the activity which is a post
            holder.activityType.setText(activityTypeResponse[activityType]); //setting text based on activityTypeResponse
        }
    }

    private String checkFirstName(String firstName){
        if(!firstName.equals("null")){ //if there is a first name returned
            return firstName;
        }else{ //if the first name is not filled
            return "";
        }
    }

    private String checkLastName(String lastName){
        if(!lastName.equals("null")){ //if there is a first name returned
            return " " + lastName;
        }else{ //if the first name is not filled
            return "";
        }
    }



    public interface NotificationAdapterInterface{


        void setActionType(String actionType, NotificationViewHolder holder);
        void hideActionType(NotificationViewHolder holder);
        void setActivityType(String activityType, NotificationViewHolder holder);
        void hideActivityType(NotificationViewHolder holder);
        void setTime(String time, NotificationViewHolder holder);
        void hideTime(NotificationViewHolder holder);
        void setFullName(String fullName, NotificationViewHolder holder);
    }
}
