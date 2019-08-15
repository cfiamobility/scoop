package ca.gc.inspection.scoop.notifications.notificationsofficial;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import ca.gc.inspection.scoop.R;

public class NotificationsOfficialFragment extends Fragment
//        implements NotificationsOfficialContract.View,
//        SwipeRefreshLayout.OnRefreshListener
{

//    //recycler view widgets
//    private RecyclerView officialRecyclerView;
//    private NotificationsOfficialAdapter officialAdapter;
//    private RecyclerView.LayoutManager officialLayoutManager;
//
//    //private NotificationsOfficialPresenter notificationsScreenController;
//    private NotificationsOfficialContract.Presenter mPresenter;
//    private View view;
//    private SwipeRefreshLayout mSwipeRefreshLayout;
//
//    private TextView noNotifications;
//    private ImageView noNotificationsImage;

//    @Override
//    public void setPresenter(NotificationsOfficialContract.Presenter presenter) {
//        mPresenter = checkNotNull(presenter);
//    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_notifications_official, container, false);
//        setPresenter(new NotificationsOfficialPresenter(this, NetworkUtils.getInstance(getContext())));
//        setSwipeRefreshLayout(view);
        return view;
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);
        setRecyclerView();
    }


    public void setRecyclerView() {
        // Initializing the recycler view
//        officialRecyclerView = view.findViewById(R.id.fragment_notifications_official_rv); //instantiating the recyclerview
//        officialRecyclerView.setHasFixedSize(true);
//
//        // Setting the layout manager for the recycler view
//        officialLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false); //instantiates how the layout should look like for recyclerview
//        officialRecyclerView.setLayoutManager(officialLayoutManager); //sets the layout manager to one chosen
//
//        // Setting the custom adapter for the recycler view
//        officialAdapter = new NotificationsOfficialAdapter(this,
//                (NotificationsOfficialContract.Presenter.AdapterAPI) mPresenter); //instantiates the adapter
//        officialRecyclerView.setAdapter(officialAdapter); //sets the adapter
//        //notificationsScreenController.listenOfficialRecyclerView(officialRecyclerView);
////        mPresenter.listenOfficialRecyclerView(officialRecyclerView, notificationResponse);
    }

//    private void setSwipeRefreshLayout(View view) {
//        mSwipeRefreshLayout = view.findViewById(R.id.fragment_notifications_official_swipe);
//        mSwipeRefreshLayout.setOnRefreshListener(this);
//        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary,
//                SWIPE_REFRESH_COLOUR_1,
//                SWIPE_REFRESH_COLOUR_2,
//                SWIPE_REFRESH_COLOUR_3);
//    }

//    /**
//     * To implement SwipeRefreshLayout.OnRefreshListener
//     */
//    @Override
//    public void onRefresh() {
//        loadDataFromDatabase();
//    }
//
//    @Override
//    public void onLoadedDataFromDatabase() {
//        mSwipeRefreshLayout.setRefreshing(false);
//    }
//
//    private void loadDataFromDatabase() {
//        mSwipeRefreshLayout.setRefreshing(true);
//        mPresenter.loadDataFromDatabase();
//    }
//
//    @Override
//    public void onResume() {
//        super.onResume();
//        if (!mSwipeRefreshLayout.isRefreshing())
//            loadDataFromDatabase();
//    }
//
//    /**
//     * Description: shows no new notification text and icon
//     */
//    public void showNoNotifications(){
//        noNotifications.setVisibility(View.VISIBLE);
//        noNotificationsImage.setVisibility(View.VISIBLE);
//    }
//
//    public void hideNoNotifications(){
//        noNotifications.setVisibility(View.GONE);
//        noNotificationsImage.setVisibility(View.GONE);
//    }


}
