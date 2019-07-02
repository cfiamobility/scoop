package ca.gc.inspection.scoop.displaypost;

import android.support.annotation.NonNull;

import ca.gc.inspection.scoop.feedpost.FeedPostContract;
import ca.gc.inspection.scoop.feedpost.FeedPostPresenter;
import ca.gc.inspection.scoop.postcomment.PostCommentContract;

import static com.google.gson.internal.$Gson$Preconditions.checkNotNull;

class DisplayPostPresenter extends FeedPostPresenter implements
    DisplayPostContract.Presenter {

    private DisplayPostInteractor mInteractor;
    private DisplayPostContract.View mView;

    DisplayPostPresenter(@NonNull DisplayPostContract.View view) {
        mInteractor = new DisplayPostInteractor(this);
        mView = checkNotNull(view);
    }

    @Override
    public void onBindViewHolderAtPosition(PostCommentContract.View.ViewHolder viewHolderInterface, int i) {
        super.onBindViewHolderAtPosition(viewHolderInterface, i);
    }

    public void onBindViewHolderAtPosition(DisplayPostContract.View.Fragment.V viewHolderInterface, int i) {
        super.onBindViewHolderAtPosition(viewHolderInterface, i);
    }
}
