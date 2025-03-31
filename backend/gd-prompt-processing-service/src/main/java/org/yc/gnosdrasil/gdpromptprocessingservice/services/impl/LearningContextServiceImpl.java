package org.yc.gnosdrasil.gdpromptprocessingservice.services.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.yc.gnosdrasil.gdpromptprocessingservice.config.NLPProperties;
import org.yc.gnosdrasil.gdpromptprocessingservice.services.LearningContextService;
import org.yc.gnosdrasil.gdpromptprocessingservice.services.PronounService;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class LearningContextServiceImpl implements LearningContextService {
    private final NLPProperties nlpProperties;
    private final PronounService pronounService;

    @Override
    public boolean isInLearningContext(String clause, String target) {
        if (clause.isEmpty()) return false;
        
        boolean hasLearningKeyword = hasLearningKeyword(clause);
        if (!hasLearningKeyword) return false;
        
        return clause.contains(target) || pronounService.isPronoun(target);
    }

    @Override
    public boolean hasLearningKeyword(String clause) {
        return nlpProperties.getLearningKeywords().stream()
                .anyMatch(keyword -> clause.contains(keyword));
    }

    @Override
    public boolean isLearningFocus(String clause, String target, List<String> clauses, int clauseIndex) {
        boolean currentClauseFocus = isInLearningContext(clause, target);
        
        if (currentClauseFocus) return true;
        
        // Check adjacent clauses
        if (clauseIndex > 0) {
            boolean previousClauseFocus = isInLearningContext(clauses.get(clauseIndex - 1), target);
            if (previousClauseFocus) return true;
        }
        
        if (clauseIndex < clauses.size() - 1) {
            boolean nextClauseFocus = isInLearningContext(clauses.get(clauseIndex + 1), target);
            if (nextClauseFocus) return true;
        }
        
        return false;
    }
} 