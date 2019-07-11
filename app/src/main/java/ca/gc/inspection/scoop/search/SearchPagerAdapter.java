package ca.gc.inspection.scoop.search;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import ca.gc.inspection.scoop.searchpeople.PeopleSearchResultsFragment;
import ca.gc.inspection.scoop.searchpost.SearchPostFragment;

public class SearchPagerAdapter extends FragmentStatePagerAdapter {
    private int mNumOfTabs;

    SearchPagerAdapter(FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
    }

    @Override
    public Fragment getItem(int i) {
        switch (i) {
            case 0:
                SearchPostFragment tab1 = new SearchPostFragment();
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
