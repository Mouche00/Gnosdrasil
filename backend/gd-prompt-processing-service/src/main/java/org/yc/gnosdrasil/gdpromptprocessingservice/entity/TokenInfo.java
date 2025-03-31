package org.yc.gnosdrasil.gdpromptprocessingservice.entity;

import jakarta.persistence.*;
import lombok.*;
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity
@Table(name = "token_infos")
public class TokenInfo extends BaseEntity<Long> {

    private String word;

    private String pos;

    private String lemma;

    private String ner;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sentence_analysis")
    private SentenceAnalysis sentenceAnalysis;
} 