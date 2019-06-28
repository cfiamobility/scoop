package ca.gc.inspection.scoop.postoptionsdialog;

import android.support.annotation.NonNull;

import static com.google.gson.internal.$Gson$Preconditions.checkNotNull;

class PostOptionsDialogInteractor {

    private PostOptionsDialogPresenter mPresenter;

    public PostOptionsDialogInteractor(@NonNull PostOptionsDialogPresenter presenter) {
        mPresenter = checkNotNull(presenter);
    }
}
