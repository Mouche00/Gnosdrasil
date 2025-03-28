package com.gnosdrasil.nlp.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "language_intents")
public class LanguageIntentEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String lang;

    @Column(nullable = false)
    private String level;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "nlp_result_id", nullable = false)
    private NLPResultEntity nlpResult;

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getLang() { return lang; }
    public void setLang(String lang) { this.lang = lang; }
    public String getLevel() { return level; }
    public void setLevel(String level) { this.level = level; }
    public NLPResultEntity getNlpResult() { return nlpResult; }
    public void setNlpResult(NLPResultEntity nlpResult) { this.nlpResult = nlpResult; }
} 