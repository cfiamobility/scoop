package ca.gc.inspection.scoop;

import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * performs all logic and HTTP requests for the FeedAdapter
 */
public class ProfileFeedController extends MostGenericController{
    private JSONObject post;
    private ProfileFeedViewHolder holder;
    private ProfileFeedInterface profileFeedInterface;
    private Map<String, String> likeProperties;

    public ProfileFeedController(ProfileFeedInterface profileFeedInterface, JSONArray posts, int i, ProfileFeedViewHolder holder){
        super(profileFeedInterface, posts, i, holder);
        this.profileFeedInterface = profileFeedInterface;
        try {
            this.post = posts.getJSONObject(i);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        this.holder = holder;
        likeProperties = new HashMap<>(); //map of liketype and likecount of specified post
    }

    @Override
    public void displayPost() throws JSONException{
        super.displayPost();
        checkCommentCount(post.getString("commentcount"));

        // to get the options menu to appear
        holder.optionsMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetDialog bottomSheetDialog = new bottomSheetDialog();
                final Context context = v.getContext();
                FragmentManager fragmentManager = ((AppCompatActivity)context).getSupportFragmentManager();
                bottomSheetDialog.show(fragmentManager, "bottomSheet");
            }
        });
    }

    @Override
    public void formPostTitle() throws JSONException {
        profileFeedInterface.setPostTitle(post.getString("posttitle"), holder);
    }

    /**
     * Description: checks current comment count and sets item accordingly
     * @param commentCount: current comment count
     */
    private void checkCommentCount(String commentCount){
        String defaultCount = "0";
        if(!commentCount.equals("null")){
            profileFeedInterface.setCommentCount(commentCount, holder);
        }else{
            profileFeedInterface.setCommentCount(defaultCount, holder);
        }
    }


    public interface ProfileFeedInterface extends MostGenericController.MostGenericInterface{
        void setCommentCount(String commentCount, ProfileFeedViewHolder holder);
    }
}
