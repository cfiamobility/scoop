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
    public static int commentType = 2;

    // token
    public static String token = "";

    public static final String USERID_KEY = "userid";

    // intent data keys
    public static String INTENT_ACTIVITY_ID_KEY = "ACTIVITY_ID";
    public static String INTENT_POSTER_ID_KEY = "posterid";
    public static String INTENT_ACTIVITY_TYPE_KEY = "activitytype";

    // swipe refresh colours
    public static int SWIPE_REFRESH_COLOUR_1 = android.R.color.holo_green_dark;
    public static int SWIPE_REFRESH_COLOUR_2 = android.R.color.holo_orange_dark;
    public static int SWIPE_REFRESH_COLOUR_3 = android.R.color.holo_blue_dark;

    // must be the same as the DATABASE_RESPONSE_SUCCESS constant in the scoop middle-tier repository
    public static final String DATABASE_RESPONSE_SUCCESS = "Success";
}