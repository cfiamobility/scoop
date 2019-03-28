package ca.gc.inspection.scoop;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

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
        loginUser(email.getText().toString(), password.getText().toString()); //calling the loginUser method

    }

    /**
     *
     * @param email: email of the person logging in
     * @param password: password of the person logging in
     */
    private void loginUser(final String email, final String password){

        String URL = "http://10.0.2.2:3000/login"; //url for which the http request will be made, corresponding to Node.js code
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext()); //setting up the request queue for volley
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() { //setting up the request as a post request
            @Override
            public void onResponse(String response) {
                Log.i("response", response); //gets the response sent back by the Node.js code
                Toast.makeText(getApplicationContext(), response, Toast.LENGTH_SHORT).show(); //toast appears depending on the response message
                if(response.equals("Success")){ //if login was successful it will go through this if statement
//                    Intent intent = new Intent(getApplicationContext(), UserInterface.class); //changes activities once login is successful
//                    startActivity(intent);
                }

            }
        }, new Response.ErrorListener() {


            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("error info", error.toString()); //if there is an error, it will log the error

            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("email", email); //puts the required parameters into the Hashmap which is sent to Node.js code
                params.put("password", password);
                return params;
            }
        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy( //refrains Volley from sending information twice
                0,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(stringRequest); //adds the request to the request queue

    }
}