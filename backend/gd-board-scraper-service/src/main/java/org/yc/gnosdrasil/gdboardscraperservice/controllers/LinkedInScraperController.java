package org.yc.gnosdrasil.gdboardscraperservice.controllers;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.yc.gnosdrasil.gdboardscraperservice.entities.SearchParams;
import org.yc.gnosdrasil.gdboardscraperservice.services.JobBoardScraperService;

@RestController
@RequestMapping("/api/linkedin")
public class LinkedInScraperController {
    private final JobBoardScraperService linkedInScraperService;

    public LinkedInScraperController(@Qualifier("linkedinScraperService") JobBoardScraperService linkedInScraperService) {
        this.linkedInScraperService = linkedInScraperService;
    }

    @PostMapping
    public ResponseEntity<?> startScraping(@RequestBody SearchParams request) {
        try {
            // Validate request
            if (request.getKeywords() == null || request.getKeywords().isEmpty()) {
                return ResponseEntity.badRequest().body("Keywords are required");
            }

            return ResponseEntity.ok(linkedInScraperService.scrapeJobs(request));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Failed to start scraper: " + e.getMessage());
        }
    }
}
