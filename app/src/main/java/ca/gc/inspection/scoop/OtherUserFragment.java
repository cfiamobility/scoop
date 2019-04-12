package ca.gc.inspection.scoop;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONObject;

import de.hdodenhof.circleimageview.CircleImageView;


public class OtherUserFragment extends Fragment {


	static CircleImageView profileImageIV;
	static ImageView facebookIV, instagramIV, twitterIV, locationLogo, linkedinIV;
	static TextView fullNameTV, roleTV, locationTV;
	static Activity activity;


	public OtherUserFragment() {
		// Required empty public constructor
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		View view = inflater.inflate(R.layout.fragment_other_user, container, false);

		activity = (Activity) view.getContext();

		// ImageView Definitions
		profileImageIV = view.findViewById(R.id.otherProfileImage);
		facebookIV = view.findViewById(R.id.otherFacebookLogo);
		instagramIV = view.findViewById(R.id.otherInstagramLogo);
		twitterIV = view.findViewById(R.id.otherTwitterLogo);
		linkedinIV = view.findViewById(R.id.otherLinkedinLogo);
		locationLogo = view.findViewById(R.id.otherLocationLogo);

		// Textview Definitions
		fullNameTV = view.findViewById(R.id.otherProfileName);
		roleTV = view.findViewById(R.id.otherPositionName);
		locationTV = view.findViewById(R.id.otherLocationText);

		// initializing the tab layout for profile -> posts, likes, comments
		TabLayout tabLayout = view.findViewById(R.id.otherTabLayout);
		tabLayout.addTab(tabLayout.newTab().setText("POSTS"));
		tabLayout.addTab(tabLayout.newTab().setText("LIKES"));
		tabLayout.addTab(tabLayout.newTab().setText("COMMENTS"));
		tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

		// initiailizing the view pager and page adapter for it
		final ViewPager viewPager = view.findViewById(R.id.otherViewPager);
		final PagerAdapter adapter = new profilePageAdapter(getChildFragmentManager(), tabLayout.getTabCount());
		viewPager.setAdapter(adapter);

		// a select listener to display the right tab fragment
		viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
		tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
			@Override
			public void onTabSelected(TabLayout.Tab tab) {
				viewPager.setCurrentItem(tab.getPosition());
			}

			@Override
			public void onTabUnselected(TabLayout.Tab tab) {

			}

			@Override
			public void onTabReselected(TabLayout.Tab tab) {

			}
		});

		Bundle bundle = getArguments();
		String userid = bundle.getString("userid");
		Log.i("userid", userid);

		ProfileScreenController.getOtherUserInfo(getContext(), userid);

		return view;
	}

	public static void otherInformationRespone(JSONObject response) {
		String fullname, role, location, position, division, city, province, twitterURL, linkedinURL, facebookURL, instagramURL, imageEncoded;
		try {
			// Full Name
			fullname = response.getString("fullname");
			fullNameTV.setText(fullname);

			// Profile Image
			imageEncoded = response.getString("profileimage");
			profileImageIV.setImageBitmap(MyCamera.stringToBitmap(imageEncoded));

			if (!response.get("position").toString().equals("null")) {
				position = response.getString("position");
			} else {
				position = null;
			}

			if (!response.get("division").toString().equals("null")) {
				division = response.getString("division");
			} else {
				division = null;
			}

			if (!response.get("city").toString().equals("null")) {
				city = response.getString("city");
			} else {
				city = null;
			}

			if (!response.get("province").toString().equals("null")) {
				province = response.getString("province");
			} else {
				province = null;
			}

			if (!response.get("twitter").toString().equals("null")) {
				twitterURL = "http://" + response.getString("twitter");
				SocialClicked(twitterURL, twitterIV);
			} else {
				twitterIV.setVisibility(View.GONE);
			}

			if (!response.get("linkedin").toString().equals("null")) {
				linkedinURL = "http://" + response.getString("linkedin");
				SocialClicked(linkedinURL, linkedinIV);
			} else {
				linkedinIV.setVisibility(View.GONE);
			}

			if (!response.get("facebook").toString().equals("null")) {
				facebookURL = "http://" + response.getString("facebook");
				SocialClicked(facebookURL, facebookIV);
			} else {
				facebookIV.setVisibility(View.GONE);
			}

			if (!response.get("instagram").toString().equals("null")) {
				instagramURL = "http://" + response.getString("instagram");
				SocialClicked(instagramURL, instagramIV);
			} else {
				instagramIV.setVisibility(View.GONE);
			}

			role = concatTwoWords(position, division);
			location = concatTwoWords(city, province);

			if (role.equals("")) {
				roleTV.setVisibility(View.GONE);
			} else {
				roleTV.setText(role);
			}

			if (location.equals("")) {
				locationTV.setVisibility(View.GONE);
				locationLogo.setVisibility(View.GONE);
			} else {
				locationTV.setText(location);
			}


		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static void SocialClicked(final String url, final ImageView imageView) {
		Log.i("url", url);
		if (url != null) {
			imageView.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					Intent intent = new Intent();
					intent.setAction(Intent.ACTION_VIEW);
					intent.addCategory(Intent.CATEGORY_BROWSABLE);
					intent.setData(Uri.parse(url));
					activity.startActivity(intent);
				}
			});
		}
	}

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
