package ca.gc.inspection.scoop.profilecomment;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import ca.gc.inspection.scoop.R;

public class ProfileCommentAdapter extends RecyclerView.Adapter<ProfileCommentViewHolder>
        implements ProfileCommentContract.View.Adapter {

    private ProfileCommentContract.Presenter.AdapterAPI mProfileCommentPresenter;
    private ProfileCommentFragment mProfileCommentView;

    /**
     * Constructor for the adapter
     */
    public ProfileCommentAdapter(ProfileCommentFragment profileCommentView, ProfileCommentContract.Presenter.AdapterAPI presenter) {
        mProfileCommentView = profileCommentView;
        mProfileCommentPresenter = presenter;
        mProfileCommentPresenter.setAdapter(this);
    }

    /**
     * @param viewGroup: ??
     * @param i: item iterator for each row of the recycler view
     * @returns a new view holder
     */
    @NonNull
    @Override
    public ProfileCommentViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_profile_comments, viewGroup, false);
        return new ProfileCommentViewHolder(v);
    }

    /**
     * Binds new data to the viewholder
     *
     * @param profileCommentViewHolder
     * @param i
     */
    @Override
    public void onBindViewHolder(@NonNull ProfileCommentViewHolder profileCommentViewHolder, int i) {
        mProfileCommentPresenter.onBindViewHolderAtPosition(profileCommentViewHolder, i);
        mProfileCommentView.setProfileCommentImageListener(profileCommentViewHolder);
        mProfileCommentView.setProfileCommentLikesListener(profileCommentViewHolder, i);
        mProfileCommentView.setProfileCommentUserInfoListener(profileCommentViewHolder,
                mProfileCommentPresenter.getPosterIdByIndex(i));
    }

    @Override
    public int getItemCount() {
        return mProfileCommentPresenter.getItemCount();
    }

    @Override
    public void refreshAdapter() {
        notifyDataSetChanged();
    }
}
