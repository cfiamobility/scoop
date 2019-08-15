package ca.gc.inspection.scoop.notifications;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import ca.gc.inspection.scoop.notifications.notificationsofficial.NotificationsOfficialFragment;
import ca.gc.inspection.scoop.notifications.notificationsrecent.NotificationsRecentFragment;
import ca.gc.inspection.scoop.notifications.notificationstoday.NotificationsTodayFragment;

/**
 * Notifications Pager Adapter that is responsible for switching to the respective Tab Fragments
 * when selected
 * Constructed in NotificationsFragment class of OnCreateView
 */
public class NotificationsPagerAdapter extends FragmentStatePagerAdapter {
    int mNumOfTabs;

    /**
     * Public constructor for Pager Adapters
     * @param fm Fragment Manager responsible for handling tab fragments
     * @param numOfTabs number of tabs in the tabLayout
     */
    public NotificationsPagerAdapter (FragmentManager fm, int numOfTabs){
        super(fm);
        mNumOfTabs = numOfTabs;
    }

    /**
     * PagerAdapter method required to Override
     * Returns the fragment that is selected to be launched in the NotificationsFragment
     * @param i position of tab in tabLayout
     * @return the selected tab
     */
    @Override
    public Fragment getItem(int i) {
        switch (i) {
            case 0:
                NotificationsTodayFragment tab1 = new NotificationsTodayFragment();
                return tab1;
            case 1:
                NotificationsRecentFragment tab2 = new NotificationsRecentFragment();
                return tab2;
            case 2:
                NotificationsOfficialFragment tab3 = new NotificationsOfficialFragment();
                return tab3;
            default:
                return null;
        }
    }

    /**
     * PagerAdapter method required to Override - gets the number of tabs passed in from the tabLayout when PagerAdapter is constructed
     * @return number of tabs that PagerAdapter is handling
     */
    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}
