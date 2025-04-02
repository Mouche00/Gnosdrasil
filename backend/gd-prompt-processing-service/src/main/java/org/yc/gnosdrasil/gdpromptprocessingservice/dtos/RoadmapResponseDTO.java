package org.yc.gnosdrasil.gdpromptprocessingservice.dtos;

import java.util.List;

public record RoadmapResponseDTO(String id, String title, List<Step> steps) {
    public record Step(String id, String label, List<Step> connectedSteps) {

    }
}
