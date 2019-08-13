package ca.gc.inspection.scoop.createofficialpost;

import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.widget.ArrayAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import ca.gc.inspection.scoop.base.BasePresenter;
import ca.gc.inspection.scoop.createpost.CreatePostPresenter;
import ca.gc.inspection.scoop.createpost.InteractorBundle;
import ca.gc.inspection.scoop.createpost.PostRequestReceiver;
import ca.gc.inspection.scoop.searchbuilding.BuildingListReceiver;
import ca.gc.inspection.scoop.util.NetworkUtils;

import static com.google.gson.internal.$Gson$Preconditions.checkNotNull;

public class CreateOfficialPostPresenter implements
        CreateOfficialPostContract.Presenter,
        BuildingListReceiver,
        PostRequestReceiver {
    private static final String OFFICIAL_POST_BCP_REASON_ID_KEY = "reasonid";
    private static final String OFFICIAL_POST_BCP_REASON_NAME_EN_KEY = "reasonname_en";
    private static final String OFFICIAL_POST_BCP_REASON_NAME_FR_KEY = "reasonname_fr";

    private static final String OFFICIAL_POST_BCP_ACTION_ID_KEY = "actionid";
    private static final String OFFICIAL_POST_BCP_ACTION_NAME_EN_KEY = "actionname_en";
    private static final String OFFICIAL_POST_BCP_ACTION_NAME_FR_KEY = "actionname_fr";
    /**
     * Implements the Presenter in the CreateOfficialPostContract interface to follow MVP architecture.
     *
     */

    private CreateOfficialPostInteractor mInteractor;
    private CreateOfficialPostContract.View mView;
    private HashMap<String, Integer> buildingsMap = new HashMap<>();
    private HashMap<List<String>, Integer> reasonsMap, actionsMap = new HashMap<>();

    CreateOfficialPostPresenter(@NonNull CreateOfficialPostContract.View view) {
        setInteractor(new CreateOfficialPostInteractor(this));
        setView(view);
    }

    /**
     * @param view
     */
    public void setView(@NonNull CreateOfficialPostContract.View view) {
        mView = checkNotNull(view);
    }

    /**
     * @param interactor    Handles network access.
     */
    public void setInteractor(@NonNull CreateOfficialPostInteractor interactor) {
        mInteractor = checkNotNull(interactor);
    }

    /*** sendPostToDatabase
     * Simple Post request to store the newly created Post to the postcomment table
     *
     * @param network   An instance of the singleton class which encapsulates the RequestQueue
     */
    @Override
    public void sendPostToDatabase(NetworkUtils network, String postTitle, int buildingId, int reasonForClosure, int actionRequired) {
        mInteractor.sendPostToDatabase(network, postTitle, buildingId, reasonForClosure, actionRequired);
    }

    /**
     * Callback for the database response when creating an official BCP post.
     * Routes to the view to handle Android UI changes.
     *
     * @param success           True if the post was created
     * @param interactorBundle  Data class not used but must be present to implement PostRequestReceiver
     */
    @Override
    public void onDatabaseResponse(boolean success, InteractorBundle interactorBundle) {
        mView.onDatabaseResponse(success);
    }

    public void setUserProfileImage(Bitmap profileImage) {mView.setUserProfileImage(profileImage);}

    public void getUserProfileImage(NetworkUtils network) {mInteractor.getUserProfileImage(network);}

    /**
     * Loads the building data, reasons for closure, and action required options from the database
     */
    public void loadDataFromDatabase(NetworkUtils network) {
        mInteractor.getAllBuildings(network);
    }

    @Override
    public void setBuildingsData(JSONArray response) {
        for (int i = 0; i < response.length(); i++){
            JSONObject jsonBuilding = null;
            try {
                jsonBuilding = response.getJSONObject(i);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            try {
                if (jsonBuilding != null) {
                    buildingsMap.put(jsonBuilding.getString("building"), jsonBuilding.getInt("buildingid"));
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public void setBuildingClosureReasonsData(JSONArray response) {
        for (int i = 0; i < response.length(); i++){
            JSONObject jsonReason = null;
            try {
                jsonReason = response.getJSONObject(i);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            try {
                if (jsonReason != null) {
                    reasonsMap.put(Arrays.asList(
                                    jsonReason.getString(OFFICIAL_POST_BCP_REASON_NAME_EN_KEY),
                                    jsonReason.getString(OFFICIAL_POST_BCP_REASON_NAME_FR_KEY)),
                            jsonReason.getInt(OFFICIAL_POST_BCP_REASON_ID_KEY));                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public void setRequiredActionsData(JSONArray response) {
        for (int i = 0; i < response.length(); i++){
            JSONObject jsonAction = null;
            try {
                jsonAction = response.getJSONObject(i);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            try {
                if (jsonAction != null) {
                    reasonsMap.put(Arrays.asList(
                            jsonAction.getString(OFFICIAL_POST_BCP_ACTION_NAME_EN_KEY),
                            jsonAction.getString(OFFICIAL_POST_BCP_ACTION_NAME_FR_KEY)),
                            jsonAction.getInt(OFFICIAL_POST_BCP_ACTION_ID_KEY));                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
