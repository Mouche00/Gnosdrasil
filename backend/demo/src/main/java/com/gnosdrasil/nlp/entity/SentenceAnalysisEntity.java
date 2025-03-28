package com.gnosdrasil.nlp.entity;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "sentence_analyses")
public class SentenceAnalysisEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String parseTree;

    @Column(nullable = false)
    private String sentiment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "nlp_result_id", nullable = false)
    private NLPResultEntity nlpResult;

    @OneToMany(mappedBy = "sentenceAnalysis", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TokenInfoEntity> tokens;

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getParseTree() { return parseTree; }
    public void setParseTree(String parseTree) { this.parseTree = parseTree; }
    public String getSentiment() { return sentiment; }
    public void setSentiment(String sentiment) { this.sentiment = sentiment; }
    public NLPResultEntity getNlpResult() { return nlpResult; }
    public void setNlpResult(NLPResultEntity nlpResult) { this.nlpResult = nlpResult; }
    public List<TokenInfoEntity> getTokens() { return tokens; }
    public void setTokens(List<TokenInfoEntity> tokens) { this.tokens = tokens; }
} 