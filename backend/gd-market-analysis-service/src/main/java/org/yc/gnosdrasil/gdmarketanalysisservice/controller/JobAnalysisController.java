package org.yc.gnosdrasil.gdmarketanalysisservice.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.yc.gnosdrasil.gdmarketanalysisservice.dtos.SearchParamsDTO;
import org.yc.gnosdrasil.gdmarketanalysisservice.model.JobTrendAnalysis;
import org.yc.gnosdrasil.gdmarketanalysisservice.service.JobAnalysisService;

@RestController
@RequestMapping("/api/v1/analysis")
@RequiredArgsConstructor
public class JobAnalysisController {
    private final JobAnalysisService jobAnalysisService;

    @GetMapping("/trends")
    public ResponseEntity<JobTrendAnalysis> getJobTrends(SearchParamsDTO searchParamsDTO) {
        return ResponseEntity.ok(jobAnalysisService.analyzeJobTrends(searchParamsDTO));
    }
} 