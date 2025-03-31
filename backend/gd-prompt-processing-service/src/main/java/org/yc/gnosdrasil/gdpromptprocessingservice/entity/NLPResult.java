package org.yc.gnosdrasil.gdpromptprocessingservice.entity;

import jakarta.persistence.*;
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
public class NLPResult extends BaseEntity<Long> {

    private String originalText;

    private String correctedText;

    private String SearchParams;

    @OneToMany(mappedBy = "nlpResult", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SentenceAnalysis> sentenceAnalyses = new ArrayList<>();

    @OneToMany(mappedBy = "nlpResult", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<LanguageIntent> languageIntents = new ArrayList<>();

    @PrePersist
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