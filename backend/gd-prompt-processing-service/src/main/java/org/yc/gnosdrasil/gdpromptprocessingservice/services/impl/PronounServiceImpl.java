package org.yc.gnosdrasil.gdpromptprocessingservice.services.impl;

import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.pipeline.CoreSentence;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.yc.gnosdrasil.gdpromptprocessingservice.config.NLPProperties;
import org.yc.gnosdrasil.gdpromptprocessingservice.exception.NLPProcessingException;
import org.yc.gnosdrasil.gdpromptprocessingservice.services.PronounService;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
public class PronounServiceImpl implements PronounService {
    private static final Set<String> PRONOUNS = Set.of("it", "this", "that", "these", "those");
    
    private final NLPProperties nlpProperties;

    @Override
    public Map<String, String> identifyPronounReferents(Iterable<CoreSentence> sentences) {
        Map<String, String> pronounMap = new HashMap<>();
        String lastMentionedLanguage = null;

        try {
            for (CoreSentence sentence : sentences) {
                for (CoreLabel token : sentence.tokens()) {
                    String word = token.word().toLowerCase();
                    if (isPronoun(word)) {
                        if (lastMentionedLanguage != null) {
                            pronounMap.put(word, lastMentionedLanguage);
                            log.debug("Mapped pronoun '{}' to language '{}'", word, lastMentionedLanguage);
                        }
                    } else if (isLanguage(word)) {
                        lastMentionedLanguage = word;
                    }
                }
            }
            return pronounMap;
        } catch (Exception e) {
            log.error("Failed to identify pronoun referents", e);
            throw new NLPProcessingException("Failed to identify pronoun referents", e);
        }
    }

    @Override
    public boolean isPronoun(String word) {
        return PRONOUNS.contains(word.toLowerCase()) || word.toLowerCase().startsWith("prp");
    }

    @Override
    public boolean isLanguage(String word) {
        return nlpProperties.getProgrammingLanguages().contains(word.toLowerCase());
    }
} 