package org.yc.gnosdrasil.gdpromptprocessingservice.services.impl;

import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.util.CoreMap;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.yc.gnosdrasil.gdpromptprocessingservice.config.NLPProperties;
import org.yc.gnosdrasil.gdpromptprocessingservice.entity.LanguageIntent;
import org.yc.gnosdrasil.gdpromptprocessingservice.services.ExperienceLevelService;
import org.yc.gnosdrasil.gdpromptprocessingservice.services.LanguageIntentService;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class LanguageIntentServiceImpl implements LanguageIntentService {
    private final NLPProperties nlpProperties;
    private final ExperienceLevelService experienceLevelService;

    @Override
    public List<LanguageIntent> extractLanguageIntents(List<CoreMap> sentences) {
        // Use a Set to track unique language-level combinations
        Set<String> processedCombinations = new HashSet<>();
        List<LanguageIntent> intents = new ArrayList<>();
        Map<String, String> pronounMap = identifyPronounReferents(sentences);
        log.info("Identified pronoun referents: {}", pronounMap);

        for (CoreMap sentence : sentences) {
            String sentenceText = sentence.toString().toLowerCase();
            List<CoreLabel> tokens = sentence.get(CoreAnnotations.TokensAnnotation.class);

            // Split sentence into clauses (roughly by conjunctions)
            List<String> clauses = splitIntoClauses(sentenceText);
            log.info("Split sentence into clauses: {}", clauses);

            // Look for programming languages in this sentence
            for (String lang : nlpProperties.getProgrammingLanguages()) {
                if (sentenceText.matches(".*\\b" + lang + "\\b.*")) {
                    String level = experienceLevelService.determineExperienceLevel(sentenceText, lang, tokens);
                    String combination = lang + ":" + level;
                    
                    if (!processedCombinations.contains(combination)) {
                        // Find which clause contains this language
                        String containingClause = findContainingClause(clauses, lang);
                        boolean isFocus = isLanguageInLearningClause(containingClause, lang);
                        log.info("lang {} has focus value: {}", lang, isFocus);
                        LanguageIntent intent = createLanguageIntent(lang, sentenceText, tokens, isFocus);
                        intents.add(intent);
                        processedCombinations.add(combination);
                    }
                }
            }

            // Check for pronouns that might refer to programming languages
            for (Map.Entry<String, String> entry : pronounMap.entrySet()) {
                if (sentenceText.contains(entry.getKey())) {
                    String lang = entry.getValue();
                    String level = experienceLevelService.determineExperienceLevel(sentenceText, lang, tokens);
                    String combination = lang + ":" + level;
                    
                    if (!processedCombinations.contains(combination)) {
                        // Find which clause contains this pronoun
                        String containingClause = findContainingClause(clauses, entry.getKey());
                        boolean isFocus = isPronounInLearningClause(containingClause, entry.getKey());
                        log.info("lang {} has focus value: {}", lang, isFocus);
                        LanguageIntent intent = createLanguageIntent(lang, sentenceText, tokens, isFocus);
                        intents.add(intent);
                        processedCombinations.add(combination);
                    }
                }
            }
        }

        return intents;
    }

    private List<String> splitIntoClauses(String sentence) {
        // Split by common conjunctions and punctuation
        return Arrays.asList(sentence.split("(?<=[,;]|\\s+(and|but|or|yet|so)\\s+)|(?=[,;]|\\s+(and|but|or|yet|so)\\s+)"));
    }

    private String findContainingClause(List<String> clauses, String target) {
        return clauses.stream()
                .filter(clause -> clause.matches(".*\\b" + target + "\\b.*"))
                .findFirst()
                .orElse("");
    }

    private boolean isLanguageInLearningClause(String clause, String language) {
        if (clause.isEmpty()) return false;
        log.info("Checking if {} is in learning clause {}", language, clause);

        // Check for experience-related keywords first
//        if (hasExperienceKeywords(clause)) {
//            log.info("{} has experience keywords", clause);
//            return false;
//        }

        // Check for learning keywords in the clause
        return nlpProperties.getLearningKeywords().stream()
                .anyMatch(keyword -> 
                    clause.contains(keyword) &&
                    // Ensure the language is mentioned in the same clause
                    clause.contains(language)
                );
    }

    private boolean isPronounInLearningClause(String clause, String pronoun) {
        if (clause.isEmpty()) return false;
        log.info("Checking if {} is in learning clause {}", pronoun, clause);

        // Check for experience-related keywords first
//        if (hasExperienceKeywords(clause)) {
//            log.info("{} has experience keywords", clause);
//            return false;
//        }

        // Check for learning keywords in the clause
        return nlpProperties.getLearningKeywords().stream()
                .anyMatch(keyword -> 
                    clause.contains(keyword) &&
                    // Ensure the pronoun is mentioned in the same clause
                    clause.contains(pronoun)
                );
    }

//    private boolean hasExperienceKeywords(String text) {
//        return Arrays.asList(
//                "experience", "experienced", "have experience", "worked with",
//                "used", "developed", "built", "created", "implemented",
//                "designed", "familiar", "know", "understand"
//        ).stream().anyMatch(text::contains);
//    }

    private Map<String, String> identifyPronounReferents(List<CoreMap> sentences) {
        Map<String, String> pronounMap = new HashMap<>();
        String lastMentionedLanguage = null;

        for (CoreMap sentence : sentences) {
            List<CoreLabel> tokens = sentence.get(CoreAnnotations.TokensAnnotation.class);
            
            for (CoreLabel token : tokens) {
                if (token.tag().startsWith("PRP")) { // Pronoun
                    String pronoun = token.word().toLowerCase();
                    if (lastMentionedLanguage != null) {
                        pronounMap.put(pronoun, lastMentionedLanguage);
                    }
                } else if (nlpProperties.getProgrammingLanguages().contains(token.word().toLowerCase())) {
                    lastMentionedLanguage = token.word().toLowerCase();
                }
            }
        }

        return pronounMap;
    }

    private LanguageIntent createLanguageIntent(String language, String sentence, List<CoreLabel> tokens, boolean isFocus) {
        LanguageIntent intent = new LanguageIntent();
        intent.setLang(language);
        intent.setLevel(experienceLevelService.determineExperienceLevel(sentence, language, tokens));
        intent.setFocus(isFocus);
        return intent;
    }
} 