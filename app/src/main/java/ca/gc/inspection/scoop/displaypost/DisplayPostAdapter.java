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

public class DisplayPostAdapter extends RecyclerView.Adapter<PostCommentViewHolder>
    implements DisplayPostContract.View.Fragment.Adapter {

    private DisplayPostContract.Presenter.FragmentAPI.AdapterAPI mDisplayPostPresenter;
    private DisplayPostFragment mDisplayPostView;

    public DisplayPostAdapter(DisplayPostFragment displayPostFragment,
                              DisplayPostContract.Presenter.FragmentAPI.AdapterAPI presenter) {
        mDisplayPostView = displayPostFragment;
        mDisplayPostPresenter = presenter;
        mDisplayPostPresenter.setAdapter(this);
    }

    @Override
    public int getItemViewType(int i) {
        return min(1, i);
    }

    @NonNull
    @Override
    public PostCommentViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        if (viewType == 0) {
            View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_post, viewGroup, false);
            return new FeedPostViewHolder(v, (FeedPostContract.Presenter.ViewHolderAPI) mDisplayPostPresenter);
        }
        else {
            View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_post_comments, viewGroup, false);
            return new PostCommentViewHolder(v, (PostCommentContract.Presenter.ViewHolderAPI) mDisplayPostPresenter);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull PostCommentViewHolder viewHolder, int i) {
        if (viewHolder.getItemViewType() == 0) {
            mDisplayPostPresenter.onBindViewHolder((FeedPostContract.View.ViewHolder) viewHolder);
//            ProfilePostFragment.setPostOptionsListener((ProfilePostViewHolder) viewHolder);
        }
        else {
            mDisplayPostPresenter.onBindViewHolderAtPosition(viewHolder, i);
        }
        PostCommentFragment.setDisplayPostListener(viewHolder,
                mDisplayPostPresenter.getActivityIdByIndex(i), mDisplayPostPresenter.getPosterIdByIndex(i));
        PostCommentFragment.setLikesListener(viewHolder, i);
        PostCommentFragment.setUserInfoListener(viewHolder,
                mDisplayPostPresenter.getPosterIdByIndex(i));
        PostCommentFragment.setPostOptionsListener(viewHolder, i, mDisplayPostPresenter.getActivityIdByIndex(i),
                mDisplayPostPresenter.getPosterIdByIndex(i), mDisplayPostPresenter.getSavedStateByIndex(i),
                mDisplayPostPresenter.getPosterIdByIndex(0), mDisplayPostView);
        PostCommentFragment.setSaveListener(viewHolder, i);
        PostCommentFragment.setUnsaveListener(viewHolder, i);
    }

    @Override
    public int getItemCount() {
        return mDisplayPostPresenter.getItemCount();
    }

    @Override
    public void refreshAdapter() {
        notifyDataSetChanged();
    }
}
