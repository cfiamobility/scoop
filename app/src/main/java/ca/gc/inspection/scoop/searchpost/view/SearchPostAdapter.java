package ca.gc.inspection.scoop.searchpost.view;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ca.gc.inspection.scoop.R;
import ca.gc.inspection.scoop.postcomment.PostCommentFragment;
import ca.gc.inspection.scoop.searchpost.SearchPostContract;

/**
 * Adapter used to create ViewHolders and bind new data to them for a RecyclerView.
 * Considered to be part of the View.
 */
public class SearchPostAdapter extends RecyclerView.Adapter<SearchPostViewHolder>
    implements SearchPostContract.View.Adapter {

	private SearchPostContract.Presenter.AdapterAPI mSearchPostPresenter;
    private SearchPostFragment mSearchPostView;

    /**
     * Constructor for the adapter
     */
	public SearchPostAdapter(SearchPostFragment searchPostView,
                             SearchPostContract.Presenter.AdapterAPI presenter) {
        mSearchPostView = searchPostView;
        mSearchPostPresenter = presenter;
        mSearchPostPresenter.setAdapter(this);
	}

    /**
     * @param viewGroup
     * @param i
     * @return
     */
    @NonNull
    @Override
    public SearchPostViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.custom_profile_layout, viewGroup, false);
        return new SearchPostViewHolder(v, (SearchPostContract.Presenter.ViewHolderAPI) mSearchPostPresenter);
    }

    /**
     * Binds new data to the viewholder
     *
     * @param searchPostViewHolder
     * @param i
     */
    @Override
    public void onBindViewHolder(@NonNull SearchPostViewHolder searchPostViewHolder, int i) {
        mSearchPostPresenter.onBindViewHolderAtPosition(searchPostViewHolder, i);
        PostCommentFragment.setDisplayPostListener(searchPostViewHolder,
                mSearchPostPresenter.getActivityIdByIndex(i), mSearchPostPresenter.getPosterIdByIndex(i));
        PostCommentFragment.setLikesListener(searchPostViewHolder, i);
        PostCommentFragment.setUserInfoListener(searchPostViewHolder,
                mSearchPostPresenter.getPosterIdByIndex(i));

        PostCommentFragment.setPostOptionsListener(searchPostViewHolder, i,
                mSearchPostPresenter.getActivityIdByIndex(i),mSearchPostPresenter.getPosterIdByIndex(i),
                mSearchPostPresenter.getSavedStateByIndex(i), mSearchPostPresenter.getPosterIdByIndex(0),
                mSearchPostView);
        PostCommentFragment.setSaveListener(searchPostViewHolder, i);
        PostCommentFragment.setUnsaveListener(searchPostViewHolder, i);
    }

	@Override
	public int getItemCount() {
		return mSearchPostPresenter.getItemCount();
	}

    @Override
    public void refreshAdapter() {
        notifyDataSetChanged();
    }
}
