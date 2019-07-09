package ca.gc.inspection.scoop.profile;


import android.app.Activity;
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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TableRow;
import android.widget.TextView;

import org.json.JSONObject;

import java.util.HashMap;

import ca.gc.inspection.scoop.Config;
import ca.gc.inspection.scoop.R;
import ca.gc.inspection.scoop.editprofile.EditProfileActivity;
import ca.gc.inspection.scoop.splashscreen.SplashScreenContract;
import ca.gc.inspection.scoop.util.CameraUtils;
import ca.gc.inspection.scoop.util.NetworkUtils;
import de.hdodenhof.circleimageview.CircleImageView;


/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends Fragment implements ProfileContract.View {

    // UI Declarations
    static CircleImageView profileImageIV;
    ImageView facebookIV, instagramIV, twitterIV, linkedinIV;
    TextView fullNameTV, roleTV, locationTV;
    Activity activity;
    TableRow facebookTR, instagramTR, twitterTR, linkedinTR;
    Button editProfile;
    TextView facebookTV, instagramTV, twitterTV, linkedinTV;

    // reference to the Presenter
    private ProfileContract.Presenter mProfilePresenter;


    public ProfileFragment() {
        // Required empty public constructor
    }

    public void setPresenter(ProfileContract.Presenter presenter) {
        mProfilePresenter = presenter;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        setPresenter(new ProfilePresenter(this, NetworkUtils.getInstance(getContext())));

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        // Getting the activity from the view
        activity = (Activity) view.getContext();

        // ImageView Definitions
        profileImageIV = view.findViewById(R.id.fragment_profile_img_profile);
        facebookIV = view.findViewById(R.id.fragment_profile_img_facebook);
        instagramIV = view.findViewById(R.id.fragment_profile_img_instagram);
        twitterIV = view.findViewById(R.id.fragment_profile_img_twitter);
        linkedinIV = view.findViewById(R.id.fragment_profile_img_linkedin);

        // Textview Definitions
        fullNameTV = view.findViewById(R.id.fragment_profile_txt_name);
        roleTV = view.findViewById(R.id.fragment_profile_txt_position);
        locationTV = view.findViewById(R.id.fragment_profile_txt_location);
        facebookTV = view.findViewById(R.id.fragment_profile_txt_facebook);
        instagramTV = view.findViewById(R.id.fragment_profile_txt_instagram);
        twitterTV = view.findViewById(R.id.fragment_profile_txt_twitter);
        linkedinTV = view.findViewById(R.id.fragment_profile_txt_linkedin);

        // Table Row Definitions
        facebookTR = view.findViewById(R.id.fragment_profile_tr_facebook);
        instagramTR = view.findViewById(R.id.fragment_profile_tr_instagram);
        twitterTR = view.findViewById(R.id.fragment_profile_tr_twitter);
        linkedinTR = view.findViewById(R.id.fragment_profile_tr_linkedin);

        // Button Definition
        editProfile = view.findViewById(R.id.fragment_profile_btn_edit_profile);

        // onClick Listener for Edit Button
        editProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), EditProfileActivity.class));
            }
        });

        // initializing the tab layout for profile -> posts, likes, comments
        TabLayout tabLayout = view.findViewById(R.id.fragment_profile_tl_profile);
        tabLayout.addTab(tabLayout.newTab().setText("POSTS"));
        tabLayout.addTab(tabLayout.newTab().setText("LIKES"));
        tabLayout.addTab(tabLayout.newTab().setText("COMMENTS"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        // initiailizing the view pager and page adapter for it
        final ViewPager viewPager = view.findViewById(R.id.fragment_profile_vp);
        final PagerAdapter adapter = new ProfileFragmentPagerAdapter(getChildFragmentManager(), tabLayout.getTabCount(), Config.currentUser);
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

        //Getting the user's information
        mProfilePresenter.getUserInfo();

        return view;
    }


    public void setProfileInfoFields(HashMap<String, String> profileInfoFields) {
        fullNameTV.setText(profileInfoFields.get("fullname"));
        profileImageIV.setImageBitmap(CameraUtils.stringToBitmap(profileInfoFields.get("profileImageEncoded")));

        displaySocialMediaTR(profileInfoFields.get("twitterURL"), twitterTR, twitterTV);
        displaySocialMediaTR(profileInfoFields.get("linkedinURL"), linkedinTR, linkedinTV);
        displaySocialMediaTR(profileInfoFields.get("facebookURL"), facebookTR, facebookTV);
        displaySocialMediaTR(profileInfoFields.get("instagramURL"), instagramTR, instagramTV);

        if (profileInfoFields.get("role").equals("")) {
            roleTV.setVisibility(View.GONE);
        } else {
            roleTV.setText(profileInfoFields.get("role"));
        }

        Log.i("LOCATION:",profileInfoFields.get("location"));
        if (profileInfoFields.get("location").equals("")) {
            locationTV.setVisibility(View.GONE);
        } else {
            locationTV.setText(profileInfoFields.get("location"));
        }
    }

    protected void displaySocialMediaTR(String socialMediaUrl, TableRow socialMediaTR, TextView socialMediaTV) {
        if (socialMediaUrl.equals("null")) {
            socialMediaTR.setVisibility(View.GONE);
        } else {
            socialMediaTV.setText(socialMediaUrl);
            SocialClicked(socialMediaUrl, socialMediaTR);
        }
    }

    /**
     * Setting onclick listeners for the social media text views
     *
     * @param url
     * @param tableRow
     */
    //TODO setup activity properly to open apps/prompt app store
    protected void SocialClicked(final String url, final TableRow tableRow) {
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

}
