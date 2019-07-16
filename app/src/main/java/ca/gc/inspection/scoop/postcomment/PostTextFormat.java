package ca.gc.inspection.scoop.postcomment;

import android.graphics.Color;
import android.util.Log;
import android.util.Pair;

import java.util.ArrayList;
import java.util.List;

public class PostTextFormat {
    public static final int POST_TEXT_FORMAT_BOLD_COLOUR = Color.BLUE;
    public static final int POST_TEXT_FORMAT_HIGHLIGHT_COLOUR = Color.WHITE;
    private String[] mBoldedWords;
    private List<Pair<Integer, Integer>> mBoldTextPositions;
    private String mFooter;

    public PostTextFormat(String[] boldedWords, String text, String footer) {
        mBoldTextPositions = new ArrayList<>();
        mBoldedWords = boldedWords;
        mFooter = footer;
        setBoldTextPositions(text);
    }

    private void setBoldTextPositions(String text) {
        for (String boldWord : mBoldedWords) {
            if (boldWord != null && !boldWord.isEmpty()) {
                int wordStart = 0;
                int wordEnd = -1;
                while (wordStart >= 0) {
                    wordStart = text.toLowerCase().indexOf(boldWord.toLowerCase(), wordEnd + 1);
                    if (wordStart >= 0) {
                        wordEnd = wordStart + boldWord.length();
                        mBoldTextPositions.add(Pair.create(wordStart, wordEnd));
                        Log.d("PostTextFormat", boldWord + " at index: " + wordStart + ", " + wordEnd);
                    }
                }
            }
            else Log.d("PostTextFormat", "empty word!");
        }
    }

    public List<Pair<Integer, Integer>> getBoldTextPositions() {
        return mBoldTextPositions;
    }

    public String getFooter() {
        return mFooter;
    }
}
