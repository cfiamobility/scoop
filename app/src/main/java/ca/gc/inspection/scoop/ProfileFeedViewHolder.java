package ca.gc.inspection.scoop;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class ProfileFeedViewHolder extends MostGenericViewHolder {

    TextView commentCount;
    ImageView optionsMenu;


    public ProfileFeedViewHolder(View v) {
        super(v);
        commentCount = v.findViewById(R.id.comment_count);
        optionsMenu = v.findViewById(R.id.options_menu);
    }
}
