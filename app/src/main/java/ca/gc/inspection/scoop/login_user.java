package ca.gc.inspection.scoop;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;


public class login_user extends AppCompatActivity {
    EditText email, password;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        email = (EditText) findViewById(R.id.email); //instantiating email
        password = (EditText) findViewById(R.id.password); //instantiating password
    }

    /**
     * Description: login is the method called when pressing the login button on the login screen
     * @param view
     *
     */
    public void login(View view){
        Controller.loginUser(email.getText().toString(), password.getText().toString(), getApplicationContext(), this); //calling the loginUser method
    }
}