package ca.gc.inspection.scoop.searchpost;

import android.util.Log;

import org.json.JSONObject;

import ca.gc.inspection.scoop.profilepost.ProfilePost;

public class SearchPost extends ProfilePost {
    private static final String TAG = "SearchPost";

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

    private int mRelevance = 0;
    private String postTextFormatted = null;

    protected SearchPost(JSONObject jsonPost, JSONObject jsonImage) {
        super(jsonPost, jsonImage);
    }

    public void formatBySearchQuery(SearchPostPresenter.SearchQueryParser searchQueryParser) {

    }

    public void setRelevance(SearchPostPresenter.SearchQueryParser searchQueryParser,
                             SearchPostPresenter.MatchedWordWeighting weighting) {
        double weightedLikeCount = getLikeCountWeightedBy(LikeCountWeighting.LOGARITHMIC);

        // Relevance based on a non-linear weighting of the number of matches and rating
        mRelevance = (int) Math.ceil(
                (searchQueryParser.getNumberOfMatchesWeightedBy(weighting, getPostText()) + 1) *
                (weightedLikeCount + 1)
        );
        Log.d(TAG, "relevance = " + mRelevance);
    }

    private double getLikeCountWeightedBy(LikeCountWeighting likeCountWeighting) {
        int likeCount = Integer.parseInt(getLikeCount());
        if (likeCountWeighting == LikeCountWeighting.LOGARITHMIC) {
            // composite logarithmic function which is defined for negative input values
            if (likeCount >= 0)
                return Math.log(likeCount + 1);
            else
                return -Math.log(-likeCount + 1);
        }
        else
            return likeCount;
    }

    public int getRelevance() {
        return mRelevance;
    }


    public String getPostTextFormatted() {
        if (postTextFormatted != null && !postTextFormatted.isEmpty())
            return postTextFormatted;
        else
            return getPostText();
    }
}
