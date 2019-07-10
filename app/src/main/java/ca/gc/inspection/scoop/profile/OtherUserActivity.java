package ca.gc.inspection.scoop.profile;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;

import ca.gc.inspection.scoop.R;

import static ca.gc.inspection.scoop.Config.INTENT_POSTER_ID_KEY;

/**
 * This Activity holds the OtherUserFragment View in a frame; different from viewing in the Profile tab
 */
public class OtherUserActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_other_user);

        FrameLayout frame = new FrameLayout(this);
        frame.setId(R.id.activity_other_user_frame);

        // set the system status bar color
        getWindow().setStatusBarColor(ContextCompat.getColor(getApplicationContext(), R.color.primary_dark));

        String userid;
        Bundle extras = getIntent().getExtras();
        userid = extras.getString(INTENT_POSTER_ID_KEY);
        Log.i("userid in activity", userid);


        Fragment otherUserFragment = new OtherUserFragment();
        // Passing the userid to the other user fragment using bundles
        Bundle bundle = new Bundle();
        bundle.putString("userid", userid);
        otherUserFragment.setArguments(bundle);

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.activity_other_user_frame, otherUserFragment);
        ft.commit();

    }

    public void finishActivity(View view) {
        finish();
    }



}
