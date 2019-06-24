package ca.gc.inspection.scoop.savedpost;

import android.support.annotation.NonNull;

import static com.google.gson.internal.$Gson$Preconditions.checkNotNull;

public class SavedPostPresenter implements SavedPostContract.Presenter {

    private SavedPostInteractor mInteractor;
    private SavedPostContract.View mView;

    SavedPostPresenter(@NonNull SavedPostContract.View view) {
        mInteractor = new SavedPostInteractor(this);
        mView = checkNotNull(view);
    }


}
