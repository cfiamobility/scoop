package ca.gc.inspection.scoop.feedpost;

import android.content.Intent;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.view.View;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import ca.gc.inspection.scoop.DisplayPostActivity;
import ca.gc.inspection.scoop.MyCamera;
import ca.gc.inspection.scoop.MySingleton;
import ca.gc.inspection.scoop.profilecomment.ProfileComment;
import ca.gc.inspection.scoop.profilecomment.ProfileCommentContract;
import ca.gc.inspection.scoop.profilepost.ProfilePost;
import ca.gc.inspection.scoop.profilepost.ProfilePostContract;
import ca.gc.inspection.scoop.profilepost.ProfilePostInteractor;
import ca.gc.inspection.scoop.profilepost.ProfilePostPresenter;

import static com.google.gson.internal.$Gson$Preconditions.checkNotNull;


public class FeedPostPresenter extends ProfilePostPresenter implements
        FeedPostContract.Presenter,
        FeedPostContract.Presenter.AdapterAPI {

    @NonNull
    private FeedPostContract.View mFeedPostView;
    private FeedPostInteractor mFeedPostInteractor;

    // TODO extend JSONArray mComments, mImages, and ArrayList mProfileComments from parent
    // - currently assume mComments stores only community feed fragment data
    private ArrayList<FeedPost> mFeedPosts;

    // TODO replace overriding method by creating a DataCache object in ProfileCommentPresenter and overriding it here
    @Override
    protected ProfileComment getProfileCommentByIndex(int i) {
        return getFeedPostByIndex(i);
    }

    private FeedPost getFeedPostByIndex(int i) {
        return mFeedPosts.get(i);
    }

    public FeedPostPresenter(@NonNull FeedPostContract.View viewInterface){

        mFeedPostView = checkNotNull(viewInterface);
        mFeedPostInteractor = new FeedPostInteractor(this);

    }

    // TODO implement getting different data for community vs official feed
    @Override
    public void loadDataFromDatabase(MySingleton singleton, String currentUser) {
        mFeedPostInteractor.getUserPosts(singleton, currentUser);
    }

    // TODO refactor
    @Override
    public void displayImages() throws JSONException {
        if(images != null) { // null check to see if there are images
            formatImage(images.getString("postimagepath"), "post"); //formats post image
            formatImage(images.getString("profileimage"), "user"); //formats profile image
        }else{
            mFeedPostView.hidePostImage(holder); //hides post image
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
     * @param type: type of image
     */
    public void formatImage(String image, String type){
        Bitmap bitmap = MyCamera.stringToBitmap(image); //converts image string to bitmap
        if(type.equals("post")) {
            if(!image.equals("") && !image.equals("null")) {
                mFeedPostView.setPostImage(bitmap, holder);
            } else{
                mFeedPostView.hidePostImage(holder);
            }
        }else{
            mFeedPostView.setUserImage(bitmap, holder);
        }
    }

    // TODO pass into interactor as parameter?
    public String getFeedType(){
        return mFeedPostView.getFeedType();
    }

    @Override
    public void setData(JSONArray feedPostsResponse, JSONArray imagesResponse) {
        mComments = feedPostsResponse;
        mImages = imagesResponse;
        mFeedPosts = new ArrayList<>();

        if ((feedPostsResponse.length() != imagesResponse.length()))
            throw new AssertionError("Error: length of feedPostsResponse != imagesResponse");

        for (int i=0; i<feedPostsResponse.length(); i++) {
            try {
                JSONObject jsonFeedPost = mComments.getJSONObject(i);
                JSONObject jsonImage = mImages.getJSONObject(i);
                FeedPost feedPost = new FeedPost(jsonFeedPost, jsonImage);
                mFeedPosts.add(feedPost);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onBindViewHolderAtPosition(FeedPostContract.View.ViewHolder viewHolderInterface, int i) {
        super.onBindViewHolderAtPosition(viewHolderInterface, i);
        // TODO implement onbind
    }

    /**
     * Gets the item Count of the feedposts JSONArray
     * @return the length
     */
    // TODO refactor when DataCache object is implemented
    @Override
    public int getItemCount() {
        return super.getItemCount();
    }

    // TODO refactor when DataCache object is implemented
    @Override
    public String getPosterIdByIndex(int i) {
        return getFeedPostByIndex(i).getPosterId();
    }

}
