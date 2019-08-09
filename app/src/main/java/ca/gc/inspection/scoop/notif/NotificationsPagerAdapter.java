package ca.gc.inspection.scoop.notif;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import ca.gc.inspection.scoop.notif.notificationsofficial.NotificationsOfficialFragment;
import ca.gc.inspection.scoop.notif.notificationsrecent.NotificationsRecentFragment;
import ca.gc.inspection.scoop.notif.notificationstoday.NotificationsTodayFragment;

public class NotificationsPagerAdapter extends FragmentStatePagerAdapter {
    int mNumOfTabs;

    public NotificationsPagerAdapter (FragmentManager fm, int numOfTabs){
        super(fm);
        mNumOfTabs = numOfTabs;
    }

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

    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}
