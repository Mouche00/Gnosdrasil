package org.yc.gnosdrasil.gdpromptprocessingservice.services.impl;

import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.pipeline.CoreDocument;
import edu.stanford.nlp.pipeline.CoreSentence;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.trees.Tree;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.languagetool.JLanguageTool;
import org.languagetool.language.AmericanEnglish;
import org.languagetool.rules.RuleMatch;
import org.springframework.stereotype.Service;
import org.yc.gnosdrasil.gdpromptprocessingservice.config.NLPProperties;
import org.yc.gnosdrasil.gdpromptprocessingservice.dtos.NLPResultDTO;
import org.yc.gnosdrasil.gdpromptprocessingservice.dtos.PromptRequestDTO;
import org.yc.gnosdrasil.gdpromptprocessingservice.entity.LanguageIntent;
import org.yc.gnosdrasil.gdpromptprocessingservice.entity.NLPResult;
import org.yc.gnosdrasil.gdpromptprocessingservice.entity.SentenceAnalysis;
import org.yc.gnosdrasil.gdpromptprocessingservice.services.LanguageIntentService;
import org.yc.gnosdrasil.gdpromptprocessingservice.services.NLPService;
import org.yc.gnosdrasil.gdpromptprocessingservice.utils.mapper.NLPMapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class NLPServiceImpl implements NLPService {

    private final JLanguageTool languageTool = new JLanguageTool(new AmericanEnglish());
    private final StanfordCoreNLP pipeline;
//    private final NLPResultRepository nlpResultRepository;
    private final LanguageIntentService languageIntentService;
    private final NLPMapper nlpMapper;
    private final NLPProperties nlpProperties;

    private static final int MAX_LEVENSHTEIN_DISTANCE = 2;
    private static final Set<String> PROGRAMMING_CONTEXT_WORDS = Set.of(
        "learn", "learning", "programming", "code", "coding", "develop", "development",
        "experience", "experienced", "expert", "expertise", "know", "knowledge",
        "new", "beginner", "intermediate", "advanced", "master", "mastery"
    );

//    @Transactional
    public NLPResultDTO processText(PromptRequestDTO promptRequestDTO) {
        String text = promptRequestDTO.prompt();
//        NLPResult result = nlpResultRepository.save(processTextInternal(text));
//        NLPResult result = nlpResultRepository.save(processTextInternal(text));
        return nlpMapper.toDto(processTextInternal(text));
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
            // First, check for programming language corrections
            String textWithLangCorrections = correctProgrammingLanguages(text);
            
            // Then proceed with regular spelling correction
            List<RuleMatch> matches = languageTool.check(textWithLangCorrections);
            StringBuilder correctedText = new StringBuilder(textWithLangCorrections);
            
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

    private String correctProgrammingLanguages(String text) {
        CoreDocument document = new CoreDocument(text);
        pipeline.annotate(document);
        
        StringBuilder correctedText = new StringBuilder(text);
        List<String> programmingLanguages = new ArrayList<>(nlpProperties.getProgrammingLanguages());
        
        for (CoreSentence sentence : document.sentences()) {
            List<CoreLabel> tokens = sentence.tokens();
            for (int i = 0; i < tokens.size(); i++) {
                CoreLabel token = tokens.get(i);
                String word = token.word();
                String pos = token.tag();
                
                // Only consider words that are nouns or unknown parts of speech
                if (!pos.startsWith("NN") && !pos.equals("UNKNOWN")) {
                    continue;
                }
                
                // Check if the word is in a programming context
                if (!isInProgrammingContext(sentence, i)) {
                    continue;
                }
                
                String correctedWord = findClosestProgrammingLanguage(word, programmingLanguages);
                
                if (!word.equals(correctedWord)) {
                    int startPos = token.beginPosition();
                    correctedText.replace(startPos, startPos + word.length(), correctedWord);
                }
            }
        }
        
        return correctedText.toString();
    }

    private boolean isInProgrammingContext(CoreSentence sentence, int tokenIndex) {
        List<CoreLabel> tokens = sentence.tokens();
        String sentenceText = sentence.text().toLowerCase();
        
        // Check if any programming context words are in the sentence
        boolean hasContextWord = PROGRAMMING_CONTEXT_WORDS.stream()
                .anyMatch(word -> sentenceText.contains(word));
        
        if (!hasContextWord) {
            return false;
        }
        
        // Check if the word is near a programming context word
        int windowSize = 5; // Look at 5 words before and after
        int start = Math.max(0, tokenIndex - windowSize);
        int end = Math.min(tokens.size(), tokenIndex + windowSize + 1);
        
        for (int i = start; i < end; i++) {
            String word = tokens.get(i).word().toLowerCase();
            if (PROGRAMMING_CONTEXT_WORDS.contains(word)) {
                return true;
            }
        }
        
        return false;
    }

    private String findClosestProgrammingLanguage(String word, List<String> programmingLanguages) {
        String closestMatch = word;
        int minDistance = Integer.MAX_VALUE;
        
        // Only consider words that are at least 3 characters long
        if (word.length() < 3) {
            return word;
        }
        
        for (String lang : programmingLanguages) {
            // Skip if the word is too different in length from the programming language
            if (Math.abs(word.length() - lang.length()) > 3) {
                continue;
            }
            
            int distance = levenshteinDistance(word.toLowerCase(), lang.toLowerCase());
            if (distance <= MAX_LEVENSHTEIN_DISTANCE && distance < minDistance) {
                minDistance = distance;
                closestMatch = lang;
            }
        }
        
        return closestMatch;
    }

    private int levenshteinDistance(String s1, String s2) {
        int[][] distance = new int[s1.length() + 1][s2.length() + 1];
        
        for (int i = 0; i <= s1.length(); i++) {
            distance[i][0] = i;
        }
        
        for (int j = 0; j <= s2.length(); j++) {
            distance[0][j] = j;
        }
        
        for (int i = 0; i < s1.length(); i++) {
            for (int j = 0; j < s2.length(); j++) {
                if (s1.charAt(i) == s2.charAt(j)) {
                    distance[i + 1][j + 1] = distance[i][j];
                } else {
                    distance[i + 1][j + 1] = Math.min(
                            Math.min(distance[i][j + 1] + 1, distance[i + 1][j] + 1),
                            distance[i][j] + 1
                    );
                }
            }
        }
        
        return distance[s1.length()][s2.length()];
    }

    private SentenceAnalysis analyzeSentence(CoreSentence sentence) {
        Tree tree = sentence.sentimentTree();
        String sentiment = sentence.sentiment();
        
        return SentenceAnalysis.builder()
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