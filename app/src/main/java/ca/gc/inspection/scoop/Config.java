package ca.gc.inspection.scoop;

import android.content.SharedPreferences;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class Config {
    public static String baseIP = "http://10.0.2.2:3000/";

    //gets changed in splash screen
    public static String currentUser = "";

    //For Post/Comment activity
    public static int postType = 1;
    static int commentType = 2;

    //For likes activity
    static int upvoteType = 1;
    static int downvoteType = -1;

    static RequestQueue requestQueue =  Volley.newRequestQueue(MyApplication.getContext());

    // token
    public static String token = "";

}