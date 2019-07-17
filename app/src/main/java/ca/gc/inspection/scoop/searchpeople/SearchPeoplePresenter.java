package ca.gc.inspection.scoop.searchpeople;

import android.support.annotation.NonNull;
import android.util.Log;
import android.util.Pair;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import ca.gc.inspection.scoop.postcomment.PostDataCache;
import ca.gc.inspection.scoop.profilepost.ProfilePostPresenter;
import ca.gc.inspection.scoop.searchpost.presenter.MatchedWordWeighting;
import ca.gc.inspection.scoop.searchpost.presenter.SearchPost;
import ca.gc.inspection.scoop.searchpost.presenter.SearchPostInteractor;
import ca.gc.inspection.scoop.searchpost.presenter.SearchQuery;
import ca.gc.inspection.scoop.searchpost.view.SearchPostContract;
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

    private static final String TAG = "SearchPostPresenter";

    @NonNull
    private SearchPeopleContract.View mSearchPostView;
    private SearchPeopleContract.View.Adapter mAdapter;
    private SearchPeopleInteractor mSearchPostInteractor;
    protected ProfileDataCache mDataCache;
    private boolean refreshingData = false;
    private SearchQuery currentSearchQuery;
    private Pair<String, String> refreshRequestedWhileRefreshing = null;

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
        mSearchPostView = checkNotNull(viewInterface);
    }

    /**
     * set parent interactor as a casted down version without the parent creating a new object
     * @param interactor
     */
    public void setInteractor(@NonNull SearchPeopleInteractor interactor) {
        mSearchPostInteractor = checkNotNull(interactor);
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
     * @param userId
     * @param search
     */
    @Override
    public void loadDataFromDatabase(String userId, String search) {
        if (!refreshingData) {
            Log.d(TAG, "loadDataFromDatabase = " + userId + ", " + search);
            refreshingData = true;
            refreshRequestedWhileRefreshing = null;

            if (mDataCache == null)
                mDataCache = ProfileDataCache.createWithType(SearchPost.class);
            else mDataCache.getSearchPeopleList().clear();

            currentSearchQuery = new SearchQuery(search);
            String parsedQuery = currentSearchQuery.getParsedQuery();
            Log.d(TAG, "parsed query: "+parsedQuery);
            if (parsedQuery != null && !parsedQuery.isEmpty())
                mSearchPostInteractor.getSearchPeople(userId, parsedQuery);
        }
        else {
            refreshRequestedWhileRefreshing = new Pair<>(userId, search);
            Log.d(TAG, "refreshRequestedWhileRefreshing = " + userId + ", " + search);
        }
    }

    @Override
    public void setData(JSONArray postsResponse, JSONArray imagesResponse) {

        if ((postsResponse.length() != imagesResponse.length()))
            Log.i(TAG, "length of postsResponse != imagesResponse");

        for (int i=0; i<postsResponse.length(); i++) {
            JSONObject jsonText = null;
            JSONObject jsonImage = null;
            try {
                jsonText = postsResponse.getJSONObject(i);
                jsonImage = imagesResponse.getJSONObject(i);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            SearchPeople searchPeople = new SearchPeople(jsonText, jsonImage);
            searchPeople.setRelevance(currentSearchQuery, MatchedWordWeighting.LOGARITHMIC);
            searchPeople.setFormatForSearchQuery(currentSearchQuery);
            mDataCache.getSearchPeopleList().add(searchPeople);
        }
        sortDataCacheByRelevance();
        mAdapter.refreshAdapter();
        mSearchPostView.onLoadedDataFromDatabase();     // Clear the refreshing circle from the view
        mSearchPostView.setResultsInfo(getItemCount());
        refreshingData = false;

        // Reload data from the database if a query was made while the network was busy
        if (refreshRequestedWhileRefreshing != null) {
            String userId = refreshRequestedWhileRefreshing.first;
            String search = refreshRequestedWhileRefreshing.second;
            refreshRequestedWhileRefreshing = null;
            loadDataFromDatabase(userId, search);
        }
    }

    @Override
    public void onBindViewHolderAtPosition(SearchPeopleContract.View.ViewHolder viewHolderInterface, int i) {
        SearchPeople searchPeople = getItemByIndex(i);
        // TODO implement
    }

    public static void bindSearchPostDataToViewHolder(
            SearchPeopleContract.View.ViewHolder viewHolderInterface, SearchPost searchPost) {
        // TODO implement
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
}
