package ca.gc.inspection.scoop.feedpost;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ca.gc.inspection.scoop.R;

public class FeedPostAdapter extends RecyclerView.Adapter<FeedPostViewHolder>
    implements FeedPostContract.View.Adapter {

    private FeedPostContract.Presenter.AdapterAPI mFeedPostPresenter;
    private CommunityFeedFragment mFeedPostView;    // current assumption: only implementing community feed fragment

    /**
     * Constructor for the adapter
     */
    public FeedPostAdapter(CommunityFeedFragment profileCommentView, FeedPostContract.Presenter.AdapterAPI presenter) {
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

        // TODO use inheritance and call super? - NOTE that either onBind in Adapter or Presenter
        // should call super but not both as it would cause the same information to be set to the view
        // multiple times
        mFeedPostView.setProfileCommentImageListener(feedPostViewHolder);
        mFeedPostView.setProfileCommentLikesListener(feedPostViewHolder, i);
        mFeedPostView.setProfileCommentUserInfoListener(feedPostViewHolder,
                mFeedPostPresenter.getPosterIdByIndex(i));
        mFeedPostView.setPostOptionsListener(feedPostViewHolder);
    }

    @Override
    public int getItemCount() {
        return mFeedPostPresenter.getItemCount();
    }

    // TODO remove unnecessary override?
    @Override
    public void refreshAdapter() {
        notifyDataSetChanged();
    }
}
