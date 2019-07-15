package ca.gc.inspection.scoop.profile;


import org.json.JSONObject;

import java.util.HashMap;

import ca.gc.inspection.scoop.util.NetworkUtils;


/**
 *  - The ProfilePresenter provides methods for parsing and mapping user profile information
 *    and communicating it between the View and Model
 *  - This is the Presenter for the Profile action case
 */

public class ProfilePresenter implements ProfileContract.Presenter {

    private ProfileContract.View mView;
    private ProfileInteractor mInteractor;
    private HashMap <String, String> profileInfoFields;

    /**
     * Constructor that takes in the respective action case View and constructs the Interactor
     * Creates a HashMap to map the responseStrings from the database
     * @param view View that is linked to the respective Presenter
     */
    ProfilePresenter(ProfileContract.View view, NetworkUtils network){
        mView = view;
        mInteractor = new ProfileInteractor(this, network);
        profileInfoFields = new HashMap<>();
    }

    /**
     * Invoked by the View and calls the Interactor to pull the user Profile Info
     * @param userid
     */
    public void getUserInfo(String userid){
        mInteractor.getUserInfo(userid);
    }

//    public void getOtherUserInfo(String userid){
//        mInteractor.getOtherUserInfo(userid);
//    }


    /**
     * Parses and maps the response Strings for each Profile Info field
     * @param response JSONObject pulled from the middle-tier
     */
    void informationResponse(JSONObject response){
        try {
            profileInfoFields.put("fullname", response.getString("fullname"));
            profileInfoFields.put("profileImageEncoded", response.getString("profileimage"));

            profileInfoFields.put("twitterURL", checkNullString(response.getString("twitter")));
            profileInfoFields.put("linkedinURL", checkNullString(response.getString("linkedin")));
            profileInfoFields.put("facebookURL", checkNullString(response.getString("facebook")));
            profileInfoFields.put("instagramURL", checkNullString(response.getString("instagram")));

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

    /**
     * Helper method that converts the response String from the middle-tier if null
     * @param responseGet
     * @return Null object type if "null"; Otherwise keep the received String
     */
    private static String checkNullString(String responseGet){
        if (responseGet.equals("null")){
            return null;
        }
        return responseGet;
    }

    /**
     * Helper method that concatenates two words to be displayed in the profile field with a comma in between
     * @param first
     * @param second
     * @return the concatenated word or just one if either is null
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

