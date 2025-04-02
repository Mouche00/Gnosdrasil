package org.yc.gnosdrasil.gdroadmapservice.controllers;


import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.yc.gnosdrasil.gdroadmapservice.clients.impl.RoadmapClientImpl;
import org.yc.gnosdrasil.gdroadmapservice.dtos.RoadmapResponseDTO;
import org.yc.gnosdrasil.gdroadmapservice.dtos.SearchParamsDTO;
import org.yc.gnosdrasil.gdroadmapservice.entities.Roadmap;
import org.yc.gnosdrasil.gdroadmapservice.entities.Step;
import org.yc.gnosdrasil.gdroadmapservice.services.RoadmapService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/roadmap")
public class RoadmapController {
    private final RoadmapService roadmapService;

    @PostMapping("/fetch")
    public Roadmap getRoadmap(@RequestBody SearchParamsDTO searchParamsDTO) {
        return roadmapService.constructStepGraph(searchParamsDTO.keywords().stream().findFirst().orElse(null));
    }
}
