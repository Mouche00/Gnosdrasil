package com.gnosdrasil.nlp;

import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class IntentProcessor {
    
    private static final List<String> PROGRAMMING_LANGUAGES = List.of(
        "javascript", "java", "python", "c++", "c#", "ruby", "php", "swift", "kotlin", "go",
        "rust", "scala", "r", "matlab", "typescript", "dart", "perl", "haskell", "lua", "shell"
    );
    
    private static final List<String> EXPERIENCE_LEVELS = List.of(
        "beginner", "intermediate", "advanced", "expert", "novice", "proficient"
    );
    
    private static final List<String> EXPERIENCE_INDICATORS = List.of(
        "experience", "know", "familiar", "learn", "start", "begin", "understand", "master"
    );

    public List<LanguageIntent> processIntent(String input) {
        // Convert to lowercase for case-insensitive matching
        String normalizedInput = input.toLowerCase();
        
        List<LanguageIntent> intents = new ArrayList<>();
        
        // Extract programming languages
        for (String lang : PROGRAMMING_LANGUAGES) {
            if (normalizedInput.contains(lang)) {
                LanguageIntent intent = new LanguageIntent();
                intent.setLang(lang);
                
                // Determine experience level
                String level = determineExperienceLevel(normalizedInput, lang);
                intent.setLevel(level);
                
                intents.add(intent);
            }
        }
        
        return intents;
    }
    
    private String determineExperienceLevel(String input, String language) {
        // Look for experience indicators before the language
        int langIndex = input.indexOf(language);
        String beforeLang = langIndex > 0 ? input.substring(0, langIndex) : "";
        
        // Check for negative experience indicators
        if (containsNegativeIndicators(beforeLang)) {
            return "beginner";
        }
        
        // Check for experience level keywords
        for (String level : EXPERIENCE_LEVELS) {
            if (input.contains(level)) {
                return level;
            }
        }
        
        // Default to beginner if no clear indication
        return "beginner";
    }
    
    private boolean containsNegativeIndicators(String text) {
        List<String> negativeIndicators = List.of(
            "no", "don't", "dont", "haven't", "havent", "never", "new to", "starting",
            "beginning", "want to learn", "trying to learn", "learning"
        );
        
        return negativeIndicators.stream().anyMatch(text::contains);
    }
    
    public static class LanguageIntent {
        private String lang;
        private String level;
        
        public String getLang() {
            return lang;
        }
        
        public void setLang(String lang) {
            this.lang = lang;
        }
        
        public String getLevel() {
            return level;
        }
        
        public void setLevel(String level) {
            this.level = level;
        }
    }
} 