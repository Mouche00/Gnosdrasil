package org.yc.gnosdrasil.gdpromptprocessingservice.entity;

import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
//@Entity
//@Table(name = "sentence_analyses")
public class SentenceAnalysis {
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

//    @Column(columnDefinition = "TEXT")
    private String parseTree;

    private String sentiment;

//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "nlp_result")
    private NLPResult nlpResult;

//    @OneToMany(mappedBy = "sentenceAnalysis", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TokenInfo> tokens;

//    @PrePersist
    protected void onCreate() {
        // Set up bidirectional relationships
        if (tokens != null) {
            tokens.forEach(t -> t.setSentenceAnalysis(this));
        }
    }
} 