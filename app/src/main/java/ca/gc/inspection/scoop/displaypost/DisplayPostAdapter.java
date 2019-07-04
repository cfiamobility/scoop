package ca.gc.inspection.scoop.displaypost;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ca.gc.inspection.scoop.feedpost.FeedPostAdapter;
import ca.gc.inspection.scoop.feedpost.FeedPostViewHolder;

public class DisplayPostAdapter extends RecyclerView.Adapter<FeedPostViewHolder>
    implements DisplayPostContract.View.Fragment.Adapter {

    private DisplayPostContract.Presenter.FragmentAPI.AdapterAPI mDisplayPostPresenter;
    private DisplayPostFragment mDisplayPostView;

    public DisplayPostAdapter(DisplayPostFragment displayPostFragment) {
        mDisplayPostView = displayPostFragment;
    }

    @NonNull
    @Override
    public FeedPostViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull FeedPostViewHolder feedPostViewHolder, int i) {

    }

    @Override
    public int getItemCount() {
        return mDisplayPostPresenter.getItemCount();
    }

    @Override
    public void refreshAdapter() {
        notifyDataSetChanged();
    }

//    @Override
//    public View getView (int position, View convertView, ViewGroup parent) {
//        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//        View rowView = layoutInflater.inflate(R.layout.item_post_comments, parent, false);
//
//        TextView commentName = rowView.findViewById(R.id.commentName);
//        TextView commentText = rowView.findViewById(R.id.commentText);
//        TextView timeStamp = rowView.findViewById(R.id.activity_display_post_txt_time);
//
//        commentName.setText(comments.get(position).getCommenterName());
//        commentText.setText(comments.get(position).getCommentText());
//        timeStamp.setText(comments.get(position).getTimeStamp());
//
//        return rowView;
//    }
}
