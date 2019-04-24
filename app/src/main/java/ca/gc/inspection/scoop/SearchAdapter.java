package ca.gc.inspection.scoop;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

public class SearchAdapter extends FragmentStatePagerAdapter {
    int mNumOfTabs;

    public SearchAdapter(FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
    }

    @Override
    public Fragment getItem(int i) {
        switch (i) {
            case 0:
                TopSearchResultsFragment tab1 = new TopSearchResultsFragment();
                return tab1;
            case 1:
                PeopleSearchResultsFragment tab2 = new PeopleSearchResultsFragment();
                return tab2;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}
