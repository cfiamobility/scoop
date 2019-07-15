package ca.gc.inspection.scoop.searchpost;

import ca.gc.inspection.scoop.base.BaseView;
import ca.gc.inspection.scoop.profilepost.ProfilePostContract;
import ca.gc.inspection.scoop.search.SearchContract;

public interface SearchPostContract extends SearchContract {
    /**
     * A contract between the View (layer) and Presenter for the viewing a profile post
     * action. This contract communicates the how the View and Presenter will
     * interact with each other.
     *
     * Since RecyclerView is being used in this package, we must additionally specify the interaction
     * between sub-Views (Adapter/ViewHolder) and the Presenter (via AdapterAPI/ViewHolderAPI).
     *
     * View = ProfilePostFragment - is the main View responsible for creating the Presenter and Adapter
     *      View.Adapter = SearchPostAdapter is the subview responsible for creating ViewHolders
     *      View.ViewHolder = SearchPostViewHolder is the subview which displays an individual profile comment
     *
     * Presenter = SearchPostPresenter - there's only one object which implements:
     *      Presenter.AdapterAPI - used by the Adapter to call Presenter methods
     *      Presenter.ViewHolderAPI - used by the ViewHolders to call Presenter methods
     *
     * If communication is required between the View layer and Presenter layer, interface methods must be used.
     *      - if the Presenter needs the View and vice-versa, the interface should be passed in.
     * If communication is required within the View only or Presenter only, the object itself should be passed in
     * to avoid leaking access to internal methods in the contract.
     *
     * See PostCommentContract for inheritance hierarchy for Posts/Comments
     */

    interface View extends BaseView<Presenter> {
        /**
         * Implemented by the main View (ie. ProfilePostFragment).
         * Methods specified here in the View but not in the nested View.Adapter and View.ViewHolder interfaces
         * explain how the Presenter is to communicate with the main View only.
         */

        interface Adapter extends ProfilePostContract.View.Adapter {
        }

        interface ViewHolder extends ProfilePostContract.View.ViewHolder {
        }
    }

    interface Presenter extends ProfilePostContract.Presenter {

        void loadDataFromDatabase(String userId, String search);

        interface AdapterAPI extends ProfilePostContract.Presenter.AdapterAPI {
            void setAdapter(SearchPostContract.View.Adapter adapter);
            void onBindViewHolderAtPosition(
                    SearchPostContract.View.ViewHolder postCommentViewHolder, int i);
        }

        interface ViewHolderAPI extends ProfilePostContract.Presenter.ViewHolderAPI {
        }
    }
}
