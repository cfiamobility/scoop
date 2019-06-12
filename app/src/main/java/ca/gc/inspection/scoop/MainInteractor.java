package ca.gc.inspection.scoop;

import android.support.annotation.NonNull;
import static com.google.gson.internal.$Gson$Preconditions.checkNotNull;

class MainInteractor {

    private MainPresenter mPresenter;

    MainInteractor(@NonNull MainPresenter presenter) {
        mPresenter = checkNotNull(presenter);
    }
}
