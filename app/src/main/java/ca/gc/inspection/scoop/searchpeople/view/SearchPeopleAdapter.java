package ca.gc.inspection.scoop.searchpeople.view;

import ca.gc.inspection.scoop.R;
import ca.gc.inspection.scoop.postcomment.PostCommentFragment;
import ca.gc.inspection.scoop.searchpeople.SearchPeopleContract;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class SearchPeopleAdapter extends RecyclerView.Adapter<SearchPeopleViewHolder>
    implements SearchPeopleContract.View.Adapter {

    /**
     * Adapter used to create ViewHolders and bind new data to them for a RecyclerView.
     * Considered to be part of the View.
     */

    private SearchPeopleContract.Presenter.AdapterAPI mSearchPeoplePresenter;
    private SearchPeopleFragment mSearchPeopleView;

    /**
     * Constructor for the adapter
     */
    public SearchPeopleAdapter(SearchPeopleFragment searchPeopleView,
                             SearchPeopleContract.Presenter.AdapterAPI presenter) {
        mSearchPeopleView = searchPeopleView;
        mSearchPeoplePresenter = presenter;
        mSearchPeoplePresenter.setAdapter(this);
    }

    /**
     * @param viewGroup
     * @param i
     * @return
     */
    @NonNull
    @Override
    public SearchPeopleViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_search_profile, viewGroup, false);
        return new SearchPeopleViewHolder(v, (SearchPeopleContract.Presenter.ViewHolderAPI) mSearchPeoplePresenter);
    }

    /**
     * Binds new data to the viewholder
     *
     * @param searchPeopleViewHolder
     * @param i
     */
    @Override
    public void onBindViewHolder(@NonNull SearchPeopleViewHolder searchPeopleViewHolder, int i) {
        mSearchPeoplePresenter.onBindViewHolderAtPosition(searchPeopleViewHolder, i);
        PostCommentFragment.setUserInfoListener(searchPeopleViewHolder,
                mSearchPeoplePresenter.getProfileUserIdByIndex(i));
    }

    @Override
    public int getItemCount() {
        return mSearchPeoplePresenter.getItemCount();
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
