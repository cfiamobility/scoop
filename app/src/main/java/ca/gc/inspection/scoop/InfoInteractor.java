package ca.gc.inspection.scoop;

import android.support.annotation.NonNull;

import static com.google.gson.internal.$Gson$Preconditions.checkNotNull;

class InfoInteractor {

    private InfoPresenter mPresenter;

    InfoInteractor(@NonNull InfoPresenter presenter) {
        mPresenter = checkNotNull(presenter);
    }
}
