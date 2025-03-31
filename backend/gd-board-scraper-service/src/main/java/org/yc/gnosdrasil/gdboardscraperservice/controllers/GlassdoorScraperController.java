package org.yc.gnosdrasil.gdboardscraperservice.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.yc.gnosdrasil.gdboardscraperservice.services.impl.GlassdoorScraperImpl;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/glassdoor")
public class GlassdoorScraperController {
    private final GlassdoorScraperImpl glassdoorScraperImpl;

    @PostMapping
    public ResponseEntity<?> scrape() {
        glassdoorScraperImpl.scrape();
        return ResponseEntity.ok().build();
    }
}
