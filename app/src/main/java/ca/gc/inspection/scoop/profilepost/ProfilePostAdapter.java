package ca.gc.inspection.scoop.profilepost;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ca.gc.inspection.scoop.R;

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
	public ProfilePostAdapter(ProfilePostFragment profileCommentView, ProfilePostContract.Presenter.AdapterAPI presenter) {
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

        // TODO use inheritance and call super? - NOTE that either onBind in Adapter or Presenter
        // should call super but not both as it would cause the same information to be set to the view
        // multiple times
        mProfilePostView.setProfileCommentImageListener(profilePostViewHolder);
        mProfilePostView.setProfileCommentLikesListener(profilePostViewHolder, i);
        mProfilePostView.setProfileCommentUserInfoListener(profilePostViewHolder,
                mProfilePostPresenter.getPosterIdByIndex(i));

        mProfilePostView.setPostOptionsListener(profilePostViewHolder);
    }

	@Override
	public int getItemCount() {
		return mProfilePostPresenter.getItemCount();
	}

    // TODO remove unnecessary override?
    @Override
    public void refreshAdapter() {
        notifyDataSetChanged();
    }
}
