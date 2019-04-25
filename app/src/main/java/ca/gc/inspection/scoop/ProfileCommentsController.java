package ca.gc.inspection.scoop;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;

import org.json.JSONArray;

import java.util.HashMap;
import java.util.Map;

public class ProfileCommentsController {
	private ProfileCommentsInterface profileCommentsInterface;

	/**
	 * Constructor
	 * @param profileCommentsInterface: interface
	 */
	public ProfileCommentsController(ProfileCommentsInterface profileCommentsInterface) {
		this.profileCommentsInterface = profileCommentsInterface;
	}

	/**
	 * HTTPRequests for comments and images
	 * @param userid: userid
	 */
	public void getUserComments(final String userid) {
		String url = Config.baseIP + "profile/commenttextfill/" + userid + "/" + Config.currentUser;
		JsonArrayRequest commentRequest = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
			@Override
			public void onResponse(final JSONArray commentsResponse) {
				String url = Config.baseIP + "profile/commentimagefill/" + userid;
				final JsonArrayRequest imageRequest = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
					@Override
					public void onResponse(JSONArray imagesResponse) {
						profileCommentsInterface.setCommentsRecylerView(commentsResponse, imagesResponse);
					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {

					}
				}) {
					public Map<String, String> getHeaders() throws AuthFailureError {
						// inserting the token into the response header that will be sent to the server
						Map<String, String> header = new HashMap<>();
						header.put("authorization", Config.token);
						return header;
					}
				};
				Config.requestQueue.add(imageRequest);
			}
		}, new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {

			}
		}) {
			public Map<String, String> getHeaders() throws AuthFailureError {
				// inserting the token into the response header that will be sent to the server
				Map<String, String> header = new HashMap<>();
				header.put("authorization", Config.token);
				return header;
			}
		};
		Config.requestQueue.add(commentRequest);
	}

	public interface ProfileCommentsInterface {
		void setCommentsRecylerView(JSONArray comments, JSONArray images);
	}
}
