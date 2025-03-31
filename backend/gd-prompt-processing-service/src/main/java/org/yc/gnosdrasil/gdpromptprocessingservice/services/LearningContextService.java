package org.yc.gnosdrasil.gdpromptprocessingservice.services;

import org.yc.gnosdrasil.gdpromptprocessingservice.config.NLPProperties;

import java.util.List;

public interface LearningContextService {
    boolean isInLearningContext(String clause, String target);
    boolean hasLearningKeyword(String clause);
    boolean isLearningFocus(String clause, String target, List<String> clauses, int clauseIndex);
} 