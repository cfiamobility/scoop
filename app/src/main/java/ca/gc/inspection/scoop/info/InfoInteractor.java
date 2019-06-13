package ca.gc.inspection.scoop.info;

import android.support.annotation.NonNull;

import static com.google.gson.internal.$Gson$Preconditions.checkNotNull;

class InfoInteractor {

    private InfoPresenter mPresenter;

    InfoInteractor(@NonNull InfoPresenter presenter) {
        mPresenter = checkNotNull(presenter);
    }
}
