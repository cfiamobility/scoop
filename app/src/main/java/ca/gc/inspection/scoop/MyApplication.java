package ca.gc.inspection.scoop;

import android.app.Application;

/**
 * Consider changing this into a static singleton instead of subclassing Application
 */
public class MyApplication extends Application {
    private static MyApplication mContext;

    @Override
    public void onCreate(){
        super.onCreate();
        mContext = this;
    }

    public static MyApplication getContext(){
        return mContext;
    }
}
