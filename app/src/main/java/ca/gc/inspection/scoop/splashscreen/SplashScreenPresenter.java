package ca.gc.inspection.scoop.splashscreen;

import ca.gc.inspection.scoop.util.NetworkUtils;

/**
 * This class is the presenter for the splash screen activity
 */
public class SplashScreenPresenter implements SplashScreenContract.Presenter {

    private SplashScreenContract.View mSplashScreenView;     // this stores a reference to the splash screen view (SplashScreenActivity)
    private SplashScreenInteractor mSplashScreenInteractor;

    public SplashScreenPresenter(SplashScreenContract.View view){
        mSplashScreenView = view;
        mSplashScreenView.setPresenter(this);

        mSplashScreenInteractor = new SplashScreenInteractor(this);
    }

//    /**
//     * invoked if login successful => go to the home screen
//     * @param context   context of the splash screen view (SplashScreenActivity)
//     * @param activity  splash screen view activity (SplashScreenActivity)
//     */
//    public void goToMainScreen(Context context, Activity activity){
//        SharedPreferences sharedPreferences = context.getSharedPreferences("ca.gc.inspection.scoop", Context.MODE_PRIVATE);
//        if(!sharedPreferences.getString("userid", "nothing").equals("nothing")){
//            String userId = sharedPreferences.getString("userid", "nothing");
//            String token = sharedPreferences.getString("token", "nothing");
//            Intent intent = new Intent(context, MainActivity.class);
//            Config.currentUser = userId;
//            Config.token = token;
//            context.startActivity(intent);
//            activity.finish();
//        }
//    }

    /**
     * Invoked by the view when the user clicks the login button
     * @param email     user's email
     * @param password  users's password
     */
    public void loginUser(NetworkUtils network, final String email, final String password){
        mSplashScreenInteractor.loginUser(network, email, password);
    }


    public boolean validLogin(String response){
        if (response.contains("Incorrect Password")) { // if the user entered an incorrect password
            mSplashScreenView.setPasswordLayoutError("Incorrect Password");
            return false;
        } else if (response.contains("Invalid Email")) { // if the user entered an invalid email
            mSplashScreenView.setEmailLayoutError("Invalid Email");
            return false;
        }
        return true;
    }

    public void storePreferences (String userid, String response){
        mSplashScreenView.storePreferences(userid, response);
    }

    /**
     * Call back function invoked by the interactor after the login details have been verified by the server
     * @param response  contains either an error message if login failed, or, user id if login successful
     * @param context   context of the splash screen view (SplashScreenActivity)
     * @param activity  splash screen view activity (SplashScreenActivity)
     */
//    public void loginUserCallBack(String response, Context context, Activity activity){
//        // checking to see if the server responded with error messages
//        if (response.contains("Incorrect Password")) { // if the user entered an incorrect password
//            //SplashScreenActivity.passwordLayout.setError("Incorrect Password");
//            splashScreenContract.setPasswordLayoutError("Incorrect Password");
//        } else if (response.contains("Invalid Email")) { // if the user entered an invalid email
//            //SplashScreenActivity.emailLayout.setError("Invalid Email");
//            splashScreenContract.setEmailLayoutError("Invalid Email");
//        } else {
//
//            // to grab the user id from the jwt token
//            Log.i("Response", response);
//            JWT parsedJWT = new JWT(response); // convert the response string into a JWT token
//            Claim userIdMetaData = parsedJWT.getClaim("userid"); // to get the user id claim from the token
//            String userid = userIdMetaData.asString(); // converting the claim into a string
//
//            Log.i("TOKEN", String.valueOf(parsedJWT)); // token
//            Log.i("USER ID", userid); // user id
//
//            // storing the token into shared preferences
//            SharedPreferences sharedPreferences = context.getSharedPreferences("ca.gc.inspection.scoop", Context.MODE_PRIVATE);
//            sharedPreferences.edit().putString("token", response).apply();
//            Config.token = response;
//
//            // storing the user id into shared preferences
//            sharedPreferences.edit().putString("userid", userid).apply();
//            Config.currentUser = userid;
//
//            // changes activities once login is successful
//            Intent intent = new Intent(context, MainActivity.class);
//            context.startActivity(intent);
//            activity.finish();
//        }
//    }

}
