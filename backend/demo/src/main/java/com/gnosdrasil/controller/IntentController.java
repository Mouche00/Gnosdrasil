package com.gnosdrasil.controller;

import com.gnosdrasil.nlp.service.AdvancedNLPService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/intent")
public class IntentController {

    @Autowired
    private AdvancedNLPService nlpService;

    @PostMapping("/process")
    public AdvancedNLPService.NLPResult processIntent(@RequestBody String input) {
        return nlpService.processText(input);
    }
} 