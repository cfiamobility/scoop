package ca.gc.inspection.scoop.notif;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ca.gc.inspection.scoop.R;

public class NotificationsAdapter extends RecyclerView.Adapter<NotificationsViewHolder>
        implements NotificationsContract.View.Adapter{

    private NotificationsContract.Presenter.AdapterAPI mPresenter;
    private NotificationsContract.View mView;

    public NotificationsAdapter(NotificationsContract.View view,
                                NotificationsContract.Presenter.AdapterAPI presenter){
        Log.i("NOTIFICATIONS_ADAPTER", "Constructing Adapter");
        mView = view;
        mPresenter = presenter;
        mPresenter.setAdapter(this);
    }

    @NonNull
    @Override
    public NotificationsViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        Log.i("NOTIFICATIONS_ADAPTER", "onCreateViewHolder in Adapter");
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_notifications, viewGroup, false);

        return new NotificationsViewHolder(v,
                (NotificationsContract.Presenter.ViewHolderAPI) mPresenter);
    }

    @Override
    public void onBindViewHolder(@NonNull NotificationsViewHolder holder, int i) {
        Log.i("NOTIFICATIONS_ADAPTER", "Binding data to VIEWHOLDER " + i);
        mPresenter.onBindViewHolderAtPosition(holder, i);
//        Notifications.setUserInfoListener();
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
