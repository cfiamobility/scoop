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
 * View for the notifications controller
 */
public class NotificationsFragment extends Fragment {

    public NotificationsFragment(){
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_notifications, container, false);

        // initializing the tab layout for profile -> posts, likes, comments
        TabLayout tabLayout = view.findViewById(R.id.fragment_notifications_tab);
        tabLayout.addTab(tabLayout.newTab().setText("TODAY"));
        tabLayout.addTab(tabLayout.newTab().setText("RECENT"));
        tabLayout.addTab(tabLayout.newTab().setText("OFFICIAL"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        // initiailizing the view pager and page adapter for it
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
//
//    private void setSwipeRefreshLayout(View view) {
//        mSwipeRefreshLayout = view.findViewById(R.id.fragment_notifications_swipe);
//        mSwipeRefreshLayout.setOnRefreshListener(this);
//        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary,
//                SWIPE_REFRESH_COLOUR_1,
//                SWIPE_REFRESH_COLOUR_2,
//                SWIPE_REFRESH_COLOUR_3);
//    }
//
//    /**
//     * To implement SwipeRefreshLayout.OnRefreshListener
//     */
//    @Override
//    public void onRefresh() {
//        loadDataFromDatabase();
//    }
//
//    @Override
//    public void onLoadedDataFromDatabase() {
////        waitingCallbacksCount -= 1;
////        if (waitingCallbacksCount == 0)
//            mSwipeRefreshLayout.setRefreshing(false);
//
////        if (waitingCallbacksCount < 0)
////            Log.d(TAG, "waitingCallBacksCount < 0");
//    }
//
//    private void loadDataFromDatabase() {
//        mSwipeRefreshLayout.setRefreshing(true);
//        mPresenter.loadDataFromDatabase();
//    }
//
//    @Override
//    public void onResume() {
//        Log.d("NOTIFICATIONS_FRAGMENT", "on resume");
//        super.onResume();
//        if (!mSwipeRefreshLayout.isRefreshing())
//            loadDataFromDatabase();
//    }
//
//
//    /**
//     * Description: shows no new notification text and icon
//     */
//    public void showNoNotifications(){
//        noNotifications.setVisibility(View.VISIBLE);
//        noNotificationsImage.setVisibility(View.VISIBLE);
//    }
//
//    public void hideNoNotifications(){
//        noNotifications.setVisibility(View.GONE);
//        noNotificationsImage.setVisibility(View.GONE);
//    }
//
//    /**
//     * Description: shows the today portion of the fragment
//     */
//    public void showTodaySection() {
//        today.setVisibility(View.VISIBLE);
//        todayRecyclerView.setVisibility(View.VISIBLE);
//    }
//
//    /**
//     * Description: hides the today portion of the fragment
//     */
//    public void hideTodaySection() {
//        today.setVisibility(View.GONE);
//        todayRecyclerView.setVisibility(View.GONE);
//    }
//
//    /**
//     * Description: shows the recent portion of the fragment
//     */
//    public void showRecentSection() {
//        recent.setVisibility(View.VISIBLE);
//        recentRecyclerView.setVisibility(View.VISIBLE);
//    }
//
//    /**
//     * Description: hides the recent portion of the fragment
//     */
//    public void hideRecentSection() {
//        recent.setVisibility(View.GONE);
//        recentRecyclerView.setVisibility(View.GONE);
//    }
//
//    /**
//     * Description: hides the loading panel
//     */
////    public void hideLoadingPanel(){
////        view.findViewById(R.id.fragment_notifications_rl_loading_panel).setVisibility(View.GONE); //sets the loading screen to gone
////    }
//
//    /**
//     * Description: requests focus to the top of the page rather than the today recycler view
//     */
//    public void requestTodayFocus(){
////        view.findViewById(R.id.fragment_notifications_rv_today).setFocusable(false);
////        view.findViewById(R.id.view1).requestFocus();
//    }
//
//    /**
//     * Description: requests focus to the top of the recent view rather than the recent recycler view
//     */
//    public void requestRecentFocus(){
//        view.findViewById(R.id.fragment_notifications_rv_recent).setFocusable(false);
////        view.findViewById(R.id.view3).requestFocus();
//    }
//
//


}
