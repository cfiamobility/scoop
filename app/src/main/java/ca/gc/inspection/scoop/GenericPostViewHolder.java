package ca.gc.inspection.scoop;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class GenericPostViewHolder extends MostGenericViewHolder {

    TextView commentCount;
    ImageView optionsMenu;


    public GenericPostViewHolder(View v) {
        super(v);
        commentCount = v.findViewById(R.id.comment_count);
        optionsMenu = v.findViewById(R.id.options_menu);
    }
}
