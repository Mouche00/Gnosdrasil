package org.yc.gnosdrasil.gdpromptprocessingservice.entity;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
//@Entity
//@Table(name = "language_intents")
public class LanguageIntent {
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String lang;

    private String level;

    private boolean isFocus;

//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "nlp_result")
//    private NLPResult nlpResult;
} 