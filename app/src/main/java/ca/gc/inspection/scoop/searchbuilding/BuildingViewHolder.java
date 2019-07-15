package ca.gc.inspection.scoop.searchbuilding;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;

import ca.gc.inspection.scoop.R;

/**
 * View Holder for the recycler view in the Search Building activity
 */
public class BuildingViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    TextView BuildingTextView;
    BuildingAdapter.ItemClickListener mClickListener;



    public BuildingViewHolder(@NonNull View itemView, BuildingAdapter.ItemClickListener itemClickListener) {
        super(itemView);
        BuildingTextView = itemView.findViewById(R.id.BuildingNameTextView);
        mClickListener = itemClickListener;
        itemView.setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {
        if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
    }
}
