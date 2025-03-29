package org.yc.gnosdrasil.gdpromptprocessingservice.services.impl;

import edu.stanford.nlp.ling.CoreLabel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.yc.gnosdrasil.gdpromptprocessingservice.config.NLPProperties;
import org.yc.gnosdrasil.gdpromptprocessingservice.services.ExperienceLevelService;

import java.util.List;

import static org.yc.gnosdrasil.gdpromptprocessingservice.utils.helpers.StringHelper.containsAny;
import static org.yc.gnosdrasil.gdpromptprocessingservice.utils.helpers.StringHelper.findContainingClause;

@Slf4j
@Service
@RequiredArgsConstructor
public class ExperienceLevelServiceImpl implements ExperienceLevelService {
    private final NLPProperties nlpProperties;

    @Override
    public String determineExperienceLevel(String sentence, String language, List<CoreLabel> tokens) {
        // Find the clause containing the language
        String containingClause = findContainingClause(sentence, language);
        if (containingClause == null) {
            log.info("Language {} not found in the sentence.", language);
            return "beginner"; // Default to beginner if we can't find the language in any clause
        }

        // Check for explicit experience levels in the containing clause
        if (containsAny(containingClause, nlpProperties.getBeginnerIndicators())) {
            log.debug("Experience level determined as beginner based on beginner indicators.");
            return "beginner";
        }
        if (containsAny(containingClause, nlpProperties.getIntermediateIndicators())) {
            log.debug("Experience level determined as intermediate based on intermediate indicators.");
            return "intermediate";
        }
        if (containsAny(containingClause, nlpProperties.getAdvancedIndicators())) {
            log.debug("Experience level determined as advanced based on advanced indicators.");
            return "advanced";
        }
        // Check for positive experience indicators in the containing clause
        if (containsAny(containingClause, nlpProperties.getPositiveIndicators())) {
            log.debug("Experience level adjusted to intermediate based on positive indicators.");
            return "intermediate";
        }

        log.info("Experience level determined as beginner based on default logic.");

        return "beginner";
    }
} 