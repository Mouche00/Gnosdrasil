package org.yc.gnosdrasil.common.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SearchParams {
    private String searchQuery;
    private String boardType;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private Integer maxResults;
    private String sortBy;
    private String sortOrder;
    private String userId;
    private String requestId;
} 