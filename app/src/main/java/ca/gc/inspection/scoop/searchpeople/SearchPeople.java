package ca.gc.inspection.scoop.searchpeople;

import android.util.Log;

import org.json.JSONObject;

import ca.gc.inspection.scoop.searchpost.presenter.MatchedWordWeighting;
import ca.gc.inspection.scoop.searchpost.presenter.SearchQuery;
import ca.gc.inspection.scoop.util.TextFormat;

public class SearchPeople {

    JSONObject mTextData, mImageData;
    private static final String TAG = "SearchPost";
    private static final String RELEVANCE_LABEL = "relevance: ";
    private static final double SEARCH_POST_NAME_WEIGHT_MULTIPLIER = 2;

    private double mRelevance = 0;
    private TextFormat mNameFormat = null;
    private TextFormat mPositionFormat = null;
    private TextFormat mDivisionFormat = null;
    private TextFormat mLocationFormat = null;

    protected SearchPeople(JSONObject textData, JSONObject imageData) {
        mTextData = textData;
        mImageData = imageData;
    }

    public void setFormatForSearchQuery(SearchQuery searchQuery) {
        String relevanceFooter = RELEVANCE_LABEL + getRelevance();
        mNameFormat = new TextFormat(searchQuery.getQueryWords(), getName(), null);
        mPositionFormat = new TextFormat(searchQuery.getQueryWords(), getPosition(), null);
        mDivisionFormat = new TextFormat(searchQuery.getQueryWords(), getDivision(), null);
        mLocationFormat = new TextFormat(searchQuery.getQueryWords(), getLocation(), relevanceFooter);
    }

    public TextFormat getNameFormat() {
        return mNameFormat;
    }

    public TextFormat getPositionFormat() {
        return mPositionFormat;
    }

    public TextFormat getDivisionFormat() {
        return mDivisionFormat;
    }

    public TextFormat getLocationFormat() {
        return mLocationFormat;
    }

    public void setRelevance(SearchQuery searchQuery,
                             MatchedWordWeighting weighting) {

        // Relevance based on a non-linear weighting of the number of and length of matched search words
        mRelevance = Math.ceil(
                (
                        searchQuery.getNumberOfMatchesWeightedBy(weighting, getName())
                                * SEARCH_POST_NAME_WEIGHT_MULTIPLIER +
                        searchQuery.getNumberOfMatchesWeightedBy(weighting,
                                getPosition() + getDivision() + getLocation())
                )
        )/100;
        Log.d(TAG, "relevance = " + mRelevance);
    }

    public double getRelevance() {
        return mRelevance;
    }

    // todo implement
    public String getName() {
        return "";
    }

    // todo implement
    public String getPosition() {
        return "";
    }

    // todo implement
    public String getDivision() {
        return "";
    }

    // todo implement
    public String getLocation() {
        return "";
    }
}
