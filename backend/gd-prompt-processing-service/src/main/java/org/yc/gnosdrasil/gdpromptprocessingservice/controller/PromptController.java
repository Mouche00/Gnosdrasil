package org.yc.gnosdrasil.gdpromptprocessingservice.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.yc.gnosdrasil.gdpromptprocessingservice.dtos.GlobalResponseDTO;
import org.yc.gnosdrasil.gdpromptprocessingservice.dtos.JobAnalysisDTO;
import org.yc.gnosdrasil.gdpromptprocessingservice.dtos.JobListingDTO;
import org.yc.gnosdrasil.gdpromptprocessingservice.dtos.PromptRequestDTO;
import org.yc.gnosdrasil.gdpromptprocessingservice.services.SearchParamsService;

import java.util.List;

@RestController
@RequestMapping("/api/prompt")
@RequiredArgsConstructor
public class PromptController {

    private final SearchParamsService searchParamsService;

    @PostMapping("/process")
    public ResponseEntity<GlobalResponseDTO> processIntent(@RequestBody PromptRequestDTO input) {
        return ResponseEntity.ok(searchParamsService.getGlobalResponse(input));
    }
} 