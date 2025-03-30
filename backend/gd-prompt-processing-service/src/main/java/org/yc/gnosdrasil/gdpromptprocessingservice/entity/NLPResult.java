package org.yc.gnosdrasil.gdpromptprocessingservice.entity;

import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
//@Entity
//@Table(name = "nlp_results")
public class NLPResult {
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

//    @NotBlank(message = "Original text cannot be empty")
//    @Column(nullable = false, columnDefinition = "TEXT")
//    private String originalText;

//    @NotBlank(message = "Corrected text cannot be empty")
//    @Column(columnDefinition = "TEXT")
    private String correctedText;

//    @NotBlank(message = "Overall sentiment cannot be empty")
//    @Column(nullable = false)
    private String overallSentiment;

//    @NotNull(message = "Creation timestamp cannot be null")
//    @Column(nullable = false)
//    private LocalDateTime createdAt;

//    @OneToMany(mappedBy = "nlpResult", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SentenceAnalysis> sentenceAnalyses = new ArrayList<>();

//    @OneToMany(mappedBy = "nlpResult", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<LanguageIntent> languageIntents = new ArrayList<>();

//    @PrePersist
    protected void onCreate() {
        // Set up bidirectional relationships
        if (sentenceAnalyses != null) {
            sentenceAnalyses.forEach(sa -> sa.setNlpResult(this));
        }
        if (languageIntents != null) {
            languageIntents.forEach(li -> li.setNlpResult(this));
        }
    }
} 