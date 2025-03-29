package org.yc.gnosdrasil.gdpromptprocessingservice.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.yc.gnosdrasil.gdpromptprocessingservice.dtos.PromptRequestDTO;
import org.yc.gnosdrasil.gdpromptprocessingservice.entity.NLPResult;
import org.yc.gnosdrasil.gdpromptprocessingservice.services.NLPService;

@RestController
@RequestMapping("/api/intent")
@RequiredArgsConstructor
public class IntentController {

    private final NLPService nlpService;

    @PostMapping("/process")
    public NLPResult processIntent(@RequestBody PromptRequestDTO input) {
        return nlpService.processText(input.prompt());
    }
} 