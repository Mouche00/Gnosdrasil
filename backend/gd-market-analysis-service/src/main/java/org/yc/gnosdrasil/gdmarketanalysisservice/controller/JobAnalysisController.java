package org.yc.gnosdrasil.gdmarketanalysisservice.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.yc.gnosdrasil.gdmarketanalysisservice.dtos.JobAnalysisDTO;
import org.yc.gnosdrasil.gdmarketanalysisservice.dtos.SearchParamsDTO;
import org.yc.gnosdrasil.gdmarketanalysisservice.model.JobTrendAnalysis;
import org.yc.gnosdrasil.gdmarketanalysisservice.service.JobAnalysisService;

@RestController
@RequestMapping("/api/analysis")
@RequiredArgsConstructor
public class JobAnalysisController {
    private final JobAnalysisService jobAnalysisService;

    @PostMapping("/trends")
    public JobAnalysisDTO getJobTrends(@RequestBody SearchParamsDTO searchParamsDTO) {
        return jobAnalysisService.analyzeJobTrends(searchParamsDTO);
    }
} 