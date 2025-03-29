package org.yc.gnosdrasil.gdpromptprocessingservice.services;

import edu.stanford.nlp.ling.CoreLabel;

import java.util.List;

public interface ExperienceLevelService {
    String determineExperienceLevel(String sentence, String language, List<CoreLabel> tokens);
} 