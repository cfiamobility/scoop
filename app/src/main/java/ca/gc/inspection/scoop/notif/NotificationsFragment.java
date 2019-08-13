package ca.gc.inspection.scoop.notif;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import ca.gc.inspection.scoop.R;

/**
 * Notifications Fragment that is responsible for hosting the Tab Layout for the notification options
 * This is the fragment that is created when the user selects the Notifications Tab in the bottom Navigation Bar
 */
public class NotificationsFragment extends Fragment {

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_notifications, container, false);

        // initializing the tab layout for notifications
        // TODAY, RECENT, OFFICIAL
        TabLayout tabLayout = view.findViewById(R.id.fragment_notifications_tab);
        tabLayout.addTab(tabLayout.newTab().setText("TODAY"));
        tabLayout.addTab(tabLayout.newTab().setText("RECENT"));
        tabLayout.addTab(tabLayout.newTab().setText("OFFICIAL"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        // initializing the view pager and page adapter for it
        final ViewPager viewPager = view.findViewById(R.id.fragment_notifications_vp);
        final PagerAdapter adapter = new NotificationsPagerAdapter(getChildFragmentManager(), tabLayout.getTabCount());
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
}
