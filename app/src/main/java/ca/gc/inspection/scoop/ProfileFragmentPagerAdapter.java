package ca.gc.inspection.scoop;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

public class ProfileFragmentPagerAdapter  extends FragmentStatePagerAdapter {
    int mNumOfTabs;

    public ProfileFragmentPagerAdapter (FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
    }

    @Override
    public Fragment getItem(int i) {
        switch (i) {
            case 0:
                ProfilePostsFragment tab1 = new ProfilePostsFragment();
                return tab1;
            case 1:
                ProfileLikesFragment tab2 = new ProfileLikesFragment();
                return tab2;
            case 2:
                ProfileCommentsFragment tab3 = new ProfileCommentsFragment();
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
