package org.yc.gnosdrasil.gdboardscraperservice.controllers;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.yc.gnosdrasil.gdboardscraperservice.dtos.JobListingDTO;
import org.yc.gnosdrasil.gdboardscraperservice.dtos.SearchParamsDTO;
import org.yc.gnosdrasil.gdboardscraperservice.entities.SearchParams;
import org.yc.gnosdrasil.gdboardscraperservice.repositories.JobBoardScraperRepository;
import org.yc.gnosdrasil.gdboardscraperservice.services.JobBoardScraperService;

import java.util.List;
import java.util.concurrent.Future;

@RestController
@RequestMapping("/api/board")
public class JobScraperController {
    private final JobBoardScraperService linkedInScraperService;

    public JobScraperController(@Qualifier("linkedinScraperService") JobBoardScraperService linkedInScraperService) {
        this.linkedInScraperService = linkedInScraperService;
    }

    @PostMapping("get")
    public void getJob(@RequestBody SearchParamsDTO request) {
        linkedInScraperService.scrapeJobs(request);
    }

    @PostMapping("scrape")
    public List<JobListingDTO> startScraping(@RequestBody SearchParamsDTO request) {
        return linkedInScraperService.getAllJobListings(request);
    }
}
