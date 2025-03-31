package org.yc.gnosdrasil.gdpromptprocessingservice.services.impl;

import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.pipeline.CoreSentence;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.yc.gnosdrasil.gdpromptprocessingservice.config.NLPProperties;
import org.yc.gnosdrasil.gdpromptprocessingservice.entity.LanguageIntent;
import org.yc.gnosdrasil.gdpromptprocessingservice.exception.NLPProcessingException;
import org.yc.gnosdrasil.gdpromptprocessingservice.services.ExperienceLevelService;
import org.yc.gnosdrasil.gdpromptprocessingservice.services.LanguageIntentService;
import org.yc.gnosdrasil.gdpromptprocessingservice.services.LearningContextService;
import org.yc.gnosdrasil.gdpromptprocessingservice.services.PronounService;

import java.util.*;

import static org.yc.gnosdrasil.gdpromptprocessingservice.utils.helpers.StringHelper.findContainingClause;
import static org.yc.gnosdrasil.gdpromptprocessingservice.utils.helpers.StringHelper.splitIntoClauses;

@Slf4j
@Service
@RequiredArgsConstructor
public class LanguageIntentServiceImpl implements LanguageIntentService {
    private static final Set<String> PRONOUNS = Set.of("it", "this", "that", "these", "those");
    
    private final NLPProperties nlpProperties;
    private final ExperienceLevelService experienceLevelService;
    private final PronounService pronounService;
    private final LearningContextService learningContextService;

    @Override
    public List<LanguageIntent> extractLanguageIntents(List<CoreSentence> sentences) {
        try {
            log.debug("Starting language intent extraction for {} sentences", sentences.size());
            Set<String> processedCombinations = new HashSet<>();
            List<LanguageIntent> intents = new ArrayList<>();
            Map<String, String> pronounMap = pronounService.identifyPronounReferents(sentences);

            sentences.forEach(sentence -> processSentence(sentence, processedCombinations, intents, pronounMap));

            log.info("Extracted {} language intents, {} with focus", 
                    intents.size(), 
                    intents.stream().filter(LanguageIntent::isFocus).count());
            return intents;
        } catch (Exception e) {
            log.error("Failed to extract language intents", e);
            throw new NLPProcessingException("Failed to extract language intents", e);
        }
    }

    private void processSentence(CoreSentence sentence, Set<String> processedCombinations, 
            List<LanguageIntent> intents, Map<String, String> pronounMap) {
        try {
            String sentenceText = sentence.toString().toLowerCase();
            List<CoreLabel> tokens = sentence.tokens();
            List<String> clauses = splitIntoClauses(sentenceText);
            
            log.debug("Processing sentence: {}", sentenceText);
            log.debug("Split into {} clauses", clauses.size());

            processLanguageMentions(sentenceText, tokens, clauses, processedCombinations, intents);
            processPronounReferences(sentenceText, tokens, clauses, pronounMap, processedCombinations, intents);
        } catch (Exception e) {
            log.error("Failed to process sentence: {}", sentence.toString(), e);
            throw new NLPProcessingException("Failed to process sentence", e);
        }
    }

    private void processLanguageMentions(String sentenceText, List<CoreLabel> tokens, 
            List<String> clauses, Set<String> processedCombinations, List<LanguageIntent> intents) {
        nlpProperties.getProgrammingLanguages().stream()
                .filter(lang -> sentenceText.matches(".*\\b" + lang + "\\b.*"))
                .forEach(lang -> processLanguage(lang, sentenceText, tokens, clauses, processedCombinations, intents));
    }

    private void processLanguage(String lang, String sentenceText, List<CoreLabel> tokens,
            List<String> clauses, Set<String> processedCombinations, List<LanguageIntent> intents) {
        try {
            String level = experienceLevelService.determineExperienceLevel(sentenceText, lang, tokens);
            String combination = lang + ":" + level;
            
            if (!processedCombinations.contains(combination)) {
                String containingClause = findContainingClause(clauses, lang);
                int clauseIndex = clauses.indexOf(containingClause);
                
                boolean isFocus = learningContextService.isLearningFocus(containingClause, lang, clauses, clauseIndex);
                
                log.debug("Language '{}' found with focus: {}", lang, isFocus);
                intents.add(createLanguageIntent(lang, sentenceText, tokens, isFocus));
                processedCombinations.add(combination);
            }
        } catch (Exception e) {
            log.error("Failed to process language: {}", lang, e);
            throw new NLPProcessingException("Failed to process language: " + lang, e);
        }
    }

