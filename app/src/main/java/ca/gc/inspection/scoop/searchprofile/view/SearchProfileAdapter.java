package ca.gc.inspection.scoop.searchprofile.view;

import ca.gc.inspection.scoop.R;
import ca.gc.inspection.scoop.postcomment.PostCommentFragment;
import ca.gc.inspection.scoop.searchprofile.SearchProfileContract;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class SearchProfileAdapter extends RecyclerView.Adapter<SearchProfileViewHolder>
    implements SearchProfileContract.View.Adapter {

    /**
     * Adapter used to create ViewHolders and bind new data to them for a RecyclerView.
     * Considered to be part of the View.
     */

    private SearchProfileContract.Presenter.AdapterAPI mSearchProfilePresenter;
    private SearchProfileFragment mSearchProfileView;

    /**
     * Constructor for the adapter
     */
    public SearchProfileAdapter(SearchProfileFragment searchProfileView,
                                SearchProfileContract.Presenter.AdapterAPI presenter) {
        mSearchProfileView = searchProfileView;
        mSearchProfilePresenter = presenter;
        mSearchProfilePresenter.setAdapter(this);
    }

    /**
     * @param viewGroup
     * @param i
     * @return
     */
    @NonNull
    @Override
    public SearchProfileViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_search_profile, viewGroup, false);
        return new SearchProfileViewHolder(v, (SearchProfileContract.Presenter.ViewHolderAPI) mSearchProfilePresenter);
    }

    /**
     * Binds new data to the viewholder
     *
     * @param searchProfileViewHolder
     * @param i
     */
    @Override
    public void onBindViewHolder(@NonNull SearchProfileViewHolder searchProfileViewHolder, int i) {
        mSearchProfilePresenter.onBindViewHolderAtPosition(searchProfileViewHolder, i);
        PostCommentFragment.setUserInfoListener(searchProfileViewHolder,
                mSearchProfilePresenter.getProfileUserIdByIndex(i));
    }

    @Override
    public int getItemCount() {
        return mSearchProfilePresenter.getItemCount();
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
