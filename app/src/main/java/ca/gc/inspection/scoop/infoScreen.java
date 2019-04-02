package ca.gc.inspection.scoop;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class infoScreen extends AppCompatActivity {

    // when the back button is pressed
    public void finishActivity(View view) {
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);

    }
}
