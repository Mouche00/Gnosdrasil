package org.yc.gnosdrasil.gdpromptprocessingservice.entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "language_intents")
public class LanguageIntent extends BaseEntity<Long> {

    private String lang;

    private String level;

    private boolean isFocus;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "nlp_result")
    private NLPResult nlpResult;
} 