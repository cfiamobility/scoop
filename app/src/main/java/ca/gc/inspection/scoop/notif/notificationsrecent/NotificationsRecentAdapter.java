package ca.gc.inspection.scoop.notif.notificationsrecent;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ca.gc.inspection.scoop.R;

public class NotificationsRecentAdapter extends RecyclerView.Adapter<NotificationsRecentViewHolder>
        implements NotificationsRecentContract.View.Adapter{

    private NotificationsRecentContract.Presenter.AdapterAPI mPresenter;
    private NotificationsRecentFragment mView;

    public NotificationsRecentAdapter(NotificationsRecentFragment view,
                                     NotificationsRecentContract.Presenter.AdapterAPI presenter){
        Log.i("NOTIFICATIONS_ADAPTER", "Constructing Adapter");
        mView = view;
        mPresenter = presenter;
        mPresenter.setAdapter(this);
    }

    @NonNull
    @Override
    public NotificationsRecentViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        Log.i("NOTIFICATIONS_ADAPTER", "onCreateViewHolder in Adapter");
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_notifications, viewGroup, false);

        return new NotificationsRecentViewHolder(v,
                (NotificationsRecentContract.Presenter.ViewHolderAPI) mPresenter);
    }

    @Override
    public void onBindViewHolder(@NonNull NotificationsRecentViewHolder holder, int i) {
        Log.i("NOTIFICATIONS_ADAPTER", "Binding data to VIEWHOLDER " + i);
        mPresenter.onBindViewHolderAtPosition(holder, i);
//        NotificationsRecent.setUserInfoListener();
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
