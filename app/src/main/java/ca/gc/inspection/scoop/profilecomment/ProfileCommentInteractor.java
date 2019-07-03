package ca.gc.inspection.scoop.profilecomment;

import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.HashMap;
import java.util.Map;

import ca.gc.inspection.scoop.Config;
import ca.gc.inspection.scoop.postcomment.LikeState;
import ca.gc.inspection.scoop.postcomment.PostCommentInteractor;
import ca.gc.inspection.scoop.util.NetworkUtils;

import static ca.gc.inspection.scoop.Config.USERID_KEY;
import static ca.gc.inspection.scoop.profilecomment.ProfileComment.PROFILE_COMMENT_ACTIVITYID_KEY;
import static ca.gc.inspection.scoop.profilecomment.ProfileComment.PROFILE_COMMENT_LIKE_POSTERID_KEY;
import static ca.gc.inspection.scoop.profilecomment.ProfileComment.PROFILE_COMMENT_LIKE_TYPE_KEY;
import static com.google.gson.internal.$Gson$Preconditions.checkNotNull;

public class ProfileCommentInteractor extends PostCommentInteractor {
    /**
     * Interactor used to send requests to the network
     */

    /**
     * Empty constructor called by child classes (ie. ProfilePostInteractor) to allow them to set
     * their own presenter
     */
    public ProfileCommentInteractor() {
    }

    public ProfileCommentInteractor(ProfileCommentPresenter presenter){
        mPresenter = checkNotNull(presenter);
    }

}
