package ca.gc.inspection.scoop;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TableRow;
import android.widget.TextView;

import org.json.JSONObject;

import ca.gc.inspection.scoop.util.CameraUtils;
import de.hdodenhof.circleimageview.CircleImageView;


public class OtherUserFragment extends Fragment {

	// UI Declarations
	static CircleImageView profileImageIV;
	static ImageView facebookIV, instagramIV, twitterIV, linkedinIV;
	static TextView fullNameTV, roleTV, locationTV;
	static Activity activity;
	static TableRow facebookTR, instagramTR, twitterTR, linkedinTR;
	static TextView facebookTV, instagramTV, twitterTV, linkedinTV;

	static String title;

	public OtherUserFragment() {
		// Required empty public constructor
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		// Inflate the layout for this fragment
		View view = inflater.inflate(R.layout.fragment_other_user, container, false);

		title = (String) ((AppCompatActivity) getActivity()).getSupportActionBar().getTitle();

		((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("");

		// Getting the activity from the view
		activity = (Activity) view.getContext();

		// Getting the clicked on user's userid from the bundle set in mainscreen
		Bundle bundle = getArguments();
		String userid = bundle.getString("userid");
		Log.i("userid", userid);

		// ImageView Definitions
		profileImageIV = view.findViewById(R.id.fragment_other_user_img_profile);
		facebookIV = view.findViewById(R.id.fragment_other_user_img_facebook);
		instagramIV = view.findViewById(R.id.fragment_other_user_img_instagram);
		twitterIV = view.findViewById(R.id.fragment_other_user_img_twitter);
		linkedinIV = view.findViewById(R.id.fragment_other_user_img_linkedin);

		// Textview Definitions
		fullNameTV = view.findViewById(R.id.fragment_other_user_txt_name);
		roleTV = view.findViewById(R.id.fragment_other_user_txt_position);
		locationTV = view.findViewById(R.id.fragment_other_user_txt_location);

		// Table Row Definitions
		facebookTR = view.findViewById(R.id.fragment_other_user_tr_facebook);
		instagramTR = view.findViewById(R.id.fragment_other_user_tr_instagram);
		twitterTR = view.findViewById(R.id.fragment_other_user_tr_twitter);
		linkedinTR = view.findViewById(R.id.fragment_other_user_tr_linkedin);

		// Table Row Text View Definitions
		facebookTV = view.findViewById(R.id.fragment_other_user_txt_facebook);
		instagramTV = view.findViewById(R.id.fragment_other_user_txt_instagram);
		twitterTV = view.findViewById(R.id.fragment_other_user_txt_twitter);
		linkedinTV = view.findViewById(R.id.fragment_other_user_txt_linkedin);

		// initializing the tab layout for profile -> posts, likes, comments
		TabLayout tabLayout = view.findViewById(R.id.fragment_other_user_tl);
		tabLayout.addTab(tabLayout.newTab().setText("POSTS"));
		tabLayout.addTab(tabLayout.newTab().setText("LIKES"));
		tabLayout.addTab(tabLayout.newTab().setText("COMMENTS"));
		tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

		// initiailizing the view pager and page adapter for it
		final ViewPager viewPager = view.findViewById(R.id.fragment_other_user_vp);
		final PagerAdapter adapter = new ProfileFragmentPagerAdapter(getChildFragmentManager(), tabLayout.getTabCount(), userid);
		viewPager.setAdapter(adapter);

		// a select listener to display the right tab fragment
		viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
		tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
			@Override
			public void onTabSelected(TabLayout.Tab tab) {
				viewPager.setCurrentItem(tab.getPosition());
			}

			@Override
			public void onTabUnselected(TabLayout.Tab tab) { }

			@Override
			public void onTabReselected(TabLayout.Tab tab) {}
		});

		// Getting the information from the database - ProfileScreenController
		ProfileScreenController.getOtherUserInfo(getContext(), userid);

		return view;
	}

