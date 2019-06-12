package ca.gc.inspection.scoop;

import android.support.annotation.NonNull;

import static com.google.gson.internal.$Gson$Preconditions.checkNotNull;

class SearchInteractor {

    private SearchPresenter mPresenter;

    SearchInteractor(@NonNull SearchPresenter presenter) {
        mPresenter = checkNotNull(presenter);
    }
}
