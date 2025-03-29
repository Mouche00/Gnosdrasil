package org.yc.gnosdrasil.gdpromptprocessingservice.services.impl;

import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.pipeline.Annotation;
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

    private final StanfordCoreNLP stanfordCoreNLP;
    private final NLPResultRepository nlpResultRepository;
    private final LanguageIntentService languageIntentService;
    private final NLPMapper nlpMapper;

    @Transactional
    public NLPResultDTO processText(PromptRequestDTO promptRequestDTO) {
        String text = promptRequestDTO.prompt();

        // Save to database
        NLPResult result = nlpResultRepository.save(processTextInternal(text));

        return nlpMapper.toDto(result);
    }

    private NLPResult processTextInternal(String text) {
        // Correct spelling
        String correctedText = correctSpelling(text);

        // Process with Stanford CoreNLP
        Annotation document = new Annotation(correctedText);
        stanfordCoreNLP.annotate(document);

        // Extract sentences
        List<CoreMap> sentences = document.get(CoreAnnotations.SentencesAnnotation.class);

        // Process each sentence
        List<SentenceAnalysis> sentenceAnalyses = sentences.stream()
                .map(this::analyzeSentence)
                .collect(Collectors.toList());

        log.info("Extracting language levels");

        // Extract programming languages and experience levels
        List<LanguageIntent> languageIntents = new ArrayList<>(languageIntentService.extractLanguageIntents(sentences));

        // Calculate overall sentiment
        String overallSentiment = calculateOverallSentiment(sentences);

        // Build and set up relationships
        NLPResult result = NLPResult.builder()
                .correctedText(correctedText)
                .sentenceAnalyses(sentenceAnalyses)
                .languageIntents(languageIntents)
                .overallSentiment(overallSentiment)
                .build();

        // Set up bidirectional relationships
        sentenceAnalyses.forEach(sa -> sa.setNlpResult(result));
        languageIntents.forEach(li -> li.setNlpResult(result));

        log.info("NLPResult: {}", result);
        return result;
    }

    private String correctSpelling(String text) {
        try {
            List<RuleMatch> matches = languageTool.check(text);
            StringBuilder correctedText = new StringBuilder(text);
            for (RuleMatch match : matches) {
                if (match.getSuggestedReplacements().size() > 0) {
                    correctedText.replace(
                            match.getFromPos(),
                            match.getToPos(),
                            match.getSuggestedReplacements().get(0)
                    );
                }
            }
            return correctedText.toString();
        } catch (Exception e) {
            return text;
        }
    }

    private SentenceAnalysis analyzeSentence(CoreMap sentence) {
        // Get tokens and their POS tags
        List<CoreLabel> tokens = sentence.get(CoreAnnotations.TokensAnnotation.class);
        List<TokenInfo> tokenInfos = tokens.stream()
                .map(token -> TokenInfo.builder()
                        .word(token.word())
                        .pos(token.tag())
                        .lemma(token.lemma())
                        .ner(token.ner())
                        .build())
                .collect(Collectors.toList());

        // Get parse tree
        Tree tree = sentence.get(TreeCoreAnnotations.TreeAnnotation.class);

        // Get sentiment
        String sentiment = sentence.get(SentimentCoreAnnotations.SentimentClass.class);

        return SentenceAnalysis.builder()
                .tokens(tokenInfos)
                .parseTree(tree.toString())
                .sentiment(sentiment)
                .build();
    }

    private String calculateOverallSentiment(List<CoreMap> sentences) {
        return sentences.stream()
                .map(sentence -> sentence.get(SentimentCoreAnnotations.SentimentClass.class))
                .collect(Collectors.groupingBy(
                        sentiment -> sentiment,
                        Collectors.counting()
                ))
                .entrySet()
                .stream()
                .max((e1, e2) -> e1.getValue().compareTo(e2.getValue()))
                .map(Map.Entry::getKey)
                .orElse("Neutral");
    }
} 