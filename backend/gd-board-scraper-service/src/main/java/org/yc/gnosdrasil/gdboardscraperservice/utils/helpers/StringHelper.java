package org.yc.gnosdrasil.gdboardscraperservice.utils.helpers;

import lombok.extern.slf4j.Slf4j;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
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

    /**
     * Apply a regex pattern to extract specific content
     */
    public String applyRegexPattern(String input, String regexPattern, String defaultValue) {
        if (input == null || regexPattern == null) {
            return defaultValue;
        }

        try {
            Pattern pattern = Pattern.compile(regexPattern);
            Matcher matcher = pattern.matcher(input);

            if (matcher.find()) {
                // Return the first capturing group, or the entire match if no groups
                return matcher.groupCount() > 0 ? matcher.group(1) : matcher.group();
            }
        } catch (Exception e) {
            log.debug("Error applying regex pattern: {}", regexPattern, e);
        }

        return defaultValue;
    }
}
