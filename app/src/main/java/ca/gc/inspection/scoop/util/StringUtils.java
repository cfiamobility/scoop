package ca.gc.inspection.scoop.util;

public class StringUtils {
    // Method to call for capitalizing the first letter of any string
    public static String capFirstLetter(String word) {
        String newWord;
        if (word.length() > 1) {
            newWord = word.substring(0, 1).toUpperCase() + word.substring(1);
            return newWord;
        } else if (word.length() == 1) {
            newWord = word.substring(0, 1).toUpperCase();
            return newWord;
        } else {
            return null;
        }
    }
}
