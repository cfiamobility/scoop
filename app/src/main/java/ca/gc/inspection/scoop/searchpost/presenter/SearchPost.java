package ca.gc.inspection.scoop.searchpost.presenter;

import android.util.Log;

import org.json.JSONObject;

import ca.gc.inspection.scoop.search.MatchedWordWeighting;
import ca.gc.inspection.scoop.search.SearchQuery;
import ca.gc.inspection.scoop.util.TextFormat;
import ca.gc.inspection.scoop.profilepost.ProfilePost;

/**
 * Data class which stores information for a single search post.
 * Should only interact with the Presenter as this class is a helper data class.
 * - Not an inner class of Presenter to simplify inheritance.
 *
 * The data is stored in the Json format provided by the database.
 * Static string keys are used to access the relevant values in the Json objects
 *
 */
public class SearchPost extends ProfilePost {
    private static final String TAG = "SearchPost";
    private static final String RELEVANCE_LABEL = "Relevance: ";
    private static final double LIKE_COUNT_LOGARITHMIC_WEIGHTING = 0.25;
    // Weight of postTitle as a multiple of postText
    private static final double SEARCH_POST_TITLE_WEIGHT_MULTIPLIER = 2;


    private enum LikeCountWeighting {
        LINEAR, LOGARITHMIC
    }

    private double mRelevance = 0;
    private TextFormat mPostTitleFormat = null;

    protected SearchPost(JSONObject jsonPost, JSONObject jsonImage) {
        super(jsonPost, jsonImage);
    }

    public void setFormatForSearchQuery(SearchQuery searchQuery) {
        String relevanceFooter = RELEVANCE_LABEL + getRelevance();
        mTextFormat.setBoldTextPositions(searchQuery.getQueryWords(), getPostText())
                .appendFooter(relevanceFooter, "\n");
        mPostTitleFormat = new TextFormat(searchQuery.getQueryWords(), getPostTitle(), null);
    }

    public TextFormat getTitleFormat() {
        return mPostTitleFormat;
    }

    public TextFormat getTextFormat() {
        return mTextFormat;
    }

    /**
     * See MatchedWordWeighting documentation.
     *
     * To calculate relevance we compute the getNumberOfMatchesWeightedBy separately for the
     * postTitle and postText. postTitle is multiplied by SEARCH_POST_TITLE_WEIGHT_MULTIPLIER as
     * matching words in the title should have more significance.
     *
     * Finally, the relevance score is weighted by the weightedLikeCount. The like count itself is
     * also passed through a continuous logarithmic function which is defined for negative values.
     * This function provides diminishing returns on a large number of likes in either the positive
     * or negative direction.
     *
     * @param searchQuery
     * @param weighting
     */
    public void setRelevance(SearchQuery searchQuery,
                             MatchedWordWeighting weighting) {
        double weightedLikeCount = getLikeCountWeightedBy(LikeCountWeighting.LOGARITHMIC);

        // Relevance based on a non-linear weighting of the number of matches and rating
        mRelevance = Math.ceil(
                (
                        searchQuery.getNumberOfMatchesWeightedBy(weighting, getPostTitle())
                                * SEARCH_POST_TITLE_WEIGHT_MULTIPLIER +
                        searchQuery.getNumberOfMatchesWeightedBy(weighting, getPostText()) + 1
                ) *
                (weightedLikeCount + 1) * 100
        )/100;
        Log.d(TAG, "relevance = " + mRelevance);
    }

    private double getLikeCountWeightedBy(LikeCountWeighting likeCountWeighting) {
        int likeCount = Integer.parseInt(getLikeCount());
        if (likeCountWeighting == LikeCountWeighting.LOGARITHMIC) {
            // composite logarithmic function which is defined for negative input values
            if (likeCount >= 0)
                return Math.log(likeCount + 1) * LIKE_COUNT_LOGARITHMIC_WEIGHTING;
            else
                return - Math.log(-likeCount + 1) * LIKE_COUNT_LOGARITHMIC_WEIGHTING;
        }
        else
            return likeCount;
    }

    public double getRelevance() {
        return mRelevance;
    }
}
