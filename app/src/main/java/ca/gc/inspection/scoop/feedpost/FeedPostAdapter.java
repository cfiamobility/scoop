package ca.gc.inspection.scoop.feedpost;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.json.JSONArray;
import org.json.JSONException;

import ca.gc.inspection.scoop.R;
import ca.gc.inspection.scoop.profilepost.ProfilePostContract;
import ca.gc.inspection.scoop.profilepost.ProfilePostFragment;

public class FeedPostAdapter extends RecyclerView.Adapter<FeedPostViewHolder>  {

    private FeedPostContract.Presenter.AdapterAPI mFeedPostPresenter;
    private FeedPostContract.View mFeedPostView;    //TODO determine best practice for getting fragment views

    /**
     * Constructor for the adapter
     */
    public FeedPostAdapter(FeedPostContract.View profileCommentView, ProfilePostContract.Presenter.AdapterAPI presenter) {
        mFeedPostView = profileCommentView;
        mFeedPostPresenter = presenter;
    }

    /**
     * @param viewGroup
     * @param i
     * @return
     */
    @NonNull
    @Override
    public FeedPostViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_post, viewGroup, false);
        return new FeedPostViewHolder(view);
    }

    /**
     * Binds new data to the viewholder
     * @param holder
     * @param i
     */
    @Override
    public void onBindViewHolder(@NonNull FeedPostViewHolder feedPostViewHolder, int i) {
        mFeedPostPresenter.onBindViewHolderAtPosition(feedPostViewHolder, i);

        //TODO set listeners
    }

    @Override
    public int getItemCount() {
        return mFeedPostPresenter.getItemCount();
    }
}
