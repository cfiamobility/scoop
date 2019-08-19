package ca.gc.inspection.scoop.profile;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import ca.gc.inspection.scoop.Config;
import ca.gc.inspection.scoop.R;
import ca.gc.inspection.scoop.editprofile.EditProfileActivity;
import ca.gc.inspection.scoop.util.NetworkUtils;

/**
 * - ProfileFragment is the screen that is displayed when the profile tab is clicked in the navigation bar
 * - It holds the profile information as well as a frame (ViewPagerAdapater) to display the three profile tabs and the respective fragments
 * - This is one of two Views for the Profile action case
 */
public class ProfileFragment extends OtherUserFragment {

    Button editProfile;

    /**
     * Required empty default constructor for Fragments
     */
    public ProfileFragment() {
    }

    /**
     * Creates the View and populates it with the associate Android Views of the Profile Screen
     * Note: Same as OtherUserFragment, but contains an Edit Profile button
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return view with appropriate tablerows and imageviews and tab fragments
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        setPresenter(new ProfilePresenter(this, NetworkUtils.getInstance(getContext())));

        //Getting the user's information
        mProfilePresenter.getUserInfo(Config.currentUser);

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

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
        editProfile.setOnClickListener(v -> startActivity(new Intent(getContext(), EditProfileActivity.class)));

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
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
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
        return view;
    }

    /**
     * Overrides Android callback to load in user information on the profile when fragment is resumed
     * Invokes Presenter method, which invokes Interactor method to get data
     */
    @Override
    public void onResume() {
        super.onResume();
        mProfilePresenter.getUserInfo(Config.currentUser);
    }
}
