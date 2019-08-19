package ca.gc.inspection.scoop.profile;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.HashMap;

import ca.gc.inspection.scoop.R;
import ca.gc.inspection.scoop.util.CameraUtils;
import ca.gc.inspection.scoop.util.NetworkUtils;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * - OtherUserFragment is the screen that is displayed when a users profile/name other than the current user is clicked on any screen
 * - It holds the profile information as well as a frame (ViewPagerAdapater) to display the three profile tabs and the respective fragments
 * - This is one of two Views for the Profile action case
 */
public class OtherUserFragment extends Fragment implements ProfileContract.View {

	// UI Declarations
	static CircleImageView profileImageIV;
	ImageView facebookIV, instagramIV, twitterIV, linkedinIV;
	TextView fullNameTV, roleTV, locationTV;
	TableRow facebookTR, instagramTR, twitterTR, linkedinTR;
	TextView facebookTV, instagramTV, twitterTV, linkedinTV;

	public static String title;

    protected ProfileContract.Presenter mProfilePresenter;
    private String userid;

    /**
     * Required empty default constructor for Fragments
     */
    public OtherUserFragment() {
	}

    /**
     * Invoked by the View in onCreateView in which it constructs the Presenter
     * @param presenter Presenter to be associated with the View and access later
     */
	public void setPresenter(ProfileContract.Presenter presenter) {
		mProfilePresenter = presenter;
	}

    /**
     * Creates the View and populates it with the associate Android Views of the Other User Screen
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return view with appropriate tablerows and imageviews and tab fragments
     */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setPresenter(new ProfilePresenter(this, NetworkUtils.getInstance(getContext())));

        // Inflate the layout for this fragment
		View view = inflater.inflate(R.layout.fragment_other_user, container, false);

        // Getting the clicked on user's userid from the bundle set in mainscreen
		Bundle bundle = getArguments();
		userid = bundle.getString("userid");

        // Getting the information from the database - ProfilePresenter
        mProfilePresenter.getUserInfo(userid);

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
		tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
			@Override
			public void onTabSelected(TabLayout.Tab tab) {
				viewPager.setCurrentItem(tab.getPosition());
			}

			@Override
			public void onTabUnselected(TabLayout.Tab tab) { }

			@Override
			public void onTabReselected(TabLayout.Tab tab) {}
		});

		return view;
	}

    /**
     * Invoked by the Presenter and stores all profile information pulled from database into respective Views
     * @param profileInfoFields HashMap of profile information for each field
     */
    public void setProfileInfoFields(HashMap<String, String> profileInfoFields) {
	    fullNameTV.setText(profileInfoFields.get("fullname"));
        profileImageIV.setImageBitmap(CameraUtils.stringToBitmap(profileInfoFields.get("profileImageEncoded")));

        displaySocialMediaTR(profileInfoFields.get("twitterURL"), twitterTR, twitterTV, "twitter");
        displaySocialMediaTR(profileInfoFields.get("linkedinURL"), linkedinTR, linkedinTV, "linkedin");
        displaySocialMediaTR(profileInfoFields.get("facebookURL"), facebookTR, facebookTV, "facebook");
        displaySocialMediaTR(profileInfoFields.get("instagramURL"), instagramTR, instagramTV, "instagram");

        if (profileInfoFields.get("role").equals("")) {
            roleTV.setVisibility(View.GONE);
        } else {
            roleTV.setText(profileInfoFields.get("role"));
        }

        if (profileInfoFields.get("location").equals("")) {
            locationTV.setVisibility(View.GONE);
        } else {
            locationTV.setText(profileInfoFields.get("location"));
        }
    }


    /**
     * Helper method that sets the View of the given Social Media View and invokes the listener
     * @param socialMediaUrl username of user for the social media
     * @param socialMediaTR tablerow that holds the textview
     * @param socialMediaTV textview that holds the username string
     * @param socialMediaType type of social media
     */
    protected void displaySocialMediaTR(String socialMediaUrl, TableRow socialMediaTR,
                                        TextView socialMediaTV, String socialMediaType) {
        if (socialMediaUrl == null) {
            socialMediaTR.setVisibility(View.GONE);
        } else {
            socialMediaTV.setText(socialMediaUrl);
            SocialClicked(socialMediaUrl, socialMediaTR, socialMediaType);
        }
    }

    /**
     * Setting onclick listeners for the social media table rows and opens the social media profile
     * in browser
     *
     * @param url user's personal social media url
     * @param tableRow social media table row
     */
    protected void SocialClicked(final String url, final TableRow tableRow, String socialMediaType) {
        if (url != null) {
            tableRow.setOnClickListener(v -> {
                // Opens social media on chosen application
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                intent.addCategory(Intent.CATEGORY_BROWSABLE);

                switch(socialMediaType){
                    case ("facebook"):
                        intent.setData(Uri.parse("http://www.facebook.com/" + url));
                        break;
                    case ("instagram"):
                        intent.setData(Uri.parse("http://www.instagram.com/" + url));
                        break;
                    case ("linkedin"):
                        intent.setData(Uri.parse("http://www.linkedin.com/in/" + url));
                        break;
                    case ("twitter"):
                        intent.setData(Uri.parse("http://www.twitter.com/" + url));
                        break;
                }
                getContext().startActivity(intent);
            });
        }
    }

    /**
     * Overrides Android callback to load in updated other user information on the profile when fragment is resumed
     * Invokes Presenter method, which invokes Interactor method to get data
     */
    @Override
    public void onResume() {
        super.onResume();
        if (userid != null)
            mProfilePresenter.getUserInfo(userid);
    }
}
