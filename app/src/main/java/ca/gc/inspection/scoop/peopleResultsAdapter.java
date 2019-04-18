package ca.gc.inspection.scoop;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class peopleResultsAdapter extends RecyclerView.Adapter<peopleResultsAdapter.peopleResultsViewHolder> {
    List<String> name;

    public peopleResultsAdapter(List<String> name) {
        this.name = name;

    }

    @NonNull
    @Override
    public peopleResultsViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_people_search_results, viewGroup, false);
        peopleResultsViewHolder vh = new peopleResultsViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull peopleResultsViewHolder peopleResultsViewHolder, int i) {
        peopleResultsViewHolder.peopleSearchResultsName.setText(name.get(i));

    }

    @Override
    public int getItemCount() {
        return name.size();
    }

    public class peopleResultsViewHolder extends RecyclerView.ViewHolder {
        TextView peopleSearchResultsName;
        ImageView peopleSearchResultsProfileImage;

        public peopleResultsViewHolder(@NonNull View itemView) {
            super(itemView);
            peopleSearchResultsName = itemView.findViewById(R.id.searchResultsName);
            peopleSearchResultsProfileImage = itemView.findViewById(R.id.searchResultsProfileImage);
        }
    }
}
