package com.example.demo.controllers;

import com.example.demo.services.NLPService;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/prompt")
public class PromptController {

    private final NLPService nlpService;

    public PromptController(NLPService nlpService) {
        this.nlpService = nlpService;
    }

    @PostMapping("/process")
    public Map<String, Object> processPrompt(@RequestBody Map<String, String> request) {
        String prompt = request.get("prompt");
        return nlpService.processPrompt(prompt);
    }
}