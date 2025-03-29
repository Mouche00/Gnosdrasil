package org.yc.gnosdrasil.gdpromptprocessingservice.services.impl;

import edu.stanford.nlp.ling.CoreLabel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.yc.gnosdrasil.gdpromptprocessingservice.config.NLPProperties;
import org.yc.gnosdrasil.gdpromptprocessingservice.services.ExperienceLevelService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ExperienceLevelServiceImpl implements ExperienceLevelService {
    private final NLPProperties nlpProperties;

    @Override
    public String determineExperienceLevel(String sentence, String language, List<CoreLabel> tokens) {
        int langIndex = sentence.indexOf(language);
        String beforeLang = langIndex > 0 ? sentence.substring(0, langIndex) : "";
        String afterLang = langIndex + language.length() < sentence.length() ? 
                sentence.substring(langIndex + language.length()) : "";

        // Check for explicit experience levels
        if (containsAny(sentence, nlpProperties.getBeginnerIndicators())) {
            return "beginner";
        }
        if (containsAny(sentence, nlpProperties.getIntermediateIndicators())) {
            return "intermediate";
        }
        if (containsAny(sentence, nlpProperties.getAdvancedIndicators())) {
            return "advanced";
        }

        // Check for negative experience indicators
        if (containsAny(beforeLang, nlpProperties.getNegativeIndicators()) || 
            containsAny(afterLang, nlpProperties.getNegativeIndicators())) {
            return "beginner";
        }

        // Check for positive experience indicators
        if (containsAny(beforeLang, nlpProperties.getPositiveIndicators()) || 
            containsAny(afterLang, nlpProperties.getPositiveIndicators())) {
            return "intermediate";
        }

        // Default to beginner if no clear indication
        return "beginner";
    }

    private boolean containsAny(String text, Iterable<String> indicators) {
        for (String indicator : indicators) {
            if (text.contains(indicator)) {
                return true;
            }
        }
        return false;
    }
} 