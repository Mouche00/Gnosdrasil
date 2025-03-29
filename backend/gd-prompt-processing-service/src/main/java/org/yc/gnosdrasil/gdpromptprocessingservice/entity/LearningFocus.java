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
@Table(name = "learning_focus")
public class LearningFocus {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String language;

    private String level;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "nlp_result")
    private NLPResult nlpResult;
} 