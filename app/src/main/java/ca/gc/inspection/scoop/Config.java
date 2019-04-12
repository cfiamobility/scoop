package ca.gc.inspection.scoop;

import android.content.SharedPreferences;

class Config {
    static String baseIP = "http://10.0.2.2:3000/";

    //gets changed in splash screen
    static String currentUser = "";

    //For Post/Comment activity
    static int postType = 1;
    static int commentType = 2;

    //For likes activity
    static int upvoteType = 1;
    static int downvoteType = 2;
}
