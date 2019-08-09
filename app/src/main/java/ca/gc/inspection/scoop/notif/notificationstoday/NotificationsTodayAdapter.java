package ca.gc.inspection.scoop.notif.notificationstoday;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ca.gc.inspection.scoop.R;

public class NotificationsTodayAdapter extends RecyclerView.Adapter<NotificationsTodayViewHolder>
        implements NotificationsTodayContract.View.Adapter{

    private NotificationsTodayContract.Presenter.AdapterAPI mPresenter;
    private NotificationsTodayContract.View mView;

    public NotificationsTodayAdapter(NotificationsTodayFragment view,
                                     NotificationsTodayContract.Presenter.AdapterAPI presenter){
        Log.i("NOTIFICATIONS_ADAPTER", "Constructing Adapter");
        mView = view;
        mPresenter = presenter;
        mPresenter.setAdapter(this);
    }

    @NonNull
    @Override
    public NotificationsTodayViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        Log.i("NOTIFICATIONS_ADAPTER", "onCreateViewHolder in Adapter");
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_notifications, viewGroup, false);

        return new NotificationsTodayViewHolder(v,
                (NotificationsTodayContract.Presenter.ViewHolderAPI) mPresenter);
    }

    @Override
    public void onBindViewHolder(@NonNull NotificationsTodayViewHolder holder, int i) {
        Log.i("NOTIFICATIONS_ADAPTER", "Binding data to VIEWHOLDER " + i);
        mPresenter.onBindViewHolderAtPosition(holder, i);
        NotificationsTodayFragment.setDisplayPostListener(holder, mPresenter.getActivityIdByIndex(i), mPresenter.getReferenceIdByIndex(i));
        NotificationsTodayFragment.setUserInfoListener(holder, mPresenter.getPosterIdByIndex(i));
    }

    @Override
    public int getItemCount() {
        return mPresenter.getItemCount();
    }

    @Override
    public void refreshAdapter() {
        notifyDataSetChanged();
    }
}
