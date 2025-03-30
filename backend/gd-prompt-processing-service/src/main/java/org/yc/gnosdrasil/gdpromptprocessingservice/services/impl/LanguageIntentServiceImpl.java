package org.yc.gnosdrasil.gdpromptprocessingservice.services.impl;

import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.pipeline.CoreSentence;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.yc.gnosdrasil.gdpromptprocessingservice.config.NLPProperties;
import org.yc.gnosdrasil.gdpromptprocessingservice.entity.LanguageIntent;
import org.yc.gnosdrasil.gdpromptprocessingservice.services.ExperienceLevelService;
import org.yc.gnosdrasil.gdpromptprocessingservice.services.LanguageIntentService;

import java.util.*;

import static org.yc.gnosdrasil.gdpromptprocessingservice.utils.helpers.StringHelper.findContainingClause;
import static org.yc.gnosdrasil.gdpromptprocessingservice.utils.helpers.StringHelper.splitIntoClauses;

@Slf4j
@Service
@RequiredArgsConstructor
public class LanguageIntentServiceImpl implements LanguageIntentService {
    private final NLPProperties nlpProperties;
    private final ExperienceLevelService experienceLevelService;

    @Override
    public List<LanguageIntent> extractLanguageIntents(List<CoreSentence> sentences) {
        Set<String> processedCombinations = new HashSet<>();
        List<LanguageIntent> intents = new ArrayList<>();
        Map<String, String> pronounMap = identifyPronounReferents(sentences);

        for (CoreSentence sentence : sentences) {
            String sentenceText = sentence.toString().toLowerCase();
            List<CoreLabel> tokens = sentence.tokens();
            List<String> clauses = splitIntoClauses(sentenceText);
            log.info("Sentence splitting result: {}", clauses);

            // Process direct language mentions
            processLanguageMentions(sentenceText, tokens, clauses, processedCombinations, intents);

            // Process pronoun references
            processPronounReferences(sentenceText, tokens, clauses, pronounMap, processedCombinations, intents);
        }

        log.info("Language intents extracted: {}", intents.stream().filter(LanguageIntent::isFocus).count());

        return intents;
    }

    private void processLanguageMentions(String sentenceText, List<CoreLabel> tokens, 
            List<String> clauses, Set<String> processedCombinations, List<LanguageIntent> intents) {
        nlpProperties.getProgrammingLanguages().stream()
                .filter(lang -> sentenceText.matches(".*\\b" + lang + "\\b.*"))
                .forEach(lang -> {
                    String level = experienceLevelService.determineExperienceLevel(sentenceText, lang, tokens);
                    String combination = lang + ":" + level;
                    
                    if (!processedCombinations.contains(combination)) {
                        String containingClause = findContainingClause(clauses, lang);
                        // Check both the containing clause and the next clause for learning context
                        boolean isFocus = isInLearningClause(containingClause, lang) || 
                                        (clauses.indexOf(containingClause) < clauses.size() - 1 && 
                                         isInLearningClause(clauses.get(clauses.indexOf(containingClause) + 1), lang));
                        intents.add(createLanguageIntent(lang, sentenceText, tokens, isFocus));
                        processedCombinations.add(combination);
                    }
                });
    }

    private void processPronounReferences(String sentenceText, List<CoreLabel> tokens,
            List<String> clauses, Map<String, String> pronounMap, Set<String> processedCombinations,
            List<LanguageIntent> intents) {
        pronounMap.entrySet().stream()
                .filter(entry -> sentenceText.contains(entry.getKey()))
                .forEach(entry -> {
                    String lang = entry.getValue();
                    String level = experienceLevelService.determineExperienceLevel(sentenceText, lang, tokens);
                    String combination = lang + ":" + level;
                    
                    if (!processedCombinations.contains(combination)) {
                        String containingClause = findContainingClause(clauses, entry.getKey());
                        // Check both the containing clause and the previous clause for learning context
                        boolean isFocus = isInLearningClause(containingClause, entry.getKey()) || 
                                        (clauses.indexOf(containingClause) > 0 && 
                                         isInLearningClause(clauses.get(clauses.indexOf(containingClause) - 1), lang));
                        intents.add(createLanguageIntent(lang, sentenceText, tokens, isFocus));
                        processedCombinations.add(combination);
                    }
                });
    }

    private boolean isInLearningClause(String clause, String target) {
        if (clause.isEmpty()) return false;
        log.info("Checking if {} is in learning clause {}", target, clause);
        
        // Check if the clause contains learning keywords and either the target or a pronoun
        boolean hasLearningKeyword = nlpProperties.getLearningKeywords().stream()
                .anyMatch(keyword -> clause.contains(keyword));
                
        if (!hasLearningKeyword) return false;
        
        // Check for direct language mention
        if (clause.contains(target)) return true;
        
        // Check for pronouns that might refer to the language
        List<String> pronouns = Arrays.asList("it", "this", "that", "these", "those");
        boolean hasPronoun = pronouns.stream()
                .anyMatch(pronoun -> clause.contains(pronoun));
                
        return hasPronoun;
    }

    private Map<String, String> identifyPronounReferents(List<CoreSentence> sentences) {
        Map<String, String> pronounMap = new HashMap<>();
        String lastMentionedLanguage = null;

        for (CoreSentence sentence : sentences) {
            for (CoreLabel token : sentence.tokens()) {
                if (token.tag().startsWith("PRP")) {
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