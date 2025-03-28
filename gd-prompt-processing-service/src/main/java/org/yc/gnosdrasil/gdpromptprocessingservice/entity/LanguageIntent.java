package org.yc.gnosdrasil.gdpromptprocessingservice.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "language_intents")
public class LanguageIntent {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Programming language cannot be empty")
    @Size(max = 50, message = "Programming language name cannot exceed 50 characters")
    @Column(nullable = false, length = 50)
    private String lang;

    @NotBlank(message = "Experience level cannot be empty")
    @Size(max = 20, message = "Experience level cannot exceed 20 characters")
    @Column(nullable = false, length = 20)
    private String level;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "nlp_result_id", nullable = false)
    private NLPResult nlpResult;
} 