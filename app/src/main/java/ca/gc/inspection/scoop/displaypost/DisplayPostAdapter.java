package ca.gc.inspection.scoop.displaypost;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ca.gc.inspection.scoop.R;
import ca.gc.inspection.scoop.feedpost.FeedPostContract;
import ca.gc.inspection.scoop.feedpost.FeedPostViewHolder;
import ca.gc.inspection.scoop.postcomment.PostCommentContract;
import ca.gc.inspection.scoop.postcomment.PostCommentFragment;
import ca.gc.inspection.scoop.postcomment.PostCommentViewHolder;

import static java.lang.Integer.min;

/**
 * Adapter used to create ViewHolders and bind new data to them for the RecyclerView in DisplayPostFragment.
 * Part of the View layer.
 */
public class DisplayPostAdapter extends RecyclerView.Adapter<PostCommentViewHolder>
    implements DisplayPostContract.View.Fragment.Adapter {

    // Must interact with the Presenter through the interface as communication occurs across the View-Presenter boundary
    private DisplayPostContract.Presenter.FragmentAPI.AdapterAPI mDisplayPostPresenter;

    /* Interacts with the Fragment directly as they are both part of the View layer.
    Interacting with the Fragment using the Contract is discouraged as it would require defining and therefore exposing
    View-specific methods to the Presenter */
    private DisplayPostFragment mDisplayPostView;

    public DisplayPostAdapter(DisplayPostFragment displayPostFragment,
                              DisplayPostContract.Presenter.FragmentAPI.AdapterAPI presenter) {
        mDisplayPostView = displayPostFragment;
        mDisplayPostPresenter = presenter;
        mDisplayPostPresenter.setAdapter(this);
    }

    /**
     * Get the ViewHolder type.
     * 0 for the single FeedPost
     * 1 for all other PostComments
     * @param i     index of ViewHolder passed in by Android framework
     * @return
     */
    @Override
    public int getItemViewType(int i) {
        return min(1, i);
    }

    @NonNull
    @Override
    public PostCommentViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        // Create the first post as a FeedPostViewHolder
        if (viewType == 0) {
            View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_post, viewGroup, false);
            return new FeedPostViewHolder(v, (FeedPostContract.Presenter.ViewHolderAPI) mDisplayPostPresenter);
        }
        // remaining ViewHolders are PostCommentViewHolders
        else {
            View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_post_comments, viewGroup, false);
            return new PostCommentViewHolder(v, (PostCommentContract.Presenter.ViewHolderAPI) mDisplayPostPresenter);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull PostCommentViewHolder viewHolder, int i) {

        /* Bind data to the single Post (implemented as a FeedPost) using the contract
         * method and set listeners which are exclusive to FeedPost */
        if (viewHolder.getItemViewType() == 0) {
            mDisplayPostPresenter.onBindViewHolder((FeedPostContract.View.ViewHolder) viewHolder);
            PostCommentFragment.setPostOptionsListener(viewHolder, i, mDisplayPostPresenter.getActivityIdByIndex(i),
                    mDisplayPostPresenter.getPosterIdByIndex(i), mDisplayPostPresenter.getSavedStateByIndex(i),
                    mDisplayPostPresenter.getPosterIdByIndex(0),
                    mDisplayPostView);

            PostCommentFragment.setSaveListener(viewHolder, i);
            PostCommentFragment.setUnsaveListener(viewHolder, i);
        }
        else {
            // Bind data to the PostComment using the contract method
            mDisplayPostPresenter.onBindViewHolderAtPosition(viewHolder, i);

            /* Overloaded static method is only called for comments
             * FeedPost also calls setPostOptionsListener but with different parameters */
            PostCommentFragment.setPostOptionsListener(viewHolder, i, mDisplayPostPresenter.getActivityIdByIndex(i),
                    mDisplayPostPresenter.getPosterIdByIndex(i), mDisplayPostView);
        }

        // Set listeners which are present in both FeedPosts and PostComments
        PostCommentFragment.setDisplayPostListener(viewHolder,
                mDisplayPostPresenter.getActivityIdByIndex(i), mDisplayPostPresenter.getPosterIdByIndex(i));
        PostCommentFragment.setLikesListener(viewHolder, i);
        PostCommentFragment.setUserInfoListener(viewHolder,
                mDisplayPostPresenter.getPosterIdByIndex(i));

    }

    @Override
    public int getItemCount() {
        return mDisplayPostPresenter.getItemCount();
    }

    /**
     * Used by Presenter to notify the Adapter of changes to the PostDataCache.
     * Should be called directly after altering the size of the PostDataCache to avoid a possible
     * index out of bounds error when scrolling through the RecyclerView.
     */
    @Override
    public void refreshAdapter() {
        notifyDataSetChanged();
    }
}
