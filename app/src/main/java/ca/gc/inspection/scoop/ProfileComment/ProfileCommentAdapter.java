package ca.gc.inspection.scoop.ProfileComment;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class ProfileCommentAdapter extends RecyclerView.Adapter<ProfileCommentViewHolder> {

    private ProfileCommentContract.Presenter.AdapterAPI mProfileCommentPresenter;
    private ProfileCommentFragment mProfileCommentView;

    public void setView (ProfileCommentFragment profileCommentView){
        mProfileCommentView = profileCommentView;
    }

    /**
     * Constructor for the adapter
     */
    public ProfileCommentAdapter(ProfileCommentFragment profileCommentView, ProfileCommentContract.Presenter.AdapterAPI presenter) {
        mProfileCommentView = profileCommentView;
        mProfileCommentPresenter = presenter;
    }

    /**
     * Creates the viewholder that contains all the parts of the adapter
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
        mProfileCommentPresenter.onBindProfileCommentViewHolderAtPosition(profileCommentViewHolder, i);
        mProfileCommentView.setProfileCommentImageListener(profileCommentViewHolder);
        mProfileCommentView.setProfileCommentLikesListener(profileCommentViewHolder, i);
        mProfileCommentView.setProfileCommentUserInfoListener(profileCommentViewHolder,
                mProfileCommentPresenter.getPosterIdByIndex(i));
    }

    @Override
    public int getItemCount() {
        return mProfileCommentPresenter.getItemCount();
    }

}
