package ca.gc.inspection.scoop.createofficialpost;

import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.ArrayAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import ca.gc.inspection.scoop.base.BasePresenter;
import ca.gc.inspection.scoop.createpost.CreatePostPresenter;
import ca.gc.inspection.scoop.createpost.InteractorBundle;
import ca.gc.inspection.scoop.createpost.PostRequestReceiver;
import ca.gc.inspection.scoop.searchbuilding.BuildingListReceiver;
import ca.gc.inspection.scoop.util.NetworkUtils;

import static com.google.gson.internal.$Gson$Preconditions.checkNotNull;

public class CreateOfficialPostPresenter implements
        CreateOfficialPostContract.Presenter,
        CreateOfficialPostContract.Presenter.BuildingAdapterAPI,
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
    // HashMap where the key is a list containing the english and french text
    private HashMap<List<String>, Integer> reasonsMap = new HashMap<>();
    private HashMap<List<String>, Integer> actionsMap = new HashMap<>();
    private Integer mBuildingId;

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
    public void sendPostToDatabase(NetworkUtils network, String postTitle, String building, String reasonForClosure, String actionRequired) {
        int buildingId = -1;
        int reasonId = -1;
        int actionId = -1;

        if (buildingsMap.containsKey(building)) {
            buildingId = buildingsMap.get(building);
        }

        if (buildingId == -1) {
            mView.displayInvalidInputError();
        }
//        mInteractor.sendPostToDatabase(network, postTitle, buildingId, reasonId, actionId);
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
        mInteractor.getAllReasons(network);
        mInteractor.getAllActions(network);
    }

    @Override
    public void setCurrentBuilding(String buildingName) {
        mBuildingId = buildingsMap.get(buildingName);
        Log.d("CreateOfficialPostPresenter", "current building:" + buildingName + ", id:" + mBuildingId);
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
//                    Log.d("CreateOfficialPostPresenter", "building: "+jsonBuilding.getString("building"));
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        mView.setBuildingsData(buildingsMap.keySet());
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
                            jsonReason.getInt(OFFICIAL_POST_BCP_REASON_ID_KEY));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        mView.setReasonsData(getEnglishWords(reasonsMap));
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
                    actionsMap.put(Arrays.asList(
                            jsonAction.getString(OFFICIAL_POST_BCP_ACTION_NAME_EN_KEY),
                            jsonAction.getString(OFFICIAL_POST_BCP_ACTION_NAME_FR_KEY)),
                            jsonAction.getInt(OFFICIAL_POST_BCP_ACTION_ID_KEY));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        mView.setActionsData(getEnglishWords(actionsMap));
    }

    private Set<String> getEnglishWords(HashMap<List<String>, Integer> map) {
        Set<String> setOfEnglishWords = new HashSet<>();
        for (List<String> englishAndFrenchTuple: map.keySet()) {
            setOfEnglishWords.add(englishAndFrenchTuple.get(0));
        }
        Log.d("CreateOfficialPostPresenter", setOfEnglishWords.toString());
        return setOfEnglishWords;
    }
}
