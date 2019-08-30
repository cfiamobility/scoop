package ca.gc.inspection.scoop.search;

import ca.gc.inspection.scoop.base.BasePresenter;
import ca.gc.inspection.scoop.base.BaseView;

/**
 * A contract between the View (layer) and Presenter for the search action. This contract communicates
 * how the View and Presenter will interact with each other.
 *
 * View = SearchActivity - is the main View responsible for creating the Presenter and Adapter
 *      View.Fragment = SearchPostFragment, SearchProfileFragment are the subviews which contain the RecyclerView
 *
 * Presenter = SearchPresenter

 * If communication is required between the View layer and Presenter layer, interface methods must be used.
 *      - if the Presenter needs the View and vice-versa, the interface should be passed in.
 * If communication is required within the View only or Presenter only, the object itself should be passed in
 * to avoid leaking access to internal methods in the contract.
 */

public interface SearchContract {
    interface View extends BaseView<SearchContract.Presenter> {

        interface Fragment {
            /**
             * Not called by the Presenter - provides a common interface for SearchActivity to
             * send a search query to the appropriate subview.
             *
             * Can make custom interface such as SearchQueryReceiver to keep the Contract consistent
             * with MVP (Contract will be devoid of methods in that case).
             *
             * @param query     inputted by the user
             */
            void searchQuery(String query);
        }
    }

    interface Presenter extends BasePresenter {

    }
}
