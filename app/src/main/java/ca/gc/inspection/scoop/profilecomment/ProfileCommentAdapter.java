package ca.gc.inspection.scoop.profilecomment;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import ca.gc.inspection.scoop.R;
import ca.gc.inspection.scoop.postcomment.PostCommentFragment;
import ca.gc.inspection.scoop.util.NetworkUtils;

public class ProfileCommentAdapter extends RecyclerView.Adapter<ProfileCommentViewHolder>
        implements ProfileCommentContract.View.Adapter {

    /**
     * Adapter used to create ViewHolders and bind new data to them for a RecyclerView.
     * Considered to be part of the View.
     */

    private ProfileCommentContract.Presenter.AdapterAPI mProfileCommentPresenter;
    private ProfileCommentFragment mProfileCommentView;
    private NetworkUtils mNetworkUtil;

    /**
     * Constructor for the adapter.
     *
     * @param profileCommentView    The fragment instead of view contract can be taken in as both are considered part of the view. (see Contract documentation)
     * @param presenter             The presenter is passed in as the contract which specifies View-Presenter interaction.
     */
    public ProfileCommentAdapter(ProfileCommentFragment profileCommentView,
                                 ProfileCommentContract.Presenter.AdapterAPI presenter,
                                 NetworkUtils network) {
        mProfileCommentView = profileCommentView;
        mProfileCommentPresenter = presenter;
        mProfileCommentPresenter.setAdapter(this);
        mNetworkUtil = network;
    }

    /**
     * Create the ViewHolder objects
     *
     * @param viewGroup:    The RecyclerView will be automatically passed in by Android - used for layout parameters
     * @param i:            item iterator for each row of the recycler view
     * @returns a new view holder
     */
    @NonNull
    @Override
    public ProfileCommentViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_profile_comments, viewGroup, false);
        // Pass in the presenter as a Presenter.ViewHolderAPI to provide the minimum required methods
        return new ProfileCommentViewHolder(v, (ProfileCommentContract.Presenter.ViewHolderAPI) mProfileCommentPresenter);
    }

    /**
     * Binds new data to the ViewHolder as the user scrolls through the RecyclerView.
     * Calls the Presenter (interface) to retrieve the data and update the ViewHolder through the View.ViewHolder contract.
     * Calls the View object to set the listeners for the ViewHolders - contract not necessary as this is intra-View communication.
     *
     * @param profileCommentViewHolder  ViewHolder automatically passed in by Android
     * @param i                         Index of the item to be bound
     */
    @Override
    public void onBindViewHolder(@NonNull ProfileCommentViewHolder profileCommentViewHolder, int i) {
        mProfileCommentPresenter.onBindViewHolderAtPosition(profileCommentViewHolder, i);
        PostCommentFragment.setDisplayPostListener(profileCommentViewHolder);
        PostCommentFragment.setLikesListener(mNetworkUtil, profileCommentViewHolder, i);
        PostCommentFragment.setUserInfoListener(profileCommentViewHolder,
                mProfileCommentPresenter.getPosterIdByIndex(i));
    }

    /**
     * Necessary Android method for RecyclerView.Adapter
     * @return result from Presenter
     */
    @Override
    public int getItemCount() {
        return mProfileCommentPresenter.getItemCount();
    }

    /**
     * Called by presenter when it's data is updated. This lets the adapter know when
     * binding new data to the view (without being triggered by scrolling) is necessary.
     */
    @Override
    public void refreshAdapter() {
        notifyDataSetChanged();
    }
}
