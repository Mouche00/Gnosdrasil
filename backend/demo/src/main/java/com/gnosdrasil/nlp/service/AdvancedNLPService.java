package com.gnosdrasil.nlp.service;

import com.gnosdrasil.nlp.entity.NLPResultEntity;
import com.gnosdrasil.nlp.mapper.NLPMapper;
import com.gnosdrasil.nlp.repository.NLPResultRepository;
import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.sentiment.SentimentCoreAnnotations;
import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.trees.TreeCoreAnnotations;
import edu.stanford.nlp.util.CoreMap;
import org.languagetool.JLanguageTool;
import org.languagetool.language.AmericanEnglish;
import org.languagetool.rules.RuleMatch;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class AdvancedNLPService {

    @Autowired
    private StanfordCoreNLP stanfordCoreNLP;
    
    @Autowired
    private NLPResultRepository nlpResultRepository;
    
    @Autowired
    private NLPMapper nlpMapper;

    private final JLanguageTool languageTool = new JLanguageTool(new AmericanEnglish());
    private final Set<String> programmingLanguages = new HashSet<>(Arrays.asList(
        "javascript", "java", "python", "c++", "c#", "ruby", "php", "swift", "kotlin", "go",
        "rust", "scala", "r", "matlab", "typescript", "dart", "perl", "haskell", "lua", "shell",
        "assembly", "c", "objective-c", "sql", "html", "css", "xml", "yaml", "json", "markdown"
    ));

    @Transactional
    public NLPResult processText(String text) {
        // Process the text
        NLPResult result = processTextInternal(text);
        
        // Save to database
        NLPResultEntity entity = nlpMapper.toEntity(result, text);
        nlpResultRepository.save(entity);
        
        return result;
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
        
        // Extract programming languages and experience levels
        List<LanguageIntent> languageIntents = extractLanguageIntents(sentences);
        
        // Calculate overall sentiment
        String overallSentiment = calculateOverallSentiment(sentences);
        
        return new NLPResult(
            correctedText,
            sentenceAnalyses,
            languageIntents,
            overallSentiment
        );
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
            .map(token -> new TokenInfo(
                token.word(),
                token.tag(),
                token.lemma(),
                token.ner()
            ))
            .collect(Collectors.toList());
        
        // Get parse tree
        Tree tree = sentence.get(TreeCoreAnnotations.TreeAnnotation.class);
        
        // Get sentiment
        String sentiment = sentence.get(SentimentCoreAnnotations.SentimentClass.class);
        
        return new SentenceAnalysis(tokenInfos, tree.toString(), sentiment);
    }
    
    private List<LanguageIntent> extractLanguageIntents(List<CoreMap> sentences) {
        List<LanguageIntent> intents = new ArrayList<>();
        
        for (CoreMap sentence : sentences) {
            List<CoreLabel> tokens = sentence.get(CoreAnnotations.TokensAnnotation.class);
            String sentenceText = sentence.toString().toLowerCase();
            
            for (String lang : programmingLanguages) {
                if (sentenceText.contains(lang)) {
                    LanguageIntent intent = new LanguageIntent();
                    intent.setLang(lang);
                    intent.setLevel(determineExperienceLevel(sentenceText, lang, tokens));
                    intents.add(intent);
                }
            }
        }
        
        return intents;
    }
    
    private String determineExperienceLevel(String sentence, String language, List<CoreLabel> tokens) {
        // Look for experience level indicators in the sentence
        int langIndex = sentence.indexOf(language);
        String beforeLang = langIndex > 0 ? sentence.substring(0, langIndex) : "";
        
        // Check for explicit experience levels
        if (sentence.contains("beginner") || sentence.contains("novice")) return "beginner";
        if (sentence.contains("intermediate") || sentence.contains("proficient")) return "intermediate";
        if (sentence.contains("advanced") || sentence.contains("expert")) return "advanced";
        
        // Check for negative experience indicators
        if (containsNegativeIndicators(beforeLang)) return "beginner";
        
        // Check for positive experience indicators
        if (containsPositiveIndicators(beforeLang)) return "intermediate";
        
        // Default to beginner if no clear indication
        return "beginner";
    }
    
    private boolean containsNegativeIndicators(String text) {
        return Arrays.asList(
            "no", "don't", "dont", "haven't", "havent", "never", "new to", "starting",
            "beginning", "want to learn", "trying to learn", "learning", "just started",
            "beginner", "novice", "basic", "simple"
        ).stream().anyMatch(text::contains);
    }
    
    private boolean containsPositiveIndicators(String text) {
        return Arrays.asList(
            "experienced", "familiar", "know", "understand", "worked with", "used",
            "developed", "built", "created", "implemented", "designed"
        ).stream().anyMatch(text::contains);
    }
    
    private String calculateOverallSentiment(List<CoreMap> sentences) {
        Map<String, Integer> sentimentCounts = new HashMap<>();
        sentences.forEach(sentence -> {
            String sentiment = sentence.get(SentimentCoreAnnotations.SentimentClass.class);
            sentimentCounts.merge(sentiment, 1, Integer::sum);
        });
        
        return sentimentCounts.entrySet().stream()
            .max(Map.Entry.comparingByValue())
            .map(Map.Entry::getKey)
            .orElse("Neutral");
    }
    
    // Data classes
    public record NLPResult(
        String correctedText,
        List<SentenceAnalysis> sentenceAnalyses,
        List<LanguageIntent> languageIntents,
        String overallSentiment
    ) {}
    
    public record SentenceAnalysis(
        List<TokenInfo> tokens,
        String parseTree,
        String sentiment
    ) {}
    
    public record TokenInfo(
        String word,
        String pos,
        String lemma,
        String ner
    ) {}
    
    public static class LanguageIntent {
        public String lang;
        public String level;
        
        public String getLang() { return lang; }
        public void setLang(String lang) { this.lang = lang; }
        public String getLevel() { return level; }
        public void setLevel(String level) { this.level = level; }
    }
} 