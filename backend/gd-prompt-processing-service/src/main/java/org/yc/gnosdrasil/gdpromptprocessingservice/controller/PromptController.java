package org.yc.gnosdrasil.gdpromptprocessingservice.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.yc.gnosdrasil.gdpromptprocessingservice.dtos.NLPResultDTO;
import org.yc.gnosdrasil.gdpromptprocessingservice.dtos.PromptRequestDTO;
import org.yc.gnosdrasil.gdpromptprocessingservice.services.NLPService;

@RestController
@RequestMapping("/api/prompt")
@RequiredArgsConstructor
public class PromptController {

    private final NLPService nlpService;

    @PostMapping("/process")
    public ResponseEntity<NLPResultDTO> processIntent(@RequestBody PromptRequestDTO input) {
        return ResponseEntity.ok(nlpService.processText(input));
    }
} 