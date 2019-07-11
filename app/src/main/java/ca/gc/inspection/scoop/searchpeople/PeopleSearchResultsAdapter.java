package ca.gc.inspection.scoop.searchpeople;

import ca.gc.inspection.scoop.R;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class PeopleSearchResultsAdapter extends RecyclerView.Adapter<PeopleSearchResultsAdapter.peopleResultsViewHolder> {
    List<String> name;

    public PeopleSearchResultsAdapter(List<String> name) {
        this.name = name;

    }

    @NonNull
    @Override
    public peopleResultsViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_search_people, viewGroup, false);
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
            peopleSearchResultsName = itemView.findViewById(R.id.item_search_people_txt_name);
            peopleSearchResultsProfileImage = itemView.findViewById(R.id.item_search_people_img_profile);
        }
    }
}
