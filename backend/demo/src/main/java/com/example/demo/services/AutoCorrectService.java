//package com.example.demo.services;
//
//import org.languagetool.JLanguageTool;
//import org.languagetool.language.AmericanEnglish;
//import org.languagetool.rules.RuleMatch;
//import org.springframework.stereotype.Service;
//
//import java.util.*;
//
//@Service
//public class AutoCorrectService {
//    private final JLanguageTool langTool;
//
//    public AutoCorrectService() {
//        langTool = new JLanguageTool(new AmericanEnglish());
//    }
//
//    public String correctText(String input) {
//        try {
//            List<RuleMatch> matches = langTool.check(input);
//            String corrected = applyCorrections(input, matches);
//
//            return fixTechnicalTerms(corrected);
//        } catch (Exception e) {
//            return input;
//        }
//    }
//
//    private String applyCorrections(String text, List<RuleMatch> matches) {
//        Collections.reverse(matches);
//        for (RuleMatch match : matches) {
//            text = text.substring(0, match.getFromPos()) +
//                    match.getSuggestedReplacements().get(0) +
//                    text.substring(match.getToPos());
//        }
//        return text;
//    }
//
//    private String fixTechnicalTerms(String text) {
//        for (String term : techTerms.getTerms()) {
//            String pattern = "(?i)\\b" + term.replace(" ", "\\s+") + "\\b";
//            text = text.replaceAll(pattern, term);
//        }
//        return text;
//    }
//}