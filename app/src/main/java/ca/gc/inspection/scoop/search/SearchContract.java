package ca.gc.inspection.scoop.search;

import ca.gc.inspection.scoop.base.BasePresenter;
import ca.gc.inspection.scoop.base.BaseView;

/**
 * todo add documentation
 */

public interface SearchContract {
    interface View extends BaseView<SearchContract.Presenter> {

        interface Fragment {
            void searchQuery(String query);
        }
    }

    interface Presenter extends BasePresenter {

    }
}
