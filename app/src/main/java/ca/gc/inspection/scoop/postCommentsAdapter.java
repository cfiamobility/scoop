package ca.gc.inspection.scoop;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class postCommentsAdapter extends ArrayAdapter<Comments> {

    private Context context;
    private List<Comments> comments;

    public postCommentsAdapter(@NonNull  Context context, @LayoutRes int resource, @NonNull List<Comments> objects) {
        super(context, resource, objects);
        this.comments = objects;
        this.context = context;
    }

    @Override
    public View getView (int position, View convertView, ViewGroup parent) {
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = layoutInflater.inflate(R.layout.item_display_post_comments, parent, false);

        TextView commentName = rowView.findViewById(R.id.commentName);
        TextView commentText = rowView.findViewById(R.id.commentText);
        TextView timeStamp = rowView.findViewById(R.id.timeStamp);

        commentName.setText(comments.get(position).getCommenterName());
        commentText.setText(comments.get(position).getCommentText());
        timeStamp.setText(comments.get(position).getTimeStamp());

        return rowView;
    }
}
