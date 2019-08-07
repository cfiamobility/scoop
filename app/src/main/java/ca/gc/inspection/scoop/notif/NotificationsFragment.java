package ca.gc.inspection.scoop.notif;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import ca.gc.inspection.scoop.R;
import ca.gc.inspection.scoop.util.NetworkUtils;

import static ca.gc.inspection.scoop.Config.SWIPE_REFRESH_COLOUR_1;
import static ca.gc.inspection.scoop.Config.SWIPE_REFRESH_COLOUR_2;
import static ca.gc.inspection.scoop.Config.SWIPE_REFRESH_COLOUR_3;
import static com.google.gson.internal.$Gson$Preconditions.checkNotNull;

/**
 * View for the notifications controller
 */
public class NotificationsFragment extends Fragment implements
        NotificationsContract.View,
        SwipeRefreshLayout.OnRefreshListener {

    //recycler view widgets
    private RecyclerView todayRecyclerView, recentRecyclerView;
    private NotificationsAdapter todayAdapter, recentAdapter;
    private RecyclerView.LayoutManager todayLayoutManager, recentLayoutManager;

    //private NotificationsPresenter notificationsScreenController;
    private NotificationsContract.Presenter mPresenter;
    private View view;
    private SwipeRefreshLayout mSwipeRefreshLayout;

    private static final String TAG = "Notifications Fragment";

    private TextView today, recent, noNotifications;
    private ImageView noNotificationsImage;


    @Override
    public void setPresenter(NotificationsContract.Presenter presenter) {
        mPresenter = checkNotNull(presenter);
    }

    public NotificationsFragment(){
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_notifications, container, false);
        setPresenter(new NotificationsPresenter(this, NetworkUtils.getInstance(getContext())));
        setSwipeRefreshLayout(view);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);
        setRecentRecyclerView();
//        setTodayRecyclerView();
//        today = view.findViewById(R.id.fragment_notifications_txt_today); //instantiating the today textview
        recent = view.findViewById(R.id.fragment_notifications_txt_recent); //instantiating the recent textview
//
        noNotifications = view.findViewById(R.id.fragment_notifications_no_new_text);
        noNotificationsImage = view.findViewById(R.id.fragment_notifications_no_new_image);
    }
    //    /**
//     * Description: sets the recycler view for recent notifications
//     * @param currentTime: the current time of when notifications were loaded
//     * @param requestQueue: request queue to add requests to
//     * @param notificationResponse: all the notifications received
//     * @param imageResponse: all the images received
//     */
    public void setRecentRecyclerView() {
        // Initializing the recycler view
        recentRecyclerView = view.findViewById(R.id.fragment_notifications_rv_recent); //instantiating the recyclerview
        recentRecyclerView.setHasFixedSize(true);

        // Setting the layout manager for the recycler view
        recentLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false); //instantiates how the layout should look like for recyclerview
        recentRecyclerView.setLayoutManager(recentLayoutManager); //sets the layout manager to one chosen

        // Setting the custom adapter for the recycler view
        recentAdapter = new NotificationsAdapter(this,
                (NotificationsContract.Presenter.AdapterAPI) mPresenter); //instantiates the adapter
        recentRecyclerView.setAdapter(recentAdapter); //sets the adapter
        Log.i("NOTIFICATIONS_FRAGMENT", recentAdapter.toString());
        //notificationsScreenController.listenRecentRecyclerView(recentRecyclerView);
