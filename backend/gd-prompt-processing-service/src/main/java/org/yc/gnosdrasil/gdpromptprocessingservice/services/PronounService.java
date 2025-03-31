package org.yc.gnosdrasil.gdpromptprocessingservice.services;

import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.pipeline.CoreSentence;
import org.yc.gnosdrasil.gdpromptprocessingservice.config.NLPProperties;

import java.util.HashMap;
import java.util.Map;

public interface PronounService {
    Map<String, String> identifyPronounReferents(Iterable<CoreSentence> sentences);
    boolean isPronoun(String word);
    boolean isLanguage(String word);
} 