package ca.gc.inspection.scoop.searchpeople.presenter;

import android.util.Log;

import org.json.JSONObject;

import ca.gc.inspection.scoop.search.MatchedWordWeighting;
import ca.gc.inspection.scoop.search.SearchQuery;
import ca.gc.inspection.scoop.util.TextFormat;

import static ca.gc.inspection.scoop.postcomment.PostComment.PROFILE_COMMENT_FIRST_NAME_KEY;
import static ca.gc.inspection.scoop.postcomment.PostComment.PROFILE_COMMENT_LAST_NAME_KEY;

public class SearchPeople {

    JSONObject mProfile;
    private static final String TAG = "SearchPost";
    private static final String RELEVANCE_LABEL = "relevance: ";
    private static final double SEARCH_POST_NAME_WEIGHT_MULTIPLIER = 2;

    public static final String SEARCH_PROFILE_POSITION_KEY = "position";
    public static final String SEARCH_PROFILE_DIVISION_KEY = "division_en";
    public static final String SEARCH_PROFILE_ADDRESS_KEY = "address";
    public static final String SEARCH_PROFILE_CITY_KEY = "city";
    public static final String SEARCH_PROFILE_PROVINCE_KEY = "province";

    private double mRelevance = 0;
    private TextFormat mNameFormat = null;
    private TextFormat mPositionFormat = null;
    private TextFormat mDivisionFormat = null;
    private TextFormat mLocationFormat = null;

    protected SearchPeople(JSONObject profileData) {
        mProfile = profileData;
    }

    public void setFormatForSearchQuery(SearchQuery searchQuery) {
        String relevanceFooter = RELEVANCE_LABEL + getRelevance();
        mNameFormat = new TextFormat(searchQuery.getQueryWords(), getValidFullName(), null);
        mPositionFormat = new TextFormat(searchQuery.getQueryWords(), getPosition(), null);
        mDivisionFormat = new TextFormat(searchQuery.getQueryWords(), getDivision(), null);
        mLocationFormat = new TextFormat(searchQuery.getQueryWords(), getValidLocation(), relevanceFooter);
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
                        searchQuery.getNumberOfMatchesWeightedBy(weighting, getValidFullName())
                                * SEARCH_POST_NAME_WEIGHT_MULTIPLIER +
                        searchQuery.getNumberOfMatchesWeightedBy(weighting,
                                getPosition() + getDivision() + getValidLocation())
                )
        )/100;
        Log.d(TAG, "relevance = " + mRelevance);
    }

    public double getRelevance() {
        return mRelevance;
    }

    public String getFirstName() {
        try {
            return mProfile.getString(PROFILE_COMMENT_FIRST_NAME_KEY);
        }
        catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public String getLastName() {
        try {
            return mProfile.getString(PROFILE_COMMENT_LAST_NAME_KEY);
        }
        catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    /**
     * Description: returns a valid full name format.
     * Add spacing only if both first and last names are non empty
     */
    public String getValidFullName() {
        if (!getFirstName().equals("") && !getLastName().equals(""))
            return getFirstName() + " " + getLastName();
        return getFirstName() + getLastName();
    }

    public String getPosition() {
        try {
            return mProfile.getString(SEARCH_PROFILE_POSITION_KEY);
        }
        catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public String getDivision() {
        try {
            return mProfile.getString(SEARCH_PROFILE_DIVISION_KEY);
        }
        catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public String getAddress() {
        try {
            return mProfile.getString(SEARCH_PROFILE_ADDRESS_KEY);
        }
        catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public String getCity() {
        try {
            return mProfile.getString(SEARCH_PROFILE_CITY_KEY);
        }
        catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public String getProvince() {
        try {
            return mProfile.getString(SEARCH_PROFILE_PROVINCE_KEY);
        }
        catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    /**
     * Description: returns a valid location format.
     * Add spacing only if both first and last names are non empty
     */
    public String getValidLocation() {
        StringBuilder location = new StringBuilder();
        String spacing = "";

        location.append(getAddress());

        if (!getCity().isEmpty()) {
            location.append(spacing);
            location.append(getCity());
        }

        if (!getProvince().isEmpty()) {
            location.append(spacing);
            location.append(getProvince());
        }
        return location.toString();
    }
}
