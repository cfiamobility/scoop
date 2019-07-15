package ca.gc.inspection.scoop.search;

import android.support.annotation.NonNull;

import static com.google.gson.internal.$Gson$Preconditions.checkNotNull;

public class SearchPresenter implements SearchContract.Presenter {
    /**
     * Presenter for SearchActivity
     * Currently not necessary, however this can be extended to store the user's search history
     * using the interactor
     */

    private SearchInteractor mInteractor;
    private SearchContract.View mView;

    SearchPresenter(@NonNull SearchContract.View view) {
        mInteractor = new SearchInteractor(this);
        mView = checkNotNull(view);
    }


}
