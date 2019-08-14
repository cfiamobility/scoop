package ca.gc.inspection.scoop.createofficialpost;

import android.graphics.Bitmap;
import android.support.annotation.NonNull;

import java.util.Set;

import ca.gc.inspection.scoop.base.BasePresenter;
import ca.gc.inspection.scoop.base.BaseView;
import ca.gc.inspection.scoop.createpost.CreatePostContract;
import ca.gc.inspection.scoop.createpost.InteractorBundle;
import ca.gc.inspection.scoop.util.NetworkUtils;

/**
 * Defines the interaction between the View and Presenter for the Create Official BCP Post action case.
 */
public interface CreateOfficialPostContract {

    interface View extends BaseView<Presenter> {

        void setUserProfileImage(Bitmap profileImageBitmap);

        void onDatabaseResponse(boolean success);

        void setBuildingsData(Set<String> buildings);

        void setReasonsData(Set<String> reasons);

        void setActionsData(Set<String> reasons);

        void displayInvalidInputError();

        interface BuildingAdapter {

        }
    }

    interface Presenter extends BasePresenter {

        void sendPostToDatabase(NetworkUtils instance, String postTitle, String building, String reasonForClosure, String actionRequired);

        void onDatabaseResponse(boolean success, InteractorBundle interactorBundle);

        void getUserProfileImage(NetworkUtils instance);

        void loadDataFromDatabase(NetworkUtils network);

        interface BuildingAdapterAPI {

            void setCurrentBuilding(String buildingName);

        }
    }
}
