package org.yc.gnosdrasil.gdpromptprocessingservice.entity;

import lombok.*;
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
//@Entity
//@Table(name = "token_infos")
public class TokenInfo {
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String word;

    private String pos;

    private String lemma;

    private String ner;

//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "sentence_analysis")
    private SentenceAnalysis sentenceAnalysis;
} 