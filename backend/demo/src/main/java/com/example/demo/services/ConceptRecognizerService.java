//package com.example.demo.services;
//
//import edu.stanford.nlp.ling.CoreAnnotations;
//import edu.stanford.nlp.ling.CoreLabel;
//import edu.stanford.nlp.pipeline.Annotation;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Map;
//
//public class ConceptRecognizerService {
//    private static final Map<String, String> CONCEPT_MAP = Map.of(
//            "spring boot", "Java Framework",
//            "generic classes", "Type Systems",
//            "js", "JavaScript"
//    );
//
//    public List<String> findConcepts(Annotation document) {
//        List<String> concepts = new ArrayList<>();
//        for (CoreLabel token : document.get(CoreAnnotations.TokensAnnotation.class)) {
//            String lemma = token.lemma().toLowerCase();
//            if (CONCEPT_MAP.containsKey(lemma)) {
//                concepts.add(CONCEPT_MAP.get(lemma));
//            }
//        }
//        return concepts;
//    }
//}