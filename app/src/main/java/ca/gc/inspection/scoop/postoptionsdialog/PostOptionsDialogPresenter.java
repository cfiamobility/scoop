package ca.gc.inspection.scoop.postoptionsdialog;

import android.support.annotation.NonNull;

import org.json.JSONObject;

import ca.gc.inspection.scoop.util.NetworkUtils;

import static com.google.gson.internal.$Gson$Preconditions.checkNotNull;

public class PostOptionsDialogPresenter implements PostOptionsDialogContract.Presenter {

    private PostOptionsDialogInteractor mInteractor;
    private PostOptionsDialogContract.View mView;

    PostOptionsDialogPresenter(@NonNull PostOptionsDialogContract.View view) {
        mInteractor = new PostOptionsDialogInteractor(this);
        mView = checkNotNull(view);
    }

    public void savePost(NetworkUtils network, final String posterId, final String userid){
        mInteractor.savePost(network, posterId, userid);
    }

//    public void loadDataFromDatabase(NetworkUtils network, String currentUser) {
//        // gets activityId
//        mInteractor.getSavePostData(network, currentUser);
//    }
//
//    public void getActivityId(String response){
//        savePostActivityId = response;
//    }



}
