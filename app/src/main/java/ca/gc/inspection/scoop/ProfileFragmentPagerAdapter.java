package ca.gc.inspection.scoop;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import ca.gc.inspection.scoop.ProfilePost.ProfilePostFragment;
import ca.gc.inspection.scoop.ProfileComment.ProfileCommentFragment;

public class ProfileFragmentPagerAdapter  extends FragmentStatePagerAdapter {
    int mNumOfTabs;
    String userid;

    public ProfileFragmentPagerAdapter (FragmentManager fm, int NumOfTabs, String userid) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
        this.userid = userid;
    }

    @Override
    public Fragment getItem(int i) {
        Bundle bundle = new Bundle();
        bundle.putString("userid", userid);
        switch (i) {
            case 0:
                ProfilePostFragment tab1 = new ProfilePostFragment();
                tab1.setArguments(bundle);
                return tab1;
            case 1:
                ProfileLikesFragment tab2 = new ProfileLikesFragment();
                tab2.setArguments(bundle);
                return tab2;
            case 2:
                ProfileCommentFragment tab3 = new ProfileCommentFragment();
                tab3.setArguments(bundle);
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
