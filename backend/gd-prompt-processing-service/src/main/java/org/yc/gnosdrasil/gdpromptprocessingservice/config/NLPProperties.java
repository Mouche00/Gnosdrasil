package org.yc.gnosdrasil.gdpromptprocessingservice.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.Set;

@Data
@Configuration
@ConfigurationProperties(prefix = "nlp")
public class NLPProperties {
    private Set<String> programmingLanguages;
    private Set<String> learningKeywords;
    private Set<String> beginnerIndicators;
    private Set<String> intermediateIndicators;
    private Set<String> advancedIndicators;
    private Set<String> negativeIndicators;
    private Set<String> positiveIndicators;
} 