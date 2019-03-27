package ca.gc.inspection.scoop;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.i("TEST", "Hello World");
        Intent intent = new Intent(this, login_user.class);
        startActivity(intent);
        //deleted the commend to test jenkins
    }
}
