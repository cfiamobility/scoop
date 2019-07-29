package ca.gc.inspection.scoop.postcomment;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ca.gc.inspection.scoop.R;

public class PostCommentAdapter extends RecyclerView.Adapter<PostCommentViewHolder>
        implements PostCommentContract.View.Adapter {

    /**
     * Adapter used to create ViewHolders and bind new data to them for a RecyclerView.
     * Considered to be part of the View.
     */

    private PostCommentContract.Presenter.AdapterAPI mPostCommentPresenter;
    private PostCommentFragment mPostCommentView;

    /**
     * Constructor for the adapter.
     *
     * @param postCommentView    The fragment instead of view contract can be taken in as both are considered part of the view. (see Contract documentation)
     * @param presenter             The presenter is passed in as the contract which specifies View-Presenter interaction.
     */
    public PostCommentAdapter(PostCommentFragment postCommentView,
                              PostCommentContract.Presenter.AdapterAPI presenter) {
        mPostCommentView = postCommentView;
        mPostCommentPresenter = presenter;
        mPostCommentPresenter.setAdapter(this);
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
    public PostCommentViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_post_comments, viewGroup, false);
        // Pass in the presenter as a Presenter.ViewHolderAPI to provide the minimum required methods
        return new PostCommentViewHolder(v, (PostCommentContract.Presenter.ViewHolderAPI) mPostCommentPresenter);
    }

    /**
     * Binds new data to the ViewHolder as the user scrolls through the RecyclerView.
     * Calls the Presenter (interface) to retrieve the data and update the ViewHolder through the View.ViewHolder contract.
     * Calls the View object to set the listeners for the ViewHolders - contract not necessary as this is intra-View communication.
     *
     * @param postCommentViewHolder  ViewHolder automatically passed in by Android
     * @param i                         Index of the item to be bound
     */
    @Override
    public void onBindViewHolder(@NonNull PostCommentViewHolder postCommentViewHolder, int i) {
        mPostCommentPresenter.onBindViewHolderAtPosition(postCommentViewHolder, i);
        PostCommentFragment.setDisplayPostListener(postCommentViewHolder,
                mPostCommentPresenter.getActivityIdByIndex(i));
        PostCommentFragment.setLikesListener(postCommentViewHolder, i);
        PostCommentFragment.setUserInfoListener(postCommentViewHolder,
                mPostCommentPresenter.getPosterIdByIndex(i));
        PostCommentFragment.setPostOptionsListener(postCommentViewHolder, i,
                mPostCommentPresenter.getActivityIdByIndex(i),
                mPostCommentPresenter.getPosterIdByIndex(i),
                mPostCommentView);
    }

    /**
     * Necessary Android method for RecyclerView.Adapter
     * @return result from Presenter
     */
    @Override
    public int getItemCount() {
        return mPostCommentPresenter.getItemCount();
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
