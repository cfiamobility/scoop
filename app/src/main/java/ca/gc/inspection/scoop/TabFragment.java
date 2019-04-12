package ca.gc.inspection.scoop;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;


public class TabFragment extends Fragment {

	// UI Declarations
	static ViewPager viewPager;
	static Button createPost;

	// Application support declaration
	MenuItem previousMenuItem;

	// Fragment Declarations
	communityFeedScreen communityFragment;
	officialFeedScreen officialFragment;
	NotificationsScreen notificationFragment;
	profileScreen profileFragment;

	public TabFragment() {
		// Required empty public constructor
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
	                         Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		final View view = inflater.inflate(R.layout.fragment_tab, container, false);

		// UI Definitions
		viewPager = view.findViewById(R.id.tabFrameLayout);
		createPost = view.findViewById(R.id.createPost);

		// When + button is pressed and create post activity is opened
		createPost.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				startActivity(new Intent(view.getContext(), CreatePostScreen.class));
			}
		});

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
					mainScreen.bottomNavigationView.getMenu().getItem(0).setChecked(true);
				}

				// Setting the highlight for swipe
				mainScreen.bottomNavigationView.getMenu().getItem(i).setChecked(true);
				previousMenuItem = mainScreen.bottomNavigationView.getMenu().getItem(i);

				// setting the title of the page every time you switch tabs and whether or not the create post button shows
				if (i == 0) {
					// Sets Title
					((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Community");
					// Sets + button visibility
					createPost.setVisibility(View.VISIBLE);
				} else if (i == 1) {
					((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Official");
					createPost.setVisibility(View.VISIBLE);
				} else if (i == 2) {
					((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Notifications");
					createPost.setVisibility(View.INVISIBLE);
				} else {
					((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Profile");
					createPost.setVisibility(View.INVISIBLE);
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
				// Sets button visibility
				createPost.setVisibility(View.VISIBLE);
				// Sets return value
				returnedValue = 0;
				break;
			case R.id.official:
				viewPager.setCurrentItem(1);
				createPost.setVisibility(View.VISIBLE);
				returnedValue = 1;
				break;
			case R.id.notifications:
				viewPager.setCurrentItem(2);
				createPost.setVisibility(View.INVISIBLE);
				returnedValue = 2;
				break;
			case R.id.profile:
				viewPager.setCurrentItem(3);
				createPost.setVisibility(View.INVISIBLE);
				returnedValue = 3;
				break;
		}
		return returnedValue;
	}

	/**
	 * Setting up the viewPager with all the fragments
	 * @param viewPager
	 */
	private void setupViewPager(ViewPager viewPager) {
		viewPagerAdapter adapter = new viewPagerAdapter(mainScreen.manager);

		communityFragment = new communityFeedScreen();
		officialFragment = new officialFeedScreen();
		notificationFragment = new NotificationsScreen();
		profileFragment =  new profileScreen();

		adapter.addFragment(communityFragment);
		adapter.addFragment(officialFragment);
		adapter.addFragment(notificationFragment);
		adapter.addFragment(profileFragment);

		viewPager.setAdapter(adapter);
	}


}
