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
import org.yc.gnosdrasil.gdpromptprocessingservice.entity.LanguageIntent;
import org.yc.gnosdrasil.gdpromptprocessingservice.entity.NLPResult;
import org.yc.gnosdrasil.gdpromptprocessingservice.entity.SentenceAnalysis;
import org.yc.gnosdrasil.gdpromptprocessingservice.repository.NLPResultRepository;
import org.yc.gnosdrasil.gdpromptprocessingservice.services.LanguageIntentService;
import org.yc.gnosdrasil.gdpromptprocessingservice.services.NLPService;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class NLPServiceImpl implements NLPService {

    private final JLanguageTool languageTool = new JLanguageTool(new AmericanEnglish());
    private final StanfordCoreNLP pipeline;
    private final LanguageIntentService languageIntentService;
    private final NLPProperties nlpProperties;
    private final NLPResultRepository nlpResultRepository;

    private static final int MAX_LEVENSHTEIN_DISTANCE = 2;
    private static final Set<String> PROGRAMMING_CONTEXT_WORDS = Set.of(
        "learn", "learning", "programming", "code", "coding", "develop", "development",
        "experience", "experienced", "expert", "expertise", "know", "knowledge",
        "new", "beginner", "intermediate", "advanced", "master", "mastery"
    );

    // Cache for processed results
    private final Map<String, NLPResult> resultCache = new ConcurrentHashMap<>();

    public NLPResult processText(String text) {
        // Pre-processing
        String preprocessedText = preprocessText(text);

        // Check cache
        if (resultCache.containsKey(preprocessedText)) {
            return resultCache.get(preprocessedText);
        }

        // Fetch from database and save to cache
        Optional<NLPResult> nlpResult = nlpResultRepository.findByCorrectedText(preprocessedText);
        if (nlpResult.isPresent()) {
            resultCache.put(preprocessedText, nlpResult.get());
            return nlpResult.get();
        }
        
        // Core NLP processing
        CoreDocument document = new CoreDocument(preprocessedText);
        pipeline.annotate(document);

        // Extract sentences and analyze
        List<CoreSentence> sentences = document.sentences();
        List<SentenceAnalysis> sentenceAnalyses = sentences.parallelStream()
                .map(this::analyzeSentence)
                .collect(Collectors.toList());

        // Extract language intents with context awareness
        List<LanguageIntent> languageIntents = new ArrayList<>(
            languageIntentService.extractLanguageIntents(sentences)
        );

        return nlpResultRepository.save(
                NLPResult.builder()
                    .originalText(text)
                    .correctedText(preprocessedText)
                    .sentenceAnalyses(sentenceAnalyses)
                    .languageIntents(languageIntents)
                    .build()
        );
    }

    private String preprocessText(String text) {
        // Apply text normalization
        String normalizedText = normalizeText(text);
        
        // Correct programming languages with context awareness
        String correctedText = correctProgrammingLanguages(normalizedText);
        
        // Apply spelling correction with technical term awareness
        return correctSpelling(correctedText);
    }

    private String normalizeText(String text) {
        return text.replaceAll("\\s+", " ")
                  .trim()
                  .toLowerCase();
    }

    private String correctSpelling(String text) {
        try {
            List<RuleMatch> matches = languageTool.check(text);
            StringBuilder correctedText = new StringBuilder(text);
            
            // Sort matches by position to avoid overlapping corrections
            matches.sort(Comparator.comparingInt(RuleMatch::getFromPos).reversed());
            
            for (RuleMatch match : matches) {
                if (!match.getSuggestedReplacements().isEmpty()) {
                    String replacement = match.getSuggestedReplacements().get(0);
                    
                    // Skip correction if it's a technical term
                    if (nlpProperties.isEnableTechnicalTermDetection() && 
                        isTechnicalTerm(text.substring(match.getFromPos(), match.getToPos()))) {
                        continue;
                    }
                    
                    correctedText.replace(
                            match.getFromPos(),
                            match.getToPos(),
                            replacement
                    );
                }
            }
            return correctedText.toString();
        } catch (Exception e) {
            log.error("Error correcting spelling", e);
            return text;
        }
    }

    private boolean isTechnicalTerm(String term) {
        return nlpProperties.getTechnicalTerms().contains(term.toLowerCase()) ||
               nlpProperties.getFrameworkKeywords().contains(term.toLowerCase()) ||
               nlpProperties.getToolKeywords().contains(term.toLowerCase());
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
                
                if (!pos.startsWith("NN") && !pos.equals("UNKNOWN")) {
                    continue;
                }
                
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
        if (!nlpProperties.isEnableContextAwareness()) {
            return true;
        }

        List<CoreLabel> tokens = sentence.tokens();
        String sentenceText = sentence.text().toLowerCase();
        
        // Check for programming context words
        boolean hasContextWord = PROGRAMMING_CONTEXT_WORDS.stream()
                .anyMatch(word -> sentenceText.contains(word));
        
        if (!hasContextWord) {
            return false;
        }
        
        // Check for technical terms and frameworks
        if (nlpProperties.isEnableTechnicalTermDetection()) {
            boolean hasTechnicalTerm = nlpProperties.getTechnicalTerms().stream()
                    .anyMatch(term -> sentenceText.contains(term.toLowerCase()));
            if (hasTechnicalTerm) return true;
        }
        
        // Check proximity to context words
        int windowSize = 5;
        int start = Math.max(0, tokenIndex - windowSize);
        int end = Math.min(tokens.size(), tokenIndex + windowSize + 1);
        
        return tokens.subList(start, end).stream()
                .map(token -> token.word().toLowerCase())
                .anyMatch(word -> PROGRAMMING_CONTEXT_WORDS.contains(word));
    }

    private String findClosestProgrammingLanguage(String word, List<String> programmingLanguages) {
        if (word.length() < 3) return word;
        
        return programmingLanguages.stream()
                .filter(lang -> Math.abs(word.length() - lang.length()) <= 3)
                .min(Comparator.comparingInt(lang -> 
                    levenshteinDistance(word.toLowerCase(), lang.toLowerCase())))
                .filter(lang -> levenshteinDistance(word.toLowerCase(), lang.toLowerCase()) <= MAX_LEVENSHTEIN_DISTANCE)
                .orElse(word);
    }

    private SentenceAnalysis analyzeSentence(CoreSentence sentence) {
        Tree tree = sentence.sentimentTree();
        String sentiment = sentence.sentiment();
        
        return SentenceAnalysis.builder()
                .parseTree(tree.toString())
                .sentiment(sentiment)
                .build();
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
} 