package ca.gc.inspection.scoop;

import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import java.util.Map;

public class CreatePostPresenter implements CreatePostContract.Presenter {

    private CreatePostInteractor mInteractor;

    CreatePostPresenter() {
        mInteractor = new CreatePostInteractor(this);
    }

    @Override
    public void start() {
    }

    /*** sendPostToDatabase
     * Simple post request to store the newly created post to the postcomment table
     *
     * @param singleton MySingleton
     * @param userId    current user's userid
     * @param title     user inputted title (Mandatory)
     * @param text      user inputted test (Mandatory)
     * @param imageBitmap   user inputted image (Optional)
     */
    @Override
    public void sendPostToDatabase(MySingleton singleton, final String userId, final String title, final String text, final String imageBitmap) {
        mInteractor.sendPostToDatabase(singleton, userId, title, text, imageBitmap);
    }
}
