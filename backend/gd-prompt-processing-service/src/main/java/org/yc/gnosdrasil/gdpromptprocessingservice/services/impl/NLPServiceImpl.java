package org.yc.gnosdrasil.gdpromptprocessingservice.services.impl;

import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.pipeline.CoreDocument;
import edu.stanford.nlp.pipeline.CoreSentence;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.sentiment.SentimentCoreAnnotations;
import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.trees.TreeCoreAnnotations;
import edu.stanford.nlp.util.CoreMap;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.languagetool.JLanguageTool;
import org.languagetool.language.AmericanEnglish;
import org.languagetool.rules.RuleMatch;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.yc.gnosdrasil.gdpromptprocessingservice.dtos.NLPResultDTO;
import org.yc.gnosdrasil.gdpromptprocessingservice.dtos.PromptRequestDTO;
import org.yc.gnosdrasil.gdpromptprocessingservice.entity.*;
import org.yc.gnosdrasil.gdpromptprocessingservice.repository.NLPResultRepository;
import org.yc.gnosdrasil.gdpromptprocessingservice.services.LanguageIntentService;
import org.yc.gnosdrasil.gdpromptprocessingservice.services.NLPService;
import org.yc.gnosdrasil.gdpromptprocessingservice.utils.mapper.NLPMapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class NLPServiceImpl implements NLPService {

    private final JLanguageTool languageTool = new JLanguageTool(new AmericanEnglish());
    private final StanfordCoreNLP pipeline;
    private final NLPResultRepository nlpResultRepository;
    private final LanguageIntentService languageIntentService;
    private final NLPMapper nlpMapper;

    @Transactional
    public NLPResultDTO processText(PromptRequestDTO promptRequestDTO) {
        String text = promptRequestDTO.prompt();
        NLPResult result = nlpResultRepository.save(processTextInternal(text));
        return nlpMapper.toDto(result);
    }

    private NLPResult processTextInternal(String text) {
        String correctedText = correctSpelling(text);
        CoreDocument document = new CoreDocument(correctedText);
        pipeline.annotate(document);

        List<CoreSentence> sentences = document.sentences();
        List<SentenceAnalysis> sentenceAnalyses = sentences.stream()
                .map(this::analyzeSentence)
                .collect(Collectors.toList());

        List<LanguageIntent> languageIntents = new ArrayList<>(languageIntentService.extractLanguageIntents(sentences));
        String overallSentiment = calculateOverallSentiment(sentences);

        NLPResult result = NLPResult.builder()
                .correctedText(correctedText)
                .sentenceAnalyses(sentenceAnalyses)
                .languageIntents(languageIntents)
                .overallSentiment(overallSentiment)
                .build();

        sentenceAnalyses.forEach(sa -> sa.setNlpResult(result));
        languageIntents.forEach(li -> li.setNlpResult(result));

        return result;
    }

    private String correctSpelling(String text) {
        try {
            List<RuleMatch> matches = languageTool.check(text);
            StringBuilder correctedText = new StringBuilder(text);
            for (RuleMatch match : matches) {
                if (!match.getSuggestedReplacements().isEmpty()) {
                    correctedText.replace(
                            match.getFromPos(),
                            match.getToPos(),
                            match.getSuggestedReplacements().get(0)
                    );
                }
            }
            return correctedText.toString();
        } catch (Exception e) {
            log.error("Error correcting spelling", e);
            return text;
        }
    }

    private SentenceAnalysis analyzeSentence(CoreSentence sentence) {
        Tree tree = sentence.sentimentTree();
        String sentiment = sentence.sentiment();
        
        return SentenceAnalysis.builder()
//                .sentence(sentence.toString())
                .parseTree(tree.toString())
                .sentiment(sentiment)
                .build();
    }

    private String calculateOverallSentiment(List<CoreSentence> sentences) {
        Map<String, Long> sentimentCounts = sentences.stream()
                .map(CoreSentence::sentiment)
                .collect(Collectors.groupingBy(s -> s, Collectors.counting()));

        return sentimentCounts.entrySet().stream()
                .max((e1, e2) -> e1.getValue().compareTo(e2.getValue()))
                .map(Map.Entry::getKey)
                .orElse("neutral");
    }
} 