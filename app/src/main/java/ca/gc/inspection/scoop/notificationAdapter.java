package ca.gc.inspection.scoop;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class notificationAdapter extends RecyclerView.Adapter<notificationAdapter.notificationViewHolder> {
    List<String> testNotification;

    public notificationAdapter(List<String> test) {
        this.testNotification = test;
    }

    @NonNull
    @Override
    public notificationViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.custom_row_notfications, viewGroup, false);
        notificationViewHolder vh = new notificationViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull notificationViewHolder notificationViewHolder, int i) {
        notificationViewHolder.notificationText.setText(testNotification.get(i));
    }

    @Override
    public int getItemCount() {
        return testNotification.size();
    }

    public class notificationViewHolder extends RecyclerView.ViewHolder {
        TextView notificationText;

        public notificationViewHolder(View itemView) {
            super(itemView);
            notificationText = itemView.findViewById(R.id.notification_text);
        }
    }
}
