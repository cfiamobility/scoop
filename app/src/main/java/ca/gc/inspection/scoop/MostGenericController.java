package ca.gc.inspection.scoop;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

public class MostGenericController {
    private JSONObject post, profileImage;
    private MostGenericViewHolder holder;
    private MostGenericInterface mostGenericInterface;
    private Map<String, String> likeProperties;

    public MostGenericController(MostGenericInterface mostGenericInterface, JSONArray posts, JSONArray profileImages, int i, MostGenericViewHolder holder){
        this.mostGenericInterface = mostGenericInterface;
        try {
            this.post = posts.getJSONObject(i);
            this.profileImage = profileImages.getJSONObject(i);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        this.holder = holder;
        likeProperties = new HashMap<>(); //map of liketype and likecount of specified post
    }

    /**
     * Description: main method to display a single post
     * @throws JSONException
     */
    public void displayPost() throws JSONException {
        final String activityid = post.getString("activityid");
        final String posterid = post.getString("userid");
        likeProperties.put("liketype", post.getString("liketype")); //puts liketype into properties map
        likeProperties.put("likecount", checkLikeCount(post.getString("likecount"))); //puts likecount into properties map
        mostGenericInterface.setPostText(post.getString("posttext"), holder);
        formatDate(post.getString("createddate"));
        checkFullName();
        checkLikeState(likeProperties.get("liketype"));
        holder.upvote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    changeUpvoteLikeState(activityid, posterid); //changes upvote state on click
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        holder.downvote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    changeDownvoteLikeState(activityid, posterid); //changes downvote state on click
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        // tapping on any item from the view holder will go to the display post activity
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.getContext().startActivity(new Intent(v.getContext(), DisplayPostActivity.class));
            }
        });

        // tapping on profile picture will bring user to poster's profile page
        holder.profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TabFragment.viewPager.setCurrentItem(3);
            }
        });

        holder.username.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TabFragment.viewPager.setCurrentItem(3);
            }
        });
    }

    public void formPostTitle() throws JSONException {
        mostGenericInterface.setPostTitle(post.getString("posttitle"), holder);
    }

    /**
     * Description: checks to see if full name is valid before setting name
     * @throws JSONException
     */
    private void checkFullName() throws JSONException {
        String fullName = checkFirstName(post.getString("firstname")) + checkLastName(post.getString("lastname"));
        mostGenericInterface.setUserName(fullName, holder);
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
     * Description: formats date accordingly
     * @param time
     */
    private void formatDate(String time){
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"); //formats the date accordingly
            Date parsedDate = dateFormat.parse(time); //parses the created date to be in specified date format
            DateFormat properDateFormat = new SimpleDateFormat("MM-dd-yy"); //formats the date to be how we want it to output
            mostGenericInterface.setDate(properDateFormat.format(parsedDate),holder);
        }catch(Exception e){
            e.printStackTrace();
            mostGenericInterface.hideDate(holder);
        }
    }


    /**
     * Description: initial setting of likeCount and checks if likeCount is null
     * @param likeCount: the number of likes on a post
     * @return the proper like count
     */
    private String checkLikeCount(String likeCount){
        String defaultCount = "0";
        if(likeCount.equals("null")){
            mostGenericInterface.setLikeCount(defaultCount, holder);
            return defaultCount;
        }else{
            mostGenericInterface.setLikeCount(likeCount, holder);
            return likeCount;
        }
    }

    /**
     * Description: checks likeType currently on the post and sets upvote and downvote buttons accordingly
     * @param likeState: like type of post
     */
    private void checkLikeState(String likeState){
        switch(likeState){
            case "1": mostGenericInterface.setLikeUpvoteState(holder);
                break;
            case "-1": mostGenericInterface.setLikeDownvoteState(holder);
                break;
            case "0": mostGenericInterface.setLikeNeutralState(holder);
                break;
            default: mostGenericInterface.setLikeNeutralState(holder);
                break;
        }
    }

    /**
     * Description: changes upvote like state depending on the current state
     * @param activityid: activity id of the post
     * @param posterid: user who posted the post
     * @throws JSONException
     */
    private void changeUpvoteLikeState(String activityid, String posterid) throws JSONException{
        String likeType = likeProperties.get("liketype"); //gets current like type
        int likeCount = Integer.parseInt(likeProperties.get("likecount")); //gets current like count
        Log.i("liketype1", String.valueOf(likeType));
        switch(likeType){
            case "1": //if it's already liked, it'll be set to neutral if pressed
                likeCount -= 1;
                mostGenericInterface.setLikeNeutralState(holder);
                updateLikes("0", activityid, posterid);
                break;
            case "-1": //if it's downvoted, it'll be set to upvote state
                likeCount += 2;
                mostGenericInterface.setLikeUpvoteState(holder);
                updateLikes("1", activityid, posterid);
                break;
            case "0": //if it's neutral, it'll be set to upvote state
                likeCount += 1;
                mostGenericInterface.setLikeUpvoteState(holder);
                updateLikes("1", activityid, posterid);
                break;
            default: //default will be upvote state, if liketype is null
                likeCount += 1;
                insertLikes("1", activityid, posterid); //will insert the like for the first time
                mostGenericInterface.setLikeUpvoteState(holder);
                break;
        }
        Log.i("likecount1", String.valueOf(likeCount));
        updateLikeCount(String.valueOf(likeCount)); //updates like count to new total
    }

    /**
     * Description: changes downvote like state depending on the current state
     * @param activityid: activity id of the post
     * @param posterid: poster id of the post
     * @throws JSONException
     */
    private void changeDownvoteLikeState(String activityid, String posterid) throws JSONException {
        String likeType = likeProperties.get("liketype"); //gets current like type
        int likeCount = Integer.parseInt(likeProperties.get("likecount")); //gets current like count
        Log.i("liketype2", String.valueOf(likeType));
        switch(likeType){
            case "1": //if it's liked, it'll be set to downvote state
                likeCount -= 2;
                mostGenericInterface.setLikeDownvoteState(holder);
                updateLikes("-1", activityid, posterid);
                break;
            case "-1": //if it's downvoted, it'll be set to neutral state
                likeCount += 1;
                mostGenericInterface.setLikeNeutralState(holder);
                updateLikes("0", activityid, posterid);
                break;
            case "0": //if it's netural state, it'll be set to downvote state
                likeCount -= 1;
                mostGenericInterface.setLikeDownvoteState(holder);
                updateLikes("-1", activityid, posterid);
                break;
            default: //default will be downvote state, if liketype is null
                likeCount -= 1;
                insertLikes("-1", activityid, posterid); //will insert the downvote for the first time
                mostGenericInterface.setLikeDownvoteState(holder);
                break;
        }
        Log.i("likecount2", String.valueOf(likeCount));
        updateLikeCount(String.valueOf(likeCount)); //updates the likecount to new total
    }

    /**
     * Description: updates the like count to new total
     * @param likeCount
     * @throws JSONException
     */
    private void updateLikeCount(String likeCount) throws JSONException {
        likeProperties.put("likecount", likeCount); //updates in map
        post.put("likecount", likeCount); //updates in post object
        mostGenericInterface.setLikeCount(likeCount,holder); //sets like count to new total
    }


    /**
     * Description: updates likes in table and adds notifications if like type is 1
     * @param likeType: current like type
     * @param activityid: activity id of post
     * @param posterid: user id of poster of post
     * @throws JSONException
     */
    private void updateLikes(final String likeType, final String activityid, final String posterid) throws JSONException {
        post.put("liketype", likeType); //updates post object
        likeProperties.put("liketype", likeType); //updates properties map
        Log.i("hello", "should be here");
        String URL = Config.baseIP + "display-post/updatelikes";
        StringRequest request = new StringRequest(Request.Method.PUT, URL, new Response.Listener<String>() { //sends a PUT request to update new likes
            @Override
            public void onResponse(String response) {
                Log.i("response", response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        }){
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                Log.i("hello", "cmon");
                params.put("liketype", likeProperties.get("liketype"));
                params.put("userid", Config.currentUser);
                params.put("activityid",activityid);
                params.put("posterid", posterid);
                return params;
            }
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                // inserting the token into the response header that will be sent to the server
                Map<String, String> header = new HashMap<>();
                header.put("authorization", Config.token);
                return header;
            }
        };
        request.setRetryPolicy(new DefaultRetryPolicy(
                30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        Config.requestQueue.add(request);
    }

    /**
     * Description: inserts likes in table and adds notifications if like type is 1
     * @param likeType: current like type
     * @param activityid: activity id of post
     * @param posterid: user id of poster of post
     * @throws JSONException
     */
    private void insertLikes(final String likeType, final String activityid, final String posterid) throws JSONException {
        post.put("liketype", likeType); //updates post object
        likeProperties.put("liketype", likeType); //updates properties map
        String URL = Config.baseIP + "display-post/insertlikes";
        StringRequest request = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() { //sends a POST request to insert new like
            @Override
            public void onResponse(String response) {
                Log.i("response", response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        }){
            @Override
            protected Map<String, String> getParams(){
                Map<String, String> params = new HashMap<>();
                params.put("liketype", likeType);
                params.put("activityid", activityid);
                params.put("posterid", posterid);
                params.put("userid", Config.currentUser);
                return params;
            }
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                // inserting the token into the response header that will be sent to the server
                Map<String, String> header = new HashMap<>();
                header.put("authorization", Config.token);
                return header;
            }
        };
        request.setRetryPolicy(new DefaultRetryPolicy(
                30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        Config.requestQueue.add(request);
    }

    public void displayImages() throws JSONException {
        if(profileImage != null) { // null check to see if there are images
            formatImage(profileImage.getString("profileimage")); //formats profile image
        }
        // tapping on any item from the view holder will go to the display post activity
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.getContext().startActivity(new Intent(v.getContext(), DisplayPostActivity.class));
            }
        });
    }


    /**
     * Description: changes image from a string to a bitmap, then setting image
     * @param image: image to convert
     *
     */
    private void formatImage(String image){
        Bitmap bitmap = MyCamera.stringToBitmap(image); //converts image string to bitmap
        mostGenericInterface.setUserImage(bitmap, holder);

    }

    public interface MostGenericInterface{
        void setPostText(String postText, MostGenericViewHolder holder);
        void setPostTitle(String postTitle, MostGenericViewHolder holder);
        void setUserName(String userName, MostGenericViewHolder holder);
        void setLikeCount(String likeCount, MostGenericViewHolder holder);
        void setDate(String date, MostGenericViewHolder holder);
        void setLikeNeutralState(MostGenericViewHolder holder);
        void setLikeUpvoteState(MostGenericViewHolder holder);
        void setLikeDownvoteState(MostGenericViewHolder holder);
        void hideDate(MostGenericViewHolder holder);
        void setUserImage(Bitmap image, MostGenericViewHolder  holder);
    }
}
