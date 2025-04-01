package org.yc.gnosdrasil.gdpromptprocessingservice.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Entity
@Table(name = "search_params")
public class SearchParams extends BaseEntity<Long> {
    @ElementCollection
    private List<String> keywords;
    private String experienceLevel;
    private String location;

    @OneToOne
    @JoinColumn(name = "nlp_result")
    private NLPResult nlpResult;
} 