	/**
	 * Dealing with the response
	 * @param response
	 */
	public static void otherInformationRespone(JSONObject response) {
		// Declaring the strings needed
		String fullname, role, location, position, division, city, province, twitterURL, linkedinURL, facebookURL, instagramURL, imageEncoded;
		try {
			// Full Name
			fullname = response.getString("fullname");
			fullNameTV.setText(fullname);

			// Profile Image
			imageEncoded = response.getString("profileimage");
			profileImageIV.setImageBitmap(CameraUtils.stringToBitmap(imageEncoded));

			// Position string set
			if (!response.get("position").toString().equals("null")) {
				position = response.getString("position");
			} else {
				position = null;
			}

			// Division string set
			if (!response.get("division").toString().equals("null")) {
				division = response.getString("division");
			} else {
				division = null;
			}

			// City string set
			if (!response.get("city").toString().equals("null")) {
				city = response.getString("city");
			} else {
				city = null;
			}

			// Province string set
			if (!response.get("province").toString().equals("null")) {
				province = response.getString("province");
			} else {
				province = null;
			}

			// Twitter string set
			if (!response.get("twitter").toString().equals("null")) {
				twitterURL = "http://" + response.getString("twitter");
				twitterTV.setText(twitterURL);
				// Setting the onclick listener for the imageview
				SocialClicked(twitterURL, twitterTR);
			} else {
				twitterTR.setVisibility(View.GONE);
			}

			// Linkedin string set
			if (!response.get("linkedin").toString().equals("null")) {
				linkedinURL = "http://" + response.getString("linkedin");
				linkedinTV.setText(linkedinURL);
				// Setting the onclick listener for the imageview
				SocialClicked(linkedinURL, linkedinTR);
			} else {
				linkedinTR.setVisibility(View.GONE);
			}

			// Facebook String set
			if (!response.get("facebook").toString().equals("null")) {
				facebookURL = "http://" + response.getString("facebook");
				facebookTV.setText(facebookURL);
				// Setting the onclick listener for the imageview
				SocialClicked(facebookURL, facebookTR);
			} else {
				facebookTR.setVisibility(View.GONE);
			}

			// Instagram String set
			if (!response.get("instagram").toString().equals("null")) {
				instagramURL = "http://" + response.getString("instagram");
				instagramTV.setText(instagramURL);
				// Setting the onclick listener for the imageview
				SocialClicked(instagramURL, instagramTR);
			} else {
				instagramTR.setVisibility(View.GONE);
			}

			// Combining positions and division to fit into textview
			role = concatTwoWords(position, division);

			// Combining city and province to fit into textview
			location = concatTwoWords(city, province);

			// Dealing with null or empty textviews
			if (role.equals("")) {
				roleTV.setVisibility(View.GONE);
			} else {
				roleTV.setText(role);
			}
			if (location.equals("")) {
				locationTV.setVisibility(View.GONE);
			} else {
				locationTV.setText(location);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Setting onclick listeners for the social media text views
	 * @param url
	 * @param tableRow
	 */
	private static void SocialClicked(final String url, final TableRow tableRow) {
		if (url != null) {
			tableRow.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					// Opens social media on chosen application
					Intent intent = new Intent();
					intent.setAction(Intent.ACTION_VIEW);
					intent.addCategory(Intent.CATEGORY_BROWSABLE);
					intent.setData(Uri.parse(url));
					activity.startActivity(intent);
				}
			});
		}
	}

	/**
	 * Method to concatenate two words based on their validity (null or not null)
	 * @param first
	 * @param second
	 * @return
	 */
	private static String concatTwoWords(String first, String second) {
		if (first != null && second != null) {
			return (first + ", " + second);
		} else if (first != null && second == null) {
			return first;
		} else if (first == null && second != null) {
			return second;
		} else if (first == null && second == null) {
			return "";
		}
		return "";
	}
}
