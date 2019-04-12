package ca.gc.inspection.scoop;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;


public class TabFragment extends Fragment {

	static ViewPager viewPager;

	MenuItem previousMenuItem;

	communityFeedScreen communityFragment;
	officialFeedScreen officialFragment;
	notificationScreen notificationFragment;
	profileScreen profileFragment;

	public TabFragment() {
		// Required empty public constructor
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
	                         Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		View view = inflater.inflate(R.layout.fragment_tab, container, false);

		viewPager = view.findViewById(R.id.tabFrameLayout);
		setupViewPager(viewPager);

		viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

			@Override
			public void onPageScrolled(int i, float v, int i1) {}

			@Override
			public void onPageSelected(int i) {
				if (previousMenuItem != null) {
					previousMenuItem.setChecked(false);
				} else {
					mainScreen.bottomNavigationView.getMenu().getItem(0).setChecked(true);
				}
				mainScreen.bottomNavigationView.getMenu().getItem(i).setChecked(true);
				previousMenuItem = mainScreen.bottomNavigationView.getMenu().getItem(i);

				// setting the title of the page every time you switch tabs
				if (i == 0) {
					((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Community");
					mainScreen.createPost.setVisibility(View.VISIBLE);
				} else if (i == 1) {
					((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Official");
					mainScreen.createPost.setVisibility(View.VISIBLE);
				} else if (i == 2) {
					((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Notifications");
					mainScreen.createPost.setVisibility(View.INVISIBLE);
				} else {
					((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Profile");
					mainScreen.createPost.setVisibility(View.INVISIBLE);
				}
			}

			@Override
			public void onPageScrollStateChanged(int i) {}
		});

		viewPager.setCurrentItem(0);
		return view;
	}

	public static int setNavigation(int menuItemID) {
		int returnedValue = -1;
		switch (menuItemID) {
			case R.id.community:
				viewPager.setCurrentItem(0);
				mainScreen.createPost.setVisibility(View.VISIBLE);
				returnedValue = 0;
				break;
			case R.id.official:
				viewPager.setCurrentItem(1);
				mainScreen.createPost.setVisibility(View.VISIBLE);
				returnedValue = 1;
				break;
			case R.id.notifications:
				viewPager.setCurrentItem(2);
				mainScreen.createPost.setVisibility(View.INVISIBLE);
				returnedValue = 2;
				break;
			case R.id.profile:
				viewPager.setCurrentItem(3);
				mainScreen.createPost.setVisibility(View.INVISIBLE);
				returnedValue = 3;
				break;
		}
		return returnedValue;
	}

	private void setupViewPager(ViewPager viewPager) {
		viewPagerAdapter adapter = new viewPagerAdapter(getFragmentManager());

		communityFragment = new communityFeedScreen();
		officialFragment = new officialFeedScreen();
		notificationFragment = new notificationScreen();
		profileFragment =  new profileScreen();

		adapter.addFragment(communityFragment);
		adapter.addFragment(officialFragment);
		adapter.addFragment(notificationFragment);
		adapter.addFragment(profileFragment);

		viewPager.setAdapter(adapter);
	}


}
