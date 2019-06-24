package ca.gc.inspection.scoop.util;

public class StringUtils {
    /**
     * Capitalize first letter of given String and changes all other characters in the
     * String to lowercase
     * @param word Given string to be cleaned
     * @return String that has been cleaned (Uppercase first letter, rest lowercase)
     */
    public static String capitalizeFirstLetter(String word) {
        char ch[] = word.toCharArray();
        for (int i = 0; i < word.length(); i++) {

            // If first character of a word is found
            if ((i == 0 && ch[i] != ' ') || (ch[i] != ' ' && ch[i - 1] == ' ')) {
                // If it is in lower-case
                if (ch[i] >= 'a' && ch[i] <= 'z') {
                    // Convert into Upper-case
                    ch[i] = (char)(ch[i] - 'a' + 'A');
                }
            }
            // If apart from first character
            // Any one is in Upper-case
            else if (ch[i] >= 'A' && ch[i] <= 'Z')
                // Convert into Lower-Case
                ch[i] = (char)(ch[i] + 'a' - 'A');
        }
        // Convert the char array to equivalent String
        return new String(ch);
    }
}
