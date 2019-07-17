package ca.gc.inspection.scoop.searchpost.presenter;

import android.util.Log;

/**
 * Parses the user's query into a format used by the Interactor/database.
 */
public class SearchQuery {

    private static final String TAG = "SearchQuery";
    private String mParsedQuery = null;
    private String[] mWords = null;

    /**
     * Parse the query in the constructor
     * @param query
     */
    public SearchQuery(String query) {
        if (query != null) {
            StringBuilder parsedQuery = new StringBuilder();
            mWords = query.split("(\\s|\\.|,)+");
            for (int i = 0; i < mWords.length; i++) {
                mWords[i] = mWords[i].replaceAll("[^A-Za-z0-9\'\\-]", "");
                String queryWord = mWords[i].replaceAll("['\\-]", "");
                if (!queryWord.isEmpty()) {
                    if (parsedQuery.length() > 0)
                        parsedQuery.append(" | ");
                    parsedQuery.append(queryWord);
                }
            }
            mParsedQuery = parsedQuery.toString();
        }
    }

    public String getParsedQuery() {
        return mParsedQuery;
    }

    public String[] getQueryWords() {
        return mWords;
    }

    /**
     * Scores how relevant a text is to the user's query
     * @param weighting     The type of weighting
     * @param text          The text to be scored
     * @return
     */
    public double getNumberOfMatchesWeightedBy(MatchedWordWeighting weighting, String text) {
        double matches = 0;
        if (text != null && text.length() > 0)
            for (String mWord : mWords) {
                if (mWord != null && mWord.length() > 0) {
                    String temp = text.toLowerCase().replace(mWord.toLowerCase(), "");
                    int wordMatches = (text.length() - temp.length()) / mWord.length();
                    // by default, the weight of each word scales linearly with its length
                    double wordMatchWeight = mWord.length();

                    if (weighting == MatchedWordWeighting.UNIQUE) {
                        matches += Math.min(1, wordMatches) * wordMatchWeight;   // each matched word in user's query can only be counted once
                    }
                    else if (weighting == MatchedWordWeighting.LOGARITHMIC) {
                        /*
                        The weight of each word scales logarithmically with the length of the word to provide diminishing returns on the length of the matched word
                        The number of words counted also scales logarithmically to provide diminishing returns on multiple occurrences of the same word
                         */
                        wordMatchWeight = Math.log(mWord.length() + 1);
                        matches += Math.log(wordMatches + 1) * wordMatchWeight;     // log base e
                    }
                    else {
                        matches += wordMatches * wordMatchWeight;
                    }
                    Log.d(TAG, "wordMatches = " + wordMatches + " for " + mWord);
                }
            }
        Log.d(TAG, "getNumberOfMatchesWeightedBy = " + matches);
        return matches;
    }
}