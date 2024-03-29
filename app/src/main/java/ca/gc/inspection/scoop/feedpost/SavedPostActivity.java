package ca.gc.inspection.scoop.feedpost;

import ca.gc.inspection.scoop.*;

import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;


public class SavedPostActivity extends AppCompatActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saved_post);

        FrameLayout frame = new FrameLayout(this);
        frame.setId(R.id.activity_saved_post_frame);

        // set the system status bar color
        getWindow().setStatusBarColor(ContextCompat.getColor(getApplicationContext(), R.color.primary_dark));

        if (savedInstanceState == null) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.activity_saved_post_frame, new SavedPostFragment());
            ft.commit();
        }
    }

    public void finishActivity(View view) {
        finish();
    }

}
