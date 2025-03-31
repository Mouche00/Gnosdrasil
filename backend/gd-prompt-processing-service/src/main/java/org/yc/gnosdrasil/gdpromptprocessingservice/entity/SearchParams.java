package org.yc.gnosdrasil.gdpromptprocessingservice.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "search_params")
public class SearchParams extends BaseEntity<Long> {
    @Transient
    private List<String> keywords;
    private String experienceLevel;
    private String location;

    @OneToOne
    @JoinColumn(name = "nlp_result")
    private NLPResult nlpResult;
} 