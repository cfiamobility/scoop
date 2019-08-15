package ca.gc.inspection.scoop.notif.notificationsrecent;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
 * NotificationsRecentFragment which acts as the main view for the Notifications Recent Tab Fragment
 * Responsible for creating the Presenter and Adapter and communicating whether the fragment needs to be refreshed
 * Note: This does not have a superclass like the the Presenter - does not extend NotificationsTodayFragment
 *       Instead, important methods are made static in the parent class; see NotificationsTodayFragment for details
 */
public class NotificationsRecentFragment extends Fragment implements
        NotificationsRecentContract.View,
        SwipeRefreshLayout.OnRefreshListener {

    //recycler view widgets
    private RecyclerView recentRecyclerView;
    private NotificationsRecentAdapter recentAdapter;
    private RecyclerView.LayoutManager recentLayoutManager;

    private NotificationsRecentContract.Presenter mPresenter;
    private View view;
    private SwipeRefreshLayout mSwipeRefreshLayout;

    private TextView noNotificationsTitle, noNotificationsText;
    private ImageView noNotificationsImage;

    /**
     * Invoked by the Presenter and stores a reference to itself (Presenter) after being constructed by the View
     * @param presenter Presenter to be associated with the View and referenced later
     */
    @Override
    public void setPresenter(NotificationsRecentContract.Presenter presenter) {
        mPresenter = checkNotNull(presenter);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_notifications_recent, container, false);
        setPresenter(new NotificationsRecentPresenter(this, NetworkUtils.getInstance(getContext())));
        setSwipeRefreshLayout(view);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);
        setRecyclerView();

        noNotificationsTitle = view.findViewById(R.id.fragment_notifications_no_new_title);
        noNotificationsText = view.findViewById(R.id.fragment_notifications_no_new_text);
        noNotificationsImage = view.findViewById(R.id.fragment_notifications_no_new_image);
    }

    public void setRecyclerView() {
        // Initializing the recycler view
        recentRecyclerView = view.findViewById(R.id.fragment_notifications_recent_rv); //instantiating the recyclerview
        recentRecyclerView.setHasFixedSize(true);

        // Setting the layout manager for the recycler view
        recentLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false); //instantiates how the layout should look like for recyclerview
        recentRecyclerView.setLayoutManager(recentLayoutManager); //sets the layout manager to one chosen

        // Setting the custom adapter for the recycler view
        recentAdapter = new NotificationsRecentAdapter(this,
                (NotificationsRecentContract.Presenter.AdapterAPI) mPresenter); //instantiates the adapter
        recentRecyclerView.setAdapter(recentAdapter); //sets the adapter
        //notificationsScreenController.listenRecentRecyclerView(recentRecyclerView);
//        mPresenter.listenRecentRecyclerView(recentRecyclerView, notificationResponse);
    }

    /**
     * Sets the swipe refresh layout and it's color schemes
     * @param view NotificationsToday View that holds the swipe layout
     */
    private void setSwipeRefreshLayout(View view) {
        mSwipeRefreshLayout = view.findViewById(R.id.fragment_notifications_recent_swipe);
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

    /**
     * Once data has been loaded, this method is invoked to set refreshing status to false
     */
    @Override
    public void onLoadedDataFromDatabase() {
        mSwipeRefreshLayout.setRefreshing(false);
    }

    /**
     * Sets the refreshing status to true and loads the data from the database through the Presenter and Interactor
     */
    private void loadDataFromDatabase() {
        mSwipeRefreshLayout.setRefreshing(true);
        mPresenter.loadDataFromDatabase();
    }

    /**
     * Overrides Android Callback onResume to load data from the database when a user goes to the fragment
     */
    @Override
    public void onResume() {
        super.onResume();
        if (!mSwipeRefreshLayout.isRefreshing())
            loadDataFromDatabase();
    }

    /**
     * Sets the Views for when there are no notifications to VISIBLE
     */
    public void showNoNotifications(){
        noNotificationsTitle.setVisibility(View.VISIBLE);
        noNotificationsText.setVisibility(View.VISIBLE);
        noNotificationsImage.setVisibility(View.VISIBLE);
    }

    /**
     * Sets the Views for when there are no notifications to GONE
     */
    public void hideNoNotifications(){
        noNotificationsTitle.setVisibility(View.GONE);
        noNotificationsText.setVisibility(View.GONE);
        noNotificationsImage.setVisibility(View.GONE);
    }

}
