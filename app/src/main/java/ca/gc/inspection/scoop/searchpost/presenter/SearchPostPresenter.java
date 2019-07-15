package ca.gc.inspection.scoop.searchpost.presenter;

import android.support.annotation.NonNull;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Collections;
import java.util.Comparator;

import ca.gc.inspection.scoop.postcomment.PostDataCache;
import ca.gc.inspection.scoop.profilepost.ProfilePostPresenter;
import ca.gc.inspection.scoop.searchpost.view.SearchPostContract;
import ca.gc.inspection.scoop.util.NetworkUtils;

import static com.google.gson.internal.$Gson$Preconditions.checkNotNull;


/**
 * Presenter for viewing a profile post.
 * Inherits from ProfileCommentPresenter to extend the method for binding data.
 * Implements the AdapterAPI and ViewHolderAPI to allow adapter and viewHolder to communicate with
 * the presenter.
 */
public class SearchPostPresenter extends ProfilePostPresenter implements
        SearchPostContract.Presenter,
        SearchPostContract.Presenter.AdapterAPI,
        SearchPostContract.Presenter.ViewHolderAPI {

    private static final String TAG = "SearchPostPresenter";

    @NonNull
    private SearchPostContract.View mSearchPostView;
    private SearchPostContract.View.Adapter mAdapter;
    private SearchPostInteractor mSearchPostInteractor;
    private boolean refreshingData = false;
    private SearchQueryParser currentSearchQuery;

    private SearchPost getItemByIndex(int i) {
        if (mDataCache == null)
            return null;
        return mDataCache.getSearchPostByIndex(i);
    }


    /**
     * Empty constructor called by child classes (ie. FeedPostPresenter) to allow them to create
     * their own View and Interactor objects
     */
    protected SearchPostPresenter() {
    }

    public SearchPostPresenter(@NonNull SearchPostContract.View viewInterface, NetworkUtils network){

        setView(viewInterface);
        setInteractor(new SearchPostInteractor(this, network));

    }

    public void setView(@NonNull SearchPostContract.View viewInterface) {
        mSearchPostView = checkNotNull(viewInterface);
    }

    /**
     * set parent interactor as a casted down version without the parent creating a new object
     * @param interactor
     */
    public void setInteractor(@NonNull SearchPostInteractor interactor) {
        super.setInteractor(interactor);
        mSearchPostInteractor = checkNotNull(interactor);
    }

    @Override
    public void setAdapter(SearchPostContract.View.Adapter adapter) {
        mAdapter = adapter;
    }

    @Override
    public void loadDataFromDatabase(String userId, String search) {
        if (!refreshingData) {
            refreshingData = true;

            if (mDataCache == null)
                mDataCache = PostDataCache.createWithType(SearchPost.class);
            else mDataCache.getSearchPostList().clear();

            currentSearchQuery = new SearchQueryParser(search);
            String parsedQuery = currentSearchQuery.getParsedQuery();
            Log.d(TAG, "parsed query: "+parsedQuery);
            if (parsedQuery != null && !parsedQuery.isEmpty())
                mSearchPostInteractor.getSearchPosts(userId, parsedQuery);
        }
    }

    @Override
    public void setData(JSONArray postsResponse, JSONArray imagesResponse) {

        if ((postsResponse.length() != imagesResponse.length()))
            Log.i(TAG, "length of postsResponse != imagesResponse");

        for (int i=0; i<postsResponse.length(); i++) {
            JSONObject jsonPost = null;
            JSONObject jsonImage = null;
            try {
                jsonPost = postsResponse.getJSONObject(i);
                jsonImage = imagesResponse.getJSONObject(i);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            SearchPost searchPost = new SearchPost(jsonPost, jsonImage);
            searchPost.setRelevance(currentSearchQuery, MatchedWordWeighting.LOGARITHMIC);
            searchPost.formatBySearchQuery(currentSearchQuery);
            mDataCache.getSearchPostList().add(searchPost);
        }
        sortDataCacheByRelevance();

        mAdapter.refreshAdapter();
        refreshingData = false;
    }

    @Override
    public void onBindViewHolderAtPosition(SearchPostContract.View.ViewHolder viewHolderInterface, int i) {
        SearchPost searchPost = getItemByIndex(i);
        bindSearchPostDataToViewHolder(viewHolderInterface, searchPost);
    }

    public static void bindSearchPostDataToViewHolder(
            SearchPostContract.View.ViewHolder viewHolderInterface, SearchPost searchPost) {
        // call bindPostCommentDataToViewHolder instead of bindProfilePostDataToViewHolder as we are setting a different title/text
        bindPostCommentDataToViewHolder(viewHolderInterface, searchPost);
        if (searchPost != null) {
            viewHolderInterface
                    .setPostTitle(searchPost.getPostTitle())
                    .setPostText(searchPost.getPostTextFormatted());
        }
    }

    private void sortDataCacheByRelevance() {
        mDataCache.getSearchPostList().sort(
                (item1, item2) -> Double.compare(item2.getRelevance(), item1.getRelevance()));
    }
}
