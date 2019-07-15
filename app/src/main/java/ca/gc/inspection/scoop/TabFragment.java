package ca.gc.inspection.scoop;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import ca.gc.inspection.scoop.feedpost.CommunityFeedFragment;
import ca.gc.inspection.scoop.feedpost.OfficialFeedFragment;
import ca.gc.inspection.scoop.notifications.NotificationsFragment;
import ca.gc.inspection.scoop.profile.ProfileFragment;


public class TabFragment extends Fragment {

	// UI Declarations
	static ViewPager viewPager;
	static FloatingActionButton createPost;
	static MainViewPagerAdapter adapter;

	// Application support declaration
	MenuItem previousMenuItem;

	// Fragment Declarations
	static CommunityFeedFragment communityFragment;
	static OfficialFeedFragment officialFragment;
	static NotificationsFragment notificationFragment;
	static ProfileFragment profileFragment;

	public TabFragment() {
		// Required empty public constructor
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
	                         Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		final View view = inflater.inflate(R.layout.fragment_tab, container, false);

		Log.i("recreated", "recreated");
		// UI Definitions
		viewPager = view.findViewById(R.id.tabFrameLayout);

		// Setting up the view pager with all the neccessary fragments
		setupViewPager(viewPager);

		// Swipe change listener - used to change the bottom navigation highlight
		viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

			@Override
			public void onPageScrolled(int i, float v, int i1) {}

			@Override
			public void onPageSelected(int i) {

				// Initial run
				if (previousMenuItem != null) {
					previousMenuItem.setChecked(false);
				} else {
					MainActivity.bottomNavigationView.getMenu().getItem(0).setChecked(true);

				}

				// Setting the highlight for swipe
				MainActivity.bottomNavigationView.getMenu().getItem(i).setChecked(true);
				previousMenuItem = MainActivity.bottomNavigationView.getMenu().getItem(i);

				// setting the title of the page every time you switch tabs and whether or not the create Post button shows
				if (i == 0) {
					// Sets Title
					((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Community");
				} else if (i == 1) {
					((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Official");

				} else if (i == 2) {
					((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Notifications");

				} else {
					((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Profile");

				}
			}

			@Override
			public void onPageScrollStateChanged(int i) {}
		});

		// Initial page set
		viewPager.setCurrentItem(0);

		return view;
	}

	/**
	 * Method to set up the onclicks for the bottom nav bar - communicates with bottomnavigation item selected
	 * @param menuItemID
	 * @return
	 */
	public static int setNavigation(int menuItemID) {
		// Bad menuItem id
		int returnedValue = -1;

		switch (menuItemID) {
			// If community is pressed
			case R.id.community:
				// Set the page
				viewPager.setCurrentItem(0);
				// Sets return value
				returnedValue = 0;
				break;
			case R.id.official:
				viewPager.setCurrentItem(1);
				returnedValue = 1;
				break;
			case R.id.notifications:
				viewPager.setCurrentItem(2);
				returnedValue = 2;
				break;
			case R.id.profile:
				viewPager.setCurrentItem(3);
				returnedValue = 3;
				break;
		}
		return returnedValue;
	}

	/**
	 * Setting up the viewPager with all the fragments
	 * @param viewPager
	 */
	private static void setupViewPager(ViewPager viewPager) {
		adapter = new MainViewPagerAdapter(MainActivity.manager);

		communityFragment = new CommunityFeedFragment();
		officialFragment = new OfficialFeedFragment();
		notificationFragment = new NotificationsFragment();
		profileFragment =  new ProfileFragment();

		adapter.addFragment(communityFragment);
		adapter.addFragment(officialFragment);
		adapter.addFragment(notificationFragment);
		adapter.addFragment(profileFragment);

		viewPager.setAdapter(adapter);
	}

	public static void refresh() {
		int currentitem = viewPager.getCurrentItem();
		setupViewPager(viewPager);
		viewPager.setCurrentItem(currentitem);

	}


}
