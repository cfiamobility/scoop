package ca.gc.inspection.scoop.profilepost;
        import android.util.Log;

        import com.android.volley.AuthFailureError;
        import com.android.volley.Request;
        import com.android.volley.Response;
        import com.android.volley.VolleyError;
        import com.android.volley.toolbox.JsonArrayRequest;

        import org.json.JSONArray;

        import java.util.HashMap;
        import java.util.Map;

        import ca.gc.inspection.scoop.Config;
        import ca.gc.inspection.scoop.MySingleton;
        import ca.gc.inspection.scoop.profilecomment.ProfileCommentInteractor;
        import ca.gc.inspection.scoop.profilecomment.ProfileCommentPresenter;

        import static com.google.gson.internal.$Gson$Preconditions.checkNotNull;


public class ProfilePostInteractor extends ProfileCommentInteractor {

    ProfilePostInteractor(ProfilePostPresenter presenter) {
        mPresenter = checkNotNull(presenter);
    }

    /**
     * HTTP Requests to get all the user posts infos
     * @param userid: passes the userid of the profile clicked on
     */
    public void getUserPosts(MySingleton singleton, final String userid) {
        String url = Config.baseIP + "profile/posttextfill/" + userid + "/" + Config.currentUser;
        String responseUrl = Config.baseIP + "profile/postimagefill/" + userid;
        JsonArrayRequest postRequest = super.newProfileJsonArrayRequest(url, responseUrl);
        Config.requestQueue.add(postRequest);
//        singleton.addToRequestQueue(postRequest);
    }
}

