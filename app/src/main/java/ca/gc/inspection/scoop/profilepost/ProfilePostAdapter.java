package ca.gc.inspection.scoop.profilepost;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ca.gc.inspection.scoop.R;
import ca.gc.inspection.scoop.postcomment.PostCommentFragment;

public class ProfilePostAdapter extends RecyclerView.Adapter<ProfilePostViewHolder>
    implements ProfilePostContract.View.Adapter {
    /**
     * Adapter used to create ViewHolders and bind new data to them for a RecyclerView.
     * Considered to be part of the View.
     */

	private ProfilePostContract.Presenter.AdapterAPI mProfilePostPresenter;
    private ProfilePostFragment mProfilePostView;

    /**
     * Constructor for the adapter
     */
	public ProfilePostAdapter(ProfilePostFragment profilePostView,
                              ProfilePostContract.Presenter.AdapterAPI presenter) {
        mProfilePostView = profilePostView;
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
    public ProfilePostViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.custom_profile_layout, viewGroup, false);
        return new ProfilePostViewHolder(v, (ProfilePostContract.Presenter.ViewHolderAPI) mProfilePostPresenter);
    }

    /**
     * Binds new data to the viewholder
     *
     * @param profilePostViewHolder
     * @param i
     */
    @Override
    public void onBindViewHolder(@NonNull ProfilePostViewHolder profilePostViewHolder, int i) {
        mProfilePostPresenter.onBindViewHolderAtPosition(profilePostViewHolder, i);
        PostCommentFragment.setDisplayPostListener(profilePostViewHolder,
                mProfilePostPresenter.getActivityIdByIndex(i));
        PostCommentFragment.setLikesListener(profilePostViewHolder, i);
        PostCommentFragment.setUserInfoListener(profilePostViewHolder,
                mProfilePostPresenter.getPosterIdByIndex(i));
        PostCommentFragment.setPostOptionsListener(profilePostViewHolder, i,
                mProfilePostPresenter.getActivityIdByIndex(i),mProfilePostPresenter.getPosterIdByIndex(i),
                mProfilePostPresenter.getSavedStateByIndex(i), mProfilePostPresenter.getPosterIdByIndex(0),
                mProfilePostPresenter.getPostTitleByIndex(i), mProfilePostPresenter.getPostTextByIndex(i),
                null, mProfilePostView);
        PostCommentFragment.setSaveListener(profilePostViewHolder, i);
        PostCommentFragment.setUnsaveListener(profilePostViewHolder, i);
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
