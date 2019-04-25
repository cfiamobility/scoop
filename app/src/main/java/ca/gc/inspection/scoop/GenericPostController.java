package ca.gc.inspection.scoop;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.DefaultRetryPolicy;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * performs all logic and HTTP requests for the FeedAdapter
 */
public class GenericPostController extends MostGenericController{
    private JSONObject post;
    private GenericPostViewHolder holder;
    private GenericPostInterface genericPostInterface;
    private Map<String, String> likeProperties;

    public GenericPostController(GenericPostInterface genericPostInterface, JSONArray posts, JSONArray images, int i, GenericPostViewHolder holder){
        super(genericPostInterface, posts, images, i, holder);
        this.genericPostInterface = genericPostInterface;
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
        genericPostInterface.setPostTitle(post.getString("posttitle"), holder);
    }

    /**
     * Description: checks current comment count and sets item accordingly
     * @param commentCount: current comment count
     */
    private void checkCommentCount(String commentCount){
        String defaultCount = "0";
        if(!commentCount.equals("null")){
            genericPostInterface.setCommentCount(commentCount, holder);
        }else{
            genericPostInterface.setCommentCount(defaultCount, holder);
        }
    }


    public interface GenericPostInterface extends MostGenericController.MostGenericInterface{
        void setCommentCount(String commentCount, GenericPostViewHolder holder);
    }
}
