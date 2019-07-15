package ca.gc.inspection.scoop.searchbuilding;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Filter;
import android.widget.Filterable;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import ca.gc.inspection.scoop.R;

/**
 * Adapter for the recycler view in the Search Building activity
 */
public class BuildingAdapter extends RecyclerView.Adapter<BuildingViewHolder> implements Filterable {

    private static List<String> mData;          // contains list of building addresses
    private LayoutInflater mInflator;
    private ItemClickListener mClickListener;   // click listener for each row in the recycler view

    private MyFilter filter;    // Filter used when the user searches the recycler view for a building address

    BuildingAdapter(Context context, List<String> data){
        this.mInflator = LayoutInflater.from(context);
        this.mData = data;
    }

    @NonNull
    @Override
    public BuildingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View view = mInflator.inflate(R.layout.search_building_result_row, parent, false);
        return new BuildingViewHolder(view, mClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull BuildingViewHolder buildingViewHolder, int i) {
        String building = mData.get(i);
        buildingViewHolder.BuildingTextView.setText(building);

    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    String getItem(int id){
        return mData.get(id);
    }

    void setClickListener(ItemClickListener itemClickListener){
        this.mClickListener = itemClickListener;
    }

    /**
     * gets filter in way that ensure there's only ever one instance of it (see singleton)
     * @return the filter
     */
    @Override
    public Filter getFilter() {
        if (filter == null){
            filter = new MyFilter(this, mData);
        }
        return filter;
    }

    public interface ItemClickListener{
        void onItemClick(View view, int position);
    }

    /**
     * This class is used to filter the recycler view containing the building addresses when the user performs a search
     */
    private static class MyFilter extends Filter {

        private final BuildingAdapter adapter;
        private final List<String> originalList;
        private final List<String> filteredList;

        private MyFilter(BuildingAdapter adapter, List<String> originalList) {
            super();
            this.adapter = adapter;
            this.originalList = new LinkedList<>(originalList);
            this.filteredList = new ArrayList<>();
        }

        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            filteredList.clear();
            final FilterResults results = new FilterResults();

            if (charSequence.length() == 0) {
                filteredList.addAll(originalList);
            } else {
                final String filterPattern = charSequence.toString().toLowerCase().trim();
                for (String item : originalList) {
                    if (item.toLowerCase().contains(filterPattern)) {
                        filteredList.add(item);
                    }
                }
            }

            results.values = filteredList;
            results.count = filteredList.size();
            return results;
        }

        @SuppressWarnings("unchecked")
        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            mData.clear();
            mData.addAll((ArrayList) filterResults.values);
            adapter.notifyDataSetChanged();
        }
    }




}
