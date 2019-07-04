package ca.gc.inspection.scoop;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

// TODO delete when adapters for SavedPostActivity, TopSearchResultsFragment and ProfileLikesFragment have been implemented
public class ProfileAdapter extends RecyclerView.Adapter<ProfileViewHolder> {
    @NonNull
    @Override
    public ProfileViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull ProfileViewHolder profileViewHolder, int i) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }
}