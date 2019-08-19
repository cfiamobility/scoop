package ca.gc.inspection.scoop.profile;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;

import java.util.HashMap;
import java.util.Map;

import ca.gc.inspection.scoop.Config;
import ca.gc.inspection.scoop.util.NetworkUtils;

/**
 * ProfileInteractor used by Presenter to create GET request and display data in the Profile View
 */
public class ProfileInteractor {

    protected ProfilePresenter mPresenter;
    private NetworkUtils mNetwork;

	/**
	 * Constructor that requires a reference to the Presenter and the Network
	 * @param presenter a ProfilePresenter
	 * @param network Singleton utility to add string request to global application request queue
	 */
    ProfileInteractor(ProfilePresenter presenter, NetworkUtils network){
        mPresenter = presenter;
        mNetwork = network;
    }

	/**
	 * Request to get the current user's profile information
	 * @param userid id of the respective user clicked on
	 */
	void getUserInfo(String userid) {
		String url = Config.baseIP + "profile/initialfill/" + userid;

		JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, response -> {
			// Sending the response back to be processed by the Presenter
			mPresenter.informationResponse(response);
		}, error -> {}) {
			@Override
			public Map<String, String> getHeaders() throws AuthFailureError {
				// inserting the token into the response header that will be sent to the server
				Map<String, String> header = new HashMap<>();
				header.put("authorization", Config.token);
				return header;
			}
		};
		// submitting the request
		mNetwork.addToRequestQueue(jsonObjectRequest);
	}
}
