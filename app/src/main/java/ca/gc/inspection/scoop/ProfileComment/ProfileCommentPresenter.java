package ca.gc.inspection.scoop.ProfileComment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
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

import ca.gc.inspection.scoop.Config;
import ca.gc.inspection.scoop.DisplayPostActivity;
import ca.gc.inspection.scoop.MainActivity;
import ca.gc.inspection.scoop.MyCamera;
import ca.gc.inspection.scoop.MySingleton;
import static com.google.gson.internal.$Gson$Preconditions.checkNotNull;

/**
 * Presenter for the replying to a post action; it is the most generic presenter
 * related to "posting" actions. Parent presenter for ProfilePostPresenter.
 */

public class ProfileCommentPresenter implements ProfileCommentContract.Presenter {
    private JSONObject post, profileImage;
    private ProfileCommentViewHolder holder;
    private Map<String, String> likeProperties;

    @NonNull
    private ProfileCommentContract.View mProfileCommentView;
    private ProfileCommentInteractor mProfileCommentInteractor;


    public ProfileCommentPresenter(@NonNull ProfileCommentContract.View profileCommentView, JSONArray posts, JSONArray profileImages,
                                   int i, ProfileCommentViewHolder holder){
        mProfileCommentView = profileCommentView;
        try {
            this.post = posts.getJSONObject(i);
            this.profileImage = profileImages.getJSONObject(i);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        this.holder = holder;
        likeProperties = new HashMap<>(); //map of liketype and likecount of specified post

        mProfileCommentView.setPresenter(checkNotNull(this));
//        mProfileCommentInteractor = new ProfileCommentInteractor(this);
    }

//    public void setPresenterView (ProfileCommentContract.View profileCommentView){
//        mProfileCommentView = profileCommentView;
//    }
//
//    public ProfileCommentContract.View getPresenterView (){
//        return mProfileCommentView;
//    }


//    public void setPresenterInteractor (ProfileCommentInteractor profileCommentInteractor){
//        mProfileCommentInteractor = profileCommentInteractor;
//    }

    /**
     * Description: main method to display a single post
     * @throws JSONException
     */
    public void displayPost() throws JSONException {
        final String activityid = post.getString("activityid");
        final String posterid = post.getString("userid");
        likeProperties.put("liketype", post.getString("liketype")); //puts liketype into properties map
        likeProperties.put("likecount", checkLikeCount(post.getString("likecount"))); //puts likecount into properties map
        mProfileCommentView.setPostText(post.getString("posttext"), holder);
        formatDate(post.getString("createddate"));
        checkFullName();
        checkLikeState(likeProperties.get("liketype"));
        mProfileCommentView.displayPostListener(holder, activityid, posterid);
//        holder.upvote.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                try {
//                    changeUpvoteLikeState(activityid, posterid); //changes upvote state on click
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
//        });
//
//        holder.downvote.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                try {
//                    changeDownvoteLikeState(activityid, posterid); //changes downvote state on click
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
//        });
//
//        // tapping on any item from the view holder will go to the display post activity
//        holder.itemView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                v.getContext().startActivity(new Intent(v.getContext(), DisplayPostActivity.class));
//            }
//        });
//
//        // tapping on profile picture will bring user to poster's profile page
//        holder.profileImage.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                MainActivity.otherUserClicked(posterid);
//            }
//        });
//
//        holder.username.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                MainActivity.otherUserClicked(posterid);
//            }
//        });
    }

    public void formPostTitle() throws JSONException {
        mProfileCommentView.setPostTitle("Replying to " + post.getString("postfirstname") + " " + post.getString("postlastname") + "'s post", holder);
    }

    /**
     * Description: checks to see if full name is valid before setting name
     * @throws JSONException
     */
    public void checkFullName() throws JSONException {
        String fullName = checkFirstName(post.getString("firstname")) + checkLastName(post.getString("lastname"));
        mProfileCommentView.setUserName(fullName, holder);
    }

    /**
     * Description: checks the first name to see if it is valid
     *
     * @param firstName: the first name of user
     * @return first name if not null
     */
    public String checkFirstName(String firstName) {
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
    public String checkLastName(String lastName) {
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
    public void formatDate(String time){
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"); //formats the date accordingly
            Date parsedDate = dateFormat.parse(time); //parses the created date to be in specified date format
            DateFormat properDateFormat = new SimpleDateFormat("MM-dd-yy"); //formats the date to be how we want it to output
            mProfileCommentView.setDate(properDateFormat.format(parsedDate),holder);
        }catch(Exception e){
            e.printStackTrace();
            mProfileCommentView.hideDate(holder);
        }
    }


    /**
     * Description: initial setting of likeCount and checks if likeCount is null
     * @param likeCount: the number of likes on a post
     * @return the proper like count
     */
    public String checkLikeCount(String likeCount){
        String defaultCount = "0";
        if(likeCount.equals("null")){
            mProfileCommentView.setLikeCount(defaultCount, holder);
            return defaultCount;
        }else{
            mProfileCommentView.setLikeCount(likeCount, holder);
            return likeCount;
        }
    }

    /**
     * Description: checks likeType currently on the post and sets upvote and downvote buttons accordingly
     * @param likeState: like type of post
     */
    public void checkLikeState(String likeState){
        Log.i("likestate", likeState);
        switch(likeState){
            case "1": mProfileCommentView.setLikeUpvoteState(holder);
                break;
            case "-1": mProfileCommentView.setLikeDownvoteState(holder);
                break;
            case "0": mProfileCommentView.setLikeNeutralState(holder);
                break;
            default: mProfileCommentView.setLikeNeutralState(holder);
                break;
        }
    }

    @Override
    public void getUserComments(MySingleton instance, String currentUser) {
        mProfileCommentInteractor.getUserComments(instance, currentUser);
    }

    @Override
    public void setRecyclerView(JSONArray commentsResponse, JSONArray imagesResponse) {
        mProfileCommentView.setRecyclerView(commentsResponse, imagesResponse);
    }

    /**
     * Description: changes upvote like state depending on the current state
     * @param activityid: activity id of the post
     * @param posterid: user who posted the post
     * @throws JSONException
     */
    public void changeUpvoteLikeState(MySingleton singleton, String activityid, String posterid) throws JSONException{
        String likeType = likeProperties.get("liketype"); //gets current like type
        int likeCount = Integer.parseInt(likeProperties.get("likecount")); //gets current like count
        Log.i("liketype1", String.valueOf(likeType));
        switch(likeType){
            case "1": //if it's already liked, it'll be set to neutral if pressed
                likeCount -= 1;
                mProfileCommentView.setLikeNeutralState(holder);
                mProfileCommentInteractor.updateLikes(singleton, "0", activityid, posterid, post, likeProperties);
                break;
            case "-1": //if it's downvoted, it'll be set to upvote state
                likeCount += 2;
                mProfileCommentView.setLikeUpvoteState(holder);
                mProfileCommentInteractor.updateLikes(singleton, "1", activityid, posterid, post, likeProperties);
                break;
            case "0": //if it's neutral, it'll be set to upvote state
                likeCount += 1;
                mProfileCommentView.setLikeUpvoteState(holder);
                mProfileCommentInteractor.updateLikes(singleton, "1", activityid, posterid, post, likeProperties);
                break;
            default: //default will be upvote state, if liketype is null
                likeCount += 1;
                mProfileCommentInteractor.insertLikes(singleton, "1", activityid, posterid, post, likeProperties); //will insert the like for the first time
                mProfileCommentView.setLikeUpvoteState(holder);
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
    public void changeDownvoteLikeState(MySingleton singleton, String activityid, String posterid) throws JSONException {
        String likeType = likeProperties.get("liketype"); //gets current like type
        int likeCount = Integer.parseInt(likeProperties.get("likecount")); //gets current like count
        Log.i("liketype2", String.valueOf(likeType));
        switch(likeType){
            case "1": //if it's liked, it'll be set to downvote state
                likeCount -= 2;
                mProfileCommentView.setLikeDownvoteState(holder);
                mProfileCommentInteractor.updateLikes(singleton, "-1", activityid, posterid, post, likeProperties);
                break;
            case "-1": //if it's downvoted, it'll be set to neutral state
                likeCount += 1;
                mProfileCommentView.setLikeNeutralState(holder);
                mProfileCommentInteractor.updateLikes(singleton, "0", activityid, posterid, post, likeProperties);
                break;
            case "0": //if it's netural state, it'll be set to downvote state
                likeCount -= 1;
                mProfileCommentView.setLikeDownvoteState(holder);
                mProfileCommentInteractor.updateLikes(singleton, "-1", activityid, posterid, post, likeProperties);
                break;
            default: //default will be downvote state, if liketype is null
                likeCount -= 1;
                mProfileCommentInteractor.insertLikes(singleton, "-1", activityid, posterid, post, likeProperties); //will insert the downvote for the first time
                mProfileCommentView.setLikeDownvoteState(holder);
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
    public void updateLikeCount(String likeCount) throws JSONException {
        likeProperties.put("likecount", likeCount); //updates in map
        post.put("likecount", likeCount); //updates in post object
        mProfileCommentView.setLikeCount(likeCount,holder); //sets like count to new total
    }


//    /**
//     * Description: changes image from a string to a bitmap, then setting image
//     * @param image: image to convert
//     *
//     */
//    public void formatImage(String image){
//        Bitmap bitmap = MyCamera.stringToBitmap(image); //converts image string to bitmap
//        mProfileCommentView.setUserImage(bitmap, holder);
//    }

//    public void getRecyclerView(JSONArray posts, JSONArray images){
//        mProfileCommentView.setRecyclerView(posts, images);
//    }
//
//    /**
//     * GETS USER COMMENTS DONT FORGET IT BABY
//     * @param singleton
//     * @param userId
//     */
//    public void getPosts(MySingleton singleton, final String userId) {
//        mProfileCommentInteractor.getUserComments(singleton, userId);
//    }

    /**
     *
     * @throws JSONException
     */
    public void displayImages() throws JSONException {
        if(profileImage != null) { // null check to see if there are images
            mProfileCommentView.formatImage(profileImage.getString("profileimage"), holder); //formats profile image
        }
        mProfileCommentView.displayImagesListener(holder);
//        // tapping on any item from the view holder will go to the display post activity
//        holder.itemView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                v.getContext().startActivity(new Intent(v.getContext(), DisplayPostActivity.class));
//            }
//        });
    }

}
