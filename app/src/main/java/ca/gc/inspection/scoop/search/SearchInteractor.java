package ca.gc.inspection.scoop.search;

import android.support.annotation.NonNull;

import static com.google.gson.internal.$Gson$Preconditions.checkNotNull;

class SearchInteractor {
    /**
     * Interactor for SearchActivity to communicate with the database.
     * Currently not necessary, however this can be extended to store the user's search history
     * in the database.
     */

    private SearchPresenter mPresenter;

    SearchInteractor(@NonNull SearchPresenter presenter) {
        mPresenter = checkNotNull(presenter);
    }
}
