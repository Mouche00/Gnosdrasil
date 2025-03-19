package org.yc.gnosdrasil.gdexternalresourceservice.dtos.response;

import lombok.Builder;

@Builder
public record RoadmapResponseDTO(String nodeId,
                                 String type,
                                 String title,
                                 String parentId,
                                 String parentTitle) {
}
