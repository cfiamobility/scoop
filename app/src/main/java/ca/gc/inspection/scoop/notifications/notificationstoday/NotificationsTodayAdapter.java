package ca.gc.inspection.scoop.notifications.notificationstoday;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ca.gc.inspection.scoop.R;

/**
 * NotificationsTodayAdapter is used to create ViewHolders and bind new data to them for a RecyclerView.
 * Considered to be part of the NotificationsToday View.
 */
public class NotificationsTodayAdapter extends RecyclerView.Adapter<NotificationsTodayViewHolder>
        implements NotificationsTodayContract.View.Adapter{

    private NotificationsTodayContract.Presenter.AdapterAPI mPresenter;
    private NotificationsTodayContract.View mView;

    /**
     * Constructor for Adapter
     * @param view The fragment instead of view contract can be taken in as both are considered part of the view. (see Contract documentation)
     * @param presenter The presenter is passed in as the contract which specifies View-Presenter interaction.
     */
    NotificationsTodayAdapter(NotificationsTodayFragment view,
                                     NotificationsTodayContract.Presenter.AdapterAPI presenter){
        mView = view;
        mPresenter = presenter;
        mPresenter.setAdapter(this);
    }


    /**
     * Creates the ViewHolder object
     * @param viewGroup The RecyclerView will be automatically passed in by Android - used for layout parameters
     * @param i item iterator for each row of the recycler view
     * @returns a new view holder
     */
    @NonNull
    @Override
    public NotificationsTodayViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_notifications, viewGroup, false);

        return new NotificationsTodayViewHolder(v,
                (NotificationsTodayContract.Presenter.ViewHolderAPI) mPresenter);
    }

    /**
     * Binds new data to the ViewHolder as the user scrolls through the RecyclerView.
     * Calls the Presenter (interface) to retrieve the data and update the ViewHolder through the View.ViewHolder contract.
     * Calls the View object to set the listeners for the ViewHolders - contract not necessary as this is intra-View communication.
     * @param holder a NotificationsTodayViewHolder in the recycler view
     * @param i the position of the ViewHolder in the recycler view
     */
    @Override
    public void onBindViewHolder(@NonNull NotificationsTodayViewHolder holder, int i) {
        mPresenter.onBindViewHolderAtPosition(holder, i);
        NotificationsTodayFragment.setDisplayPostListener(holder, mPresenter.getActivityIdByIndex(i), mPresenter.getReferenceIdByIndex(i));
        NotificationsTodayFragment.setUserInfoListener(holder, mPresenter.getNotifierIdByIndex(i));
    }

    /**
     * Necessary Android method for RecyclerView.Adapter
     * @return result from Presenter
     */
    @Override
    public int getItemCount() {
        return mPresenter.getItemCount();
    }

    /**
     * Called by presenter when it's data is updated. This lets the adapter know when
     * binding new data to the view (without being triggered by scrolling) is necessary.
     */
    @Override
    public void refreshAdapter() {
        notifyDataSetChanged();
    }
}
