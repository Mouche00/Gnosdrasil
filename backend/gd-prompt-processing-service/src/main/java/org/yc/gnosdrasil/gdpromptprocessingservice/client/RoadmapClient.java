package org.yc.gnosdrasil.gdpromptprocessingservice.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.yc.gnosdrasil.gdpromptprocessingservice.dtos.JobAnalysisDTO;
import org.yc.gnosdrasil.gdpromptprocessingservice.dtos.RoadmapResponseDTO;
import org.yc.gnosdrasil.gdpromptprocessingservice.dtos.SearchParamsDTO;

@FeignClient(name = "roadmapClient", url = "${feign.client.url.roadmap:http://localhost:8085}")
public interface RoadmapClient {
    @PostMapping("/api/roadmap/fetch")
    RoadmapResponseDTO getRoadmap(@RequestBody SearchParamsDTO request);
}