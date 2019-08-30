package ca.gc.inspection.scoop.searchprofile.presenter;

import android.util.Log;

import org.json.JSONObject;

import ca.gc.inspection.scoop.search.MatchedWordWeighting;
import ca.gc.inspection.scoop.search.SearchQuery;
import ca.gc.inspection.scoop.util.TextFormat;

import static ca.gc.inspection.scoop.Config.USERID_KEY;
import static ca.gc.inspection.scoop.postcomment.PostComment.PROFILE_COMMENT_FIRST_NAME_KEY;
import static ca.gc.inspection.scoop.postcomment.PostComment.PROFILE_COMMENT_LAST_NAME_KEY;
import static ca.gc.inspection.scoop.postcomment.PostComment.PROFILE_COMMENT_PROFILE_IMAGE_KEY;

/**
 * Data class which stores information for a single user profile that was searched.
 * Should only interact with the Presenter as this class is a helper data class.
 * - Not an inner class of Presenter to simplify inheritance.
 *
 * The data is stored in the Json format provided by the database.
 * Static string keys are used to access the relevant values in the Json objects
 *
 */
public class SearchProfile {

    JSONObject mProfile;
    private static final String TAG = "SearchPost";
    /* When calculating relevance, this multiplier determines how much weight matching the name has
       over other fields such as position or location */
    private static final double SEARCH_POST_NAME_WEIGHT_MULTIPLIER = 2;

    public static final String SEARCH_PROFILE_POSITION_KEY = "positionname";
    public static final String SEARCH_PROFILE_DIVISION_KEY = "division_en";
    public static final String SEARCH_PROFILE_ADDRESS_KEY = "address";
    public static final String SEARCH_PROFILE_CITY_KEY = "city";
    public static final String SEARCH_PROFILE_PROVINCE_KEY = "province";

    private double mRelevance = 0;
    private TextFormat mFullNameFormat = null;
    private TextFormat mPositionFormat = null;
    private TextFormat mDivisionFormat = null;
    private TextFormat mLocationFormat = null;

    protected SearchProfile(JSONObject profileData) {
        mProfile = profileData;
    }

    public void setFormatForSearchQuery(SearchQuery searchQuery) {
        mFullNameFormat = new TextFormat(searchQuery.getQueryWords(), getFullName(), null);
        mPositionFormat = new TextFormat(searchQuery.getQueryWords(), getPosition(), null);
        mDivisionFormat = new TextFormat(searchQuery.getQueryWords(), getDivision(), null);
        mLocationFormat = new TextFormat(searchQuery.getQueryWords(), getValidLocation(), null);
    }

    public TextFormat getFullNameFormat() {
        return mFullNameFormat;
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

    /**
     * See SearchPost.setRelevance documentation
     *
     * @param searchQuery
     * @param weighting
     */
    public void setRelevance(SearchQuery searchQuery,
                             MatchedWordWeighting weighting) {

        // Relevance based on a non-linear weighting of the number of and length of matched search words
        mRelevance = Math.ceil(
                (
                        searchQuery.getNumberOfMatchesWeightedBy(weighting, getFullName())
                                * SEARCH_POST_NAME_WEIGHT_MULTIPLIER +
                        searchQuery.getNumberOfMatchesWeightedBy(weighting,
                                getPosition() + getDivision() + getValidLocation())
                ) * 100
        )/100;
        Log.d(TAG, "relevance = " + mRelevance);
    }

    public double getRelevance() {
        return mRelevance;
    }

    /**
     * @return userid of this profile (not necessarily the current user)
     */
    public String getProfileUserId() {
        try {
            return mProfile.getString(USERID_KEY);
        }
        catch (Exception e) {
            e.printStackTrace();
            return "";
        }
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
    public String getFullName() {
        if (!getFirstName().equals("") && !getLastName().equals(""))
            return getFirstName() + " " + getLastName();
        return getFirstName() + getLastName();
    }

    public String getPosition() {
        String position = "";
        try {
            position = mProfile.getString(SEARCH_PROFILE_POSITION_KEY);
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        if (position != null && !position.isEmpty() && !position.equals("null"))
            return position;
        else return "";
    }

    public String getDivision() {
        String division = "";
        try {
            division = mProfile.getString(SEARCH_PROFILE_DIVISION_KEY);
        }
        catch (Exception e) {
            e.printStackTrace();
        }


        if (division != null && !division.isEmpty() && !division.equals("null"))
            return division;
        else return "";
    }

    public String getAddress() {
        String address = "";
        try {
            address = mProfile.getString(SEARCH_PROFILE_ADDRESS_KEY);
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        if (address != null && !address.isEmpty() && !address.equals("null"))
            return address;
        else return "";
    }

    public String getCity() {
        String city = "";
        try {
            city = mProfile.getString(SEARCH_PROFILE_CITY_KEY);
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        if (city != null && !city.isEmpty() && !city.equals("null"))
            return city;
        else return "";
    }

    public String getProvince() {
        String province = "";
        try {
            province = mProfile.getString(SEARCH_PROFILE_PROVINCE_KEY);
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        if (province != null && !province.isEmpty() && !province.equals("null"))
            return province;
        else return "";
    }

    /**
     * Description: returns a valid location format.
     * Add spacing only if both first and last names are non empty
     */
    public String getValidLocation() {
        StringBuilder location = new StringBuilder();
        String spacing = "";

        location.append(getAddress());
        if (!getAddress().isEmpty())
            spacing = ", ";

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

    public String getProfileImageString() {
        try {
            return mProfile.getString(PROFILE_COMMENT_PROFILE_IMAGE_KEY);
        }
        catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }
}