//        mPresenter.listenRecentRecyclerView(recentRecyclerView, notificationResponse);
    }

    private void setSwipeRefreshLayout(View view) {
        mSwipeRefreshLayout = view.findViewById(R.id.fragment_notifications_swipe);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary,
                SWIPE_REFRESH_COLOUR_1,
                SWIPE_REFRESH_COLOUR_2,
                SWIPE_REFRESH_COLOUR_3);
    }

    /**
     * To implement SwipeRefreshLayout.OnRefreshListener
     */
    @Override
    public void onRefresh() {
        loadDataFromDatabase();
    }

    @Override
    public void onLoadedDataFromDatabase() {
//        waitingCallbacksCount -= 1;
//        if (waitingCallbacksCount == 0)
            mSwipeRefreshLayout.setRefreshing(false);

//        if (waitingCallbacksCount < 0)
//            Log.d(TAG, "waitingCallBacksCount < 0");
    }

    private void loadDataFromDatabase() {
        mSwipeRefreshLayout.setRefreshing(true);
        mPresenter.loadDataFromDatabase();
    }

    @Override
    public void onResume() {
        Log.d("NOTIFICATIONS_FRAGMENT", "on resume");
        super.onResume();
        if (!mSwipeRefreshLayout.isRefreshing())
            loadDataFromDatabase();
    }



//    /**
//     * Description: sets the recycler view for today notifications
//     * @param currentTime: the current time of when the notifications were loaded
//     * @param requestQueue: request queue to add requests to
//     * @param notificationResponse: all the notifications received
//     * @param imageResponse: all the images received
//     */
//    public void setTodayRecyclerView() {
//        // Initializing the recycler view
//        todayRecyclerView = view.findViewById(R.id.fragment_notifications_rv_today); //instantiating the recyclerview
//        todayRecyclerView.setHasFixedSize(false); //
//
//        // Setting the layout manager for the recycler view
//        todayLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false); //instantiates how the layout should look like for recyclerview
//        todayRecyclerView.setLayoutManager(todayLayoutManager); //sets the layout manager to one chosen
//
//        // Setting the custom adapter for the recycler view
//        todayAdapter = new NotificationsAdapter(this, (NotificationsContract.Presenter.AdapterAPI) mPresenter); //instantiates the adapter
//        todayRecyclerView.setAdapter(todayAdapter); //sets the adapter
//        //notificationsScreenController.listenTodayRecyclerView(todayRecyclerView, notificationResponse);\
////        mPresenter.listenTodayRecyclerView(todayRecyclerView, notificationResponse);
//    }


    /**
     * Description: shows no new notification text and icon
     */
    public void showNoNotifications(){
        noNotifications.setVisibility(View.VISIBLE);
        noNotificationsImage.setVisibility(View.VISIBLE);
    }

    public void hideNoNotifications(){
        noNotifications.setVisibility(View.GONE);
        noNotificationsImage.setVisibility(View.GONE);
    }

    /**
     * Description: shows the today portion of the fragment
     */
    public void showTodaySection() {
        today.setVisibility(View.VISIBLE);
        todayRecyclerView.setVisibility(View.VISIBLE);
    }

    /**
     * Description: hides the today portion of the fragment
     */
    public void hideTodaySection() {
        today.setVisibility(View.GONE);
        todayRecyclerView.setVisibility(View.GONE);
    }

    /**
     * Description: shows the recent portion of the fragment
     */
    public void showRecentSection() {
        recent.setVisibility(View.VISIBLE);
        recentRecyclerView.setVisibility(View.VISIBLE);
    }

    /**
     * Description: hides the recent portion of the fragment
     */
    public void hideRecentSection() {
        recent.setVisibility(View.GONE);
        recentRecyclerView.setVisibility(View.GONE);
    }

    /**
     * Description: hides the loading panel
     */
//    public void hideLoadingPanel(){
//        view.findViewById(R.id.fragment_notifications_rl_loading_panel).setVisibility(View.GONE); //sets the loading screen to gone
//    }

    /**
     * Description: requests focus to the top of the page rather than the today recycler view
     */
    public void requestTodayFocus(){
//        view.findViewById(R.id.fragment_notifications_rv_today).setFocusable(false);
//        view.findViewById(R.id.view1).requestFocus();
    }

    /**
     * Description: requests focus to the top of the recent view rather than the recent recycler view
     */
    public void requestRecentFocus(){
        view.findViewById(R.id.fragment_notifications_rv_recent).setFocusable(false);
//        view.findViewById(R.id.view3).requestFocus();
    }


}
