package ca.gc.inspection.scoop;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class feedAdapter extends RecyclerView.Adapter<feedAdapter.myViewHolder> {
    List<String> testName;

    public feedAdapter(List<String> name) {
        this.testName = name;
    }


    @NonNull
    @Override
    public feedAdapter.myViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.custom_row_feed, viewGroup, false);
        myViewHolder vh = new myViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull feedAdapter.myViewHolder myViewHolder, int i) {
        myViewHolder.textName.setText(testName.get(i));

        // to get the options menu to appear
        myViewHolder.optionsMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetDialog bottomSheetDialog = new bottomSheetDialog();
                final Context context = v.getContext();
                FragmentManager fragmentManager = ((AppCompatActivity)context).getSupportFragmentManager();
                bottomSheetDialog.show(fragmentManager, "bottomSheet");
            }
        });
    }

    @Override
    public int getItemCount() {
        return testName.size();
    }

    public class myViewHolder extends RecyclerView.ViewHolder{
        TextView textName;
        ImageView optionsMenu;

        public myViewHolder(View itemView) {
            super(itemView);
            textName = itemView.findViewById(R.id.name);
            optionsMenu = itemView.findViewById(R.id.options_menu);
        }
    }
}
