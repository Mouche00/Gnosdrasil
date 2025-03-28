package com.gnosdrasil.nlp.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "token_infos")
public class TokenInfoEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String word;

    @Column(nullable = false)
    private String pos;

    @Column(nullable = false)
    private String lemma;

    @Column(nullable = false)
    private String ner;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sentence_analysis_id", nullable = false)
    private SentenceAnalysisEntity sentenceAnalysis;

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getWord() { return word; }
    public void setWord(String word) { this.word = word; }
    public String getPos() { return pos; }
    public void setPos(String pos) { this.pos = pos; }
    public String getLemma() { return lemma; }
    public void setLemma(String lemma) { this.lemma = lemma; }
    public String getNer() { return ner; }
    public void setNer(String ner) { this.ner = ner; }
    public SentenceAnalysisEntity getSentenceAnalysis() { return sentenceAnalysis; }
    public void setSentenceAnalysis(SentenceAnalysisEntity sentenceAnalysis) { this.sentenceAnalysis = sentenceAnalysis; }
} 