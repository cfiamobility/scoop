package ca.gc.inspection.scoop.profilelikes;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ca.gc.inspection.scoop.R;
import ca.gc.inspection.scoop.postcomment.PostCommentFragment;

public class ProfileLikesAdapter extends RecyclerView.Adapter<ProfileLikesViewHolder>
        implements ProfileLikesContract.View.Adapter {
    /**
     * Adapter used to create ViewHolders and bind new data to them for a RecyclerView.
     * Considered to be part of the View.
     */

    private ProfileLikesContract.Presenter.AdapterAPI mProfileLikesPresenter;
    private ProfileLikesFragment mProfileLikesView;

    /**
     * Constructor for the adapter
     */
    public ProfileLikesAdapter(ProfileLikesFragment profileCommentView,
                              ProfileLikesContract.Presenter.AdapterAPI presenter) {
        mProfileLikesView = profileCommentView;
        mProfileLikesPresenter = presenter;
        mProfileLikesPresenter.setAdapter(this);
    }

    /**
     * @param viewGroup
     * @param i
     * @return
     */
    @NonNull
    @Override
    public ProfileLikesViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.custom_profile_layout, viewGroup, false);
        return new ProfileLikesViewHolder(v, (ProfileLikesContract.Presenter.ViewHolderAPI) mProfileLikesPresenter);
    }

    /**
     * Binds new data to the viewholder
     *
     * @param profilePostViewHolder
     * @param i
     */
    @Override
    public void onBindViewHolder(@NonNull ProfileLikesViewHolder profilePostViewHolder, int i) {
        mProfileLikesPresenter.onBindViewHolderAtPosition(profilePostViewHolder, i);
        PostCommentFragment.setDisplayPostListener(profilePostViewHolder,
                mProfileLikesPresenter.getActivityIdByIndex(i));
        PostCommentFragment.setLikesListener(profilePostViewHolder, i);
        PostCommentFragment.setUserInfoListener(profilePostViewHolder,
                mProfileLikesPresenter.getPosterIdByIndex(i));
        PostCommentFragment.setPostOptionsListener(profilePostViewHolder, i,
                mProfileLikesPresenter.getActivityIdByIndex(i), mProfileLikesPresenter.getPosterIdByIndex(i),
                mProfileLikesPresenter.getSavedStateByIndex(i), mProfileLikesPresenter.getPosterIdByIndex(0),
                mProfileLikesView);
        PostCommentFragment.setSaveListener(profilePostViewHolder, i);
        PostCommentFragment.setUnsaveListener(profilePostViewHolder, i);
    }

    @Override
    public int getItemCount() {
        return mProfileLikesPresenter.getItemCount();
    }

    @Override
    public void refreshAdapter() {
        notifyDataSetChanged();
    }
}