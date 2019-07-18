package ca.gc.inspection.scoop.searchpeople.presenter;

import android.support.annotation.NonNull;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Objects;

import ca.gc.inspection.scoop.searchpeople.SearchPeopleContract;
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
public class SearchPeoplePresenter implements
        SearchPeopleContract.Presenter,
        SearchPeopleContract.Presenter.AdapterAPI,
        SearchPeopleContract.Presenter.ViewHolderAPI {

    private static final String TAG = "SearchPeoplePresenter";

    @NonNull
    private SearchPeopleContract.View mSearchPeopleView;
    private SearchPeopleContract.View.Adapter mAdapter;
    private SearchPeopleInteractor mSearchPeopleInteractor;
    protected ProfileDataCache mDataCache;
    private boolean refreshingData = false;
    private SearchQuery currentSearchQuery;
    private String refreshRequestedWhileRefreshing = null;

    private SearchPeople getItemByIndex(int i) {
        if (mDataCache == null)
            return null;
        return mDataCache.getSearchPeopleByIndex(i);
    }


    /**
     * Empty constructor called by child classes (ie. FeedPostPresenter) to allow them to create
     * their own View and Interactor objects
     */
    protected SearchPeoplePresenter() {
    }

    public SearchPeoplePresenter(@NonNull SearchPeopleContract.View viewInterface, NetworkUtils network){

        setView(viewInterface);
        setInteractor(new SearchPeopleInteractor(this, network));

    }

    public void setView(@NonNull SearchPeopleContract.View viewInterface) {
        mSearchPeopleView = checkNotNull(viewInterface);
    }

    /**
     * set parent interactor as a casted down version without the parent creating a new object
     * @param interactor
     */
    public void setInteractor(@NonNull SearchPeopleInteractor interactor) {
        mSearchPeopleInteractor = checkNotNull(interactor);
    }

    @Override
    public void setAdapter(SearchPeopleContract.View.Adapter adapter) {
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
                mDataCache = ProfileDataCache.createWithType(SearchPeople.class);
            else mDataCache.getSearchPeopleList().clear();

            currentSearchQuery = new SearchQuery(search);
            String parsedQuery = currentSearchQuery.getParsedQuery();
            Log.d(TAG, "parsed query: "+parsedQuery);
            if (parsedQuery != null && !parsedQuery.isEmpty())
                mSearchPeopleInteractor.getSearchPeople(parsedQuery);
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
            SearchPeople searchPeople = new SearchPeople(jsonProfile);
            searchPeople.setRelevance(currentSearchQuery, MatchedWordWeighting.LOGARITHMIC);
            searchPeople.setFormatForSearchQuery(currentSearchQuery);
            mDataCache.getSearchPeopleList().add(searchPeople);
        }
        sortDataCacheByRelevance();
        mAdapter.refreshAdapter();
        mSearchPeopleView.onLoadedDataFromDatabase();     // Clear the refreshing circle from the view
        mSearchPeopleView.setResultsInfo(getItemCount());
        refreshingData = false;

        // Reload data from the database if a query was made while the network was busy
        if (refreshRequestedWhileRefreshing != null) {
            String search = refreshRequestedWhileRefreshing;
            refreshRequestedWhileRefreshing = null;
            loadDataFromDatabase(search);
        }
    }

    @Override
    public void onBindViewHolderAtPosition(SearchPeopleContract.View.ViewHolder viewHolderInterface, int i) {
        SearchPeople searchPeople = getItemByIndex(i);
        bindSearchPeopleDataToViewHolder(viewHolderInterface, searchPeople);
    }

    public static void bindSearchPeopleDataToViewHolder(
            SearchPeopleContract.View.ViewHolder viewHolderInterface, SearchPeople searchPeople) {
        if (searchPeople != null) {
            viewHolderInterface
                    .setFullNameWithFormat(searchPeople.getFullName(), searchPeople.getFullNameFormat())
                    .setPositionWithFormat(searchPeople.getPosition(), searchPeople.getPositionFormat())
                    .setDivisionWithFormat(searchPeople.getDivision(), searchPeople.getDivisionFormat())
                    .setLocationWithFormat(searchPeople.getValidLocation(), searchPeople.getLocationFormat())
                    .setUserImageFromString(searchPeople.getProfileImageString());
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
        mDataCache.getSearchPeopleList().sort(
                (item1, item2) -> Double.compare(item2.getRelevance(), item1.getRelevance()));
    }

    @Override
    public String getProfileUserIdByIndex(int i) {
        return Objects.requireNonNull(getItemByIndex(i)).getProfileUserId();
    }
}
