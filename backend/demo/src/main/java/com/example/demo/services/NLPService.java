package com.example.demo.services;

import com.example.demo.utils.enums.LearningIntent;
import com.example.demo.utils.enums.LearningScope;
import com.example.demo.utils.enums.SkillLevel;
import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.trees.TreeCoreAnnotations;
import edu.stanford.nlp.util.CoreMap;
import jakarta.annotation.PostConstruct;
import org.apache.commons.text.similarity.LevenshteinDistance;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
public class NLPService {

    private StanfordCoreNLP pipeline;
    private Set<String> techTerms;
    private LevenshteinDistance levenshteinDistance;
    private Pattern chunkingPattern;

    @PostConstruct
    public void init() throws IOException {
        Properties props = new Properties();
        props.setProperty("annotators", "tokenize,ssplit,pos,lemma,ner,parse,coref,sentiment");
        props.setProperty("ner.model", "/home/mushi/Downloads/english.conll.4class.caseless.distsim.crf.ser.gz");
        pipeline = new StanfordCoreNLP(props);

        techTerms = new HashSet<>();
        loadWordList("tech_terms.txt"); // Loads programming languages and frameworks

        levenshteinDistance = new LevenshteinDistance();
        chunkingPattern = Pattern.compile("(<JJ.*>|<NN.*>)+"); // Matches adjective/noun phrases
    }

    private void loadWordList(String filename) throws IOException {
        try {
            techTerms.addAll(Files.readAllLines(Paths.get(filename)).stream()
                    .map(String::toLowerCase)
                    .collect(Collectors.toSet()));
        } catch (IOException e) {
            System.err.println("Error loading " + filename + ": " + e.getMessage());
            // Consider throwing the exception or using a default list if the file is essential.
        }
    }

    public Map<String, Object> processPrompt(String prompt) {
        String correctedPrompt = autocorrect(prompt);
        String cleanedPrompt = cleanText(correctedPrompt);

        Annotation document = new Annotation(cleanedPrompt);
        pipeline.annotate(document);

        Map<String, Object> result = new HashMap<>();
        result.put("tokens", new ArrayList<>());
        result.put("levels", new ArrayList<SkillLevel>());
        result.put("intents", new ArrayList<LearningIntent>());
        result.put("scopes", new ArrayList<LearningScope>());
        result.put("programming_languages", new ArrayList<String>());
        result.put("frameworks", new ArrayList<String>());
//        result.put("nouns", new ArrayList<String>());
        result.put("entities", new ArrayList<Map<String, String>>());
//        result.put("chunks", new ArrayList<String>());
        result.put("sentences", new ArrayList<Map<String, Object>>());
//        result.put("sentence_sentiment", new ArrayList<Map<String, String>>());
//        result.put("coreferences", new HashMap<Integer, String>());

        List<Map<String, String>> relevantTokens = new ArrayList<>(); // To collect relevant tokens

        for (CoreMap sentence : document.get(CoreAnnotations.SentencesAnnotation.class)) {
            List<Map<String, String>> sentenceTokens = new ArrayList<>();

            for (CoreLabel token : sentence.get(CoreAnnotations.TokensAnnotation.class)) {
                Map<String, String> tokenMap = createTokenMap(token);
                result.put("tokens", tokenMap);
                sentenceTokens.add(tokenMap);

                String word = token.word().toLowerCase();
                extractEnum(word, SkillLevel.class).ifPresent(level -> result.put("levels", level));
                extractEnum(word, LearningIntent.class).ifPresent(intent -> result.put("intents", intent));
                extractEnum(word, LearningScope.class).ifPresent(scope -> result.put("scopes", scope));

                if (techTerms.contains(word)) {
                    if (isProgrammingLanguage(word)) result.put("programming_languages", word);
                    if (isFramework(word)) result.put("frameworks", word);
                }

                String pos = token.get(CoreAnnotations.PartOfSpeechAnnotation.class);
                if (pos.contains("NN") || pos.contains("VB") || pos.contains("JJ")) {
                    relevantTokens.add(tokenMap);
//                    result.get("nouns", List.class).add(token.get(CoreAnnotations.LemmaAnnotation.class).toLowerCase()); // Add to the nouns list
                }

                String ner = token.get(CoreAnnotations.NamedEntityTagAnnotation.class);
                if (!ner.equals("O")) {
                    Map<String, String> entity = new HashMap<>();
                    entity.put("value", token.word());
                    entity.put("type", ner);
                    result.put("entities", entity);
                }
            }

            String sentenceText = sentence.toString();
            Matcher matcher = chunkingPattern.matcher(sentenceText);
            while (matcher.find()) {
                result.put("chunks", matcher.group());
            }

            Tree tree = sentence.get(TreeCoreAnnotations.TreeAnnotation.class);
            Map<String, Object> sentenceData = new HashMap<>();
            sentenceData.put("tokens", sentenceTokens);
            sentenceData.put("dependency_tree", tree.toString());
            result.put("sentences", sentenceData);
            result.put("tokens", relevantTokens); // Put the relevant tokens in the result

            // Sentiment analysis - consider removing if not used
            //String sentiment = sentence.get(CoreAnnotations.SentimentAnnotation.class);
            //Map<String, String> sentimentMap = new HashMap<>();
            //sentimentMap.put("text", sentence.toString());
            //sentimentMap.put("sentiment", sentiment);
            //result.get("sentence_sentiment", List.class).add(sentimentMap);
        }

        // Coreference resolution - consider removing if not used
        //if (document.get(CoreAnnotations.CorefChainAnnotation.class) != null) {
        //    for (Map.Entry<Integer, CorefChain> entry : document.get(CoreAnnotations.CorefChainAnnotation.class).entrySet()) {
        //        CorefChain chain = entry.getValue();
        //        CorefChain.CorefMention representative = chain.getRepresentativeMention();
        //        result.get("coreferences", Map.class).put(entry.getKey(), representative.mentionSpan);
        //    }
        //}

        // Set default learning intent if none are found.
//        if (result.get("intents").) {
//            result.put("intents", List.of(LearningIntent.LEARNING));
//        }

        return result;
    }


    private Map<String, String> createTokenMap(CoreLabel token) {
        return new HashMap<>() {{
            put("word", token.word());
            put("lemma", token.get(CoreAnnotations.LemmaAnnotation.class));
            put("pos", token.get(CoreAnnotations.PartOfSpeechAnnotation.class));
            put("ner", token.get(CoreAnnotations.NamedEntityTagAnnotation.class));
        }};
    }

    private String autocorrect(String text) {
        String[] words = text.split("\\s+");
        return Arrays.stream(words)
                .map(word -> {
                    String lowerWord = word.toLowerCase();
                    return techTerms.contains(lowerWord) ? word : findClosestWord(lowerWord).orElse(word);
                })
                .collect(Collectors.joining(" "));
    }

    private Optional<String> findClosestWord(String word) {
        return techTerms.stream()
                .min(Comparator.comparingInt(dictWord -> levenshteinDistance.apply(word, dictWord)));
    }


    private String cleanText(String text) {
        return text.replaceAll("[^a-zA-Z0-9\\s]", "").replaceAll("\\s+", " ").trim();
    }

    private <T extends Enum<T>> Optional<T> extractEnum(String word, Class<T> enumClass) {
        try {
            return Optional.of(Enum.valueOf(enumClass, word.toUpperCase()));
        } catch (IllegalArgumentException e) {
            return Optional.empty();
        }
    }

    private boolean isProgrammingLanguage(String word) {
        return techTerms.contains(word);
    }

    private boolean isFramework(String word) {
        return techTerms.contains(word);
    }
}