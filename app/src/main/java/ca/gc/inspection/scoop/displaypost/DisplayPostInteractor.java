package ca.gc.inspection.scoop.displaypost;

import android.support.annotation.NonNull;

import static com.google.gson.internal.$Gson$Preconditions.checkNotNull;

class DisplayPostInteractor {

    private DisplayPostPresenter mPresenter;

    DisplayPostInteractor(@NonNull DisplayPostPresenter presenter) {
        mPresenter = checkNotNull(presenter);
    }
}
