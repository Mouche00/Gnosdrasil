package com.gnosdrasil.nlp.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "nlp_results")
public class NLPResultEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String originalText;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String correctedText;

    @Column(nullable = false)
    private String overallSentiment;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "nlpResult", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SentenceAnalysisEntity> sentenceAnalyses;

    @OneToMany(mappedBy = "nlpResult", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<LanguageIntentEntity> languageIntents;

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getOriginalText() { return originalText; }
    public void setOriginalText(String originalText) { this.originalText = originalText; }
    public String getCorrectedText() { return correctedText; }
    public void setCorrectedText(String correctedText) { this.correctedText = correctedText; }
    public String getOverallSentiment() { return overallSentiment; }
    public void setOverallSentiment(String overallSentiment) { this.overallSentiment = overallSentiment; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public List<SentenceAnalysisEntity> getSentenceAnalyses() { return sentenceAnalyses; }
    public void setSentenceAnalyses(List<SentenceAnalysisEntity> sentenceAnalyses) { this.sentenceAnalyses = sentenceAnalyses; }
    public List<LanguageIntentEntity> getLanguageIntents() { return languageIntents; }
    public void setLanguageIntents(List<LanguageIntentEntity> languageIntents) { this.languageIntents = languageIntents; }
} 