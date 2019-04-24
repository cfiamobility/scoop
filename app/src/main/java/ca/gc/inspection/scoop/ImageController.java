package ca.gc.inspection.scoop;

import android.content.Intent;
import android.graphics.Bitmap;
import android.view.View;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class ImageController extends GenericPostController{
    private JSONObject images;
    private FeedPostViewHolder holder;
    private ImageInterface imageInterface;

    public ImageController(ImageInterface imageInterface, JSONArray posts, JSONArray images, int i, FeedPostViewHolder holder){
        super(imageInterface, posts, images, i, holder);

        this.imageInterface = imageInterface;
        try {
            this.images = images.getJSONObject(i);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        this.holder = holder;
    }

    @Override
    public void displayImages() throws JSONException {
        if(images != null) { // null check to see if there are images
            formatImage(images.getString("postimagepath"), "post"); //formats post image
            formatImage(images.getString("profileimage"), "user"); //formats profile image
        }else{
            imageInterface.hidePostImage(holder); //hides post image
        }

        // tapping on any item from the view holder will go to the display post activity
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.getContext().startActivity(new Intent(v.getContext(), displayPost.class));
            }
        });
    }

    /**
     * Description: changes image from a string to a bitmap, then setting image
     * @param image: image to convert
     * @param type: type of image
     */
    private void formatImage(String image, String type){
        Bitmap bitmap = MyCamera.stringToBitmap(image); //converts image string to bitmap
        if(type.equals("post")) {
            if(!image.equals("") && !image.equals("null")) {
               imageInterface.setPostImage(bitmap, holder);
            } else{
                imageInterface.hidePostImage(holder);
            }
        }else{
            imageInterface.setUserImage(bitmap, holder);
        }
    }

    public interface ImageInterface extends GenericPostInterface {
        void setPostImage(Bitmap image, FeedPostViewHolder  holder);
        void hidePostImage(FeedPostViewHolder  holder);
    }
}
