package org.yc.gnosdrasil.gdboardscraperservice.controllers;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.yc.gnosdrasil.gdboardscraperservice.entities.SearchParams;
import org.yc.gnosdrasil.gdboardscraperservice.repositories.JobBoardScraperRepository;
import org.yc.gnosdrasil.gdboardscraperservice.services.JobBoardScraperService;

@RestController
@RequestMapping("/api/board")
public class JobScraperController {
    private final JobBoardScraperService linkedInScraperService;

    public JobScraperController(@Qualifier("linkedinScraperService") JobBoardScraperService linkedInScraperService) {
        this.linkedInScraperService = linkedInScraperService;
    }

    @PostMapping("scrape")
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
