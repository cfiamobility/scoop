package ca.gc.inspection.scoop.profile;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;

import ca.gc.inspection.scoop.Config;
import ca.gc.inspection.scoop.R;

import static ca.gc.inspection.scoop.Config.INTENT_POSTER_ID_KEY;

/**
 * This Activity holds the OtherUserFragment View in a frame; different from viewing in the Profile tab
 */
public class OtherUserActivity extends AppCompatActivity {

    private static FragmentManager manager;
    private String userid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_other_user);

        FrameLayout frame = new FrameLayout(this);
        frame.setId(R.id.activity_other_user_frame);

        // set the system status bar color
        getWindow().setStatusBarColor(ContextCompat.getColor(getApplicationContext(), R.color.primary_dark));

        Bundle extras = getIntent().getExtras();
        userid = extras.getString(INTENT_POSTER_ID_KEY);
        Log.i("userid in activity", userid);

        manager = getSupportFragmentManager();
        startCorrectFragment(userid);
    }

    /**
     * If the posterID received is the same as the current user, send to a ProfileFragment that is hosted within OtherUserActivity frame
     * Otherwise, display OtherUserFragment in OtherUserActivity frame
     * @param userid id of the user that the current user is clicking on (posterId)
     */
    public static void startCorrectFragment(String userid){
        if (userid.equals(Config.currentUser)){
            Fragment profileFragment = new ProfileFragment();
            // Passing the userid to the other user fragment using bundles
            Bundle bundle = new Bundle();
            bundle.putString("userid", userid);
            profileFragment.setArguments(bundle);

            FragmentTransaction ft = manager.beginTransaction();
            ft.replace(R.id.activity_other_user_frame, profileFragment);
            ft.commit();
        } else {
            Fragment otherUserFragment = new OtherUserFragment();
            // Passing the userid to the other user fragment using bundles
            Bundle bundle = new Bundle();
            bundle.putString("userid", userid);
            otherUserFragment.setArguments(bundle);

            FragmentTransaction ft = manager.beginTransaction();
            ft.replace(R.id.activity_other_user_frame, otherUserFragment);
            ft.commit();
        }

    }


    public void finishActivity(View view) {
        finish();
    }



}
