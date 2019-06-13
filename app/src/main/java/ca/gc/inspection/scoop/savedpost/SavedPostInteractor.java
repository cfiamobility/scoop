package ca.gc.inspection.scoop.savedpost;

import android.support.annotation.NonNull;

import static com.google.gson.internal.$Gson$Preconditions.checkNotNull;

class SavedPostInteractor {

    private SavedPostPresenter mPresenter;

    public SavedPostInteractor(@NonNull SavedPostPresenter presenter) {
        mPresenter = checkNotNull(presenter);
    }
}
