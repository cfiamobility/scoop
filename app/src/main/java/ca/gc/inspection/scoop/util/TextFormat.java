package ca.gc.inspection.scoop.util;

import android.graphics.Color;
import android.util.Log;
import android.util.Pair;

import java.util.ArrayList;
import java.util.List;

/**
 * Presenter layer object which specifies how text should be formatted. Used in the post/comment and
 * search profile ViewHolders to highlight specific words. Also contains an italicized footer at
 * the end of the text body.
 */
public class TextFormat {
    private static final String TAG = "TextFormat";

    public static final int POST_TEXT_FORMAT_BOLD_COLOUR = Color.BLACK;
    public static final int POST_TEXT_FORMAT_HIGHLIGHT_COLOUR = Color.YELLOW;
    public static final String TEXT_FORMAT_FOOTER_SEPARATOR = "\n\n";
    private String[] mBoldedWords;
    private List<Pair<Integer, Integer>> mBoldTextPositions;
    private String mFooter;

    public TextFormat(String[] boldedWords, String text, String footer) {
        mBoldTextPositions = new ArrayList<>();
        setFooter(footer);
        setBoldTextPositions(boldedWords, text);
    }

    /**
     * PostComment TextFormat does not highlight/bold any text. This helper method allows the subclass
     * SearchPost to manually set the bold text positions.
     *
     * @param boldedWords   List of words to be highlighted/bolded
     * @param text          to be searched for boldedwords
     * @return              List of Pairs containing the start and end index in the post text to be highlighted
     */
    public TextFormat setBoldTextPositions(String[] boldedWords, String text) {
        mBoldedWords = boldedWords;
        if (mBoldedWords != null) {
            mBoldTextPositions = new ArrayList<>();

            for (String boldWord : mBoldedWords) {
                if (boldWord != null && !boldWord.isEmpty()) {
                    int wordStart = 0;
                    int wordEnd = -1;
                    while (wordStart >= 0) {
                        wordStart = text.toLowerCase().indexOf(boldWord.toLowerCase(), wordEnd + 1);
                        if (wordStart >= 0) {
                            wordEnd = wordStart + boldWord.length();
                            mBoldTextPositions.add(Pair.create(wordStart, wordEnd));
                            Log.d(TAG + ".setBoldTextPositions", boldWord + " at index: " + wordStart + ", " + wordEnd);
                        }
                    }
                } else Log.d(TAG + ".setBoldTextPositions", "empty word!");
            }
        }
        return this;
    }

    /**
     * Used by the ViewHolder for highlighting the relevant text
     * @return List of Pairs containing the start and end index in the post text to be highlighted
     */
    public List<Pair<Integer, Integer>> getBoldTextPositions() {
        return mBoldTextPositions;
    }

    public String getFooter() {
        return mFooter;
    }

    public TextFormat setFooter(String footer) {
        mFooter = footer;
        return this;
    }

    /**
     * Allows Presenter layer to add another line to the italicized footer message
     * @param footer        Text to be appended
     * @param separator     Usually a newline
     * @return
     */
    public TextFormat appendFooter(String footer, String separator) {
        if (mFooter == null) {
            mFooter = footer;
        }
        else {
            mFooter = mFooter + separator + footer;
        }
        return this;
    }
}
