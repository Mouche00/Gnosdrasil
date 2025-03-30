package org.yc.gnosdrasil.gdpromptprocessingservice.services;

import edu.stanford.nlp.pipeline.CoreSentence;
import org.yc.gnosdrasil.gdpromptprocessingservice.entity.LanguageIntent;

import java.util.List;

public interface LanguageIntentService {
    List<LanguageIntent> extractLanguageIntents(List<CoreSentence> sentences);
} 