package org.yc.gnosdrasil.gdpromptprocessingservice.entity;

import jakarta.persistence.*;
import lombok.*;
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "token_infos")
public class TokenInfo {
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
    private SentenceAnalysis sentenceAnalysis;
} 