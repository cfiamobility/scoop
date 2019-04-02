package ca.gc.inspection.scoop;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class commentsAdapter extends RecyclerView.Adapter<commentsAdapter.likesViewHolder> {

    // test array list
    List<String> testLikes;

    public commentsAdapter(List<String> test) {
        this.testLikes = test;
    }

    @NonNull
    @Override
    public likesViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.custom_row_comments, viewGroup, false);
        likesViewHolder vh = new likesViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull likesViewHolder likesViewHolder, int i) {
        likesViewHolder.nameText.setText(testLikes.get(i));
    }

    @Override
    public int getItemCount() {
        return testLikes.size();
    }

    public class likesViewHolder extends RecyclerView.ViewHolder {
        TextView nameText;

        public likesViewHolder(@NonNull View itemView) {
            super(itemView);
            nameText = itemView.findViewById(R.id.name);
        }
    }
}
