package org.yc.gnosdrasil.gdpromptprocessingservice.utils.helpers;

import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.List;

@Slf4j
public class StringHelper {

    public static List<String> splitIntoClauses(String sentence) {
        return Arrays.asList(sentence.split("(?<=[,;]|\\s+(and|but|or|yet|so)\\s+)|(?=[,;]|\\s+(and|but|or|yet|so)\\s+)"));
    }

    public static String findContainingClause(List<String> clauses, String target) {
        return clauses.stream()
                .filter(clause -> clause.matches(".*\\b" + target + "\\b.*"))
                .findFirst()
                .orElse("");
    }

    public static String findContainingClause(String sentence, String target) {
        return splitIntoClauses(sentence).stream()
                .filter(clause -> clause.matches(".*\\b" + target + "\\b.*"))
                .findFirst()
                .orElse("");
    }

    public static boolean containsAny(String text, Iterable<String> indicators) {
        log.info("Checking if {} contains any of {}", text, indicators);
        for (String indicator : indicators) {
            if (text.contains(indicator)) return true;
        }
        return false;
    }
}
