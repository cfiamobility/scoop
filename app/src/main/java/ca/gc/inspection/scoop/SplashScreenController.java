package ca.gc.inspection.scoop;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

public class SplashScreenController {

    public static void goToMainScreen(Context context, Activity activity){
        SharedPreferences sharedPreferences = context.getSharedPreferences("ca.gc.inspection.scoop", Context.MODE_PRIVATE);
        if(!sharedPreferences.getString("userid", "nothing").equals("nothing")){
            String userId = sharedPreferences.getString("userid", "nothing");
            String token = sharedPreferences.getString("token", "nothing");
            Intent intent = new Intent(context, mainScreen.class);
            Config.currentUser = userId;
            Config.token = token;
            context.startActivity(intent);
            activity.finish();
        }
    }
}
