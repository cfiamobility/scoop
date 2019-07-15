package ca.gc.inspection.scoop.searchpost;

import android.support.annotation.NonNull;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import ca.gc.inspection.scoop.postcomment.PostDataCache;
import ca.gc.inspection.scoop.profilepost.ProfilePostPresenter;
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

    SearchPostPresenter(@NonNull SearchPostContract.View viewInterface, NetworkUtils network){

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
            searchPost.formatBySearchQuery(currentSearchQuery);
            searchPost.setRelevance(currentSearchQuery, MatchedWordWeighting.LOGARITHMIC);
            mDataCache.getSearchPostList().add(searchPost);
        }

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
        // call bindPostCommentDataToViewHolder instead of bindProfileCommentDataToViewHolder as we are setting a different title
        bindProfilePostDataToViewHolder(viewHolderInterface, searchPost);
    }

    public enum MatchedWordWeighting {
        LINEAR, LOGARITHMIC, UNIQUE
    }

    public class SearchQueryParser {

        private String mParsedQuery = null;
        private String[] mWords = null;

        SearchQueryParser(String query) {
            if (query != null) {
                StringBuilder parsedQuery = new StringBuilder();
                mWords = query.split("\\s+");
                for (int i = 0; i < mWords.length; i++) {
                    mWords[i] = mWords[i].replaceAll("[^A-Za-z0-9]", "");
                    if (!(mWords[i] == null) && !mWords[i].isEmpty()) {
                        if (parsedQuery.length() > 0)
                            parsedQuery.append(" & ");
                        parsedQuery.append(mWords[i]);
                    }
                }
                mParsedQuery = parsedQuery.toString();
            }
        }

        public String getParsedQuery() {
            return mParsedQuery;
        }

        public double getNumberOfMatchesWeightedBy(MatchedWordWeighting weighting, String text) {
            double matches = 0;
            if (text != null && text.length() > 0)
                for (String mWord : mWords) {
                    if (mWord != null && mWord.length() > 0) {
                        String temp = text.replace(mWord, "");
                        int wordMatches = (text.length() - temp.length()) / mWord.length();

                        if (weighting == MatchedWordWeighting.UNIQUE)
                            matches += Math.min(1, wordMatches);   // each matched word in user's query can only be counted once
                        else if (weighting == MatchedWordWeighting.LOGARITHMIC)
                            matches += Math.log(wordMatches + 1);   // log base e
                        else
                            matches += wordMatches;
                    }
                }
            Log.d(TAG, "getNumberOfMatchesWeightedBy = " + matches);
            return matches;
        }

//        private String parseQuery(String query) {
//            if (query == null)
//                return null;
//            StringBuilder parsedQuery = new StringBuilder();
//            String[] words = query.split("\\s+");
//            for (int i = 0; i < words.length; i++) {
//                words[i] = words[i].replaceAll("[^A-Za-z0-9]", "");
//                if (!(words[i] == null) && !words[i].isEmpty()) {
//                    if (parsedQuery.length() > 0)
//                        parsedQuery.append(" <3> ");
//                    parsedQuery.append(words[i]);
//                }
//            }
//            return parsedQuery.toString();
//        }
    }
}
