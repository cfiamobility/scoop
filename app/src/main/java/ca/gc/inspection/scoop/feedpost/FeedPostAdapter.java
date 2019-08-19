package ca.gc.inspection.scoop.feedpost;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ca.gc.inspection.scoop.R;
import ca.gc.inspection.scoop.postcomment.PostComment;
import ca.gc.inspection.scoop.postcomment.PostCommentFragment;
import ca.gc.inspection.scoop.postoptionsdialog.PostOptionsDialogReceiver;
import ca.gc.inspection.scoop.profilelikes.ProfileLikesFragment;
import ca.gc.inspection.scoop.profilepost.ProfilePostFragment;

public class FeedPostAdapter extends RecyclerView.Adapter<FeedPostViewHolder>
    implements FeedPostContract.View.Adapter {
    /**
     * Adapter used to create ViewHolders and bind new data to them for a RecyclerView.
     * Considered to be part of the View.
     */

    private FeedPostContract.Presenter.AdapterAPI mFeedPostPresenter;
    private FeedPostContract.View mFeedPostView;    // current assumption: only implementing community feed fragment

    /**
     * Constructor for the adapter
     */
    public FeedPostAdapter(FeedPostContract.View profileCommentView,
                           FeedPostContract.Presenter.AdapterAPI presenter) {
        mFeedPostView = profileCommentView;
        mFeedPostPresenter = presenter;
        mFeedPostPresenter.setAdapter(this);
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
        return new FeedPostViewHolder(view, (FeedPostContract.Presenter.ViewHolderAPI) mFeedPostPresenter);
    }

    /**
     * Binds new data to the viewholder
     * @param i
     */
    @Override
    public void onBindViewHolder(@NonNull FeedPostViewHolder feedPostViewHolder, int i) {
        mFeedPostPresenter.onBindViewHolderAtPosition(feedPostViewHolder, i);
        PostCommentFragment.setDisplayPostListener(feedPostViewHolder,
                mFeedPostPresenter.getActivityIdByIndex(i), mFeedPostPresenter.getPosterIdByIndex(i));
        PostCommentFragment.setLikesListener(feedPostViewHolder, i);
        PostCommentFragment.setUserInfoListener(feedPostViewHolder,
                mFeedPostPresenter.getPosterIdByIndex(i));
        Log.i("feed post adapter", Boolean.toString(mFeedPostPresenter.getSavedStateByIndex(i)));
        PostCommentFragment.setPostOptionsListener(feedPostViewHolder, i,
                mFeedPostPresenter.getActivityIdByIndex(i),mFeedPostPresenter.getPosterIdByIndex(i), mFeedPostPresenter.getSavedStateByIndex(i),
                mFeedPostPresenter.getPosterIdByIndex(0),  mFeedPostView);
        PostCommentFragment.setSaveListener(feedPostViewHolder, i);
        PostCommentFragment.setUnsaveListener(feedPostViewHolder, i);

    }

    @Override
    public int getItemCount() {
        return mFeedPostPresenter.getItemCount();
    }

    @Override
    public void refreshAdapter() {
        notifyDataSetChanged();
    }
}
