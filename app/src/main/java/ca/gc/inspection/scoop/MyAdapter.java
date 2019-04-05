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

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder>{
    private JSONArray notifications;
    private RequestQueue requestQueue;
    private Context context;
    private String timeType;
    private Timestamp currentTime;

    class MyViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        ImageView profileImage;
        TextView firstName, lastName, actionType, activityType, time;
        LinearLayout fullName;

        /**
         * Description: Viewholder holds each item in the recycler view and holds each piece that altogether makes the item
         * @param v: passed in the view from the custom row layout
         */
        MyViewHolder(View v) {
            super(v);
            profileImage = (ImageView) v.findViewById(R.id.profile_image); //instantiating the profile image imageview
            firstName = (TextView) v.findViewById(R.id.firstname); //instantiating the first name textview
            lastName = (TextView) v.findViewById(R.id.lastname); //instantiating the last name textview
            actionType = (TextView) v.findViewById(R.id.actiontype); //instantiating the action type textview
            activityType = (TextView) v.findViewById(R.id.activitytype); //instantiating the activity type textview
            time = (TextView) v.findViewById(R.id.time); //instantiating the time textview
            fullName = (LinearLayout) v.findViewById(R.id.fullname); //instantiating the full name linearlayout
        }
    }

    /**
     *
     * @param notifs: represents the JSONArray for all the notifications relevant to the user
     * @param requestQueue: represents the requestQueue used for adding future requests to
     * @param context: represents the context of the activity which is relevant to the adapter
     * @param timeType: represents whether the type of time which should be displayed is for today or for recent notifications
     */
    MyAdapter(JSONArray notifs, RequestQueue requestQueue, Context context, String timeType, Timestamp currentTime){
        this.notifications = notifs;
        this.requestQueue = requestQueue;
        this.context = context;
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
    public MyAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.custom_row_notifications, viewGroup, false);

        return new MyViewHolder(v);
    }

    /**
     * Description: used to set all the views for the holder of row i
     * @param holder
     * @param i
     */
    @Override
    public void onBindViewHolder(@NonNull final MyAdapter.MyViewHolder holder, int i) {
        final Map<String, String> ids = new HashMap<>(); //instantiates hashmap which will hold each viewholder's corresponding userid and activityid
        try{
            JSONObject notification = notifications.getJSONObject(i); //gets the specific notification from the JSONarray

            if(timeType.equals("today")){ //for today notifications, sets appropriate time formatting
                setTodayTime(notification, holder);
            }else{ //for recent notifications, sets appropriate time formatting
                setRecentTime(notification, holder);
            }
            if(!notification.getString("activityid").equals("null")){ //for notifications with an activity id
                final String[] actionTypeResponses = new String[]{"X", "posted", "commented on"}; //the possible actionType responses for activityid
                final String[] activityTypeResponses  = new String[]{"your post", "a new post", "your comment"}; //the possible activityType responses for activityid
                String activityURL = "http://10.0.2.2:3000/activitynotifs/" + notification.getString("activityid"); //url to get activity notifications
                JsonArrayRequest activityRequest = new JsonArrayRequest(Request.Method.GET, activityURL, null, new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try{
                            JSONObject activityResponse = response.getJSONObject(0); //gets the JSONobject from the JSONarray
                            final String referenceActivityId = activityResponse.getString("activityreference"); //gets the id for which the activity is referencing towards
                            final int activityType = Integer.parseInt(activityResponse.getString("activitytype")); //gets the activity's activity type
                            ids.put("userid", activityResponse.getString("userid")); //puts the corresponding user id into the map
                            holder.fullName.setOnClickListener(new View.OnClickListener() { //sets an on click listener for when full name is clicked
                                @Override
                                public void onClick(View view) {
                                    goToProfile(ids); //calls goToProfile
                                }
                            });

                            if(!referenceActivityId.equals("null")) { //if there is a reference activity id (the activity is not a post)
                                holder.activityType.setText(activityTypeResponses[0]); //sets the activity type response
                                ids.put("activityid", activityResponse.getString("activityreference")); //puts the activity reference id into the ids map

                            }else{ //if there is no reference activity id (the activity is a post)
                                holder.activityType.setText(activityTypeResponses[1]); //sets the activity type response
                                ids.put("activityid", activityResponse.getString("activityid")); //puts the activity id into the ids map

                            }
                            holder.activityType.setOnClickListener(new View.OnClickListener() { //sets on click listener for when activity type is clicked
                                @Override
                                public void onClick(View view) {
                                    goToPost(ids); //calls goToPost
                                }
                            });

                            //getUserImage(activityResponse.getString("userid"), holder); //gets the user image based on the user id
                            if(!activityResponse.getString("firstname").equals("null")){ //if there is a first name returned
                                holder.firstName.setText(activityResponse.getString("firstname")); //sets text to the first name
                                holder.firstName.setOnClickListener(new View.OnClickListener() { //sets on click listener for when firstName is clicked
                                    @Override
                                    public void onClick(View view) {
                                        goToProfile(ids); //calls goToProfile
                                    }
                                });
                            }else{ //if the first name is not filled
                                holder.firstName.setVisibility(View.GONE);  //sets the visibility of the first name to gone if so
                            }
                            if(!activityResponse.getString("lastname").equals("null")) { //if there is a last name returned
                                holder.lastName.setText(activityResponse.getString("lastname")); //stes text to the last name
                                holder.lastName.setOnClickListener(new View.OnClickListener() { //sets on click listener for when lastName is clicked
                                    @Override
                                    public void onClick(View view) {
                                        goToProfile(ids); //calls goToProfile
                                    }
                                });
                            }else{ //if last name is not filled
                                holder.lastName.setVisibility(View.GONE); //sets visibility of the last name to gone if so
                            }
                            holder.actionType.setText(actionTypeResponses[activityType]); //sets the action type for corresponding activity type
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
                requestQueue.add(activityRequest); //adds the activity request to the queue
            }else{
                final String[] activityTypeResponse = new String[]{"X", "your post", "your comment", "your reply"}; //the possible activityType responses for userid
                String likesURL = "http://10.0.2.2:3000/likenotifs/" + notification.getString("likeid"); //the url to get like notifications
                JsonArrayRequest likeRequest = new JsonArrayRequest(Request.Method.GET, likesURL, null, new Response.Listener<JSONArray>() { //making a like request
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            JSONObject likeResponse = response.getJSONObject(0); //converting the response to a JSONObject
                            String activityId = likeResponse.getString("activityid"); //getting the activity id related to the like
                            String activityURL = "http://10.0.2.2:3000/activitynotifs/" + activityId; //the url to get activity notifications
                            JsonArrayRequest activityRequest = new JsonArrayRequest(Request.Method.GET, activityURL, null, new Response.Listener<JSONArray>() {
                                @Override
                                public void onResponse(JSONArray response) {
                                    try {
                                        JSONObject activityResponse = response.getJSONObject(0); //converting the response to a JSONObject
                                        int activityType = Integer.parseInt(activityResponse.getString("activitytype")); //getting the activity type of the activity
                                        holder.actionType.setText("liked"); //setting the default action type to 'liked'
                                        if(activityType == 1){ //if the activity is a post
                                            ids.put("activityid", activityResponse.getString("activityid")); //enter the activity id of the post into ids
                                            holder.activityType.setText(activityTypeResponse[activityType]); //setting text based on activityTypeResponse
                                        }else { //if the activity is a comment
                                            ids.put("activityid", activityResponse.getString("activityreference")); //putting the activity reference of the activity which is a post
                                            holder.activityType.setText(activityTypeResponse[activityType]); //setting text based on activityTypeResponse
                                        }
                                        holder.activityType.setOnClickListener(new View.OnClickListener() { //setting on click listener for activity type
                                            @Override
                                            public void onClick(View view) {
                                                goToPost(ids); //calling goToPost
                                            }
                                        });
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }, new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    error.printStackTrace();
                                }
                            });
                            requestQueue.add(activityRequest); //adding activity request to the queue
                            ids.put("userid", likeResponse.getString("userid")); //putting the user id of the like into ids
                            holder.fullName.setOnClickListener(new View.OnClickListener() { //setting an on click listener for full name
                                @Override
                                public void onClick(View view) {
                                    goToProfile(ids); //calling goToProfile
                                }
                            });
                            // getUserImage(likeResponse.getString("userid"), holder); //gets the user image based on the user id
                            if(!likeResponse.getString("firstname").equals("null")){ //if there is an entry filled in first name
                                holder.firstName.setText(likeResponse.getString("firstname")); //sets firstname
                                holder.firstName.setOnClickListener(new View.OnClickListener() { //sets on click listener for first name
                                    @Override
                                    public void onClick(View view) {
                                        goToProfile(ids); //calling goToProfile
                                    }
                                });
                            }else{ //if there isn't an entry in first name
                                holder.firstName.setVisibility(View.GONE); //setting visibility to gone
                            }
                            if(!likeResponse.getString("lastname").equals("null")) { //if there is an entry filled in last name
                                holder.lastName.setText(likeResponse.getString("lastname")); //sets last name
                                holder.lastName.setOnClickListener(new View.OnClickListener() { //sets on click listener for last name
                                    @Override
                                    public void onClick(View view) {
                                        goToProfile(ids); //calling goToProfile
                                    }
                                });
                            }else{
                                holder.lastName.setVisibility(View.GONE); //setting visibility to gone
                            }
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
        }catch(JSONException e){
            e.printStackTrace();
        }
    }

    /**
     * Description: goes to the user profile indicated
     * @param ids: representing the user id and the activity id
     */
    private void goToProfile(Map<String, String> ids){
        Intent intent = new Intent(context, userprofile.class);
        intent.putExtra("userid", ids.get("userid")); //puts the user id into the intent
        context.startActivity(intent); //changes to the user profile activity
    }

    /**
     * Description: goes to the post indicated
     * @param ids
     */
    private void goToPost(Map<String, String> ids){
        Intent intent = new Intent(context, post.class);
        intent.putExtra("activityid", ids.get("activityid")); //puts the activity id into the intent
        context.startActivity(intent); //changes to the post activity
    }

    /**
     * Description: sets time formatting for today notifications
     * @param notification: the notification to set time format of
     * @param holder: the holder for the row in recycler view
     *
     */
    private void setTodayTime(JSONObject notification, @NonNull final MyAdapter.MyViewHolder holder){
        try {
            String createdDate = notification.getString("createddate"); //gets when the notification was created
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"); //formats the date accordingly
            Date parsedDate = dateFormat.parse(createdDate); //parses the created date to be in specified date format
            Timestamp timestamp = new Timestamp(parsedDate.getTime()); //creates a timestamp from the date
            long diff = currentTime.getTime() - timestamp.getTime(); //gets the difference between the two timestamps
            String diffHours = String.valueOf((int)((diff/(1000*60*60)))); //stores it in a string representing hours
            holder.time.setText(diffHours +" hours ago"); //sets text to hours ago
        }catch(Exception e){
            e.printStackTrace();
            holder.time.setVisibility(View.GONE); //sets visibility to gone
        }
    }

    /**
     * Description: sets time formatting for recent notifications
     * @param notification: the notification to set time format of
     * @param holder: the holder for the row in recycler view
     */
    private void setRecentTime(JSONObject notification, @NonNull final MyAdapter.MyViewHolder holder){
        try {
            String time = notification.getString("createddate"); //gets when notification was created
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"); //formats the date accordingly
            Date parsedDate = dateFormat.parse(time); //parses the created date to be in specified date format
            DateFormat properDateFormat = new SimpleDateFormat("MMMM d 'at' h:mm a"); //formats the date to be how we want it to output
            holder.time.setText(properDateFormat.format(parsedDate)); //sets text to appropriate formatted date
        }catch(Exception e){
            e.printStackTrace();
            holder.time.setVisibility(View.GONE); //sets visibility to gone
        }
    }

    /**
     * Description: gets the user image and sets the image view
     * @param userId: the user id that you want the user image of
     * @param holder: the holder for the image view
     */
    private void getUserImage(String userId, @NonNull final MyAdapter.MyViewHolder holder){
        String imageURL = "http://10.0.2.2:3000/userpic/" + userId;
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

        requestQueue.add(imageRequest);
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