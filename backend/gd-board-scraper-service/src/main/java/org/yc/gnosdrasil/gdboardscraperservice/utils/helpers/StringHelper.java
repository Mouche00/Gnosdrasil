package org.yc.gnosdrasil.gdboardscraperservice.utils.helpers;

public class StringHelper {

    /**
     * Splits given text by given separator and returns array of sub strings.
     *
     * @param text       the text to be split
     * @param separator  the separator to be used for splitting
     * @return           array of sub strings
     */
    public static String[] splitBySeparator(String text, String separator) {
        return text.split(separator);
    }

    /**
     * Returns the last sub string from given text after splitting it by given separator.
     *
     * @param text       the text to be split
     * @param separator  the separator to be used for splitting
     * @return           the last sub string
     */
    public static String getLastSubString(String text, String separator) {
        String[] subStrings = splitBySeparator(text, separator);
        return subStrings[subStrings.length - 1];
    }
}
