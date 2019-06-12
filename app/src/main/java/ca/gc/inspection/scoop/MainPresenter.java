package ca.gc.inspection.scoop;

import android.support.annotation.NonNull;
import static com.google.gson.internal.$Gson$Preconditions.checkNotNull;

class MainPresenter implements MainContract.Presenter {

    private MainInteractor mInteractor;
    private MainContract.View mView;

    MainPresenter(@NonNull MainContract.View view) {
        mInteractor = new MainInteractor(this);
        mView = checkNotNull(view);
    }

    @Override
    public void start() {

    }
}
