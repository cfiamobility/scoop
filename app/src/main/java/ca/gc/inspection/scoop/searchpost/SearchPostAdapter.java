package ca.gc.inspection.scoop.searchposts;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ca.gc.inspection.scoop.R;
import ca.gc.inspection.scoop.postcomment.PostCommentFragment;
import ca.gc.inspection.scoop.profilepost.ProfilePostFragment;

public class SearchPostAdapter extends RecyclerView.Adapter<SearchPostViewHolder>
    implements SearchPostContract.View.Adapter {
    /**
     * Adapter used to create ViewHolders and bind new data to them for a RecyclerView.
     * Considered to be part of the View.
     */

	private SearchPostContract.Presenter.AdapterAPI mProfilePostPresenter;
    private ProfilePostFragment mProfilePostView;

    /**
     * Constructor for the adapter
     */
	public SearchPostAdapter(ProfilePostFragment profileCommentView,
                             SearchPostContract.Presenter.AdapterAPI presenter) {
        mProfilePostView = profileCommentView;
        mProfilePostPresenter = presenter;
        mProfilePostPresenter.setAdapter(this);
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
        return new SearchPostViewHolder(v, (SearchPostContract.Presenter.ViewHolderAPI) mProfilePostPresenter);
    }

    /**
     * Binds new data to the viewholder
     *
     * @param searchPostViewHolder
     * @param i
     */
    @Override
    public void onBindViewHolder(@NonNull SearchPostViewHolder searchPostViewHolder, int i) {
        mProfilePostPresenter.onBindViewHolderAtPosition(searchPostViewHolder, i);
        PostCommentFragment.setDisplayPostListener(searchPostViewHolder,
                mProfilePostPresenter.getActivityIdByIndex(i));
        PostCommentFragment.setLikesListener(searchPostViewHolder, i);
        PostCommentFragment.setUserInfoListener(searchPostViewHolder,
                mProfilePostPresenter.getPosterIdByIndex(i));

        ProfilePostFragment.setPostOptionsListener(searchPostViewHolder, mProfilePostPresenter.getActivityIdByIndex(i));
    }

	@Override
	public int getItemCount() {
		return mProfilePostPresenter.getItemCount();
	}

    @Override
    public void refreshAdapter() {
        notifyDataSetChanged();
    }
}
