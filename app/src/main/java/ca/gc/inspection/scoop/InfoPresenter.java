package ca.gc.inspection.scoop;

import android.support.annotation.NonNull;

import static com.google.gson.internal.$Gson$Preconditions.checkNotNull;

public class InfoPresenter implements InfoContract.Presenter {

    private InfoInteractor mInteractor;
    private InfoContract.View mView;

    InfoPresenter(@NonNull InfoContract.View view) {
        mInteractor = new InfoInteractor(this);
        mView = checkNotNull(view);
    }

    @Override
    public void start() {

    }
}
