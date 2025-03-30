package org.yc.gnosdrasil.gdpromptprocessingservice.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.Map;
import java.util.Set;

@Data
@Configuration
@ConfigurationProperties(prefix = "nlp")
public class NLPProperties {
    // Core language sets
    private Set<String> programmingLanguages;
    private Set<String> learningKeywords;
    
    // Experience level indicators
    private Set<String> beginnerIndicators;
    private Set<String> intermediateIndicators;
    private Set<String> advancedIndicators;
    
    // Sentiment indicators
    private Set<String> negativeIndicators;
    private Set<String> positiveIndicators;
    
    // Enhanced language processing
    private Set<String> technicalTerms;
    private Set<String> frameworkKeywords;
    private Set<String> toolKeywords;
    private Set<String> methodologyKeywords;
    
    // Context patterns
    private Map<String, Set<String>> contextPatterns;
    
    // Language-specific patterns
    private Map<String, Set<String>> languageSpecificPatterns;
    
    // Confidence thresholds
    private double minConfidenceThreshold = 0.7;
    private double maxAmbiguityThreshold = 0.3;
    
    // Processing options
    private boolean enableContextAwareness = true;
    private boolean enableAmbiguityResolution = true;
    private boolean enableTechnicalTermDetection = true;
    private boolean enableFrameworkDetection = true;
} 