    private void processPronounReferences(String sentenceText, List<CoreLabel> tokens,
            List<String> clauses, Map<String, String> pronounMap, Set<String> processedCombinations,
            List<LanguageIntent> intents) {
        pronounMap.entrySet().stream()
                .filter(entry -> sentenceText.contains(entry.getKey()))
                .forEach(entry -> processPronoun(entry, sentenceText, tokens, clauses, processedCombinations, intents));
    }

    private void processPronoun(Map.Entry<String, String> entry, String sentenceText, List<CoreLabel> tokens,
            List<String> clauses, Set<String> processedCombinations, List<LanguageIntent> intents) {
        try {
            String lang = entry.getValue();
            String level = experienceLevelService.determineExperienceLevel(sentenceText, lang, tokens);
            String combination = lang + ":" + level;
            
            if (!processedCombinations.contains(combination)) {
                String containingClause = findContainingClause(clauses, entry.getKey());
                int clauseIndex = clauses.indexOf(containingClause);
                
                boolean isFocus = learningContextService.isLearningFocus(containingClause, entry.getKey(), clauses, clauseIndex);
                
                log.debug("Pronoun '{}' refers to '{}' with focus: {}", entry.getKey(), lang, isFocus);
                intents.add(createLanguageIntent(lang, sentenceText, tokens, isFocus));
                processedCombinations.add(combination);
            }
        } catch (Exception e) {
            log.error("Failed to process pronoun reference: {}", entry.getKey(), e);
            throw new NLPProcessingException("Failed to process pronoun reference: " + entry.getKey(), e);
        }
    }

    private boolean isInLearningClause(String clause, String target) {
        if (clause.isEmpty()) return false;
        
        boolean hasLearningKeyword = nlpProperties.getLearningKeywords().stream()
                .anyMatch(keyword -> clause.contains(keyword));
                
        if (!hasLearningKeyword) return false;
        
        return clause.contains(target) || PRONOUNS.stream().anyMatch(clause::contains);
    }

    private Map<String, String> identifyPronounReferents(List<CoreSentence> sentences) {
        Map<String, String> pronounMap = new HashMap<>();
        String lastMentionedLanguage = null;

        try {
            for (CoreSentence sentence : sentences) {
                for (CoreLabel token : sentence.tokens()) {
                    if (token.tag().startsWith("PRP")) {
                        String pronoun = token.word().toLowerCase();
                        if (lastMentionedLanguage != null) {
                            pronounMap.put(pronoun, lastMentionedLanguage);
                            log.debug("Mapped pronoun '{}' to language '{}'", pronoun, lastMentionedLanguage);
                        }
                    } else if (nlpProperties.getProgrammingLanguages().contains(token.word().toLowerCase())) {
                        lastMentionedLanguage = token.word().toLowerCase();
                    }
                }
            }
            return pronounMap;
        } catch (Exception e) {
            log.error("Failed to identify pronoun referents", e);
            throw new NLPProcessingException("Failed to identify pronoun referents", e);
        }
    }

    private LanguageIntent createLanguageIntent(String language, String sentence, List<CoreLabel> tokens, boolean isFocus) {
        try {
            String level = experienceLevelService.determineExperienceLevel(sentence, language, tokens);
            LanguageIntent intent = new LanguageIntent();
            intent.setLang(language);
            intent.setLevel(level);
            intent.setFocus(isFocus);
            log.debug("Created language intent: language='{}', level='{}', focus={}", language, level, isFocus);
            return intent;
        } catch (Exception e) {
            log.error("Failed to create language intent for: {}", language, e);
            throw new NLPProcessingException("Failed to create language intent for: " + language, e);
        }
    }
} 