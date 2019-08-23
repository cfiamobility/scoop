package ca.gc.inspection.scoop.search;

/**
 * When searching for people or posts, we use an enum to specify which algorithm we are using to
 * calculate the relevance score of results. The relevance score is used to sort the results.
 *
 * Currently set to LOGARITHMIC, should be included a search setting
 * (a set of defined constants beyond the MatchedWordWeighting).
 *
 * See setRelevance for how MatchedWordWeighting is incorporated into the relevance calculation.
 * NumberOfWeightedMatches calculation occurs in SearchQuery.getNumberOfMatchesWeightedBy
 */
public enum MatchedWordWeighting {
    /*
    The weight of each word scales linearly with its length.
    The number of words counted also scales linearly.
     */
    LINEAR,

    /*
    The weight of each word scales logarithmically with the length of the word to provide diminishing returns on the length of the matched word.
    The number of words counted also scales logarithmically to provide diminishing returns on multiple occurrences of the same word.
     */
    LOGARITHMIC,

    /*
    The weight of each word scales linearly with its length.
    Only unique word matches are counted.
     */
    UNIQUE
}