package ca.gc.inspection.scoop;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //fluff code to test jenkins
        for (int i = 0; i < 4; i++){
            Toast.makeText(this, "Hello " + i, Toast.LENGTH_SHORT).show();
            Log.i("Hello", Integer.toString(i));
        }
        Log.i("TEST", "Hello World, this is another jenkins test");
        for (int i = 0; i < 4; i++){
            Toast.makeText(this, "Hello " + i, Toast.LENGTH_SHORT).show();
            Log.i("Hello", Integer.toString(i));
        }
        //deleted the commend to test jenkins
    }
}
