package ca.gc.inspection.scoop.searchprofile.presenter;

import android.support.annotation.NonNull;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Objects;

import ca.gc.inspection.scoop.searchprofile.SearchProfileContract;
import ca.gc.inspection.scoop.search.MatchedWordWeighting;
import ca.gc.inspection.scoop.search.SearchQuery;
import ca.gc.inspection.scoop.util.NetworkUtils;

import static com.google.gson.internal.$Gson$Preconditions.checkNotNull;


/**
 * Presenter for viewing a profile post.
 * Inherits from ProfileCommentPresenter to extend the method for binding data.
 * Implements the AdapterAPI and ViewHolderAPI to allow adapter and viewHolder to communicate with
 * the presenter.
 */
public class SearchProfilePresenter implements
        SearchProfileContract.Presenter,
        SearchProfileContract.Presenter.AdapterAPI,
        SearchProfileContract.Presenter.ViewHolderAPI {

    private static final String TAG = "SearchProfilePresenter";

    @NonNull
    private SearchProfileContract.View mSearchProfileView;
    private SearchProfileContract.View.Adapter mAdapter;
    private SearchProfileInteractor mSearchProfileInteractor;
    protected ProfileDataCache mDataCache;
    private boolean refreshingData = false;
    private SearchQuery currentSearchQuery;
    private String refreshRequestedWhileRefreshing = null;

    private SearchProfile getItemByIndex(int i) {
        if (mDataCache == null)
            return null;
        return mDataCache.getSearchProfileByIndex(i);
    }


    /**
     * Empty constructor called by child classes (ie. FeedPostPresenter) to allow them to create
     * their own View and Interactor objects
     */
    protected SearchProfilePresenter() {
    }

    public SearchProfilePresenter(@NonNull SearchProfileContract.View viewInterface, NetworkUtils network){

        setView(viewInterface);
        setInteractor(new SearchProfileInteractor(this, network));

    }

    public void setView(@NonNull SearchProfileContract.View viewInterface) {
        mSearchProfileView = checkNotNull(viewInterface);
    }

    /**
     * set parent interactor as a casted down version without the parent creating a new object
     * @param interactor
     */
    public void setInteractor(@NonNull SearchProfileInteractor interactor) {
        mSearchProfileInteractor = checkNotNull(interactor);
    }

    @Override
    public void setAdapter(SearchProfileContract.View.Adapter adapter) {
        mAdapter = adapter;
    }

    /**
     * Tries to load the searched posts from the database. If the Presenter is already waiting for
     * a database response, set refreshRequestedWhileRefreshing to the latest search query so
     * that it will be run when the network is available.
     *
     * @param search
     */
    @Override
    public void loadDataFromDatabase(String search) {
        if (!refreshingData) {
            Log.d(TAG, "loadDataFromDatabase = " + ", " + search);
            refreshingData = true;
            refreshRequestedWhileRefreshing = null;

            if (mDataCache == null)
                mDataCache = ProfileDataCache.createWithType(SearchProfile.class);
            else mDataCache.getSearchProfileList().clear();

            currentSearchQuery = new SearchQuery(search);
            String parsedQuery = currentSearchQuery.getParsedQuery();
            Log.d(TAG, "parsed query: "+parsedQuery);
            if (parsedQuery != null && !parsedQuery.isEmpty())
                mSearchProfileInteractor.getSearchProfile(parsedQuery);
        }
        else {
            refreshRequestedWhileRefreshing = search;
            Log.d(TAG, "refreshRequestedWhileRefreshing = " + ", " + search);
        }
    }

    public void setData(JSONArray response) {

        for (int i=0; i<response.length(); i++) {
            JSONObject jsonProfile = null;
            try {
                jsonProfile = response.getJSONObject(i);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            SearchProfile searchProfile = new SearchProfile(jsonProfile);
            searchProfile.setRelevance(currentSearchQuery, MatchedWordWeighting.LOGARITHMIC);
            searchProfile.setFormatForSearchQuery(currentSearchQuery);
            mDataCache.getSearchProfileList().add(searchProfile);
        }
        sortDataCacheByRelevance();
        mAdapter.refreshAdapter();
        mSearchProfileView.onLoadedDataFromDatabase();     // Clear the refreshing circle from the view
        mSearchProfileView.setResultsInfo(getItemCount());
        refreshingData = false;

        // Reload data from the database if a query was made while the network was busy
        if (refreshRequestedWhileRefreshing != null) {
            String search = refreshRequestedWhileRefreshing;
            refreshRequestedWhileRefreshing = null;
            loadDataFromDatabase(search);
        }
    }

    @Override
    public void onBindViewHolderAtPosition(SearchProfileContract.View.ViewHolder viewHolderInterface, int i) {
        SearchProfile searchProfile = getItemByIndex(i);
        bindSearchProfileDataToViewHolder(viewHolderInterface, searchProfile);
    }

    public static void bindSearchProfileDataToViewHolder(
            SearchProfileContract.View.ViewHolder viewHolderInterface, SearchProfile searchProfile) {
        if (searchProfile != null) {
            viewHolderInterface
                    .setFullNameWithFormat(searchProfile.getFullName(), searchProfile.getFullNameFormat())
                    .setPositionWithFormat(searchProfile.getPosition(), searchProfile.getPositionFormat())
                    .setDivisionWithFormat(searchProfile.getDivision(), searchProfile.getDivisionFormat())
                    .setLocationWithFormat(searchProfile.getValidLocation(), searchProfile.getLocationFormat())
                    .setUserImageFromString(searchProfile.getProfileImageString());
        }
    }

    /**
     * Gets the number of items in the DataCache
     * @return the count
     */
    @Override
    public int getItemCount() {
        if (mDataCache == null)
            return 0;
        return mDataCache.getItemCount();
    }

    private void sortDataCacheByRelevance() {
        mDataCache.getSearchProfileList().sort(
                (item1, item2) -> Double.compare(item2.getRelevance(), item1.getRelevance()));
    }

    @Override
    public String getProfileUserIdByIndex(int i) {
        return Objects.requireNonNull(getItemByIndex(i)).getProfileUserId();
    }
}
