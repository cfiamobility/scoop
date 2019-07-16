package ca.gc.inspection.scoop.searchpost.presenter;

import android.util.Log;

import org.json.JSONObject;

import ca.gc.inspection.scoop.postcomment.PostTextFormat;
import ca.gc.inspection.scoop.profilepost.ProfilePost;

public class SearchPost extends ProfilePost {
    private static final String TAG = "SearchPost";
    private static final String RELEVANCE_LABEL = "relevance: ";
    private static final double LIKE_COUNT_LOGARITHMIC_WEIGHTING = 0.25;

    /**
     * Data class which stores information for a single search post.
     * Should only interact with the Presenter as this class is a helper data class.
     * - Not an inner class of Presenter to simplify inheritance.
     *
     * The data is stored in the Json format provided by the database.
     * Static string keys are used to access the relevant values in the Json objects
     *
     */
    private enum LikeCountWeighting {
        LINEAR, LOGARITHMIC
    }

    private double mRelevance = 0;
    private PostTextFormat mPostTextFormat = null;

    protected SearchPost(JSONObject jsonPost, JSONObject jsonImage) {
        super(jsonPost, jsonImage);
    }

    public void setFormatForSearchQuery(SearchQueryParser searchQueryParser) {
        String relevanceFooter = RELEVANCE_LABEL + getRelevance();
        mPostTextFormat = new PostTextFormat(searchQueryParser.getQueryWords(), getPostText(), relevanceFooter);
    }

    public PostTextFormat getFormat() {
        return mPostTextFormat;
    }

    public void setRelevance(SearchQueryParser searchQueryParser,
                             MatchedWordWeighting weighting) {
        double weightedLikeCount = getLikeCountWeightedBy(LikeCountWeighting.LOGARITHMIC);

        // Relevance based on a non-linear weighting of the number of matches and rating
        mRelevance = Math.ceil(
                (searchQueryParser.getNumberOfMatchesWeightedBy(weighting, getPostTitle() + getPostText()) + 1) *
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
