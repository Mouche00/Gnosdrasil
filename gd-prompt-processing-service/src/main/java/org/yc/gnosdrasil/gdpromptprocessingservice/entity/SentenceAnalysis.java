package org.yc.gnosdrasil.gdpromptprocessingservice.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "sentence_analyses")
public class SentenceAnalysis {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String parseTree;

    @Column(nullable = false)
    private String sentiment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "nlp_result_id", nullable = false)
    private NLPResult nlpResult;

    @OneToMany(mappedBy = "sentenceAnalysis", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TokenInfo> tokens;
} 