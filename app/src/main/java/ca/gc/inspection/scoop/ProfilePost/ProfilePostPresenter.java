package ca.gc.inspection.scoop.ProfilePost;

import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import ca.gc.inspection.scoop.PostOptionsDialog;
import ca.gc.inspection.scoop.PostReply.ReplyPostPresenter;

/**
 * performs all logic and HTTP requests for the FeedAdapter
 */
public class ProfilePostsFeedController extends ReplyPostPresenter {
    private JSONObject post;
    private ProfilePostsFeedPostViewHolder holder;
    private ProfilePostContract profilePostContract;
    private Map<String, String> likeProperties;

    /**
     * @param profilePostContract: inherits from MostGenericInterface and declares all its methods
     * @param posts: JSONArray of posts from the database
     * @param images: JSONArray of profile images from the database
     * @param i: counter for the array adapter
     * @param holder: view holder that contains all the front end declarations
     */
    public ProfilePostsFeedController(ProfilePostContract profilePostContract, JSONArray posts, JSONArray images, int i, ProfilePostsFeedPostViewHolder holder){
        super(profilePostContract, posts, images, i, holder);
        this.profilePostContract = profilePostContract;
        try {
            this.post = posts.getJSONObject(i);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        this.holder = holder;
        likeProperties = new HashMap<>(); //map of liketype and likecount of specified post
    }

    /**
     * Super runs the display post in MostGeneric
     * Displays the posts in the profile
     * @throws JSONException
     */
    @Override
    public void displayPost() throws JSONException{
        super.displayPost();
        checkCommentCount(post.getString("commentcount"));

        // to get the options menu to appear
        holder.optionsMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PostOptionsDialog bottomSheetDialog = new PostOptionsDialog();
                final Context context = v.getContext();
                FragmentManager fragmentManager = ((AppCompatActivity)context).getSupportFragmentManager();
                bottomSheetDialog.show(fragmentManager, "bottomSheet");
            }
        });
    }

    /**
     * Sets the post title
     * @throws JSONException
     */
    @Override
    public void formPostTitle() throws JSONException {
        profilePostContract.setPostTitle(post.getString("posttitle"), holder);
    }

    /**
     * Description: checks current comment count and sets item accordingly
     * @param commentCount: current comment count
     */
    private void checkCommentCount(String commentCount){
        String defaultCount = "0";
        if(!commentCount.equals("null")){
            profilePostContract.setCommentCount(commentCount, holder);
        }else{
            profilePostContract.setCommentCount(defaultCount, holder);
        }
    }


}
