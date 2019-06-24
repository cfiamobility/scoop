package ca.gc.inspection.scoop.displaypost;

import android.support.annotation.NonNull;

import static com.google.gson.internal.$Gson$Preconditions.checkNotNull;

class DisplayPostPresenter implements DisplayPostContract.Presenter {

    private DisplayPostInteractor mInteractor;
    private DisplayPostContract.View mView;

    DisplayPostPresenter(@NonNull DisplayPostContract.View view) {
        mInteractor = new DisplayPostInteractor(this);
        mView = checkNotNull(view);
    }


}
