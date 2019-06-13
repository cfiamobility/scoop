package ca.gc.inspection.scoop.search;

import android.support.annotation.NonNull;

import static com.google.gson.internal.$Gson$Preconditions.checkNotNull;

public class SearchPresenter implements SearchContract.Presenter {

    private SearchInteractor mInteractor;
    private SearchContract.View mView;

    SearchPresenter(@NonNull SearchContract.View view) {
        mInteractor = new SearchInteractor(this);
        mView = checkNotNull(view);
    }

    @Override
    public void start() {

    }
}
