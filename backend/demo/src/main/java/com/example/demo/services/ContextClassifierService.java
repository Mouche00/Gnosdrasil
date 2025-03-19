//package com.example.demo.services;
//
//import com.example.demo.utils.enums.LearningIntent;
//import com.example.demo.utils.enums.SkillLevel;
//
//import static edu.stanford.nlp.scenegraph.image.SceneGraphImageUtils.containsLemma;
//
//public class ContextClassifierService {
//    public ClassificationResult classify(ProcessedText text) {
//        return ClassificationResult.builder()
//                .level(detectSkillLevel(text))
//                .intent(detectIntent(text))
//                .subject(detectSubject(text))
//                .scope(detectScope(text))
//                .build();
//    }
//
//    private SkillLevel detectSkillLevel(ProcessedText text) {
//        if (containsPhrase(text, "new to") || containsLemma(text, "beginner")) {
//            return SkillLevel.BEGINNER;
//        }
//        if (containsPhrase(text, "refresher") || containsLemma(text, "review")) {
//            return SkillLevel.INTERMEDIATE;
//        }
//        return SkillLevel.UNKNOWN;
//    }
//
//    private LearningIntent detectIntent(ProcessedText text) {
//        if (containsDependencyPath(text, "want/VB -> learn/VB")) {
//            return LearningIntent.FULL_COURSE;
//        }
//        if (containsLemma(text, "refresher") || containsLemma(text, "quick")) {
//            return LearningIntent.QUICK_NOTES;
//        }
//        return LearningIntent.UNSPECIFIED;
//    }
//}