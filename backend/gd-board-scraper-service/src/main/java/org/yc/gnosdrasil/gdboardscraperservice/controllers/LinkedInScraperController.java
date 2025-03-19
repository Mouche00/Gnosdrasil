package org.yc.gnosdrasil.gdboardscraperservice.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.yc.gnosdrasil.gdboardscraperservice.entities.SearchParams;
import org.yc.gnosdrasil.gdboardscraperservice.services.LinkedInScraperService;

import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/api/linkedin")
@RequiredArgsConstructor
public class LinkedInScraperController {
    private final LinkedInScraperService linkedInScraperService;

    @PostMapping
    public ResponseEntity<?> startScraping(@RequestBody SearchParams request) {
        try {
            // Validate request
            if (request.getKeywords() == null || request.getKeywords().isEmpty()) {
                return ResponseEntity.badRequest().body("Keywords are required");
            }

            // Start scraping job
            CompletableFuture<String> future = linkedInScraperService.startScrapingJob(request);
            String jobId = future.get(); // Wait for job ID

            return ResponseEntity.ok("Scraping job started successfully");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Failed to start scraper: " + e.getMessage());
        }
    }
}
