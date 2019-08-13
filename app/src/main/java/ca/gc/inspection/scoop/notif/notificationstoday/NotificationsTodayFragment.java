package ca.gc.inspection.scoop.notif.notificationstoday;

import android.content.Context;
import android.content.Intent;
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
import ca.gc.inspection.scoop.displaypost.DisplayPostActivity;
import ca.gc.inspection.scoop.util.NetworkUtils;

import static ca.gc.inspection.scoop.Config.INTENT_ACTIVITY_ID_KEY;
import static ca.gc.inspection.scoop.Config.SWIPE_REFRESH_COLOUR_1;
import static ca.gc.inspection.scoop.Config.SWIPE_REFRESH_COLOUR_2;
import static ca.gc.inspection.scoop.Config.SWIPE_REFRESH_COLOUR_3;
import static ca.gc.inspection.scoop.postcomment.PostCommentFragment.startFragmentOrActivity;
import static com.google.gson.internal.$Gson$Preconditions.checkNotNull;

/**
 * NotificationsTodayFragment which acts as the main view for the viewing the Notifications Today Tab Fragment
 * Responsible for creating the Presenter and Adapter and communicating whether the fragment needs to be refreshed
 */
public class NotificationsTodayFragment extends Fragment implements
        NotificationsTodayContract.View,
        SwipeRefreshLayout.OnRefreshListener{

    //recycler view widgets
    private RecyclerView mRecyclerView;
    private NotificationsTodayAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    //private NotificationsTodayPresenter notificationsScreenController;
    private NotificationsTodayContract.Presenter mPresenter;
    private View view;
    private SwipeRefreshLayout mSwipeRefreshLayout;

    private TextView noNotificationsTitle, noNotificationsText;
    private ImageView noNotificationsImage;

    /**
     * Invoked by the Presenter and stores a reference to itself (Presenter) after being constructed by the View
     * @param presenter Presenter to be associated with the View and refereneced later
     */
    @Override
    public void setPresenter(NotificationsTodayContract.Presenter presenter) {
        mPresenter = checkNotNull(presenter);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_notifications_today, container, false);
        setPresenter(new NotificationsTodayPresenter(this, NetworkUtils.getInstance(getContext())));
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
        mRecyclerView = view.findViewById(R.id.fragment_notifications_today_rv); //instantiating the recyclerview
        mRecyclerView.setHasFixedSize(true); //

        // Setting the layout manager for the recycler view
        mLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false); //instantiates how the layout should look like for recyclerview
        mRecyclerView.setLayoutManager(mLayoutManager); //sets the layout manager to one chosen

        // Setting the custom adapter for the recycler view
        mAdapter = new NotificationsTodayAdapter(this,
                (NotificationsTodayContract.Presenter.AdapterAPI) mPresenter); //instantiates the adapter
        mRecyclerView.setAdapter(mAdapter); //sets the adapter
        //notificationsScreenController.listenTodayRecyclerView(mRecyclerView, notificationResponse);
//        mPresenter.listenTodayRecyclerView(mRecyclerView, notificationResponse);
    }

    /**
     * Sets the swipe refresh layout and it's color schemes
     * @param view NotificationsToday View that holds the swipe layout
     */
    protected void setSwipeRefreshLayout(View view) {
        mSwipeRefreshLayout = view.findViewById(R.id.fragment_notifications_today_swipe);
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

    /**
     * Creates and sets a listener for a notification view and launches the notifier's user profile
     * when the FullName View or ProfileImage View is click
     * @param viewHolder viewholder that displays the current notification
     * @param notifierId user id of the notifier
     */
    public static void setUserInfoListener(NotificationsTodayViewHolder viewHolder, String notifierId) {
        viewHolder.fullName.setOnClickListener(v -> {
            Context context = v.getContext();
            startFragmentOrActivity(context, notifierId);
        });

        viewHolder.profileImage.setOnClickListener(v -> {
            Context context = v.getContext();
            startFragmentOrActivity(context, notifierId);
        });
    }

    /**
     * Creates and sets a listener for a notification view and launches a DisplayPostActivity
     * when the ActivityType View is clicked
     * If the activityType of the notification is a comment, then it will store the referenceId as an extra in the intent,
     * Otherwise it will store the activityid as this means the activityType is a post
     * @param viewHolder viewholder that displays the current notification
     * @param activityId activityid of a post
     * @param referenceId referenceid of a comment
     */
    public static void setDisplayPostListener(NotificationsTodayViewHolder viewHolder, String activityId, String referenceId){
        viewHolder.activityType.setOnClickListener(v -> {
            Context context = v.getContext();
            if (context.getClass() == DisplayPostActivity.class)
                Log.d("Notifications Today", "Already displaying post!");
            else {
                Intent intent = new Intent(context, DisplayPostActivity.class);

                if (viewHolder.getActivityType().equals("comment")) {
                    intent.putExtra(INTENT_ACTIVITY_ID_KEY, referenceId);
                } else {
                    intent.putExtra(INTENT_ACTIVITY_ID_KEY, activityId);
                }
                context.startActivity(intent);
            }
        });
    }

}
