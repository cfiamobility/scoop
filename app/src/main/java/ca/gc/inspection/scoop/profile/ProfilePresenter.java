package ca.gc.inspection.scoop.profile;

import android.net.Network;
import android.util.Log;
import android.view.View;

import org.json.JSONObject;

import java.util.HashMap;

import ca.gc.inspection.scoop.util.CameraUtils;
import ca.gc.inspection.scoop.util.NetworkUtils;

public class ProfilePresenter implements ProfileContract.Presenter {

    private ProfileContract.View mView;
    private ProfileInteractor mInteractor;
    private HashMap <String, String> profileInfoFields;


    ProfilePresenter(ProfileContract.View view, NetworkUtils network){
        mView = view;
        mInteractor = new ProfileInteractor(this, network);
        profileInfoFields = new HashMap<>();
    }



    public void getUserInfo(){
        mInteractor.getUserInfo();
    }

    public void getOtherUserInfo(String userid){
        mInteractor.getOtherUserInfo(userid);
    }


    void informationResponse(JSONObject response){
        try {
            profileInfoFields.put("fullname", response.getString("fullname"));
            profileInfoFields.put("profileImageEncoded", response.getString("profileimage"));

            profileInfoFields.put("twitterURL", response.getString("twitter"));
            profileInfoFields.put("linkedinURL", response.getString("linkedin"));
            profileInfoFields.put("facebookURL", response.getString("facebook"));
            profileInfoFields.put("instagramURL", response.getString("instagram"));

            // Combining positions and division to fit into textview
            profileInfoFields.put("role",
                    concatTwoWords(response.getString("position"), response.getString("division")));

            // Combining city and province to fit into textview
            profileInfoFields.put("location",
                    concatTwoWords(response.getString("city"), response.getString("province")));

            mView.setProfileInfoFields(profileInfoFields);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    void otherInformationResponse(JSONObject response){
    }

    /**
     *
     * @param first
     * @param second
     * @return
     */
    private static String concatTwoWords(String first, String second) {
        if (!first.equals("null") && !second.equals("null")) {
            return (first + ", " + second);
        } else if (!first.equals("null")) {
            return first;
        } else if (!second.equals("null")) {
            return second;
        }
        return "";
    }

}








//	// Request to get the current user's profile information
//	public void getUserInfo(Context context) {
//		RequestQueue requestQueue = Volley.newRequestQueue(context);
//
//		String url = Config.baseIP + "profile/initialfill/" + Config.currentUser;
//
//		JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
//			@Override
//			public void onResponse(JSONObject response) {
//				// Sending the response back to be decomposed
//				ProfileFragment.informationResponse(response);
//			}
//		}, new Response.ErrorListener() {
//			@Override
//			public void onErrorResponse(VolleyError error) {}
//		}) {
//			@Override
//			public Map<String, String> getHeaders() throws AuthFailureError {
//				// inserting the token into the response header that will be sent to the server
//				Map<String, String> header = new HashMap<>();
//				header.put("authorization", Config.token);
//				return header;
//			}
//		};
//		// submitting the request
//		requestQueue.add(jsonObjectRequest);
//	}
//
//	// Request to get the clicked on user's profile information
//	public static void getOtherUserInfo(Context context, String userid) {
//		RequestQueue requestQueue = Volley.newRequestQueue(context);
//
//		String url = Config.baseIP + "profile/initialfill/" + userid;
//
//		JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
//			@Override
//			public void onResponse(JSONObject response) {
//				// Sending the response back to be decomposed
//				OtherUserFragment.otherInformationRespone(response);
//			}
//		}, new Response.ErrorListener() {
//			@Override
//			public void onErrorResponse(VolleyError error) {}
//		}) {
//			@Override
//			public Map<String, String> getHeaders() throws AuthFailureError {
//				// inserting the token into the response header that will be sent to the server
//				Map<String, String> header = new HashMap<>();
//				header.put("authorization", Config.token);
//				return header;
//			}
//		};
//		// submitting the request
//		requestQueue.add(jsonObjectRequest);
//	}

