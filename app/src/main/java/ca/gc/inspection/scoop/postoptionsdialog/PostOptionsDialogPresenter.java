package ca.gc.inspection.scoop.postoptionsdialog;

import android.support.annotation.NonNull;

import static com.google.gson.internal.$Gson$Preconditions.checkNotNull;

public class PostOptionsDialogPresenter implements PostOptionsDialogContract.Presenter {

    private PostOptionsDialogInteractor mInteractor;
    private PostOptionsDialogContract.View mView;

    PostOptionsDialogPresenter(@NonNull PostOptionsDialogContract.View view) {
        mInteractor = new PostOptionsDialogInteractor(this);
        mView = checkNotNull(view);
    }

    public void savePost(){

    }


}
