package ca.gc.inspection.scoop;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class NetworkUtils {
    /***
     * Singleton object which provides a RequestQueue which is scoped to the application to reduce
     * redundant RequestQueue objects.
     *
     * Instructions to add a request to the RequestQueue:
     * 1. Use the View to call the Presenter
     *      1.1 Define the interaction in the respective Contract interface
     *      1.2 Pass in an instance of NetworkUtils as a parameter (i.e. NetworkUtils.getInstance(getApplicationContext()) )
     *      1.2 Pass in the necessary data as Java objects as parameters (no Android objects)
     * 2. Use the Presenter to call the Interactor:
     *      2.1 Perform any necessary business logic on Java objects
     *      2.2 Pass in the NetworkUtils instance and necessary data as parameters
     * 3. Use the Interactor to send the Volley request
     *      3.1 Construct the request object using the passed in parameters from Presenter
     *      3.2 Send the request by calling addToRequestQueue(newRequest) on the networkUtils instance
     *          which was passed in
     * 4. Receiving the network response
     *      4.1 Call the Presenter from the onResponse method of the request object if necessary
     *      4.2 From the Presenter, call the View to update the UI if necessary
     *
     * Data flow between the classes
     * View <--(Defined in Contract)--> Presenter <--> Interactor
     */
    private static NetworkUtils mInstance;
    private RequestQueue mRequestQueue;
    private static Context mContext;

    private NetworkUtils(Context context) {
        mContext = context;
        mRequestQueue = getRequestQueue();
    }

    public static synchronized NetworkUtils getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new NetworkUtils(context);
        }
        return mInstance;
    }

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            // Use getApplicationContext() to prevent leaking the Activity
            mRequestQueue = Volley.newRequestQueue(mContext.getApplicationContext());
        }
        return mRequestQueue;
    }

    public <T> void addToRequestQueue(Request<T> req) {
        getRequestQueue().add(req);
    }
}
