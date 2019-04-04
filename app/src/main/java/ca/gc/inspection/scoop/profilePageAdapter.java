package ca.gc.inspection.scoop;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

public class profilePageAdapter extends FragmentStatePagerAdapter {
    int mNumOfTabs;

    public profilePageAdapter(FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
    }

    @Override
    public Fragment getItem(int i) {
        switch(i) {
            case 0:
                profilePostsFragment tab1 = new profilePostsFragment();
                return tab1;
            case 1:
                profileLikesFragment tab2 = new profileLikesFragment();
                return tab2;
            case 2:
                profileCommentsFragment tab3 = new profileCommentsFragment();
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
