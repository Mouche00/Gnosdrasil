package org.yc.gnosdrasil.gdpromptprocessingservice.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity
@Table(name = "nlp_results")
public class NLPResult {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

//    @NotBlank(message = "Original text cannot be empty")
//    @Column(nullable = false, columnDefinition = "TEXT")
//    private String originalText;

//    @NotBlank(message = "Corrected text cannot be empty")
//    @Column(nullable = false, columnDefinition = "TEXT")
    private String correctedText;

//    @NotBlank(message = "Overall sentiment cannot be empty")
//    @Column(nullable = false)
    private String overallSentiment;

//    @NotNull(message = "Creation timestamp cannot be null")
//    @Column(nullable = false)
//    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "nlpResult", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SentenceAnalysis> sentenceAnalyses = new ArrayList<>();

    @OneToMany(mappedBy = "nlpResult", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<LanguageIntent> languageIntents = new ArrayList<>();

    @OneToMany(mappedBy = "nlpResult", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<LearningFocus> learningFocus = new ArrayList<>();
} 