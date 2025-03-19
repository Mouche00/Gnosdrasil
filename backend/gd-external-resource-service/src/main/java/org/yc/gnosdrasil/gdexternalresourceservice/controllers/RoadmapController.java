package org.yc.gnosdrasil.gdexternalresourceservice.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.yc.gnosdrasil.gdexternalresourceservice.dtos.response.RoadmapResponseDTO;
import org.yc.gnosdrasil.gdexternalresourceservice.dtos.response.SerpApiResponseDTO;
import org.yc.gnosdrasil.gdexternalresourceservice.services.RoadmapScraperService;
import org.yc.gnosdrasil.gdexternalresourceservice.services.SerpApiService;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/roadmap")
public class RoadmapController {

    private final RoadmapScraperService roadmapScraperService;

    @GetMapping
    public List<RoadmapResponseDTO> search(@RequestParam String query) {
        return roadmapScraperService.getRoadmap();
    }
}