package ca.gc.inspection.scoop.displaypost;

import android.content.Context;
import android.support.annotation.NonNull;

import ca.gc.inspection.scoop.feedpost.FeedPostContract;
import ca.gc.inspection.scoop.feedpost.FeedPostPresenter;
import ca.gc.inspection.scoop.postcomment.PostCommentContract;
import ca.gc.inspection.scoop.util.NetworkUtils;

import static com.google.gson.internal.$Gson$Preconditions.checkNotNull;

class DisplayPostPresenter extends FeedPostPresenter implements
    DisplayPostContract.Presenter {

    private DisplayPostInteractor mInteractor;
    private DisplayPostContract.View mView;

    DisplayPostPresenter(@NonNull DisplayPostContract.View view, NetworkUtils network) {
        mInteractor = new DisplayPostInteractor(this, network);
        mView = checkNotNull(view);
    }

    public void onBindViewHolderAtPosition(PostCommentContract.View.ViewHolder viewHolderInterface, int i) {

    }

    public void onBindViewHolderAtPosition(FeedPostContract.View.ViewHolder viewHolderInterface, int i) {

    }

    @Override
    public void addPostComment(String currentUserId, String commentText, String activityId) {
        mInteractor.addPostComment(currentUserId, commentText, activityId);
    }
}
